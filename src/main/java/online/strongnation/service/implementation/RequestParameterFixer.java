package online.strongnation.service.implementation;

import online.strongnation.config.Constants;
import online.strongnation.config.Floats;
import online.strongnation.config.NameProperties;
import online.strongnation.exception.IllegalCountryException;
import online.strongnation.exception.IllegalPostException;
import online.strongnation.exception.IllegalRegionException;
import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.dto.PostDTO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface RequestParameterFixer {
    static String checkAndNormalizeRegion(final String name) {
        if (name == null) {
            throw new IllegalRegionException("Name of region is null");
        }
        if (name.length() > NameProperties.REGION_NAME_LENGTH) {
            throw new IllegalRegionException("Too long name of region");
        }
        String clearName = StringUtils.normalizeSpace(name);
        if (clearName.length() == 0) {
            throw new IllegalRegionException("Empty name");
        }
        return clearName;
    }

    static String checkAndNormalizeCountry(final String name) {
        if (name == null) {
            throw new IllegalCountryException("Name of country is null");
        }
        if (name.length() > NameProperties.COUNTRY_NAME_LENGTH) {
            throw new IllegalCountryException("Too long name of country");
        }
        String clearName = StringUtils.normalizeSpace(name);
        if (clearName.length() == 0) {
            throw new IllegalCountryException("Empty name");
        }
        return clearName;
    }

    static PostDTO checkAndNormalizeNewPost(final PostDTO post) {
        checkIdOfNewPost(post.getId());
        return checkAndNormalizePost(post);
    }

    static PostDTO checkAndNormalizeUpdatedPost(final PostDTO post) {
        checkPossibleIdOfPost(post.getId());
        return checkAndNormalizePost(post);
    }

    private static void checkPossibleIdOfPost(Long id) {
        if (id == null || id < 1) {
            throw new IllegalPostException("Id of updating post can't be " + id);
        }
    }

    private static PostDTO checkAndNormalizePost(final PostDTO post) {
        final String heading = checkAndNormalizeHeadingOfPost(post.getHeading());
        final String link = checkLink(post.getLink());
        final BigDecimal money = checkAndNormalizeMoneyOfPost(post.getMoney());
        final LocalDateTime date = checkDate(post.getDate());
        final List<CategoryDTO> list = checkAndNormalizeCategories(post.getCategories());
        return PostDTO.builder()
                .id(post.getId())
                .heading(heading)
                .link(link)
                .money(money)
                .date(date)
                .categories(list)
                .build();
    }

    private static List<CategoryDTO> checkAndNormalizeCategories(List<CategoryDTO> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        if (list.size() > Constants.MAX_NUMBER_OF_CATEGORIES_OF_POST) {
            String message = "There is too many categories. Allowed: "
                    + Constants.MAX_NUMBER_OF_CATEGORIES_OF_POST +
                    " Actual: " + list.size();
            throw new IllegalPostException(message);
        }
        return list.stream().map(RequestParameterFixer::checkAndNormalizeCategory).toList();
    }

    private static CategoryDTO checkAndNormalizeCategory(CategoryDTO category) {
        final String name = checkAndNormalizeNameOfCategory(category.getName());
        final BigDecimal number = checkAndNormalizeNumberOfCategory(category.getNumber());
        final String units = checkAndNormalizeUnitsOfCategory(category.getUnits());
        return new CategoryDTO(name, number, units);
    }

    private static BigDecimal checkAndNormalizeNumberOfCategory(final BigDecimal number) {
        final String zeroMessage = "Number in category is less than zero: ";
        return checkAndNormalizeNumber(number, Floats.CATEGORY_SCALE, Floats.CATEGORY_ROUNDING, zeroMessage);
    }

    private static String checkAndNormalizeUnitsOfCategory(final String units) {
        if (units == null || units.length() == 0) {
            return null;
        }
        if (units.length() > NameProperties.CATEGORY_UNITS_LENGTH) {
            throw new IllegalPostException("Too long units' name of category");
        }
        String clearName = StringUtils.normalizeSpace(units);
        if (clearName.length() == 0) {
            String message = "Units field of category has only blank symbols. Post is not saved";
            throw new IllegalPostException(message);
        }
        return clearName;
    }

    private static String checkAndNormalizeNameOfCategory(final String name) {
        if (name == null) {
            throw new IllegalPostException("Name of category is null");
        }
        if (name.length() > NameProperties.CATEGORY_NAME_LENGTH) {
            throw new IllegalPostException("Too long name of category");
        }
        String clearName = StringUtils.normalizeSpace(name);
        if (clearName.length() == 0) {
            throw new IllegalPostException("Empty name of category");
        }
        return clearName;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static LocalDateTime checkDate(LocalDateTime date) {
        boolean isFuture = date.isAfter(LocalDateTime.now().plusDays(1));
        if (isFuture) {
            throw new IllegalPostException("Date of post is future");
        }
        boolean isPast = date.isBefore(LocalDateTime.of(1991, Month.JANUARY, 1, 0, 0));
        if (isPast) {
            throw new IllegalPostException("Date of post is past");
        }
        String s = date.toString();
        int endIndex = s.lastIndexOf('.');
        String deletedAfterDot = endIndex == -1 ? s : s.substring(0, endIndex);
        String dateAsString = deletedAfterDot.replace('T', ' ');
        return LocalDateTime.parse(dateAsString, formatter);
    }

    private static String checkLink(String link) {
        if (link == null) {
            throw new IllegalPostException("Link of post is null");
        }
        if (link.length() < 1) {
            throw new IllegalPostException("Link of post is empty");
        }
        return link;
    }

    private static BigDecimal checkAndNormalizeMoneyOfPost(final BigDecimal money) {
        final String zeroMessage = "Money in post is less than zero: ";
        return checkAndNormalizeNumber(money, Floats.MONEY_SCALE, Floats.MONEY_ROUNDING, zeroMessage);
    }

    private static BigDecimal checkAndNormalizeNumber(final BigDecimal money,
                                                      final int scale,
                                                      final RoundingMode mode,
                                                      final String zeroMessage) {
        int compare = money.compareTo(BigDecimal.ZERO);
        return switch (compare) {
            case -1 -> throw new IllegalPostException(zeroMessage + money);
            case 0 -> BigDecimal.ZERO;
            default -> money.setScale(scale, mode);
        };
    }

    private static String checkAndNormalizeHeadingOfPost(final String heading) {
        if (heading == null) {
            throw new IllegalPostException("Heading of post is null");
        }
        if (heading.length() > NameProperties.POST_HEADING_LENGTH) {
            throw new IllegalPostException("Too long heading of post");
        }
        String clearName = StringUtils.normalizeSpace(heading);
        if (clearName.length() == 0) {
            throw new IllegalPostException("Empty heading");
        }
        return clearName;
    }

    private static void checkIdOfNewPost(Long id) {
        if (id != null) {
            String message = "You should not provide your own id for post! It is work for database.";
            throw new IllegalPostException(message);
        }
    }
}

package online.strongnation.business.service.implementation;

import online.strongnation.business.exception.IllegalCountryException;
import online.strongnation.business.exception.IllegalPostException;
import online.strongnation.business.exception.IllegalRegionException;
import online.strongnation.business.config.Constants;
import online.strongnation.business.config.Floats;
import online.strongnation.business.config.NameProperties;
import online.strongnation.business.model.dto.CategoryDTO;
import online.strongnation.business.model.dto.PostDTO;
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
        final Boolean important = checkAndNormalizeImportantOfNewPost(post.getImportant());
        return checkAndNormalizePost(post).important(important).build();
    }

    static Boolean checkAndNormalizeImportantOfNewPost(Boolean important) {
        return important != null && important;
    }

    static PostDTO checkAndNormalizeUpdatedPost(final PostDTO post) {
        checkPossibleIdOfPost(post.getId());
        final String regionName = checkAndNormalizeRegion(post.getRegion());
        return checkAndNormalizePost(post).region(regionName).build();
    }

    private static void checkPossibleIdOfPost(Long id) {
        if (id == null || id < 1) {
            throw new IllegalPostException("Id of updating post can't be " + id);
        }
    }

    private static PostDTO.PostDTOBuilder checkAndNormalizePost(final PostDTO post) {
        final String description = checkAndNormalizeDescriptionOfPost(post.getDescription());
        final String link = checkLink(post.getLink());
        final LocalDateTime date = checkDate(post.getDate());
        final List<CategoryDTO> list = checkAndNormalizeCategories(post.getCategories());
        return PostDTO.builder()
                .id(post.getId())
                .description(description)
                .link(link)
                .date(date)
                .categories(list);
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

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
        String deletedAfterDot = s;
        if (Constants.DATE_FORMAT.contains(".")) {
            deletedAfterDot = endIndex == -1 ? s : s.substring(0, endIndex);
        }
        String dateAsString = deletedAfterDot.replace('T', ' ');
        if (dateAsString.length() > Constants.DATE_FORMAT.length()) {
            dateAsString = dateAsString.substring(0, Constants.DATE_FORMAT.length());
        }
        if (dateAsString.length() == 16 && Constants.DATE_FORMAT.lastIndexOf(':') == 16) {
            dateAsString += ":00";
        }
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

    private static String checkAndNormalizeDescriptionOfPost(final String description) {
        if (description == null) {
            throw new IllegalPostException("Description of post is null");
        }
        if (description.length() > NameProperties.POST_DESCRIPTION_LENGTH) {
            throw new IllegalPostException("Too long description of post");
        }
        String clearName = StringUtils.normalizeSpace(description);
        if (clearName.length() == 0) {
            throw new IllegalPostException("Empty description");
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

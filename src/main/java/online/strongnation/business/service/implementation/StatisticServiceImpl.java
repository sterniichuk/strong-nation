package online.strongnation.business.service.implementation;

import online.strongnation.business.model.statistic.StatisticModel;
import online.strongnation.business.model.statistic.StatisticResult;
import online.strongnation.business.service.StatisticService;
import online.strongnation.business.model.dto.CategoryDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;

import static online.strongnation.business.service.implementation.CategoryUtils.getCategoryMap;

@Service
public class StatisticServiceImpl implements StatisticService {

    private static final StatisticResult emptyStatistic = StatisticResult.builder()
            .updatedCategories(List.of())
            .excessiveCategories(List.of())
            .newCategories(List.of())
            .build();

    @Override
    public StatisticResult addChildToParent(StatisticModel parent, StatisticModel child) {
        if (child == null) {
            return emptyStatistic;
        }
        List<CategoryDTO> newCategories;
        List<CategoryDTO> updated;
        List<CategoryDTO> parentCategories = parent.getCategories();
        if (parentCategories == null || parentCategories.size() == 0) {
            newCategories = child.getCategories();
            updated = List.of();
        } else {
            newCategories = new ArrayList<>();
            updated = new ArrayList<>();
            groupCategoriesOfNewChild(parent, child, updated, newCategories);
        }
        return StatisticResult.builder()
                .updatedCategories(updated)
                .excessiveCategories(List.of())
                .newCategories(newCategories)
                .build();
    }

    private void groupCategoriesOfNewChild(StatisticModel parent, StatisticModel child,
                                           List<CategoryDTO> updated, List<CategoryDTO> newCategories) {
        Map<String, CategoryDTO> parentCategories = getCategoryMap(parent.getCategories());
        for (var i : child.getCategories()) {
            var categoryDTO = parentCategories.get(i.getName());
            if (categoryDTO != null) {
                updated.add(categoryDTO.addNumber(i));
                continue;
            }
            newCategories.add(i);
        }
    }

    @Override
    public <T extends StatisticModel> StatisticResult updateChild(StatisticModel parent, T old, T updated) {
        if (updated == null) {
            return emptyStatistic;
        }
        if (parent.getCategories().size() == 0) {
            return onlyNewData(updated.getCategories());
        }
        List<CategoryDTO> newCategories = new ArrayList<>();
        List<CategoryDTO> updatedCategories = new ArrayList<>();
        List<CategoryDTO> excessiveCategories = new ArrayList<>();
        groupCategoriesWhenUpdateChild(parent, old, updated, updatedCategories, newCategories, excessiveCategories);
        return StatisticResult.builder()
                .updatedCategories(updatedCategories)
                .excessiveCategories(excessiveCategories)
                .newCategories(newCategories)
                .build();
    }

    @Override
    public <T extends StatisticModel> StatisticResult updateSelf(T old, T updated) {
        if (updated == null) {
            return emptyStatistic;
        }
        List<CategoryDTO> categories = updated.getCategories() == null ? List.of() : updated.getCategories();
        if (old.getCategories().size() == 0) {
            return onlyNewData(categories);
        }
        List<CategoryDTO> newCategories = new ArrayList<>();
        List<CategoryDTO> updatedCategories = new ArrayList<>();
        List<CategoryDTO> excessiveCategories = new ArrayList<>();
        groupCategoriesWhenUpdateSelf(old, updated, updatedCategories, newCategories, excessiveCategories);
        return StatisticResult.builder()
                .updatedCategories(updatedCategories)
                .excessiveCategories(excessiveCategories)
                .newCategories(newCategories)
                .build();
    }

    private <T extends StatisticModel> void groupCategoriesWhenUpdateSelf(T old, T updated,
                                                                          List<CategoryDTO> updatedCategories,
                                                                          List<CategoryDTO> newCategories,
                                                                          List<CategoryDTO> excessiveCategories) {
        Map<String, CategoryDTO> oldMap = getCategoryMap(old.getCategories());
        Set<String> presentCategoryInNewModel = new HashSet<>();
        for (var i : updated.getCategories()) {
            var oldCategory = oldMap.get(i.getName());
            if (oldCategory != null) {
                Optional<BigDecimal> number = selfUpdateNumber(oldCategory.getNumber(), i.getNumber());
                number.ifPresent(m -> updatedCategories.add(oldCategory.updateNumber(m)));
                presentCategoryInNewModel.add(oldCategory.getName());
                continue;
            }
            newCategories.add(i);
        }
        old.getCategories().forEach(c -> {
            String name = c.getName();
            if (!presentCategoryInNewModel.remove(name)) {
                excessiveCategories.add(c);
            }
        });
    }

    Optional<BigDecimal> selfUpdateNumber(BigDecimal old, BigDecimal newNumber) {
        int compare = old.compareTo(newNumber);
        return compare == 0 ? Optional.empty() : Optional.of(newNumber);
    }

    private void groupCategoriesWhenUpdateChild(StatisticModel parent, StatisticModel old,
                                                StatisticModel updated,
                                                List<CategoryDTO> updatedCategories,
                                                List<CategoryDTO> newCategories,
                                                List<CategoryDTO> excessiveCategories) {
        Map<String, CategoryDTO> oldMap = getCategoryMap(old.getCategories());
        Map<String, CategoryDTO> parentMap = getCategoryMap(parent.getCategories());
        Set<CategoryDTO> analyzed = new HashSet<>();
        BiConsumer<BigDecimal, CategoryDTO> categoryAnalizator = (m, parentCategory) -> {
            if (m.compareTo(BigDecimal.ZERO) <= 0) {
                excessiveCategories.add(parentCategory);
            } else {
                updatedCategories.add(parentCategory.updateNumber(m));
            }
        };
        for (var i : updated.getCategories()) {
            var parentCategory = parentMap.get(i.getName());
            if (parentCategory != null) {
                var oldCategory = oldMap.get(i.getName());
                Optional<BigDecimal> number = updateNumber(parentCategory, oldCategory, i);
                number.ifPresent(m -> categoryAnalizator.accept(m, parentCategory));
                analyzed.add(oldCategory);
                continue;
            }
            newCategories.add(i);
        }
        old.getCategories().forEach(c -> {
            if (!analyzed.remove(c)) {
                var parentCategory = parentMap.get(c.getName());
                BigDecimal number = updateNumberWhenChildDeletedCategory(parentCategory, c);
                categoryAnalizator.accept(number, c);
            }
        });
    }

    private BigDecimal updateNumberWhenChildDeletedCategory(CategoryDTO parent,
                                                            CategoryDTO child) {
        return updateNumberWhenChildDeletedCategory(parent.getNumber(), child.getNumber());
    }

    private BigDecimal updateNumberWhenChildDeletedCategory(BigDecimal parent,
                                                            BigDecimal child) {
        return parent.subtract(child);
    }

    private Optional<BigDecimal> updateNumber(CategoryDTO parent, CategoryDTO old, CategoryDTO updated) {
        BigDecimal number = old == null ? BigDecimal.ZERO : old.getNumber();
        return updateNumber(parent.getNumber(), number, updated.getNumber());
    }

    private Optional<BigDecimal> updateNumber(BigDecimal parent, BigDecimal old, BigDecimal updated) {
        if (parent.compareTo(BigDecimal.ZERO) == 0) {
            if (updated.compareTo(BigDecimal.ZERO) == 0) {
                return Optional.empty();
            }
            return Optional.of(updated);
        }
        int compare = old.compareTo(updated);
        return switch (compare) {
            case 1 -> Optional.of(parent.subtract(old.subtract(updated)));
            case -1 -> Optional.of(parent.add(updated.subtract(old)));// old less than updated
            default -> Optional.empty();
        };
    }

    private StatisticResult onlyNewData(List<CategoryDTO> newCategories) {
        return StatisticResult.builder()
                .updatedCategories(List.of())
                .excessiveCategories(List.of())
                .newCategories(newCategories)
                .build();
    }

    private StatisticResult onlyNewData() {
        return onlyNewData(List.of());
    }

    @Override
    public StatisticResult deleteChild(StatisticModel parent, StatisticModel child) {
        if (child == null) {
            return emptyStatistic;
        }
        List<CategoryDTO> parentCategories = parent.getCategories();
        if (parentCategories == null || parentCategories.size() == 0) {
            return onlyNewData();
        }
        List<CategoryDTO> excessive = new ArrayList<>();
        List<CategoryDTO> updated = new ArrayList<>();
        groupCategoriesOfDeletedChild(parent, child, updated, excessive);
        return StatisticResult.builder()
                .updatedCategories(updated)
                .excessiveCategories(excessive)
                .newCategories(List.of())
                .build();
    }

    private void groupCategoriesOfDeletedChild(StatisticModel parent, StatisticModel child,
                                               List<CategoryDTO> updated, List<CategoryDTO> excessive) {
        Map<String, CategoryDTO> parentCategories = getCategoryMap(parent.getCategories());
        for (var i : child.getCategories()) {
            var parentCategory = parentCategories.get(i.getName());
            if (parentCategory != null && Objects.equals(parentCategory.getUnits(), i.getUnits())) {
                BigDecimal newNumber = parentCategory.getNumber().subtract(i.getNumber());
                if (newNumber.compareTo(BigDecimal.ZERO) > 0) {
                    updated.add(parentCategory.updateNumber(newNumber));
                    continue;
                }
                excessive.add(parentCategory);
            }
        }
    }
}

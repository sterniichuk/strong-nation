package online.strongnation.service.implementation;

import online.strongnation.model.dto.CategoryDTO;
import online.strongnation.model.statistic.StatisticModel;
import online.strongnation.model.statistic.StatisticResult;
import online.strongnation.service.StatisticService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        Optional<BigDecimal> newMoney = addChildMoney(parent, child);
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
                .newMoneyValue(newMoney.orElse(null))
                .build();
    }

    private Optional<BigDecimal> addChildMoney(StatisticModel parent, StatisticModel child) {
        BigDecimal childMoney = child.getMoney();
        return (childMoney != null) ?
                Optional.of(parent.getMoney().add(childMoney))
                : Optional.empty();
    }

    private void groupCategoriesOfNewChild(StatisticModel parent, StatisticModel child,
                                           List<CategoryDTO> updated, List<CategoryDTO> newCategories) {
        Map<String, CategoryDTO> parentCategories = getCategoryMap(parent.getCategories());
        for (var i : child.getCategories()) {
            var categoryDTO = parentCategories.get(i.getName());
            if (categoryDTO != null) {
                if (Objects.equals(categoryDTO.getUnits(), i.getUnits())) {
                    updated.add(categoryDTO.addNumber(i));
                } else {
                    newCategories.add(i);
                }
                continue;
            }
            newCategories.add(i);
        }
    }

    @Override
    public StatisticResult updateChild(StatisticModel parent, StatisticModel old, StatisticModel updated) {
        if (updated == null) {
            return emptyStatistic;
        }
        Optional<BigDecimal> newMoney = updateNumber(parent.getMoney(), old.getMoney(), updated.getMoney());
        if (parent.getCategories().size() == 0) {
            return nothingCategoriesToUpdate(updated.getCategories(), newMoney);
        }
        List<CategoryDTO> newCategories = new ArrayList<>();
        List<CategoryDTO> updatedCategories = new ArrayList<>();
        List<CategoryDTO> excessiveCategories = new ArrayList<>();
        groupCategoriesWhenUpdateChild(parent, old, updated, updatedCategories, newCategories, excessiveCategories);
        return StatisticResult.builder()
                .updatedCategories(updatedCategories)
                .excessiveCategories(excessiveCategories)
                .newCategories(newCategories)
                .newMoneyValue(newMoney.orElse(null))
                .build();
    }

    private Map<String, CategoryDTO> getCategoryMap(List<CategoryDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(CategoryDTO::getName, Function.identity()));
    }

    private void groupCategoriesWhenUpdateChild(StatisticModel parent, StatisticModel old,
                                                StatisticModel updated,
                                                List<CategoryDTO> updatedCategories,
                                                List<CategoryDTO> newCategories,
                                                List<CategoryDTO> excessiveCategories) {
        Map<String, CategoryDTO> oldMap = getCategoryMap(old.getCategories());
        Map<String, CategoryDTO> parentMap = getCategoryMap(parent.getCategories());
        Set<CategoryDTO> presentCategoryInNewModel = new HashSet<>();
        for (var i : updated.getCategories()) {
            var oldCategory = oldMap.get(i.getName());
            if (oldCategory != null) {
                if (Objects.equals(oldCategory.getUnits(), i.getUnits())) {
                    var parentMoney = parentMap.get(i.getName());
                    Optional<BigDecimal> number = updateNumber(parentMoney, oldCategory, i);
                    number.ifPresent(m -> updatedCategories.add(oldCategory.updateNumber(m)));
                    presentCategoryInNewModel.add(oldCategory);
                } else {
                    newCategories.add(i);
                }
                continue;
            }
            newCategories.add(i);
        }
        old.getCategories().forEach(c -> {
            if (!presentCategoryInNewModel.remove(c)) {
                excessiveCategories.add(c);
            }
        });
    }

    private Optional<BigDecimal> updateNumber(CategoryDTO parent, CategoryDTO old, CategoryDTO updated) {
        return updateNumber(parent.getNumber(), old.getNumber(), updated.getNumber());
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

    private StatisticResult nothingCategoriesToUpdate(List<CategoryDTO> newCategories, Optional<BigDecimal> newMoney) {
        return StatisticResult.builder()
                .updatedCategories(List.of())
                .excessiveCategories(List.of())
                .newCategories(newCategories)
                .newMoneyValue(newMoney.orElse(null))
                .build();
    }

    private StatisticResult nothingCategoriesToUpdate(Optional<BigDecimal> newMoney) {
        return nothingCategoriesToUpdate(List.of(), newMoney);
    }

    @Override
    public StatisticResult deleteChild(StatisticModel parent, StatisticModel child) {
        if (child == null) {
            return emptyStatistic;
        }
        Optional<BigDecimal> newMoney = subtractChildMoney(parent, child);
        List<CategoryDTO> parentCategories = parent.getCategories();
        if (parentCategories == null || parentCategories.size() == 0) {
            return nothingCategoriesToUpdate(newMoney);
        }
        List<CategoryDTO> excessive = new ArrayList<>();
        List<CategoryDTO> updated = new ArrayList<>();
        groupCategoriesOfDeletedChild(parent, child, updated, excessive);
        return StatisticResult.builder()
                .updatedCategories(updated)
                .excessiveCategories(excessive)
                .newCategories(List.of())
                .newMoneyValue(newMoney.orElse(null))
                .build();
    }

    private void groupCategoriesOfDeletedChild(StatisticModel parent, StatisticModel child,
                                               List<CategoryDTO> updated, List<CategoryDTO> excessive) {
        Map<String, CategoryDTO> parentCategories = getCategoryMap(parent.getCategories());
        for (var i : child.getCategories()) {
            var parentCategory = parentCategories.get(i.getName());
            if (parentCategory != null && Objects.equals(parentCategory.getUnits(), i.getUnits())) {
                BigDecimal newNumber = parentCategory.getNumber().subtract(i.getNumber());
                if(newNumber.compareTo(BigDecimal.ZERO) > 0){
                    updated.add(parentCategory.updateNumber(newNumber));
                    continue;
                }
                excessive.add(parentCategory);
            }
        }
    }

    private Optional<BigDecimal> subtractChildMoney(StatisticModel parent, StatisticModel child) {
        BigDecimal childMoney = child.getMoney();
        return (childMoney != null) ?
                Optional.of(parent.getMoney().subtract(childMoney))
                : Optional.empty();
    }
}

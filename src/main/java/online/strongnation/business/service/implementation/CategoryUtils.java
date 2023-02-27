package online.strongnation.business.service.implementation;

import online.strongnation.business.model.Category;
import online.strongnation.business.model.dto.CategoryDTO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryUtils {

    private static final CategoryDTOComparator comparator = new CategoryDTOComparator();

    public static Map<String, CategoryDTO> getCategoryNameMap(List<? extends CategoryDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(CategoryDTO::getName, Function.identity()));
    }

    public static <T extends Category> Map<T, T> getCategoryMap(List<T> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        Function.identity(),
                        (a, b) -> a,
                        () -> new TreeMap<>(comparator)
                ));
    }


    public static final class CategoryDTOComparator implements Comparator<Category> {
        @Override
        public int compare(Category o1, Category o2) {
            int name = o1.getName().compareTo(o2.getName());
            if (name != 0) {
                return name;
            }
            return compareNullable(o1.getUnits(), o2.getUnits());
        }
    }

    private static <T extends Comparable<T>> int compareNullable(T a, T b) {
        return
                a == null ?
                        (b == null ? 0 : Integer.MIN_VALUE) :
                        (b == null ? Integer.MAX_VALUE : a.compareTo(b));
    }
}

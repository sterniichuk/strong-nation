package online.strongnation.business.service.implementation;

import online.strongnation.business.model.dto.CategoryDTO;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryUtils {
    public static Map<String, CategoryDTO> getCategoryMap(List<CategoryDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(CategoryDTO::getName, Function.identity()));
    }
}

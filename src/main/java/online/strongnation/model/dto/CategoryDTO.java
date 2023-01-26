package online.strongnation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.strongnation.model.entity.Category;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String name;
    private float number;
    private String units;

    public CategoryDTO(Category category){
        this.name = category.getName();
        this.number = category.getNumber();
        this.units = category.getUnits();
    }
}

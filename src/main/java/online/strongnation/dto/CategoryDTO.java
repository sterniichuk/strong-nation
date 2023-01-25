package online.strongnation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import online.strongnation.entity.Category;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private String name;
    private Float number;
    private String units;

    public CategoryDTO(Category category){
        this.name = category.getName();
        this.number = category.getNumber();
        this.units = category.getUnits();
    }

}

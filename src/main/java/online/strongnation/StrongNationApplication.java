package online.strongnation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StrongNationApplication {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        SpringApplication.run(StrongNationApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
//            Region region = new Region();
//            String name = "Rivne";
//            region.setName(name);
//            BigDecimal money = new BigDecimal(1000);
//            region.setMoney(money);
//
//            RegionCategory regionCategory = new RegionCategory();
//            Category category = new Category();
//            category.setNumber(9.f);
//            category.setName("food");
//            category.setUnits("kg");
//            regionCategory.setCategory(category);
//
//            region.setCategories(List.of(regionCategory));
//            repository.save(region);
//
//            List<Region> all = repository.findAll();
//            Region regionSaved = all.get(0);
//            var regionCategorySaved = regionSaved.getCategories().get(0);
//
//            var savedCategory = regionCategorySaved.getCategory();
//
//            Category category1 = categoryRepository.findAll()
//                    .stream().filter(x->category.getName().equals(x.getName()))
//                    .findFirst().orElseThrow(IllegalStateException::new);
        };
    }
}

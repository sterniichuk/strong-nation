package online.strongnation.unit.entity;
    
import online.strongnation.entity.*;
import online.strongnation.exception.RegionNotFoundException;
import online.strongnation.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BlogDataSavingTest {

    @Autowired
    private BlogRepository repository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BlogCategoryRepository blogCategoryRepository;
    @Autowired
    private RegionCategoryRepository regionCategoryRepository;
    @Autowired
    private BlogPhotoRepository blogPhotoRepository;
    @Autowired
    private CountryRepository countryRepository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void simpleSave() {
        Country country = new Country();
        country.setName("Ukraine");
        Region region = new Region();
        country.setRegions(List.of(region));
        final String name = "Rivne";
        region.setName(name);
        final BigDecimal money1 = new BigDecimal(1000);
        region.setMoney(money1);

        final Blog blog = new Blog();
        final String heading = "first blog some heading";
        blog.setHeading(heading);
        final BigDecimal money = new BigDecimal(1000);
        blog.setMoney(money);
        blog.setDate(LocalDateTime.now());
        final BlogPhoto photo = new BlogPhoto();
        final String path = "some/linux/path/like/normal/operation/system";
        photo.setPathToPhoto(path);
        blog.setBlogPhoto(photo);
        final String link = "localH0sT";
        blog.setLink(link);
        region.setBlogs(List.of(blog));
        countryRepository.save(country);

        final var savedRegion = regionRepository.findFirstByName(name).orElseThrow(()->{throw new RegionNotFoundException();});
        assertThat(savedRegion).isEqualTo(region);
        assertThat(savedRegion.getBlogs()).isNotNull();
        assertThat(savedRegion.getBlogs().size()).isEqualTo(1);
        assertThat(savedRegion.getBlogs().get(0)).isEqualTo(blog);
        var blogSaved = repository.findAll().get(0);
        assertThat(blogSaved.getId()).isNotEqualTo(null);
        assertThat(blogSaved.getHeading()).isEqualTo(heading);
        assertThat(blogSaved.getMoney()).isEqualTo(money);
        assertThat(blogSaved.getCategories()).isEqualTo(null);
    }

    @Test
    void saveWithCategory() {
        Country country = new Country();
        country.setName("Ukraine");
        Region region = new Region();
        country.setRegions(List.of(region));
        final String name = "Rivne";
        region.setName(name);
        final BigDecimal money1 = new BigDecimal(1000);
        region.setMoney(money1);

        final Blog blog = new Blog();
        final String heading = "first blog some heading";
        blog.setHeading(heading);
        final BigDecimal money = new BigDecimal(1000);
        blog.setMoney(money);
        blog.setDate(LocalDateTime.now());
        final BlogPhoto photo = new BlogPhoto();
        final String path = "some/linux/path/like/normal/operation/system";
        photo.setPathToPhoto(path);
        blog.setBlogPhoto(photo);
        final String link = "localH0sT";
        blog.setLink(link);
        region.setBlogs(List.of(blog));

        BlogCategory blogCategory = new BlogCategory();
        Category category = new Category();
        category.setNumber(9.f);
        category.setName("food");
        category.setUnits("kg");
        blogCategory.setCategory(category);
        blog.setCategories(List.of(blogCategory));
        countryRepository.save(country);

        var blogSaved = repository.findAll().get(0);
        assertThat(blogSaved.getCategories()).isNotNull();
        assertThat(blogSaved.getCategories().size()).isEqualTo(1);
        BlogCategory blogCategorySavedInBlogCollection = blogSaved.getCategories().get(0);
        assertThat(blogCategorySavedInBlogCollection).isEqualTo(blogCategory);

        Category category1 = categoryRepository.findAll()
                .stream().filter(x->category.getName().equals(x.getName()))
                .findFirst().orElseThrow(IllegalStateException::new);
        assertThat(category).isEqualTo(category1);

        var blogCategories = blogCategoryRepository.findAll();
        assertThat(blogCategories.isEmpty()).isFalse();
        assertThat(blogCategories.get(0)).isEqualTo(blogCategory);

        var regionCategories = regionCategoryRepository.findAll();
        assertThat(regionCategories.isEmpty()).isTrue();
    }

    @Test
    void checkPhotoSaving(){
        Country country = new Country();
        country.setName("Ukraine");
        Region region = new Region();
        country.setRegions(List.of(region));
        final String name = "Rivne";
        region.setName(name);
        final BigDecimal money1 = new BigDecimal(1000);
        region.setMoney(money1);

        final Blog blog = new Blog();
        final String heading = "first blog some heading";
        blog.setHeading(heading);
        final BigDecimal money = new BigDecimal(1000);
        blog.setMoney(money);
        blog.setDate(LocalDateTime.now());
        final BlogPhoto photo = new BlogPhoto();
        final String path = "some/linux/path/like/normal/operation/system";
        photo.setPathToPhoto(path);
        blog.setBlogPhoto(photo);
        final String link = "localH0sT";
        blog.setLink(link);
        region.setBlogs(List.of(blog));
        countryRepository.save(country);

        List<BlogPhoto> all = blogPhotoRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        BlogPhoto savedPhoto = all.get(0);
        assertThat(savedPhoto.getId()).isNotNull();
        assertThat(savedPhoto).isEqualTo(photo);
    }
}
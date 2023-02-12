package online.strongnation.unit.repository;

import online.strongnation.business.model.dto.PostDTO;
import online.strongnation.business.model.entity.Country;
import online.strongnation.business.model.entity.Post;
import online.strongnation.business.model.entity.PostPhoto;
import online.strongnation.business.model.entity.Region;
import online.strongnation.business.repository.CountryRepository;
import online.strongnation.business.repository.PostPhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class PostPhotoRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PostPhotoRepository postPhotoRepository;

    @Test
    void findAllPostPhotoByRegionId() {
        //given
        final Country country = new Country("NaMe");
        String regionName = "some ReGion";
        Region region = new Region(regionName);
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(LocalDateTime.now())
                .link(link)
                .description(heading)
                .build();
        Post postDAO = new Post(post);
        PostPhoto photo = new PostPhoto("weweofijwepfijwefpj");
        postDAO.setPostPhoto(photo);
        region.setPosts(List.of(postDAO));
        country.setRegions(List.of(region));
        countryRepository.save(country);
        //when
        var actual = postPhotoRepository.findAllPostPhotoByRegionId(region.getId()).get(0);
        //then
        assertThat(actual).isEqualTo(photo);
    }

    @Test
    void findAllPostPhotoByCountryId() {
        //given
        final Country country = new Country("NaMe");
        String regionName = "some ReGion";
        Region region = new Region(regionName);
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(LocalDateTime.now())
                .link(link)
                .description(heading)
                .build();
        Post postDAO = new Post(post);
        PostPhoto photo = new PostPhoto("weweofijwepfijwefpj");
        postDAO.setPostPhoto(photo);
        region.setPosts(List.of(postDAO));
        country.setRegions(List.of(region));
        countryRepository.save(country);
        //when
        var actual = postPhotoRepository.findAllPostPhotoByCountryId(country.getId()).get(0);
        //then
        assertThat(actual).isEqualTo(photo);
    }
}
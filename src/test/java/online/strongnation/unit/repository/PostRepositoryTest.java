package online.strongnation.unit.repository;

import online.strongnation.business.model.dto.PostDTO;
import online.strongnation.business.model.entity.Country;
import online.strongnation.business.model.entity.Region;
import online.strongnation.business.repository.CountryRepository;
import online.strongnation.business.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    void findByRegion() {
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
        region.setPostsDTO(List.of(post));
        country.setRegions(List.of(region));
        countryRepository.save(country);
        //when
        var actual = postRepository.findGetPostResponseAllByRegionId(region.getId()).get(0);
        //then
        Long id = actual != null && actual.getId() != null ? actual.getId() : 1L;
        assertThat(actual).isEqualTo(post.getWithId(id).toGetResponse());
    }
    @Test
    void findByCountry() {
        //given
        final Country country = new Country("NaMe");
        final Country country2 = new Country("NaMe2");
        String regionName = "some ReGion";
        Region region = new Region(regionName);
        final String heading = "first post some heading";
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .date(LocalDateTime.now())
                .link(link)
                .description(heading)
                .build();
        region.setPostsDTO(List.of(post));
        country.setRegions(List.of(region));
        countryRepository.save(country2);
        countryRepository.save(country);
        //when
        var actual = postRepository.findAllGetPostResponseByCountryDTObyCountryId(country.getId()).get(0);
        //then
        Long id = actual != null && actual.getId() != null ? actual.getId() : 1L;
        assertThat(actual).isEqualTo(post.getWithId(id).toGetResponseByCountry(regionName));
    }
}
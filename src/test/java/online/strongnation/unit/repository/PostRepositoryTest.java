package online.strongnation.unit.repository;

import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Region;
import online.strongnation.repository.CountryRepository;
import online.strongnation.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
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
        final BigDecimal money = BigDecimal.valueOf(1020.22);
        final String link = "localH0sT";
        final PostDTO post = PostDTO.builder()
                .money(money)
                .date(LocalDateTime.now())
                .link(link)
                .heading(heading)
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
}
package online.strongnation.service.implementation;

import lombok.AllArgsConstructor;
import online.strongnation.exception.*;
import online.strongnation.model.dto.CountryDTO;
import online.strongnation.model.dto.GetPostResponse;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.dto.RegionDTO;
import online.strongnation.model.entity.Country;
import online.strongnation.model.entity.Post;
import online.strongnation.model.entity.PostPhoto;
import online.strongnation.model.entity.Region;
import online.strongnation.model.statistic.StatisticResult;
import online.strongnation.repository.*;
import online.strongnation.service.PostPhotoService;
import online.strongnation.service.PostService;
import online.strongnation.service.StatisticOfEntityUpdater;
import online.strongnation.service.StatisticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static online.strongnation.service.implementation.RequestParameterFixer.*;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final PostRepository postRepository;
    private final StatisticOfEntityUpdater updater;
    private final StatisticService statistic;
    private final PostPhotoService postPhotoService;

    private record Pair(Country country, Region region) {
    }

    private Pair getPairByNames(String clearNameOfCountry, String clearNameOfRegion) {
        Country country = countryRepository.findCountryByNameIgnoreCase(clearNameOfCountry)
                .orElseThrow(() -> new CountryNotFoundException("Country " + clearNameOfCountry + "doesn't exist"));
        Region region = regionRepository.findRegionInCountryByNamesIgnoringCase(clearNameOfCountry, clearNameOfRegion)
                .orElseThrow(() -> new RegionNotFoundException("Region " + clearNameOfRegion + "doesn't exist"));
        return new Pair(country, region);
    }

    private Pair getPairByRegionId(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RegionNotFoundException("Region with id: " + id + "doesn't exist"));
        Country country = regionRepository.findCountryOfRegionById(id)
                .orElseThrow(() -> {
                    String message = "Region with id: " + id + "doesn't belong to any country...";
                    throw new IllegalRegionException(message);
                });
        return new Pair(country, region);
    }

    private Pair getPairByPostId(Long id) {
        Region region = postRepository.findRegionOfPostById(id)
                .orElseThrow(() -> {
                    String message = "Post with id: " +
                            id + "doesn't belong to any region...";
                    throw new IllegalPostException(message);
                });
        Country country = regionRepository.findCountryOfRegionById(region.getId())
                .orElseThrow(() -> {
                    String message = "Region with id: " +
                            region.getId() + "doesn't belong to any country...";
                    throw new IllegalRegionException(message);
                });
        return new Pair(country, region);
    }

    @Override
    @Transactional
    public PostDTO create(final PostDTO post, String countryName, String regionName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        final String clearNameOfRegion = checkAndNormalizeRegion(regionName);
        final PostDTO checkedPost = checkAndNormalizeNewPost(post);
        Pair pair = getPairByNames(clearNameOfCountry, clearNameOfRegion);
        return create(checkedPost, pair.country, pair.region);
    }

    private PostDTO create(PostDTO checkedPost, Country country, Region region) {
        Post postDAO = new Post(checkedPost);
        postDAO.setRegion(region);
        RegionDTO oldRegion = new RegionDTO(region);

        StatisticResult regionResult = statistic.addChildToParent(oldRegion, checkedPost);
        updateParentDAOs(postDAO, region, country, oldRegion, regionResult);
        return checkedPost.getWithId(postDAO.getId());
    }

    @Override
    @Transactional
    public PostDTO create(PostDTO post, Long id) {
        final PostDTO checkedPost = checkAndNormalizeNewPost(post);
        Pair pair = getPairByRegionId(id);
        return create(checkedPost, pair.country, pair.region);
    }

    @Override
    public List<GetPostResponse> all(String countryName, String regionName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        final String clearNameOfRegion = checkAndNormalizeRegion(regionName);
        var region = regionRepository.findRegionDTOInCountryByNamesIgnoringCase(clearNameOfCountry, clearNameOfRegion)
                .orElseThrow(() -> new RegionNotFoundException("Region " + clearNameOfRegion + "doesn't exist"));
        return postRepository.findGetPostResponseAllByRegionId(region.getId());
    }

    @Override
    public List<GetPostResponse> all(Long id) {
        return postRepository.findGetPostResponseAllByRegionId(id);
    }

    @Override
    public PostDTO get(Long id) {
        return postRepository.findPostDTOById(id)
                .orElseThrow(() -> new PostNotFoundException("There is no post with id: " + id));
    }

    @Override
    @Transactional
    public PostDTO update(PostDTO post) {
        final PostDTO checkedPost = checkAndNormalizeUpdatedPost(post);
        final Post postDAO = postRepository.findById(post.getId())
                .orElseThrow(() -> new PostNotFoundException("There is no post with id: " + post.getId()));
        Pair pair = getPairByPostId(post.getId());
        RegionDTO oldRegion = new RegionDTO(pair.region);
        PostDTO old = new PostDTO(postDAO);
        var statisticResultOfPost = statistic.updateSelf(old, checkedPost);
        updater.update(postDAO, statisticResultOfPost);
        StatisticResult regionResult = statistic
                .updateChild(new RegionDTO(pair.region), old, checkedPost);
        updateParentDAOs(postDAO, pair.region, pair.country, oldRegion, regionResult);
        return checkedPost;
    }

    private void updateParentDAOs(Post postDAO, Region region, Country country,
                                  RegionDTO oldRegion, StatisticResult regionResult) {
        updateParentDAOs(region, country, oldRegion, regionResult);
        postRepository.save(postDAO);
    }

    private void updateParentDAOs(Region region, Country country,
                                  RegionDTO oldRegion, StatisticResult regionResult) {
        updater.update(region, regionResult);
        StatisticResult countryResult = statistic.updateChild(new CountryDTO(country), oldRegion, new RegionDTO(region));
        updater.update(country, countryResult);
        regionRepository.save(region);
        countryRepository.save(country);
    }

    @Override
    @Transactional
    public PostDTO delete(Long id) {
        final var post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("There is no post with id: " + id));
        Pair pair = getPairByPostId(id);
        RegionDTO oldRegion = new RegionDTO(pair.region);
        PostDTO oldPost = new PostDTO(post);
        StatisticResult regionResult = statistic
                .deleteChild(new RegionDTO(pair.region), oldPost);
        updateParentDAOs(pair.region, pair.country, oldRegion, regionResult);
        deletePhotoIfExists(post);
        postRepository.deleteById(id);
        return oldPost;
    }

    @Override
    @Transactional
    public List<PostDTO> deleteAllByRegionId(Long id) {
        Pair pair = getPairByRegionId(id);
        return deleteAll(pair.country, pair.region);
    }

    @Override
    @Transactional
    public List<PostDTO> deleteAll(String countryName, String regionName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        final String clearNameOfRegion = checkAndNormalizeRegion(regionName);
        Pair pair = getPairByNames(clearNameOfCountry, clearNameOfRegion);
        return deleteAll(pair.country, pair.region);
    }

    private List<PostDTO> deleteAll(Country country, Region region) {
        var list = postRepository.findAllByRegionId(region.getId());
        List<PostDTO> old = new LinkedList<>();
        list.forEach(post -> {
            PostDTO oldPost = new PostDTO(post);
            old.add(oldPost);
            StatisticResult regionResult = statistic.deleteChild(new RegionDTO(region), oldPost);
            RegionDTO oldRegion = new RegionDTO(region);
            updater.update(region, regionResult);
            StatisticResult countryResult = statistic
                    .updateChild(new CountryDTO(country), oldRegion, new RegionDTO(region));
            updater.update(country, countryResult);
            deletePhotoIfExists(post);
            postRepository.deleteById(post.getId());
        });
        regionRepository.save(region);
        countryRepository.save(country);
        return old;
    }

    private void deletePhotoIfExists(Post post) {
        PostPhoto postPhoto = post.getPostPhoto();
        if (postPhoto != null) {
            postPhotoService.deletePhotoByPostPhoto(postPhoto);
        }
    }
}

package online.strongnation.business.service.implementation;

import lombok.AllArgsConstructor;
import online.strongnation.business.exception.*;
import online.strongnation.business.model.dto.*;
import online.strongnation.business.model.entity.Country;
import online.strongnation.business.model.entity.Post;
import online.strongnation.business.model.entity.PostPhoto;
import online.strongnation.business.model.entity.Region;
import online.strongnation.business.model.statistic.StatisticResult;
import online.strongnation.business.repository.CountryRepository;
import online.strongnation.business.repository.PostRepository;
import online.strongnation.business.repository.RegionRepository;
import online.strongnation.business.service.PostPhotoService;
import online.strongnation.business.service.PostService;
import online.strongnation.business.service.StatisticOfEntityUpdater;
import online.strongnation.business.service.StatisticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static online.strongnation.business.service.implementation.RequestParameterFixer.*;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final RegionRepository regionRepository;
    private final CountryRepository countryRepository;
    private final PostRepository postRepository;
    private final StatisticOfEntityUpdater updater;
    private final StatisticService statistic;
    private final PostPhotoService postPhotoService;

    private record Location(Country country, Region region) {
    }

    private Location getLocationByNames(String clearNameOfCountry, String clearNameOfRegion) {
        Country country = countryRepository.findCountryByNameIgnoreCase(clearNameOfCountry)
                .orElseThrow(() -> new CountryNotFoundException("Country " + clearNameOfCountry + " doesn't exist"));
        Region region = regionRepository.findRegionInCountryByNamesIgnoringCase(clearNameOfCountry, clearNameOfRegion)
                .orElseThrow(() -> new RegionNotFoundException("Region " + clearNameOfRegion + " doesn't exist"));
        return new Location(country, region);
    }

    private Location getLocationByRegionId(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RegionNotFoundException("Region with id: " + id + " doesn't exist"));
        Country country = regionRepository.findCountryOfRegionById(id)
                .orElseThrow(() -> {
                    String message = "Region with id: " + id + " doesn't belong to any country...";
                    throw new IllegalRegionException(message);
                });
        return new Location(country, region);
    }

    private Location getLocationByPostId(Long id) {
        Region region = postRepository.findRegionOfPostById(id)
                .orElseThrow(() -> {
                    String message = "Post with id: " +
                            id + " doesn't belong to any region...";
                    throw new IllegalPostException(message);
                });
        Country country = regionRepository.findCountryOfRegionById(region.getId())
                .orElseThrow(() -> {
                    String message = "Region with id: " +
                            region.getId() + " doesn't belong to any country...";
                    throw new IllegalRegionException(message);
                });
        return new Location(country, region);
    }

    @Override
    @Transactional
    public PostDTO create(final PostDTO post, String countryName, String regionName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        final String clearNameOfRegion = checkAndNormalizeRegion(regionName);
        final PostDTO checkedPost = checkAndNormalizeNewPost(post);
        Location location = getLocationByNames(clearNameOfCountry, clearNameOfRegion);
        return create(checkedPost, location.country, location.region);
    }

    private PostDTO create(PostDTO checkedPost, Country country, Region region) {
        Post postDAO = new Post(checkedPost);
        postDAO.setRegion(region);
        RegionDTO oldRegion = new RegionDTO(region);

        StatisticResult regionResult = statistic.addChildToParent(oldRegion, checkedPost);
        updateParentDAOs(postDAO, region, country, oldRegion, regionResult);
        return new PostDTO(postDAO);
    }

    @Override
    @Transactional
    public PostDTO create(PostDTO post, Long id) {
        final PostDTO checkedPost = checkAndNormalizeNewPost(post);
        Location location = getLocationByRegionId(id);
        return create(checkedPost, location.country, location.region);
    }

    @Override
    public List<GetPostResponse> all(String countryName, String regionName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        final String clearNameOfRegion = checkAndNormalizeRegion(regionName);
        var region = regionRepository.findRegionDTOInCountryByNamesIgnoringCase(clearNameOfCountry, clearNameOfRegion)
                .orElseThrow(() -> new RegionNotFoundException("Region " + clearNameOfRegion + " doesn't exist"));
        return postRepository.findGetPostResponseAllByRegionId(region.getId())
                .stream().sorted(Comparator.comparing(GetPostResponse::getDate).reversed()).toList();
    }

    @Override
    public List<GetPostResponseByCountryDTO> all(String countryName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        var country = countryRepository.findCountryByNameIgnoreCase(clearNameOfCountry)
                .orElseThrow(() -> new CountryNotFoundException("Country " + countryName + " doesn't exist"));
        return postRepository.findAllGetPostResponseByCountryDTObyCountryId(country.getId())
                .stream().sorted(Comparator.comparing(GetPostResponseByCountryDTO::getDate).reversed()).toList();

    }

    @Override
    public List<GetPostResponse> all(Long id) {
        return postRepository.findGetPostResponseAllByRegionId(id)
                .stream().sorted(Comparator.comparing(GetPostResponse::getDate).reversed()).toList();

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
        Location location = getLocationByPostId(post.getId());
        Region region = location.region;
        if (region.getName().equalsIgnoreCase(checkedPost.getRegion())) {// region is the same
            RegionDTO oldRegion = new RegionDTO(region);
            PostDTO old = new PostDTO(postDAO);
            updatePost(postDAO, checkedPost, old);
            StatisticResult regionResult = statistic
                    .updateChild(new RegionDTO(region), old, checkedPost);
            updateParentDAOs(postDAO, region, location.country, oldRegion, regionResult);
            return checkedPost;
        }
        //region is changed for this post
        return movePostToAnotherRegion(location, postDAO, checkedPost);
    }

    private PostDTO movePostToAnotherRegion(Location location, Post postDAO, PostDTO newPost) {
        final Region targetRegion = getTargetRegion(location, newPost);//first, we should check if the target region exists, and then we can do the math
        final RegionDTO oldRegion = new RegionDTO(location.region);
        final PostDTO old = new PostDTO(postDAO);
        postDAO.setRegion(null);
        updatePost(postDAO, newPost, old);
        StatisticResult regionResult = statistic.deleteChild(oldRegion, old);
        updater.update(location.region, regionResult);
        StatisticResult countryResult = statistic
                .updateChild(new CountryDTO(location.country), oldRegion, new RegionDTO(location.region));
        updater.update(location.country, countryResult);

        RegionDTO oldTargetRegion = new RegionDTO(targetRegion);
        StatisticResult statisticResultOfRegionWithAddedPost = statistic
                .addChildToParent(oldTargetRegion, newPost);
        updater.update(targetRegion, statisticResultOfRegionWithAddedPost);
        StatisticResult countryResultAfterMovingPost = statistic
                .updateChild(new CountryDTO(location.country),
                        oldTargetRegion, new RegionDTO(targetRegion));
        updater.update(location.country, countryResultAfterMovingPost);
        postDAO.setRegion(targetRegion);
        postRepository.save(postDAO);
        regionRepository.save(location.region);
        regionRepository.save(targetRegion);
        countryRepository.save(location.country);
        return newPost;
    }

    private void updatePost(Post postDAO, PostDTO checkedPost, PostDTO old) {
        StatisticResult statisticResultOfPost = statistic.updateSelf(old, checkedPost);
        updater.update(postDAO, statisticResultOfPost);
        updateStaticFields(postDAO, checkedPost);
    }

    private Region getTargetRegion(Location location, PostDTO checkedPost) {
        return regionRepository
                .findRegionInCountryByNamesIgnoringCase(location.country.getName(), checkedPost.getRegion())
                .orElseThrow(() -> {
                    final String message = "There is no region with name"
                            + checkedPost.getRegion() + " in country " +
                            location.country.getName() +
                            ". You can't move post to region that doesn't exist.";
                    throw new IllegalPostException(message);
                });
    }

    private void updateStaticFields(Post postDAO, PostDTO newPost) {
        postDAO.setDate(newPost.getDate());
        postDAO.setDescription(newPost.getDescription());
        postDAO.setLink(newPost.getLink());
        Boolean important = newPost.getImportant();
        if(important != null){
            postDAO.setImportant(important);
        }
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
        Location location = getLocationByPostId(id);
        RegionDTO oldRegion = new RegionDTO(location.region);
        PostDTO oldPost = new PostDTO(post);
        StatisticResult regionResult = statistic
                .deleteChild(new RegionDTO(location.region), oldPost);
        updateParentDAOs(location.region, location.country, oldRegion, regionResult);
        deletePhotoIfExists(post);
        postRepository.deleteById(id);
        return oldPost;
    }


    @Override
    @Transactional
    public List<PostDTO> deleteAllByRegionId(Long id) {
        Location location = getLocationByRegionId(id);
        return deleteAll(location.country, location.region);
    }

    @Override
    @Transactional
    public List<PostDTO> deleteAll(String countryName, String regionName) {
        final String clearNameOfCountry = checkAndNormalizeCountry(countryName);
        final String clearNameOfRegion = checkAndNormalizeRegion(regionName);
        Location location = getLocationByNames(clearNameOfCountry, clearNameOfRegion);
        return deleteAll(location.country, location.region);
    }

    @Override
    @Transactional
    public Boolean setImportant(Long id, Boolean important) {
        if(id == null){
            throw new IllegalPostException("Id of post is null");
        }
        if(important == null){
            throw new IllegalPostException("Important is null");
        }
        if(!postRepository.existsById(id)){
            throw new PostNotFoundException("There is no post with id: " + id);
        }
        postRepository.setImportantOfPostById(id, important);
        return important;
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

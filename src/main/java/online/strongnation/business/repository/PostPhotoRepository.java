package online.strongnation.business.repository;

import online.strongnation.business.model.entity.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {
    @Query("SELECT post.postPhoto" +
            " FROM Post post JOIN post.region reg WHERE reg.id = :id")
    List<PostPhoto> findAllPostPhotoByRegionId(Long id);

    @Query("SELECT post.postPhoto" +
            " FROM Post post JOIN post.region.country c WHERE c.id = :id")
    List<PostPhoto> findAllPostPhotoByCountryId(Long id);
}
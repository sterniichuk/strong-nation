package online.strongnation.business.repository;

import online.strongnation.business.model.dto.GetPostResponse;
import online.strongnation.business.model.dto.GetPostResponseByCountryDTO;
import online.strongnation.business.model.dto.PostDTO;
import online.strongnation.business.model.entity.Post;
import online.strongnation.business.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new online.strongnation.business.model.dto.GetPostResponse(post)" +
            " FROM Post post JOIN post.region reg WHERE reg.id = :id")
    List<GetPostResponse> findGetPostResponseAllByRegionId(Long id);

    @Query("SELECT new online.strongnation.business.model.dto.PostDTO(post)" +
            " FROM Post post JOIN post.region reg WHERE reg.id = :id")
    List<PostDTO> findAllPostDTOByRegionId(Long id);

    @Query("SELECT new online.strongnation.business.model.dto.GetPostResponseByCountryDTO(post)" +
            " FROM Post post JOIN post.region.country c WHERE c.id = :id")
    List<GetPostResponseByCountryDTO> findAllGetPostResponseByCountryDTObyCountryId(Long id);

    @Query("SELECT post FROM Post post JOIN post.region reg WHERE reg.id = :id")
    List<Post> findAllByRegionId(Long id);

    @Query("select new online.strongnation.business.model.dto.PostDTO(p) from Post p where p.id = :id")
    Optional<PostDTO> findPostDTOById(Long id);

    @Query("SELECT reg FROM Post p JOIN p.region reg WHERE p.id = :id")
    Optional<Region> findRegionOfPostById(Long id);
}

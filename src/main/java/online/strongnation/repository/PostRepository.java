package online.strongnation.repository;

import online.strongnation.model.dto.GetPostResponse;
import online.strongnation.model.dto.PostDTO;
import online.strongnation.model.entity.Post;
import online.strongnation.model.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new online.strongnation.model.dto.GetPostResponse(post)" +
            " FROM Post post JOIN post.region reg WHERE reg.id = :id")
    List<GetPostResponse> findGetPostResponseAllByRegionId(Long id);

    @Query("SELECT new online.strongnation.model.dto.PostDTO(post)" +
            " FROM Post post JOIN post.region reg WHERE reg.id = :id")
    List<PostDTO> findPostDTOAllByRegionId(Long id);

    @Query("select new online.strongnation.model.dto.PostDTO(p) from Post p where p.id = :id")
    Optional<PostDTO> findPostDTOById(Long id);

    @Query("SELECT reg FROM Post p JOIN p.region reg WHERE p.id = :id")
    Optional<Region> findRegionOfPostById(Long id);
}

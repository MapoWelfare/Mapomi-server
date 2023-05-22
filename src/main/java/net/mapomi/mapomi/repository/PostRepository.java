package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p left join fetch p.disabled where p.id = :id")
    Optional<Post> findByIdFetchDisabled(@Param("id") Long id);

    @Query("select p from Post p left join p.disabled order by p.createdDate desc")
    Page<Post> findAllPageable(Pageable pageable);

    @Query("select p from Post p left join p.disabled where p.title like %:keyword% or p.content like %:keyword% order by p.createdDate desc")
    Page<Post> findSearchedPageable(@Param("keyword")String keyword, Pageable pageable);
}

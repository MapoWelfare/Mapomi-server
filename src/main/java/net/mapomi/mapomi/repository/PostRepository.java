package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p left join fetch p.disabled where p.id = :id")
    Optional<Post> findByIdFetchDisabled(@Param("id") Long id);
}

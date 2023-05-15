package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

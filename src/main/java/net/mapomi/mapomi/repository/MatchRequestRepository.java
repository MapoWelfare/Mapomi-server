package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.MatchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MatchRequestRepository extends JpaRepository<MatchRequest,Long> {

    @Query("select mr from MatchRequest mr left join fetch mr.abled where mr.post.id = :id")
    List<MatchRequest> getMatchRequestByPostIdFetchAbled(@Param("id") Long postId);
}

package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.Accompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccompanyRepository extends JpaRepository<Accompany, Long> {
    @Query("select a from Accompany a left join fetch a.disabled where a.id = :id")
    Optional<Accompany> findByIdFetchDisabled(@Param("id") Long id);

    @Query("select a from Accompany a left join a.disabled order by a.createdDate desc")
    Page<Accompany> findAllPageable(Pageable pageable);

    @Query("select a from Accompany a left join a.disabled where a.title like %:keyword% or a.content like %:keyword% order by a.createdDate desc")
    Page<Accompany> findSearchedPageable(@Param("keyword")String keyword, Pageable pageable);

    @Query("select a from Accompany a left join fetch a.matchRequests where a.id = :id")
    Optional<Accompany> findByIdFetchMatchRequests(@Param("id") Long id);
}

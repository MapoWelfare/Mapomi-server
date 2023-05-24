package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.Help;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HelpRepository extends JpaRepository<Help, Long> {
    @Query("select h from Help h left join fetch h.disabled where h.id = :id")
    Optional<Help> findByIdFetchDisabled(@Param("id") Long id);

    @Query("select h from Help h left join h.disabled order by h.createdDate desc")
    Page<Help> findAllPageable(Pageable pageable);

    @Query("select h from Help h left join h.disabled where h.title like %:keyword% or h.content like %:keyword% order by h.createdDate desc")
    Page<Help> findSearchedPageable(@Param("keyword")String keyword, Pageable pageable);
}
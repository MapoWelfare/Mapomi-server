package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.user.Abled;
import net.mapomi.mapomi.domain.user.Disabled;
import net.mapomi.mapomi.domain.user.Observer;
import net.mapomi.mapomi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email")String email);

    @Query("select u from User u where u.nickName = :nickName")
    Optional<User> findByNickName(@Param("nickName")String nickName);

    @Query("select d from Disabled d where d.id = :id")
    Optional<Disabled> findDisabledById(@Param("id")Long id);


    @Query("select a from Abled a left join fetch a.matchRequests where a.id = :id")
    Optional<Abled> findAbledByIdFetchMatchRequest(@Param("id")Long id);

    @Query("select a from Abled a where a.id = :id")
    Optional<Abled> findAbledById(@Param("id")Long id);

    @Query("select o from Observer o where o.id = :id")
    Optional<Observer> findObserverById(@Param("id")Long id);

}

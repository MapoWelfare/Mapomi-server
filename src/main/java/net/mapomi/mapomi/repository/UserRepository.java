package net.mapomi.mapomi.repository;

import net.mapomi.mapomi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.accountId = :accountId")
    Optional<User> findByAccountId(@Param("accountId")String accountId);

    @Query("select u from User u where u.nickName = :nickName")
    Optional<User> findByNickName(@Param("nickName")String nickName);
}

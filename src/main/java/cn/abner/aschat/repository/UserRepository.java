package cn.abner.aschat.repository;

import cn.abner.aschat.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Integer countByEmail(@Param("email") String email);

    Optional<User> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    List<User> findUsersByUidIsNot(Long uid);
}

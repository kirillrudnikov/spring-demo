package me.flainsky.society.repository;

import me.flainsky.society.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getById(Long id);
    User getByUsername(String username);
    User getByEmail(String email);

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndUsername(String email, String username);

    Optional<User> findByEmailOrUsername(String email, String username);

}
package ru.practicum.main.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id IN :ids ORDER BY u.id")
    List<User> findByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM User u ORDER BY u.id")
    List<User> findAllOrdered();
}

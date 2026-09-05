package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.model.Compilation;

import java.util.Optional;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @EntityGraph(attributePaths = {"events"})
    Page<Compilation> findByPinned(Boolean pinned, Pageable pageable);

    @EntityGraph(attributePaths = {"events"})
    Page<Compilation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"events"})
    Optional<Compilation> findById(Long id);

    // Альтернативное решение через JPQL с JOIN FETCH
    @Query("SELECT c FROM Compilation c LEFT JOIN FETCH c.events WHERE c.id = :id")
    Optional<Compilation> findByIdWithEvents(Long id);

    // Методы для проверки уникальности
    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);
}
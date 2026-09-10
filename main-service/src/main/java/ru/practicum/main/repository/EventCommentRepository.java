package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.model.EventComment;
import ru.practicum.main.model.enums.EventCommentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
    @Query("SELECT c FROM EventComment c " +
            "JOIN FETCH c.author " +
            "JOIN FETCH c.event " +
            "WHERE c.event.id = :eventId " +
            "AND c.status = :status")
    Page<EventComment> findByEventIdAndStatus(
            @Param("eventId") Long eventId,
            @Param("status") EventCommentStatus status,
            Pageable pageable
    );

    @Query(value = "SELECT c FROM EventComment c " +
            "JOIN FETCH c.author " +
            "JOIN FETCH c.event " +
            "WHERE c.status = :status")
    Page<EventComment> findByStatus(
            @Param("status") EventCommentStatus status,
            Pageable pageable);

    @Query("SELECT c FROM EventComment c " +
            "JOIN FETCH c.author " +
            "WHERE c.event.id = :eventId " +
            "AND c.author.id = :userId")
    List<EventComment> findByEventIdAndAuthorId(
            @Param("eventId") Long eventId,
            @Param("userId") Long userId);

    @Override
    @Query("SELECT c FROM EventComment c " +
            "JOIN FETCH c.author " +
            "JOIN FETCH c.event " +
            "WHERE c.id = :id")
    Optional<EventComment> findById(@Param("id") Long id);

    @Override
    @Query(value = "SELECT c FROM EventComment c " +
            "JOIN FETCH c.author " +
            "JOIN FETCH c.event",
            countQuery = "SELECT COUNT(c) FROM EventComment c")
    Page<EventComment> findAll(Pageable pageable);
}
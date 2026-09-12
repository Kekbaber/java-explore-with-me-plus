package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.model.EventComment;
import ru.practicum.main.model.enums.EventCommentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
    @EntityGraph(attributePaths = {"author", "event"})
    Page<EventComment> findByEventIdAndStatus(Long eventId, EventCommentStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "event"})
    Page<EventComment> findByStatus(EventCommentStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "event"})
    List<EventComment> findByEventIdAndAuthorIdAndStatusOrderByCreatedDesc(
            Long eventId,
            Long userId,
            EventCommentStatus status
    );

    @Override
    @EntityGraph(attributePaths = {"author", "event"})
    Optional<EventComment> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"author", "event"})
    Page<EventComment> findAll(Pageable pageable);
}
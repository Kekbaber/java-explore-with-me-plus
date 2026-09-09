package ru.practicum.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.model.EventComment;
import ru.practicum.main.model.enums.EventCommentStatus;

import java.util.List;

@Repository
public interface EventCommentRepository extends JpaRepository<EventComment, Long> {
    Page<EventComment> findByEventIdAndStatus(Long eventId, EventCommentStatus status, Pageable pageable);

    Page<EventComment> findByStatus(EventCommentStatus status, Pageable pageable);

    List<EventComment> findByEventIdAndAuthorIdAndStatus(Long eventId, Long userId, EventCommentStatus status);
}

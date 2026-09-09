package ru.practicum.main.EventComment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.main.model.*;
import ru.practicum.main.model.enums.EventCommentStatus;
import ru.practicum.main.model.enums.EventState;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventCommentRepository {

    @Autowired
    private ru.practicum.main.repository.EventCommentRepository eventCommentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private Category category;
    private Location location;
    private Event event;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .build();
        entityManager.persist(user);

        category = Category.builder()
                .name("Test Category")
                .build();
        entityManager.persist(category);

        location = Location.builder()
                .lat(55.75f)
                .lon(37.61f)
                .build();

        event = Event.builder()
                .annotation("Test annotation")
                .description("Test description")
                .eventDate(LocalDateTime.now().plusDays(1))
                .createdOn(LocalDateTime.now())
                .publishedOn(null)
                .paid(false)
                .participantLimit(10)
                .requestModeration(true)
                .title("Test Event")
                .state(EventState.PENDING)
                .location(location)
                .initiator(user)
                .category(category)
                .build();
        entityManager.persist(event);

        EventComment commentWaiting = EventComment.builder()
                .content("Waiting comment")
                .author(user)
                .event(event)
                .status(EventCommentStatus.WAITING)
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(commentWaiting);

        EventComment commentApproved = EventComment.builder()
                .content("Approved comment")
                .author(user)
                .event(event)
                .status(EventCommentStatus.APPROVED)
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(commentApproved);

        EventComment commentRejected = EventComment.builder()
                .content("Rejected comment")
                .author(user)
                .event(event)
                .status(EventCommentStatus.REJECTED)
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(commentRejected);

        entityManager.flush();
    }

    @Test
    @DisplayName("findByEventIdAndStatus - должен найти WAITING комментарии по eventId")
    void findByEventIdAndStatus_shouldFindWaitingComments() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<EventComment> page = eventCommentRepository.findByEventIdAndStatus(
                event.getId(),
                EventCommentStatus.WAITING,
                pageable
        );

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(EventCommentStatus.WAITING, page.getContent().get(0).getStatus());
    }

    @Test
    @DisplayName("findByEventIdAndStatus - должен найти APPROVED комментарии по eventId")
    void findByEventIdAndStatus_shouldFindApprovedComments() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<EventComment> page = eventCommentRepository.findByEventIdAndStatus(
                event.getId(),
                EventCommentStatus.APPROVED,
                pageable
        );

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(EventCommentStatus.APPROVED, page.getContent().get(0).getStatus());
    }

    @Test
    @DisplayName("findByEventIdAndStatus - должен вернуть пустую страницу для несуществующего события")
    void findByEventIdAndStatus_shouldReturnEmptyPage_whenEventNotFound() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<EventComment> page = eventCommentRepository.findByEventIdAndStatus(
                999L,
                EventCommentStatus.WAITING,
                pageable
        );

        assertNotNull(page);
        assertTrue(page.getContent().isEmpty());
        assertEquals(0, page.getTotalElements());
    }

    @Test
    @DisplayName("findByEventIdAndStatus - должен поддерживать пагинацию")
    void findByEventIdAndStatus_shouldSupportPagination() {
        // Добавляем ещё один WAITING комментарий
        EventComment anotherWaiting = EventComment.builder()
                .content("Another waiting")
                .author(user)
                .event(event)
                .status(EventCommentStatus.WAITING)
                .created(LocalDateTime.now())
                .build();
        entityManager.persist(anotherWaiting);
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 1);

        Page<EventComment> page = eventCommentRepository.findByEventIdAndStatus(
                event.getId(),
                EventCommentStatus.WAITING,
                pageable
        );

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
    }

    @Test
    @DisplayName("findByStatus - должен найти все WAITING комментарии")
    void findByStatus_shouldFindAllWaitingComments() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<EventComment> page = eventCommentRepository.findByStatus(
                EventCommentStatus.WAITING,
                pageable
        );

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        page.getContent().forEach(c -> assertEquals(EventCommentStatus.WAITING, c.getStatus()));
    }

    @Test
    @DisplayName("findByStatus - должен найти все APPROVED комментарии")
    void findByStatus_shouldFindAllApprovedComments() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<EventComment> page = eventCommentRepository.findByStatus(
                EventCommentStatus.APPROVED,
                pageable
        );

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        page.getContent().forEach(c -> assertEquals(EventCommentStatus.APPROVED, c.getStatus()));
    }

    @Test
    @DisplayName("findByStatus - должен поддерживать пагинацию")
    void findByStatus_shouldSupportPagination() {
        // Добавляем ещё два WAITING комментария
        for (int i = 0; i < 2; i++) {
            EventComment additional = EventComment.builder()
                    .content("Additional " + i)
                    .author(user)
                    .event(event)
                    .status(EventCommentStatus.WAITING)
                    .created(LocalDateTime.now())
                    .build();
            entityManager.persist(additional);
        }
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 2);

        Page<EventComment> page = eventCommentRepository.findByStatus(
                EventCommentStatus.WAITING,
                pageable
        );

        assertNotNull(page);
        assertEquals(2, page.getContent().size());
        assertEquals(3, page.getTotalElements());
    }
}

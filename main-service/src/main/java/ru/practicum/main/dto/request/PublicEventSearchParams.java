package ru.practicum.main.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicEventSearchParams {
    @Builder.Default
    @Size(min = 1, max = 7000)
    private String text = null;

    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;

    @Builder.Default
    private Boolean onlyAvailable = false;

    @Builder.Default
    @Pattern(regexp = "EVENT_DATE|VIEWS", message = "must be one of EVENT_DATE, VIEWS")
    private String sort = "EVENT_DATE";

    @PositiveOrZero
    @Builder.Default
    private Integer from = 0;

    @Positive
    @Builder.Default
    private Integer size = 10;
}

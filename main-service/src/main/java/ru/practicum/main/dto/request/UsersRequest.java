package ru.practicum.main.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UsersRequest {

    private List<Long> ids;

    private Integer from = 0;

    private Integer size = 10;

}

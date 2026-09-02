package ru.practicum.main.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersRequest {

    private List<Long> ids;

    @Min(value = 0, message = "From must be >= 0")
    private Integer from = 0;

    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 20;

}

package com.fyp.hotel.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaginationRequestDTO {

    @Min(value = 1, message = "Page number must be at least 1")
    private int pageNumber = 1;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private int pageSize = 10;

    private String sortBy;
}

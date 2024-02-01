package com.fyp.hotel.dto.vendorDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@RequiredArgsConstructor
@Setter
@Getter
public class ReportDto {

    @NotNull
    @Length(min = 3, max = 50)
    private String title;
    @NotNull
    @Length(min = 3, max = 2000)
    private String description;
}

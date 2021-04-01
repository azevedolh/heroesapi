package com.digitalinnovationone.heroesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoviesDTO {
    @NotNull(message = "new movies number must be informed")
    @Max(500)
    @Min(0)
    private Integer movies;
}

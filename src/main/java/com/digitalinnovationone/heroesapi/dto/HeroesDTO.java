package com.digitalinnovationone.heroesapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroesDTO {
    @Null(message = "id should not be informed")
    private String id;

    @NotBlank(message = "name must be informed")
    @Size(min = 1, max = 200)
    private String name;

    @NotBlank(message = "universe must be informed")
    @Size(min = 1, max = 200)
    private String universe;

    @NotNull(message = "movies number must be informed")
    @Max(500)
    @Min(0)
    private Integer movies;
}

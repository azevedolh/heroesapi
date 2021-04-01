package com.digitalinnovationone.heroesapi.builder;

import com.digitalinnovationone.heroesapi.dto.HeroesDTO;
import lombok.Builder;

@Builder
public class HeroesDTOBuilder {

    @Builder.Default
    private String id = "1";

    @Builder.Default
    private String name = "Superman";

    @Builder.Default
    private String universe = "DC";

    @Builder.Default
    private Integer movies = 3;

    public HeroesDTO toHeroesDTO() {
        return new HeroesDTO(
                id,
                name,
                universe,
                movies
        );
    }
}

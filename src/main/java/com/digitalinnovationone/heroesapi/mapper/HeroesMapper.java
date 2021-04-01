package com.digitalinnovationone.heroesapi.mapper;

import com.digitalinnovationone.heroesapi.document.Heroes;
import com.digitalinnovationone.heroesapi.dto.HeroesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HeroesMapper {
    HeroesMapper INSTANCE = Mappers.getMapper(HeroesMapper.class);

    Heroes toModel(HeroesDTO heroesDTO);

    HeroesDTO toDTO(Heroes heroes);
}

package com.digitalinnovationone.heroesapi.service;

import com.digitalinnovationone.heroesapi.builder.HeroesDTOBuilder;
import com.digitalinnovationone.heroesapi.document.Heroes;
import com.digitalinnovationone.heroesapi.dto.HeroesDTO;
import com.digitalinnovationone.heroesapi.exception.HeroAlreadyRegisteredException;
import com.digitalinnovationone.heroesapi.exception.HeroIdNotFoundException;
import com.digitalinnovationone.heroesapi.mapper.HeroesMapper;
import com.digitalinnovationone.heroesapi.repository.HeroesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;
import static pl.rzrz.assertj.reactor.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HeroesServiceTest {

    @Mock
    HeroesRepository heroesRepository;

    HeroesMapper heroesMapper = HeroesMapper.INSTANCE;

    @InjectMocks
    HeroesService heroesService;

    @Test
    void whenFindAllIsInvokedThenItShouldReturnAFluxOfHeroes() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findAll()).thenReturn(() -> Collections.singletonList(mockHero).iterator());

        // then
        Flux<HeroesDTO> heroesDTOFLux = heroesService.findAll();
        assertThat(heroesDTOFLux).completes().emitsCount(1).emits(mockHeroDTO);
    }

    @Test
    void whenIdIsProvidedThenItShouldReturnAHero() throws HeroIdNotFoundException {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.of(mockHero));

        // then
        Mono<HeroesDTO> heroesDTOMono = heroesService.findById(mockHeroDTO.getId());
        assertThat(heroesDTOMono).completes().emits(mockHeroDTO);
    }

    @Test
    void whenInvalidIdIsProvidedThenItShouldThrowAnException() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(HeroIdNotFoundException.class)
                .isThrownBy(() -> heroesService.findById(mockHeroDTO.getId()));
    }

    @Test
    void whenAValidheroIsInformedThenANewheroShouldBeSaved() throws HeroAlreadyRegisteredException {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findByName(mockHeroDTO.getName())).thenReturn(Optional.empty());
        when(heroesRepository.save(mockHero)).thenReturn(mockHero);

        // then
        Mono<HeroesDTO> heroesDTOMono = heroesService.save(mockHeroDTO);
        assertThat(heroesDTOMono).completes().emits(mockHeroDTO);
    }

    @Test
    void whenAlreadyRegisteredHeroIsInformedThenAnExceptionShouldBeReturned() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findByName(mockHeroDTO.getName())).thenReturn(Optional.of(mockHero));

        // then
        assertThatExceptionOfType(HeroAlreadyRegisteredException.class)
                .isThrownBy(() -> heroesService.save(mockHeroDTO));
    }

    @Test
    void whenValidIdIsInformedThenHeroShouldBeDeleted() throws HeroIdNotFoundException {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.of(mockHero));
        doNothing().when(heroesRepository).deleteById(mockHeroDTO.getId());

        // then
        heroesService.deleteById(mockHeroDTO.getId());
        verify(heroesRepository, times(1)).findById(mockHeroDTO.getId());
        verify(heroesRepository, times(1)).deleteById(mockHeroDTO.getId());
    }

    @Test
    void whenInvalidIdIsInformedThenAnExceptionShouldBeReturned() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(HeroIdNotFoundException.class)
                .isThrownBy(() -> heroesService.deleteById(mockHeroDTO.getId()));
    }

    @Test
    void whenAUniverseIsInformedThenAllHeroesFromThatUniverseShouldBeReturned() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findByUniverse(mockHeroDTO.getUniverse()))
                .thenReturn(Collections.singletonList(mockHero));

        // then
        Flux<HeroesDTO> heroesDTOFLux = heroesService.findByUniverse(mockHeroDTO.getUniverse());
        assertThat(heroesDTOFLux).completes().emitsCount(1).emits(mockHeroDTO);
    }

    @Test
    void whenNewNumberOfMoviesAndValidIdIsInformedThenHeroShouldBeUpdated() throws HeroIdNotFoundException {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.of(mockHero));
        when(heroesRepository.save(mockHero)).thenReturn(mockHero);
        HeroesDTO mockHeroDTOUpdated = HeroesDTOBuilder.builder().movies(50).build().toHeroesDTO();

        // then
        Mono<HeroesDTO> heroesDTOMono = heroesService.changeMoviesNumber(mockHeroDTO.getId(), 50);
        assertThat(heroesDTOMono).completes().emitsCount(1).emits(mockHeroDTOUpdated);
    }

    @Test
    void whenNewNumberOfMoviesAndInvalidIdIsInformedThenExceptionShouldBeThrown() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(HeroIdNotFoundException.class)
                .isThrownBy(() -> heroesService.changeMoviesNumber(mockHeroDTO.getId(), 50));
    }

    @Test
    void whenValidIdAndHeroUpdatedIsInformedThenAllInformationOfHeroShouldBeUpdated() throws HeroIdNotFoundException {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        Heroes mockHero = heroesMapper.toModel(mockHeroDTO);
        HeroesDTO mockHeroDTOUpdated = HeroesDTOBuilder.builder()
                .name("Wolverine")
                .universe("Marvel")
                .movies(50)
                .build()
                .toHeroesDTO();
        Heroes mockHeroUpdated = heroesMapper.toModel(mockHeroDTOUpdated);

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.of(mockHero));
        when(heroesRepository.save(mockHeroUpdated)).thenReturn(mockHeroUpdated);


        // then
        mockHeroDTOUpdated.setId(null);
        Mono<HeroesDTO> heroesDTOMono = heroesService.changeHero(mockHeroDTO.getId(), mockHeroDTOUpdated);
        mockHeroDTOUpdated.setId(mockHeroDTO.getId());
        assertThat(heroesDTOMono).completes().emitsCount(1).emits(mockHeroDTOUpdated);
    }

    @Test
    void whenInvalidIdAndHeroUpdatedIsInformedThenExceptionShouldBeThrown() {
        // given
        HeroesDTO mockHeroDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();
        HeroesDTO mockHeroDTOUpdated = HeroesDTOBuilder.builder()
                .id(null)
                .name("Wolverine")
                .universe("Marvel")
                .movies(50)
                .build()
                .toHeroesDTO();

        // when
        when(heroesRepository.findById(mockHeroDTO.getId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(HeroIdNotFoundException.class)
                .isThrownBy(() -> heroesService.changeHero(mockHeroDTO.getId(), mockHeroDTOUpdated));
    }
}
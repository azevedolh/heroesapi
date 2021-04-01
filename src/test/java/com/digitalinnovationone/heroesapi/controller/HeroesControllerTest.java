package com.digitalinnovationone.heroesapi.controller;

import com.digitalinnovationone.heroesapi.builder.HeroesDTOBuilder;
import com.digitalinnovationone.heroesapi.dto.HeroesDTO;
import com.digitalinnovationone.heroesapi.dto.MoviesDTO;
import com.digitalinnovationone.heroesapi.exception.HeroAlreadyRegisteredException;
import com.digitalinnovationone.heroesapi.exception.HeroIdNotFoundException;
import com.digitalinnovationone.heroesapi.service.HeroesService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@SpringBootTest
class HeroesControllerTest {
    @Autowired
    WebTestClient webTestClient;

    @MockBean
    HeroesService heroesService;

    @Test
    void whenGETListIsInvokedAndListContainsItemsThenStatusOKShouldBeReturned() {
        //given
        HeroesDTO mockHeroesDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        //when
        when(heroesService.findAll()).thenReturn(Flux.just(mockHeroesDTO));

        //then
        webTestClient.get().uri(HEROES_ENDPOINT_LOCAL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath(("$[0].name")).isEqualTo(mockHeroesDTO.getName())
                .jsonPath(("$[0].universe")).isEqualTo(mockHeroesDTO.getUniverse())
                .jsonPath(("$[0].movies")).isEqualTo(mockHeroesDTO.getMovies());
    }

    @Test
    void whenGETListIsInvokedAndListDoesNotContainItemsThenStatusOKShouldBeReturned() {
        // when
        when(heroesService.findAll()).thenReturn(Flux.empty());

        // then
        webTestClient.get().uri(HEROES_ENDPOINT_LOCAL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenGETisInvokedWithAValidIdThenStatusOKShouldBeReturned() throws HeroIdNotFoundException {
        //given
        HeroesDTO mockHeroesDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        //when
        when(heroesService.findById(mockHeroesDTO.getId())).thenReturn(Mono.just(mockHeroesDTO));

        //then
        webTestClient.get().uri(HEROES_ENDPOINT_LOCAL + "/{id}", mockHeroesDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath(("$.name")).isEqualTo(mockHeroesDTO.getName())
                .jsonPath(("$.universe")).isEqualTo(mockHeroesDTO.getUniverse())
                .jsonPath(("$.movies")).isEqualTo(mockHeroesDTO.getMovies());
    }

    @Test
    void whenGETisInvokedWithAInvalidIdThenStatusNotFoundShouldBeReturned() throws HeroIdNotFoundException {
        //given
        HeroesDTO mockHeroesDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        //when
        when(heroesService.findById(mockHeroesDTO.getId())).thenThrow(HeroIdNotFoundException.class);

        //then
        webTestClient.get().uri(HEROES_ENDPOINT_LOCAL + "/{id}", mockHeroesDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenGETListByUniverseIsInvokedAndListContainsItemsThenStatusOKShouldBeReturned() {
        //given
        HeroesDTO mockHeroesDTO = HeroesDTOBuilder.builder().build().toHeroesDTO();

        //when
        when(heroesService.findByUniverse(mockHeroesDTO.getUniverse())).thenReturn(Flux.just(mockHeroesDTO));

        //then
        webTestClient.get()
                .uri(HEROES_ENDPOINT_LOCAL + UNIVERSE_ENDPOINT_LOCAL + "/{universe}", mockHeroesDTO.getUniverse())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath(("$[0].name")).isEqualTo(mockHeroesDTO.getName())
                .jsonPath(("$[0].universe")).isEqualTo(mockHeroesDTO.getUniverse())
                .jsonPath(("$[0].movies")).isEqualTo(mockHeroesDTO.getMovies());
    }

    @Test
    void whenGETListByUniverseIsInvokedAndListDoesNotContainItemsThenStatusOKShouldBeReturned() {
        // when
        when(heroesService.findByUniverse("Teste")).thenReturn(Flux.empty());

        // then
        webTestClient.get()
                .uri(HEROES_ENDPOINT_LOCAL + UNIVERSE_ENDPOINT_LOCAL + "/{universe}", "Teste")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenPOSTisInvokedThenHeroIsCreated() throws HeroAlreadyRegisteredException {
        //given
        HeroesDTO mockHeroesDTO = HeroesDTOBuilder.builder().id(null).build().toHeroesDTO();

        //when
        when(heroesService.save(mockHeroesDTO)).thenReturn(Mono.just(mockHeroesDTO));

        //then
        webTestClient.post().uri(HEROES_ENDPOINT_LOCAL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockHeroesDTO), HeroesDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath(("$.name")).isEqualTo(mockHeroesDTO.getName())
                .jsonPath(("$.universe")).isEqualTo(mockHeroesDTO.getUniverse())
                .jsonPath(("$.movies")).isEqualTo(mockHeroesDTO.getMovies());
    }

    @Test
    void whenPOSTIsInvokedWithoutMandatoryFieldThenBadRequestShouldBeReturned() throws HeroAlreadyRegisteredException {
        //given
        HeroesDTO mockHeroesDTO = HeroesDTOBuilder.builder().id(null).build().toHeroesDTO();

        // when
        when(heroesService.save(mockHeroesDTO)).thenThrow(HeroAlreadyRegisteredException.class);

        // then
        webTestClient.post().uri(HEROES_ENDPOINT_LOCAL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockHeroesDTO), HeroesDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void whenDELETEIsInvokedWithValidIDThenNoContentStatusIsReturned() throws HeroIdNotFoundException {
        // when
        doNothing().when(heroesService).deleteById("1");

        // then
        webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL + "/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void whenDELETEIsInvokedWithInvalidIDThenNotFoundStatusIsReturned() throws HeroIdNotFoundException {
        // when
        doThrow(HeroIdNotFoundException.class).when(heroesService).deleteById("1");

        // then
        webTestClient.delete().uri(HEROES_ENDPOINT_LOCAL + "/{id}", "1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenPATCHisInvokedThenHeroMoviesNumberIsUpdated() throws HeroIdNotFoundException {
        //given
        HeroesDTO mockHeroesDTOUpdated = HeroesDTOBuilder.builder().movies(50).build().toHeroesDTO();
        MoviesDTO mockMoviesDTO = MoviesDTO.builder().movies(50).build();

        //when
        when(heroesService.changeMoviesNumber("1", 50)).thenReturn(Mono.just(mockHeroesDTOUpdated));

        //then
        webTestClient.patch().uri(HEROES_ENDPOINT_LOCAL + "/{id}" + MOVIES_ENDPOINT_LOCAL, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockMoviesDTO), MoviesDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath(("$.movies")).isEqualTo(mockHeroesDTOUpdated.getMovies());
    }

    @Test
    void whenPATCHIsInvokedWithInvalidIdThenNotFoundStatusShouldBeReturned() throws HeroIdNotFoundException {
        //given
        MoviesDTO mockMoviesDTO = MoviesDTO.builder().movies(50).build();

        //when
        when(heroesService.changeMoviesNumber("1", 50)).thenThrow(HeroIdNotFoundException.class);

        //then
        webTestClient.patch().uri(HEROES_ENDPOINT_LOCAL + "/{id}" + MOVIES_ENDPOINT_LOCAL, "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockMoviesDTO), MoviesDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenPUTisInvokedThenHeroIsUpdated() throws HeroIdNotFoundException {
        //given
        HeroesDTO mockHeroesDTOToUpdate = HeroesDTOBuilder.builder()
                .id(null)
                .name("Wolverine")
                .universe("Marvel")
                .movies(50)
                .build()
                .toHeroesDTO();

        HeroesDTO mockHeroesDTOUpdated = HeroesDTOBuilder.builder()
                .name("Wolverine")
                .universe("Marvel")
                .movies(50)
                .build()
                .toHeroesDTO();

        //when
        when(heroesService.changeHero("1", mockHeroesDTOToUpdate)).thenReturn(Mono.just(mockHeroesDTOUpdated));

        //then
        webTestClient.put().uri(HEROES_ENDPOINT_LOCAL + "/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockHeroesDTOToUpdate), HeroesDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath(("$.name")).isEqualTo(mockHeroesDTOUpdated.getName())
                .jsonPath(("$.universe")).isEqualTo(mockHeroesDTOUpdated.getUniverse())
                .jsonPath(("$.movies")).isEqualTo(mockHeroesDTOUpdated.getMovies());
    }

    @Test
    void whenPUTIsInvokedWithInvalidIdThenNotFoundStatusShouldBeReturned() throws HeroIdNotFoundException {
        //given
        HeroesDTO mockHeroesDTOToUpdate = HeroesDTOBuilder.builder()
                .id(null)
                .name("Wolverine")
                .universe("Marvel")
                .movies(50)
                .build()
                .toHeroesDTO();

        //when
        when(heroesService.changeHero("1", mockHeroesDTOToUpdate)).thenThrow(HeroIdNotFoundException.class);

        //then
        webTestClient.put().uri(HEROES_ENDPOINT_LOCAL + "/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockHeroesDTOToUpdate), HeroesDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}
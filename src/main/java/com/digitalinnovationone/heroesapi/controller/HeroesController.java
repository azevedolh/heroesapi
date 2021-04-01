package com.digitalinnovationone.heroesapi.controller;

import com.digitalinnovationone.heroesapi.dto.HeroesDTO;
import com.digitalinnovationone.heroesapi.dto.MoviesDTO;
import com.digitalinnovationone.heroesapi.exception.HeroAlreadyRegisteredException;
import com.digitalinnovationone.heroesapi.exception.HeroIdNotFoundException;
import com.digitalinnovationone.heroesapi.service.HeroesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.digitalinnovationone.heroesapi.constants.HeroesConstant.*;

@RestController
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(HEROES_ENDPOINT_LOCAL)
public class HeroesController {
    HeroesService heroesService;

    @GetMapping
    public Flux<HeroesDTO> getAllItems() {
        log.info("Requesting the list of all Heroes");
        return heroesService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<HeroesDTO> getItem(@PathVariable String id) throws HeroIdNotFoundException {
        log.info("Requesting the hero with id {}", id);
        return heroesService.findById(id);
    }

    @GetMapping(UNIVERSE_ENDPOINT_LOCAL + "/{universe}")
    public Flux<HeroesDTO> getAllItemsByUniverse(@PathVariable String universe) {
        log.info("Requesting all heros from {}", universe);
        return heroesService.findByUniverse(universe);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<HeroesDTO> addItem(@RequestBody @Valid HeroesDTO hero) throws HeroAlreadyRegisteredException {
        log.info("Adding the hero {}", hero);
        return heroesService.save(hero);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String id) throws HeroIdNotFoundException {
        log.info("Deleting the hero with id {}", id);
        heroesService.deleteById(id);
    }

    @PatchMapping("/{id}" + MOVIES_ENDPOINT_LOCAL)
    public Mono<HeroesDTO> changeMoviesNumber(
            @PathVariable String id, @RequestBody @Valid MoviesDTO moviesDTO
    ) throws HeroIdNotFoundException {
        log.info("Changing number of movies of hero with id {} to {}", id, moviesDTO.getMovies());
        return heroesService.changeMoviesNumber(id, moviesDTO.getMovies());
    }

    @PutMapping("/{id}")
    public Mono<HeroesDTO> changeHero(
            @PathVariable String id, @RequestBody @Valid HeroesDTO heroesDTO
    ) throws HeroIdNotFoundException {
        log.info("Changing all information of Hero {}", heroesDTO.getName());
        return heroesService.changeHero(id, heroesDTO);
    }
}

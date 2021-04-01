package com.digitalinnovationone.heroesapi.service;

import com.digitalinnovationone.heroesapi.document.Heroes;
import com.digitalinnovationone.heroesapi.dto.HeroesDTO;
import com.digitalinnovationone.heroesapi.exception.HeroAlreadyRegisteredException;
import com.digitalinnovationone.heroesapi.exception.HeroIdNotFoundException;
import com.digitalinnovationone.heroesapi.mapper.HeroesMapper;
import com.digitalinnovationone.heroesapi.repository.HeroesRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HeroesService {
    private final HeroesRepository heroesRepository;
    private final HeroesMapper heroesMapper = HeroesMapper.INSTANCE;

    public Flux<HeroesDTO> findAll() {
        Stream<HeroesDTO> heroesDTOStream = StreamSupport
                .stream(this.heroesRepository.findAll().spliterator(), false)
                .map(heroesMapper::toDTO);
        return Flux.fromStream(heroesDTOStream);
    }

    public Mono<HeroesDTO> findById(String id) throws HeroIdNotFoundException {
        Heroes heroes = verifyIfExists(id);
        return Mono.just(heroesMapper.toDTO(heroes));
    }

    public Mono<HeroesDTO> save(HeroesDTO heroDTO) throws HeroAlreadyRegisteredException {
        verifyIfAlreadyExists(heroDTO.getName());
        Heroes hero = heroesMapper.toModel(heroDTO);
        Heroes savedHero = heroesRepository.save(hero);
        return Mono.justOrEmpty(heroesMapper.toDTO(savedHero));
    }

    private void verifyIfAlreadyExists(String name) throws HeroAlreadyRegisteredException {
        Optional<Heroes> optSavedHero = heroesRepository.findByName(name);
        if (optSavedHero.isPresent()) {
            throw new HeroAlreadyRegisteredException(name);
        }
    }

    public void deleteById(String id) throws HeroIdNotFoundException {
        verifyIfExists(id);
        this.heroesRepository.deleteById(id);
    }

    private Heroes verifyIfExists(String id) throws HeroIdNotFoundException {
        return heroesRepository.findById(id)
                .orElseThrow(() -> new HeroIdNotFoundException(id));
    }

    public Flux<HeroesDTO> findByUniverse(String universe) {
        Stream<HeroesDTO> heroesDTOStream = StreamSupport
                .stream(this.heroesRepository.findByUniverse(universe).spliterator(), false)
                .map(heroesMapper::toDTO);
        return Flux.fromStream(heroesDTOStream);
    }

    public Mono<HeroesDTO> changeMoviesNumber(String id, Integer movies) throws HeroIdNotFoundException {
        Heroes hero = verifyIfExists(id);
        hero.setMovies(movies);
        return Mono.just(heroesMapper.toDTO(heroesRepository.save(hero)));
    }

    public Mono<HeroesDTO> changeHero(String id, HeroesDTO heroesDTO) throws HeroIdNotFoundException {
        verifyIfExists(id);
        heroesDTO.setId(id);
        Heroes savedHero = heroesRepository.save(heroesMapper.toModel(heroesDTO));
        return Mono.just(heroesMapper.toDTO(savedHero));
    }
}

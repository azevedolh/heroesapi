package com.digitalinnovationone.heroesapi.repository;

import com.digitalinnovationone.heroesapi.document.Heroes;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface HeroesRepository extends CrudRepository<Heroes, String> {
    Optional<Heroes> findByName(String name);

    List<Heroes> findByUniverse(String universe);
}

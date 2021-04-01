package com.digitalinnovationone.heroesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HeroNameNotFoundException extends Exception {

    public HeroNameNotFoundException(String heroName) {
        super(String.format("Hero with name %s not found in the system.", heroName));
    }
}

package com.digitalinnovationone.heroesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HeroAlreadyRegisteredException extends Exception {

    public HeroAlreadyRegisteredException(String heroName) {
        super(String.format("Hero with name %s already registered in the system.", heroName));
    }
}

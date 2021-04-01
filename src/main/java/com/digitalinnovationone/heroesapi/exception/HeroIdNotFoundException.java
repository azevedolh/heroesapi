package com.digitalinnovationone.heroesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HeroIdNotFoundException extends Exception {

    public HeroIdNotFoundException(String id) {
        super(String.format("Hero with id %s not found in the system.", id));
    }
}

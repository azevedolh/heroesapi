package com.digitalinnovationone.heroesapi.constants;

import software.amazon.awssdk.core.regions.Region;

public class HeroesConstant {
    public static final String HEROES_ENDPOINT_LOCAL = "/heroes";
    public static final String UNIVERSE_ENDPOINT_LOCAL = "/universe";
    public static final String MOVIES_ENDPOINT_LOCAL = "/movies";
    public static final String DYNAMO_ENDPOINT = "http://localhost:8000/";
    public static final String DYNAMO_REGION = Region.US_EAST_2.value();
}

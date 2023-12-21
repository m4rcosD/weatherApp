package com.weather;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;


@Path("/weather")
@ApplicationScoped
public class WeatherResource {

    @Inject
    @RestClient
    WeatherService weatherService;

    @Inject
    @ConfigProperty(name = "weather.api.key")
    String apiKey;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cities")
    public String getWeatherForCities(
            @QueryParam("lat1") String lat1,
            @QueryParam("lon1") String lon1,
            @QueryParam("lat2") String lat2,
            @QueryParam("lon2") String lon2) {

        String weatherCity1 = weatherService.getWeatherByCoordinates(apiKey, lat1, lon1);
        String weatherCity2 = weatherService.getWeatherByCoordinates(apiKey, lat2, lon2);

        return "{\"city1\": " + weatherCity1 + ", \"city2\": " + weatherCity2 + "}";
    }
}
package com.weather;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://api.openweathermap.org/data/2.5")

public interface WeatherService {

    @GET
    @Path("/weather")
    @Produces(MediaType.APPLICATION_JSON)
    String getWeatherByCoordinates(
            @QueryParam("appid") String apiKey,
            @QueryParam("lat") String lat,
            @QueryParam("lon") String lon);

}
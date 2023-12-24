package com.weather;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://api.openweathermap.org/data/2.5")
public interface WeatherService {

    // Define o método GET que será usado para obter dados climáticos a partir de coordenadas
    @GET
    @Path("/weather")
    @Produces(MediaType.APPLICATION_JSON)
    // Especifica os parâmetros da consulta: apiKey (chave da API), lat (latitude) e lon (longitude)
    String getWeatherByCoordinates(
            @QueryParam("appid") String apiKey,
            @QueryParam("lat") String lat,
            @QueryParam("lon") String lon);
}

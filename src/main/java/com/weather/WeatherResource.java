package com.weather;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/weather")
@ApplicationScoped
public class WeatherResource {
    
    // Injeção de propriedades de configuração para coordenadas das cidades
    @Inject
    @ConfigProperty(name = "city1.latitude")
    String city1Latitude;
    @ConfigProperty(name = "city1.longitude")
    String city1Longitude;

    @Inject
    @ConfigProperty(name = "city2.latitude")
    String city2Latitude;
    @ConfigProperty(name = "city2.longitude")
    String city2Longitude;

    // Injeção de dependências para serviços e propriedades relacionadas ao clima
    @Inject
    @RestClient
    WeatherService weatherService;
    @ConfigProperty(name = "weather.api.key")
    String apiKey;

    // Método para obter dados climáticos para as cidades
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getWeatherForCities() {
        // Obter dados climáticos das cidades usando o serviço de clima
        String weatherCity1 = weatherService.getWeatherByCoordinates(apiKey, city1Latitude, city1Longitude);
        String weatherCity2 = weatherService.getWeatherByCoordinates(apiKey, city2Latitude, city2Longitude);

        // Processar as respostas da API e extrair informações relevantes
        String processedWeatherCity1 = processWeatherData(weatherCity1);
        String processedWeatherCity2 = processWeatherData(weatherCity2);

        // Retornar os dados processados em formato JSON
        return "{\"city1\": " + processedWeatherCity1 + ", \"city2\": " + processedWeatherCity2 + "}";
    }

    // Método para processar os dados climáticos
    public String processWeatherData(String weatherData) {
        // Extrair data e hora, temperatura em Celsius e localização
        JsonObject jsonObject = Json.createReader(new StringReader(weatherData)).readObject();
        double temperatureInKelvin = jsonObject.getJsonObject("main").getJsonNumber("temp").doubleValue();
        double temperatureInCelsius = temperatureInKelvin - 273.15; // Converter para Celsius

        // Extrair data e hora formatadas, e a localização
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(jsonObject.getJsonNumber("dt").longValue(), 0, ZoneOffset.UTC);
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String location = jsonObject.getString("name");

        // Exibir informações recebidas
        System.out.println("Data e hora: " + formattedDateTime);
        System.out.println("Temperatura em Celsius: " + temperatureInCelsius);
        System.out.println("Localização: " + location);

        // Retornar os dados processados em formato JSON
        return "{\"datetime\": \"" + formattedDateTime + "\", \"temperature\": " + temperatureInCelsius + ", \"location\": \"" + location + "\"}";
    }
}
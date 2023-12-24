package com.weather;

import io.quarkus.scheduler.Scheduled;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.weather.mosquitto.MosquittoDataProducer;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import jakarta.inject.Inject;

@ApplicationScoped
public class WeatherDataScheduler {

    @Inject
    @RestClient
    WeatherService weatherService;

    @Inject
    MosquittoDataProducer weatherDataProducer;

    @ConfigProperty(name = "weather.api.key")
    String apiKey;

    @ConfigProperty(name = "city1.latitude")
    String city1Latitude;

    @ConfigProperty(name = "city1.longitude")
    String city1Longitude;

    @ConfigProperty(name = "city2.latitude")
    String city2Latitude;

    @ConfigProperty(name = "city2.longitude")
    String city2Longitude;

    @Scheduled(every="20s")
    void fetchAndPersistWeatherData() {
        String weatherCity1 = weatherService.getWeatherByCoordinates(apiKey, city1Latitude, city1Longitude);
        String weatherCity2 = weatherService.getWeatherByCoordinates(apiKey, city2Latitude, city2Longitude);

        String processedWeatherCity1 = processWeatherData(weatherCity1);
        String processedWeatherCity2 = processWeatherData(weatherCity2);

        System.out.println("Weather data for City 1: " + processedWeatherCity1);
        System.out.println("Weather data for City 2: " + processedWeatherCity2);

        weatherDataProducer.sendDataToMosquitto(processedWeatherCity1);
        weatherDataProducer.sendDataToMosquitto(processedWeatherCity2);
    }

    public String processWeatherData(String weatherData) {
        // Extrair dados do clima
        io.vertx.core.json.JsonObject jsonObject = new io.vertx.core.json.JsonObject(weatherData);
        double temperatureInKelvin = jsonObject.getJsonObject("main").getDouble("temp");
        double temperatureInCelsius = temperatureInKelvin - 273.15;

        long epochSeconds = jsonObject.getLong("dt");
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.UTC);
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String location = jsonObject.getString("name");

        // Retornar os dados processados em formato JSON
        return "{\"datetime\": \"" + formattedDateTime + "\", \"temperature\": " + temperatureInCelsius + ", \"location\": \"" + location + "\"}";
    }
}

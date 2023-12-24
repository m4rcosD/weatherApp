package com.weather.mosquitto;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.PersistenceContext;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.weather.models.WeatherDataEntity;

@ApplicationScoped
public class MosquittoDataReceiver {

    @Inject
    @Channel("weather-data")
    Emitter<MqttMessage> weatherDataEmitter;

    @Inject
    @ConfigProperty(name = "mqtt.topic")
    String mqttTopic;

    @PersistenceContext
    EntityManager entityManager;

    public void startMosquittoListener() {
        String serverUrl = "tcp://mosquitto-server:1883";
        String clientId = "WeatherAppListener";

        try (MqttClient mqttClient = new MqttClient(serverUrl, clientId)) {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);

            mqttClient.connect(options);

            String[] topics = { mqttTopic };
            int[] qos = { 1 }; 

            mqttClient.subscribe(topics, qos);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    // Lógica para lidar com a desconexão
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    weatherDataEmitter.send(mqttMessage);
                    persistDataToDatabase(new String(mqttMessage.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

@Transactional
public void persistDataToDatabase(String payload) {
    JsonObject jsonObject = Json.createReader(new StringReader(payload)).readObject();
    double temperatureInKelvin = jsonObject.getJsonObject("main").getJsonNumber("temp").doubleValue();
    double temperatureInCelsius = temperatureInKelvin - 273.15;

    LocalDateTime dateTime = LocalDateTime.ofEpochSecond(jsonObject.getJsonNumber("dt").longValue(), 0, ZoneOffset.UTC);
    String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String location = jsonObject.getString("name");

    WeatherDataEntity weatherData = new WeatherDataEntity();
    weatherData.setPayload(payload);
    weatherData.setDatetime(formattedDateTime);
    weatherData.setTemperatureInCelsius(temperatureInCelsius);
    weatherData.setLocation(location);

    entityManager.persist(weatherData);
}

}

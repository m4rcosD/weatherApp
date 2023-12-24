package com.weather.mosquitto;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;

import javax.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class MosquittoDataProducer {

    @Inject
    @Channel("weather-data")
    Emitter<MqttMessage> weatherDataEmitter;

    @Inject
    @ConfigProperty(name = "mqtt.topic")
    String mqttTopic;

    public void sendDataToMosquitto(String weatherData) {
        // Cria uma mensagem MQTT com os dados climáticos
        MqttMessage message = new MqttMessage(weatherData.getBytes(StandardCharsets.UTF_8));

        // Configura o tópico MQTT
        message.setQos(1); // Configura a qualidade de serviço (QoS)

        // Envia os dados para o tópico MQTT no Mosquitto
        weatherDataEmitter.send(message);
    }
}

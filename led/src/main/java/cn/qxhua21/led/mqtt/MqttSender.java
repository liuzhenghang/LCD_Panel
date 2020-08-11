package cn.qxhua21.led.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttSender {
    void sendToMqtt(String data,@Header(MqttHeaders.TOPIC) String topic);
    void sendToMqtt(String data);
//    void sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String data);
}
package cn.qxhua21.led.dao;

import cn.qxhua21.led.po.Message;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;

public interface MessageDao {
    boolean sendMessageByWebSocketForUser(Message message);
    void sendMessageByMail(Message message);
    void sendDeviceStateToUser(Message message);
    void sendMessageByMqtt(Message message);
    void sendMessage(Message message);
    void gainDeviceMessage(org.springframework.messaging.Message<?> msgCode);

//    void gainDeviceMessage(org.springframework.messaging.Message<?> message);
}

package cn.qxhua21.led.mqtt;

import cn.qxhua21.led.dao.MessageDao;
import cn.qxhua21.led.thread.AsyncService;
import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;

@Configuration
// spring Integration组件扫描，MessageChannel使用的就是这个组件
@IntegrationComponentScan
public class MqttConfig {

    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;

    /**
     * mqtt 服务地址
     */
    @Value("${mqtt.url}")
    private String hostUrl;

    /**
     * 接收设备id,用来区分不同的设备连接
     */
    @Value("${mqtt.consumer.clientId}")
    private String clientId;



    /**
     * 订阅那个主题
     */
    @Value("${mqtt.consumer.defaultTopic}")
    private String defaultTopic;


    /**
     * 发送设备id,用来区分不同的设备连接
     */
    @Value("${mqtt.producer.clientId}")
    private String sendId;

    /**
     * 发送消息的主题
     */
    @Value("${mqtt.producer.defaultTopic}")
    private String sendTopic;

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private AsyncService asyncService;


    /**
     * 创建连接的工厂
     * 用于构建MessageHandler
     * @return
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        // mqtt服务器url
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        // 设置会话心跳时间(秒)
        mqttConnectOptions.setKeepAliveInterval(2);
        // 每次请求是否清空连接记录
        mqttConnectOptions.setCleanSession(false);
        // 可以设置用户名密码
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    /* --------------------发布配置----------------- */

    /**
     * 1. 发布信息的MessageHandler
     * 订阅 mqttOutboundChannel 通道的信息
     * @param mqttClientFactory
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler(sendId, mqttClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultQos(0);
        messageHandler.setDefaultRetained(false);
        messageHandler.setAsyncEvents(false);
        return messageHandler;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /* --------------------接收配置-------------------- */

    /**
     * 处理订阅的MessageHandler
     * 订阅 aaInboundChannel 通道的信息
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler newHandler() {
//        return message -> System.out.println("收到消息 = " + message.getPayload());
        return message -> gainDeviceMessage(message);
//        return message -> System.out.println(message.toString());
    }

    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * 1. 订阅主题，可订阅多个主题
     * 2. 将主题返回的内容发布到指定的 MessageChannel 里
     * @param mqttClientFactory
     * @return
     */
    @Bean
    public MessageProducerSupport mqttInbound(MqttPahoClientFactory mqttClientFactory) {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId, mqttClientFactory, defaultTopic);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }
    public void gainDeviceMessage(org.springframework.messaging.Message<?> msgCode){
        String msg= (String) msgCode.getPayload();
        JSONObject json= (JSONObject) JSONObject.parse(msg);
        asyncService.messageFromDevice(json);
//        System.out.println("MAC地址："+json.get("mac"));
//        System.out.println("消息类型："+json.get("type"));
//        json= (JSONObject) json.get("data");
//        System.out.println("文本："+json.get("text"));
//        System.out.println("版本号："+json.get("version"));
//        System.out.println(msgCode);
    }
}
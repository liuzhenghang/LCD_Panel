package cn.qxhua21.led.dao.impl;

import cn.qxhua21.led.config.DeviceTool;
import cn.qxhua21.led.dao.CacheDao;
import cn.qxhua21.led.dao.DeviceStateDao;
import cn.qxhua21.led.dao.MessageDao;
import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.mapper.DeviceMapper;
import cn.qxhua21.led.mqtt.MqttSender;
import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.DeviceState;
import cn.qxhua21.led.po.Message;
import cn.qxhua21.led.websocket.WebSocketUserServer;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageDaoImpl implements MessageDao {
    @Value("${spring.redis.devices-user}")
    private String deviceTmp;
    @Value("${spring.redis.mail-message}")
    private String mailMessage;


    @Autowired
    private RedisUtils redis;
    @Autowired
    private MqttSender mqttSender;
    @Autowired
    private DeviceStateDao deviceStateDao;
    @Autowired
    private CacheDao cacheDao;
    @Override
    public boolean sendMessageByWebSocketForUser(Message message) {
        if (message.getMsg().equals("offline")){
            DeviceState deviceState=new DeviceState();
            deviceState.setOnline(false);
            deviceState.setMac(message.getMac());
            deviceStateDao.updateDeviceState(deviceState,true);
        }else {
            DeviceState deviceState=new DeviceState();
            String[] ip=((String) message.getMsg()).split(",");
            deviceState.setIp(ip[1]);
            deviceState.setOnline(true);
            deviceState.setMac(message.getMac());
            deviceStateDao.updateDeviceState(deviceState,true);
        }
        cacheDao.refreshDeviceCache();
        Device device= (Device) redis.hget(deviceTmp,message.getMac());//通过mac获取详细的用户信息
        System.out.println(message);
        JSONObject json=new JSONObject();
        json.put("msg",message);
        if (WebSocketUserServer.sendInfo(String.valueOf(device.getUserId()),message)){
            return true;
        }
        return false;
    }

    @Override
    public void sendMessageByMail(Message message) {
        cacheDao.refreshDeviceCache();
        Device device= (Device) redis.hget(deviceTmp,message.getMac());//通过mac获取详细的用户信息
        if (redis.hasKey(mailMessage+device.getUserId())){
            if (((Integer) redis.get(mailMessage+device.getUserId()))>5){
//                短时间内消息超过了5次，抛弃
                System.out.println("短时间内消息超过了5次，抛弃");
            }else {
                System.out.println("已向用户发送邮箱,用户ID:"+device.getUserId());
                redis.incr(mailMessage+device.getUserId(),1);
            }
        }else {
            System.out.println("已向用户发送邮箱,用户ID:"+device.getUserId());
            redis.set(mailMessage+device.getUserId(),1,1800);
        }
    }

    @Override
    public void sendDeviceStateToUser(Message message) {
        cacheDao.refreshDeviceCache();
        DeviceState deviceState= (DeviceState) message.getMsg();
        Device device= (Device) redis.hget(deviceTmp,message.getMac());//通过mac获取详细的用户信息
        device= DeviceTool.static2device(device,deviceState);
        message.setMsg(device);
        System.out.println(message);
        WebSocketUserServer.sendInfo(String.valueOf(device.getUserId()),message);
    }

    @Override
    public void sendMessageByMqtt(Message message) {
        if (message.getTopic()==null){
            //不指定推送主题，推送到设备对应的主题
            mqttSender.sendToMqtt(message.getMac(), message.getQos(), message.getMsg().toString());
        }else {
            //指定推送主题，推送到指定的主题
            mqttSender.sendToMqtt(message.getTopic(), message.getQos(), message.getMsg().toString());
        }
    }

    @Override
    public void sendMessage(Message message){
            switch (message.getType()){//判断消息类型
                case "line"://在线状态,推送到用户
                    System.out.println();
                    if (!sendMessageByWebSocketForUser(message)){//通过socket发送到用户
                        System.out.println("发送到socks失败，用户不在线，准备推到邮箱");
                        sendMessageByMail(message);//socket不在线的话，通过邮箱发送
                    }
                    break;
                case "instruction"://指令，推送到设备
                    System.out.println();
                    sendMessageByMqtt(message);
                    break;
                case "refresh"://用户Web界面刷新，推送到用户
                    System.out.println(message);
                    System.out.println("刷新用户界面");
                    sendDeviceStateToUser(message);
                    break;
            }
    }

    @Override
    public void gainDeviceMessage(org.springframework.messaging.Message<?> msgCode){

        String msg= (String) msgCode.getPayload();
        JSONObject json= (JSONObject) JSONObject.parse(msg);

        System.out.println("MAC地址："+json.get("mac"));
        System.out.println("消息类型："+json.get("type"));
        json= (JSONObject) json.get("data");
        System.out.println("文本："+json.get("text"));
        System.out.println("版本号："+json.get("version"));
        System.out.println();
    }
}

package cn.qxhua21.led.thread;

import cn.qxhua21.led.dao.CacheDao;
import cn.qxhua21.led.dao.DeviceStateDao;
import cn.qxhua21.led.dao.MessageDao;
import cn.qxhua21.led.po.DeviceState;
import cn.qxhua21.led.po.Message;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service

public class AsyncServiceImpl implements AsyncService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private DeviceStateDao deviceStateDao;
    @Autowired
    private CacheDao cacheDao;

    private static final Logger logger = LoggerFactory.getLogger(AsyncServiceImpl.class);
    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync(Message message) {
        logger.info("启动新线程");
        messageDao.sendMessage(message);
        logger.info("消息投递结束");
    }

    @Override
    @Async("asyncServiceExecutor")
    public void messageFromDevice(JSONObject json) {
        logger.info("开始处理来自设备的消息");
        DeviceState deviceState=new DeviceState();
        Message message;
        switch ((String) json.get("type")){
            case "put":
                deviceState.setMac(json.getString("mac"));
                deviceState.setOnline(true);
                deviceState.setText(((JSONObject) json.get("data")).getString("text"));
                deviceState.setVersion(((JSONObject) json.get("data")).getString("version"));
                message=Message.toUserRefresh(json.getString("mac"),deviceState);
                messageDao.sendMessage(message);
                deviceStateDao.updateDeviceState(deviceState,false);
                break;
            case "success":
                deviceState.setMac(json.getString("mac"));
                deviceState.setText(((JSONObject) json.get("data")).getString("text"));
                deviceState.setVersion(((JSONObject) json.get("data")).getString("version"));
                deviceState.setOnline(true);
                cacheDao.updateDeviceState(deviceState);
                message=Message.toUserRefresh(json.getString("mac"),deviceState);
                messageDao.sendMessage(message);
                deviceStateDao.updateDeviceState(deviceState,false);
        }
        logger.info("处理结束");
    }

    @Override
    @Async("asyncServiceExecutor")
    public void updateDataBase(DeviceState deviceState) {

    }
}
package cn.qxhua21.led.thread;

import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.DeviceState;
import cn.qxhua21.led.po.Message;
import com.alibaba.fastjson.JSONObject;

public interface AsyncService {

    /**
     * 执行异步任务
     */

    void executeAsync(Message message);
    void messageFromDevice(JSONObject json);
    void updateDataBase(DeviceState deviceState);

}

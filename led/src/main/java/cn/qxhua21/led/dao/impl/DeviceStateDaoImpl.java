package cn.qxhua21.led.dao.impl;

import cn.qxhua21.led.config.SHA;
import cn.qxhua21.led.dao.DeviceStateDao;
import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.po.DeviceState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceStateDaoImpl implements DeviceStateDao {
    @Value("${spring.redis.state}")
    private String state;
    @Autowired
    private RedisUtils redis;
    @Override
    public void updateDeviceState(DeviceState deviceState,boolean update) {
//        if (!redis.hasKey(state)){
//            redis.hset(state,deviceState.getMac(),deviceState);
//        }
        if (redis.hHasKey(state,deviceState.getMac())){
            DeviceState deviceState1= (DeviceState) redis.hget(state,deviceState.getMac());
            if(update){
                deviceState1.setOnline(deviceState.isOnline());
                deviceState1.setDate(SHA.getTime());
                if (deviceState.getIp()!=null){
                    deviceState1.setIp(deviceState.getIp());
                }
                redis.hset(state,deviceState.getMac(),deviceState1);
            }else {
                deviceState.setIp(deviceState1.getIp());
                redis.hset(state,deviceState.getMac(),deviceState);
            }
        }else {
            redis.hset(state,deviceState.getMac(),deviceState);
        }
    }

    @Override
    public DeviceState getDeviceState(String mac) {
        if (redis.hHasKey(state,mac)){
            return (DeviceState) redis.hget(state,mac);
        }else {
            return new DeviceState();
        }
    }
}

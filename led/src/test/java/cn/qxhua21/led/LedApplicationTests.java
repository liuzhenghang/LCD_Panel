package cn.qxhua21.led;

import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.po.DeviceOnline;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LedApplicationTests {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    void contextLoads() {
    }
    @Test
    void setRedis(){
        DeviceOnline deviceOnline=new DeviceOnline();
        deviceOnline.setMac("D6:05:99:6F:CF:11");
        deviceOnline.setDate(new Date());
        System.out.println(redisUtils.set(deviceOnline.getMac(),deviceOnline));
    }
    @Test
    void getRedis(){
        System.out.println(redisUtils.get("D6:05:99:6F:CF:11"));
    }

}

package cn.qxhua21.led.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.alibaba.fastjson.JSON;

/**
 * definition for our encoder
 *
 * @编写人: 刘正航
 */
public class ServerEncoder implements Encoder.Text<Object> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(Object messagepojo) throws EncodeException {
        return JSON.toJSONString(messagepojo);
    }

}
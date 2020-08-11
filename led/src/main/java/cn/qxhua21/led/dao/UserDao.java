package cn.qxhua21.led.dao;

import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;

public interface UserDao {
    User login(User user);
    boolean testIdLogin(User user);
    boolean catToken(Token token);
    Object register(User user);
//    void sendMessage(String mac,Object message);
}

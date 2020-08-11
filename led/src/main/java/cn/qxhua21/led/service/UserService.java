package cn.qxhua21.led.service;

import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;

public interface UserService {
    Object Login(User user);
    Object register(User user);
    Object catToken(Token token);
}

package cn.qxhua21.led.service.impl;

import cn.qxhua21.led.config.CodeMsg;
import cn.qxhua21.led.config.Result;
import cn.qxhua21.led.config.SHA;
import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.dao.UserDao;
import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;
import cn.qxhua21.led.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    final static String key="token";
    final static String login_error_key="error";
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtils redis;
    @Override
    public Object Login(User user) {
        String mail=user.getMail();
        if (redis.hasKey(mail)){
            if (5 < ((Integer) redis.get(mail))){
                return Result.error(CodeMsg.USER_LOGIN_TOO_MANY);
            }
        }
        user=userDao.login(user);
        if (user==null){
            if (redis.hasKey(mail)){
                int num=((Integer) redis.get(mail))+1;
                redis.set(mail,num,300);
            }else {
                redis.set(mail,1,300);
            }
            return Result.error(CodeMsg.USER_PASSWORD_ERROR);
        }
        String token=SHA.getDataMD5();
        redis.hset(key, String.valueOf(user.getId()),token);
        Map<String,Object> map=new HashMap<>();
        map.put("username",user.getUsername());
        map.put("token",token);
        map.put("id",user.getId());
        return Result.success(map);
    }

    @Override
    public Object register(User user) {
        return userDao.register(user);
    }

    @Override
    public Object catToken(Token token) {
        if (userDao.catToken(token)){
            return Result.success(null);
        }
        return Result.error(CodeMsg.SESSSON_ERROR);
    }
}

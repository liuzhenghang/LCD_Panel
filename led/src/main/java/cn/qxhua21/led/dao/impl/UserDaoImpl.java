package cn.qxhua21.led.dao.impl;

import cn.qxhua21.led.config.CodeMsg;
import cn.qxhua21.led.config.Result;
import cn.qxhua21.led.config.SHA;
import cn.qxhua21.led.dao.DeviceDao;
import cn.qxhua21.led.dao.RedisUtils;
import cn.qxhua21.led.dao.UserDao;
import cn.qxhua21.led.mapper.UserMapper;
import cn.qxhua21.led.po.Device;
import cn.qxhua21.led.po.Message;
import cn.qxhua21.led.po.Token;
import cn.qxhua21.led.po.User;
import cn.qxhua21.led.websocket.WebSocketUserServer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    final static String key="token";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redis;
    @Override
    public User login(User user) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getMail,user.getMail());
        queryWrapper.lambda().eq(User::getPassword,user.getPassword());
        List<User> users=userMapper.selectList(queryWrapper);
        if (users.size()>0){

            return users.get(0);
        }
        return null;
    }

    @Override
    public boolean testIdLogin(User user) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId,user.getId());
        queryWrapper.lambda().eq(User::getPassword,user.getPassword());
        List<User> users=userMapper.selectList(queryWrapper);
        if (users.size()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean catToken(Token token) {
        if (redis.hHasKey(key,String.valueOf(token.getId()))){
            if (redis.hget(key, String.valueOf(token.getId())).equals(token.getToken())){
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public Object register(User user) {
        User u=new User();
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getMail,user.getMail());
        u=userMapper.selectOne(queryWrapper);
        if (u!=null){
            return Result.error(CodeMsg.MAIL_HAS_SAVE);
        }
        queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername,user.getUsername());
        u=userMapper.selectOne(queryWrapper);
        if (u!=null){
            return Result.error(CodeMsg.USER_HAS_SAVE);
        }
        if (userMapper.insert(user)>0){
            return Result.success(null);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}

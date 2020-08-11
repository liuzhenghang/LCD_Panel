package cn.qxhua21.led.config;

public class CodeMsg {

    private int code;
    private String msg;

    //构造函数
    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //通用的错误码,定义错误码后，这些东西我们写一遍就完事了，这就是优雅的代码
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    //登录错误码
    public static CodeMsg BIND_ERROR = new CodeMsg(500201,"参数绑定异常：&s");
    public static CodeMsg USER_HAS_SAVE = new CodeMsg(500201,"用户名已存在");
    public static CodeMsg MAIL_HAS_SAVE = new CodeMsg(500201,"邮箱已被占用");
    public static CodeMsg USER_PASSWORD_ERROR = new CodeMsg(500202,"用户名或密码输入错误！");
    public static CodeMsg USER_LOGIN_TOO_MANY = new CodeMsg(500202,"登录频繁！请稍后再试");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500202,"密码验证错误！");
    public int getCode() {
        return code;
    }
    public static CodeMsg SESSSON_ERROR = new CodeMsg(400,"登录失效！请重新登录");
    //设备错误码
    public static CodeMsg MAC_NOTFOUND = new CodeMsg(500203,"不支持该设备！");
    public static CodeMsg DEVICE_HAS_BE_BIND = new CodeMsg(500203,"设备已被绑定！");
    //websocket返回码
    public static CodeMsg CONNECTING = new CodeMsg(500203,"全双工链接建立成功！");

    public String getMsg() {
        return msg;
    }

    //填充参数。
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

}

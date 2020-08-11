package cn.qxhua21.led.config;


//Result类就是一个结果类，用来放返回的结果，其中包括code、msg和data
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    private Result(int code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    private Result(T data){     //构造函数，只需写一次
        this.code = CodeMsg.SUCCESS.getCode();
        this.msg = CodeMsg.SUCCESS.getMsg();
        this.data = data;
    }
    //构造函数，设置code和msg。
    public Result(CodeMsg codeMsg) {
        if(codeMsg!=null){
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
    //成功函数，只需要添加数据即可。
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    //WebSocket函数，添加返回指令。
    public static <T> Result<T> Socket(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

    //失败，只需要添加code以及msg即可。
    public static <T> Result<T> error(CodeMsg codeMsg){
        return new Result<T>(codeMsg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}

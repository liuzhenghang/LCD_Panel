function ServerError(data) {
    if (typeof(data.error)=="undefined"){
        app.$notify.error({
            title: '错误',
            message: data.msg+":"+data.code
        });
    }else {
        errorMessage("致命错误："+data.error)
    }
}
function errorMessage(message) {
    layer.msg(message, {icon: 5});
}
function successMessage(message) {
    app.$message({
        message: message,
        type: 'success'
    });
}
function loadingMessage(message,time) {
    time=time*1000;
    layer.msg(message, {
        icon: 16
        ,shade: 0.01
        ,time:time
    });
}
function socketMessage(message) {
    app.$notify.info({
        title: '消息',
        message: message,
        position: 'bottom-right'
    });
}
function socketError(message) {
    app.$notify.error({
        title: '错误',
        message: message,
        position: 'bottom-right'
    });
}
function socketSuccess(message) {
    app.$notify({
        title: '成功',
        message: message,
        position: 'bottom-right',
        type: 'success',

    });
}

function loginOk() {
    openPanel(0);
    getDevices();
    layer.closeAll();
    openSocket();
}
function login() {
    app.RegisterPanel=false;
    let url="/user/login";
    if (app.mail==='' || app.passwordA===''){
        errorMessage("请输入邮箱或密码");
        return;
    }
    loadingMessage("正在登陆",20);
    $.post(url,{mail:app.mail,password:app.passwordA},function (data) {
        layer.closeAll();
        if (data.code===0){
            data=data.data;
            app.id=data.id;
            app.USERNAME=data.username;
            app.token=data.token;
            localStorage.setItem("id",app.id);
            localStorage.setItem("username",app.USERNAME);
            localStorage.setItem("token",app.token);
            loginOk();
        }else {
            ServerError(data);
        }
    });
}
function register() {
    if (!app.RegisterPanel){
        app.RegisterPanel=true;
        return;
    }
    if (app.mail==='' || app.passwordA===''|| app.passwordB===''|| app.username===''){
        errorMessage("请输入完整");
        return;
    }
    if (app.passwordA!==app.passwordB){
        errorMessage("两次密码不一致");
        return;
    }
    let url="/user/register";
    $.post(url,{mail:app.mail,username:app.username,password:app.passwordB},function (data) {
        if (data.code===0){
            successMessage("注册成功");
            setTimeout(function () {
                layer.closeAll();
                login();
            },500)
        }else {
            ServerError(data);
        }
    });
}
function getDevices() {
    let url="/user/devices";
    $.ajax({
        url:url,
        type:'post',
        headers:{id:app.id,token:app.token},
        success: function(res){
            if (res.code === 0) {
                // socketSuccess("设备拉取成功");
                app.device=res.data;
            }else {
                ServerError(res);
            }
        },
    })
}
function getOneDevice(i,auto) {
    const loading = app.$loading({
        lock: true,
        text: '请求数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
    });
    app.nowDevice=i;
    let url="/device/message";
    $.ajax({
        url:url,
        type:'post',
        headers:{id:app.id,token:app.token},
        data:{mac:app.device[i].mac},
        success: function(res){
            loading.close();
            if (res.code === 0) {
                openPanel(1);
                socketSuccess("已发送刷新请求");
            }else {
                ServerError(res);
            }
        },
    })
}
function sendText(mac) {
    layer.prompt({title: '输入要显示的文字，并确认', formType: 3}, function(text, index){
        if (text.length>10){
            errorMessage("最多10个汉字");
            return;
        }
        let url="/device/upText"
        $.ajax({
            url:url,
            type:'post',
            headers:{id:app.id,token:app.token},
            data:{mac:mac,text:text},
            success: function(res){
                if (res.code === 0) {
                    layer.close(index);
                    socketSuccess("提交修改成功，等待设备生效");
                }else {
                    ServerError(res);
                }
            },
        })

    });
}
function bindDevice() {
    layer.prompt({title: '起一个名字', formType: 3}, function(name, index){
        if (name.length>8){
            errorMessage("最多8个汉字");
            return;
        }
        layer.close(index);
        layer.prompt({title: '输入MAC地址', formType: 3}, function(mac, index){
            let url="/user/addDevices"
            $.ajax({
                url:url,
                type:'post',
                headers:{id:app.id,token:app.token},
                data:{mac:mac,name:name},
                success: function(res){
                    if (res.code === 0) {
                        layer.closeAll();
                        successMessage("绑定成功");
                        setTimeout(function (){
                            location.reload();
                        },1000);
                    }else {
                        ServerError(res);
                    }
                },
            })
        });
    });
}
function unBindDevice(mac) {
    layer.prompt({title: '输入账户密码', formType: 1}, function(pass, index){
        let url="/user/unbind"
        $.ajax({
            url:url,
            type:'post',
            headers:{id:app.id,token:app.token},
            data:{mac:mac,pass:pass},
            success: function(res){
                if (res.code === 0) {
                    successMessage("解绑成功");
                    setTimeout(function (){
                        location.reload();
                    },1000);
                }else {
                    ServerError(res);
                }
            },
        })
        layer.close(index);
    });
}
let socket;
function openSocket() {
    if(typeof(WebSocket) == "undefined") {
        socketError("该浏览器不支持WebSocket，无法获取实时数据");
    }else{
        console.log("您的浏览器支持WebSocket");
        //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
        let userId = app.id;
        let token=app.token;
        let socketUrl="ws://"+window.location.host+"/websocket/"+userId+"/"+token;
        console.log(socketUrl);
        if(socket!=null){
            socket.close();
            socket=null;
        }
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            socketMessage("尝试连接WebSocket");
            // console.log("websocket已打开");
            // socketSuccess("WebSocket已连接")
            //socket.send("这是来自客户端的消息" + location.href + new Date());
        };
        //获得消息事件
        socket.onmessage = function(msg) {
            // var serverMsg = "收到服务端信息：" + msg.data;

            // console.log(serverMsg);

            let serverMsg=JSON.parse(msg.data);
            // console.log(serverMsg);
            switch (serverMsg.type){
                case "line":
                    switch (serverMsg.msg){
                        case "offline":
                            for (let i in app.device){
                                if (app.device[i].mac===serverMsg.mac){
                                    app.device[i].online=false;
                                }
                            }
                            break;
                        default:
                            for (let i in app.device){
                                if (app.device[i].mac===serverMsg.mac){
                                    app.device[i].online=true;
                                }
                            }
                    }
                    break;
                case "socket":
                    switch (serverMsg.msg){
                        case "success":
                            socketSuccess("WebSocket已连接");
                            break;
                        case "KickOut":
                            socketError("异地登陆,WebSocket已断开");
                            socket.close();
                            break;
                    }
                    break;
                case "refresh":
                    for (let i in app.device){
                        if (app.device[i].id===serverMsg.msg.id){
                            // app.device[i]=serverMsg.msg;
                            let d=serverMsg.msg;
                            app.device[i].mac=d.mac;
                            app.device[i].name=d.name;
                            app.device[i].date=d.date;
                            app.device[i].id=d.id;
                            app.device[i].online=d.online;
                            app.device[i].text=d.text;
                            app.device[i].userId=d.userId;
                            app.device[i].version=d.version;
                            console.log("device")
                            console.log(app.device[i])
                            console.log("data")
                            console.log(serverMsg.msg)
                        }
                    }
                    break;

            }
            //发现消息进入    开始处理前端触发逻辑
        };
        //关闭事件
        socket.onclose = function() {
            socketMessage("WebSocket已关闭")
            console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function() {
            socketError("WebSocket错误");
        }
    }
}
function sendMessage() {
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else {
        // console.log("您的浏览器支持WebSocket");
        var toUserId = document.getElementById('toUserId').value;
        var contentText = document.getElementById('contentText').value;
        var msg = '{"toUserId":"'+toUserId+'","contentText":"'+contentText+'"}';
        console.log(msg);
        socket.send(msg);
    }
}
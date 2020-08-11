let app;
function startVue() {
    openPanel(2);
    app=new Vue({
        el:"#main",
        data:{
            USERNAME:'',id:0,token:'',
            username:'',mail:'',passwordA:'',passwordB:'',RegisterPanel:false,
            device:[],nowDevice:-1,
        },
        methods:{
            getOne:function (i,auto) {
                getOneDevice(i,auto);
            },
            upText:function () {
                sendText(app.device[app.nowDevice].mac);
            },
            unBindDevice:function () {
                layer.confirm('是否解绑:'+app.nowDevice.name, {
                    btn: ['取消','解绑'] //按钮
                }, function(){
                    layer.close(layer.index);
                }, function(){
                    unBindDevice(app.device[app.nowDevice].mac);
                });
            }
        }
    })
    beforeLogin();
}
function openPanel(id) {
    $(".home").slideUp(0);
    switch (id) {
        case 0:$(".home").eq(id).addClass("layui-anim layui-anim-upbit");break;
        case 1:$(".home").eq(id).addClass("layui-anim layui-anim-scale");break;
        case 2:$(".home").eq(id).addClass("layui-anim layui-anim-up");break;
    }

    $(".home").eq(id).slideDown(0);
}
function beforeLogin() {
    let id=localStorage.getItem("id");
    let USERNAME=localStorage.getItem("username");
    let token=localStorage.getItem("token");
    if (id===null || USERNAME===null || token===null){
        return;
    }
    const loading = app.$loading({
        lock: true,
        text: '正在自动登录',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
    });
    let url="/user/token";
    $.ajax({
        url:url,
        type:'post',
        headers:{id:id,token:token},
        success: function(res){
            loading.close();
            if (res.code === 0) {
                app.$message({
                    message: '登录成功,跳转中',
                    type: 'success'
                });
                app.id=id;
                app.USERNAME=USERNAME;
                app.token=token;
                setTimeout(function () {
                    loginOk();
                },500)

            }else if (data.code===400){
                this.$message({
                    message: '登录失效，请重新登录',
                    type: 'warning'
                });
            }else {
                ServerError(data);
            }
        },
    })
}
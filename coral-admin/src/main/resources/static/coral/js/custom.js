//获取微信
function getWechat() {
    layer.photos({
        photos: {
            "data": [{
                "src": "coral/images/769990999.png",
            }]
        }
        ,area: ['320px','435px']
        ,anim: 0
        ,end: function(){
            layer.closeAll();
        }
        ,success: function(layero){
            layer.tips('您可以添加开发者咨询', layero, {
                tips: [1, '#3595CC']
                ,time: 0
            });
        }
    });
}
//获取shiro工具栏按钮
function shiroToolbar(shiroSave,shiroDelete) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }
    toolbarHtml += '</p>';
    return toolbarHtml;
}

//获取shiro工具栏按钮
function shiroToolbar8(shiroSave,shiroDelete) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }
    toolbarHtml+="&nbsp;&nbsp;&nbsp;总客户数:<span name='kehushu'></span>&nbsp;&nbsp;&nbsp;已绑定:<span name='ybds'></span>"
    toolbarHtml += '</p>';
    return toolbarHtml;
}

function shiroToolbar5(shiroSave,shiroDelete) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }

    toolbarHtml +="<a  class=\"layui-btn layui-btn-danger layui-btn-xs\"\n" +
        "lay-event=\"scsj\">测试生成数据，请勿随意点击</a>";
    toolbarHtml += '</p>';
    return toolbarHtml;
}

function shiroToolbar2(shiroSave,shiroDelete,shirpPaigong) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }
    if(shirpPaigong){
        toolbarHtml += '<button lay-event="paigong" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe612;</i>派工</button>';
    }
    toolbarHtml += '<button lay-event="tzht" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
        '<i class="layui-icon">&#xe616;</i>停止服务</button>';

    toolbarHtml += '<button lay-event="bsfee" class="layui-btn layui-btn-sm x` icon-btn">' +
        '<i class="layui-icon">&#xe65e;</i>欠款补收</button>';

    toolbarHtml += '&nbsp;&nbsp;记账户数：(<span name="jzhs"></span>) 正常：(<span name="zc"></span>) 停户：(<span onclick="toContachInfo(4)"  name="th"></span>) 终止：(<span onclick="toContachInfo(5)" name="zz"></span>) 注销：(<span onclick="toContachInfo(6)" name="zx"></span>)</p>';
    return toolbarHtml;
}

function shiroToolbar3(shiroSave,shiroDelete) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }
    toolbarHtml += '&nbsp;&nbsp;今日金额：<span name="jrje"></span>&nbsp;&nbsp;收费总金额：<span name="zje"></span>&nbsp;&nbsp;合同金额：<span name="htje"></span>&nbsp;&nbsp;业务金额：<span name="ywje"></span>&nbsp;&nbsp;退款金额：<span name="tkje"></span></p>';
    return toolbarHtml;
}

//获取shiro工具栏按钮
function shiroToolbar4(shiroSave,shiroContractSave,shiroDelete) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加收费</button>&nbsp;';
    }
    if(shiroContractSave){
        toolbarHtml += '<button lay-event="addcontract" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加合同</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }


    toolbarHtml += '</p>';
    return toolbarHtml;
}

//获取shiro工具栏按钮
function shiroToolbar(shiroSave,shiroDelete) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
    if(shiroSave){
        toolbarHtml += '<button lay-event="add" class="layui-btn layui-btn-sm icon-btn">' +
            '<i class="layui-icon">&#xe654;</i>添加</button>&nbsp;';
    }
    if(shiroDelete){
        toolbarHtml += '<button lay-event="del" class="layui-btn layui-btn-sm layui-btn-danger icon-btn">' +
            '<i class="layui-icon">&#xe640;</i>删除</button>';
    }
    toolbarHtml += '  </p>';
    return toolbarHtml;
}
//显示数据信息
function showCountText(text) {
    //工具栏 按钮
    var toolbarHtml ='<p>';
        toolbarHtml +=text;
    toolbarHtml += '</p>';
    return toolbarHtml;
}

//获取shiro右键绑定按钮
function shiroBindCtxMenu(shiroDelete,shiroUpdate) {
    //右键绑定按钮
    var bindCtxMenu = [];
    if(shiroDelete){
        bindCtxMenu.push({
            icon: 'layui-icon layui-icon-close text-danger',
            name: '<span class="text-danger">删除</span>',
            click: function (d) {
                doDel(d);
            }
        })
    }
    if(shiroUpdate){
        bindCtxMenu.push({
            icon: 'layui-icon layui-icon-edit',
            name: '修改',
            click: function (d) {
                showEditModel(d);
            }
        })
    }
    return bindCtxMenu;
}

function previewImg(src) {
    var imgHtml = "<img src='" + src + "' />";
    //捕获页
    var setting = {
        type: 1,
            shade: 0.8,
        area: ['100%', '100%'],
        shadeClose: true,
        closeBtn: false,
        offset: '100px',
        title: false, //不显示标题
        content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        cancel: function () {
        //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });
        }
    }

    var windowH = $(window).height();
    var windowW = $(window).width();

    getImageWidth(src,function(w,h){
        //console.log("win:"+windowH+","+windowW);
        //console.log("img:"+h+","+w);
        // 调整图片大小
        if(w>windowW || h>windowH){
            if(w>windowW && h>windowH){
                w = thisimg .width()*3;
                h = thisimg .height()*3;
                setting.area = [w+"px",h+"px"];
            }else if(w>windowW){
                setting.area = [windowW+"px",windowW*0.5/w*h+"px"];
            }else{
                setting.area = [w+"px",windowH+"px"];
            }
        }else{
            setting.area = [w+"px",h+"px"];
        }
        // 设置layer
        layer.open(setting);
    });
}

function getImageWidth(url,callback){
    var img = new Image();
    img.src = url;

    // 如果图片被缓存，则直接返回缓存数据
    if(img.complete){
        callback(img.width, img.height);
    }else{
        // 完全加载完毕的事件
        img.onload = function(){
            callback(img.width, img.height);
        }
    }
}
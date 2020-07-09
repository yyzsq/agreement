(function ($) {
    $.learunindex = {
        jsonWhere: function (data, action) {
            if (action == null) return;
            var reval = new Array();
            $(data).each(function (i, v) {
                if (action(v)) {
                    reval.push(v);
                }
            })
            return reval;
        },
        makeHtml: function (datas) {
            // alert(_html+"=data.child===>"+_html)
            return get_html(datas);
        },
        loadMenu: function () {
            var mdata  ="";
            $.ajax({
                type: "get",
                url: "prekit/rbac/right/leftSidebar",
                data: {
                },
                async:false, // 异步请求
                cache:false, // 设置为 false 将不缓存此页面
                dataType: 'json', // 返回对象
                success: function(res) {
                    if(res.code == 0){
                        mdata  = res.data;
                        if(mdata == "" || mdata == null || mdata.length == 0){
                            mdata = [
                                {
                                    "deleted":0,
                                    "flag":"right:add",
                                    "icon":"layui-icon-bluetooth",
                                    "id":5,
                                    "level":3,
                                    "link":"home",
                                    "name":"控制台",
                                    "pid":0,
                                    "type":1
                                },
                                {
                                    "child":[
                                        {
                                            "deleted":0,
                                            "flag":"right",
                                            "icon":"layui-icon-gift",
                                            "id":2,
                                            "level":2,
                                            "link":"right.html",
                                            "name":"权限管理",
                                            "pid":1,
                                            "type":0
                                        }
                                    ],
                                    "deleted":0,
                                    "flag":"sys",
                                    "icon":"layui-icon-unlink",
                                    "id":1,
                                    "level":1,
                                    "link":"",
                                    "name":"系统管理",
                                    "pid":0,
                                    "type":0
                                }
                            ];
                        }
                    }else{
                        if(mdata == "" || mdata == null || mdata.length == 0){
                            mdata = menusData_def;
                        }
                    }
                },
                error: function(res) {
                    // 请求失败函数
                    console.log(res);
                    if(mdata == "" || mdata == null || mdata.length == 0){
                        mdata = menusData_def;
                    }
                }
            })
            var left_html =  get_html(mdata)
            $("#sidebar-menu").append(left_html);
        }
    };
    $(function () {
        $.learunindex.loadMenu();
    });
})(jQuery);


function get_html(datas) {
    var _html = "";
    for (var i=0;i<datas.length;i++){
        var data = datas[i];
        if(data.pid == '0'){
            _html += '<li class="layui-nav-item">';
            //如果没有子对象
            if(data.child == null){
                _html += '<a lay-href="'+data.link+'">';
                _html += '<i class="layui-icon '+data.icon+'"></i>&emsp;';
                _html += '<cite>'+data.name+'</cite>';
                _html += '</a>';
            }else{
                //如果有子菜单
                _html += '<a>';
                _html += '<i class="layui-icon '+data.icon+'"></i>&emsp;';
                _html += '<cite>'+data.name+'</cite>';
                _html += '</a>';
                _html += '<dl class="layui-nav-child">';
                _html += get_html(data.child);
                _html += '</dl>';
            }
            _html += '</li>';
        }else{
            if(data.child == null){
                _html += '<dd>';
                _html += '<a lay-href="'+data.link+'">';
                _html += '<i class="layui-icon '+data.icon+'"></i>&emsp;';
                _html += '<cite>'+data.name+'</cite>';
                _html += '</a>';
                _html += '</dd>';
            }else{
                //如果有子菜单
                _html += '<dd>';
                _html += '<a lay-href="">';
                _html += '<i class="layui-icon '+data.icon+'"></i>&emsp;';
                _html += '<cite>'+data.name+'</cite>';
                _html += '</a>';
                _html += '<dl class="layui-nav-child">';
                _html += get_html(data.child);
                _html += '</dl>';
                _html += '</dd>';
            }
        }
    }
    return _html;
}
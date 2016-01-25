$(function () {
    function htmlTemplate(arr){
        var i = 0,l = arr.length,html='',count = 0;
        html += '<div class="detail-head">收货地址';
        if(arr.length < 10){
            count = 10 - arr.length;
            $addBox.show();
        }else{
            $addBox.hide();
        }
        html += '<span class="span">（可添加10个地址，当前'+count+'个地址）</span>';
        html += '</div>';
        for(;i<l;i++){
            html += '<div class="add-box">';
            html += '<div class="add-head">';
            html += '<span class="span">'+(i+1)+'.收件人信息</span>';
            if(arr[i].isDefault == "0"){
                html += '当前默认地址';
            }else{
                html += '<a href="##" data-id="'+arr[i].id+'" class="set-default" style="color: #5F5151;">设为默认</a>';
                html += '&ensp;<a href="##" data-id="'+arr[i].id+'" class="btn del">删除</a>';
            }
            html += '</div>';
            html += '<p class="p">收 件 人：'+arr[i].name+'</p>';
            html += '<p class="p">电话号码：'+arr[i].telphone+'</p>';
            html += '<p class="p">详细地址：'+arr[i].address+'</p>';
            html += '</div>';
        }
        $addressList.html(html);
    }
    $.ajaxSetup({
        type:"post",
        dataType:"json"
    });
    var $box = $(".right-detail"),
        $addBox = $("#add-box"),
        $addressList = $("#address-list"),
        $name = $box.find("[name='name']"),
        $mobile = $box.find("[name='mobile']"),
        $address = $box.find("[name='address']"),
        $saveBtn = $box.find(".save-btn");
    /*删除地址*/
    $addressList.on("click",".del.btn", function () {
        var id = $(this).attr("data-id");
        var param = {
            "addressBean.address.id":id,
        };
        $.ajax({
            url:"/jos/address/del.action",
            data:param,
            success: function (data) {
                if(data.rCode == "0"){
                    getAddress();
                }else{/*地址不存在*/
                    getAddress();
                }
            }
        });
    });
    /*设为默认*/
    $addressList.on("click",".set-default", function () {
        var id = $(this).attr("data-id");
        var param = {
            "addressBean.address.id":id,
        };
        $.ajax({
            url:"/jos/address/setDefault.action",
            data:param,
            success: function (data) {
                if(data.rCode == "0"){
                    getAddress();
                }else if(data.rCode == "-1") {/*未登录*/
                    showLoginDialog();
                }else if(data.rCode == "-2"){/*地址不存在*/
                    getAddress();
                }
            }
        });
    });
    /*初始化地址列表*/
    function getAddress(){
        $.ajax({
            url:"/jos/address/que.action",
            success: function (data) {
                if(data.rCode == "0"){
                    htmlTemplate(data.data);
                }else if(data.rCode == "-1") {/*未登录*/
                    showLoginDialog();
                }
            }
        });
    }
    getAddress();
    /*新增地址*/
    $saveBtn.on("click", function () {
        var param = {
            "addressBean.address.name":$name.val(),
            "addressBean.address.telphone":$mobile.val(),
            "addressBean.address.address":$address.val()
        };
        $.ajax({
            url:"/jos/address/add.action",
            data:param,
            success: function (data) {
                if(data.rCode == "0"){
                    getAddress();
                }else if(data.rCode == "-1") {/*未登录*/
                    showLoginDialog();
                }
            }
        });
    });

});

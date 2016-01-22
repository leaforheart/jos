/*加载公共页面*/
$(".include-page").each(function () {
    var _self = $(this);
    var url = _self.attr("data-src");
    var index = _self.length;
    $.ajax({
        url:url,
        success: function (data) {
            _self.get(0).outerHTML = data;
        }
    });
});
$(function () {
    /*显示错误信息*/
    function showMessage(dom,msg){
        dom.html(msg);
    }
    /*禁用发送验证码按钮*/
    function disableBtn(dom){
        $(dom).attr("disabled",true);
        setTimeout(function () {
            $(dom).attr("disabled",false);
        },60000);
    }
    function msgSwitch(code){
        switch (code){
            case 0:showMessage($err,"");break;
            case -1:showMessage($err,"您的号码已被注册！");break;
            case -2:showMessage($err,"您的号码不存在！");break;
            case -3:showMessage($err,"验证码获取失败！");break;
            case -6:showMessage($err,"服务器异常，请联系工作人员！");break;
        }
    }
    var $dialog = $(".ef-dialog-box");
    /*注册 发送验证码*/
    $dialog.on("click",".mobile-code", function () {
        var $register = $("#register-dialog"),
            _self = this,
             $err = $register.find(".err"),
             mobile = $register.find("[name='mobile']").val();
        var param = {
            "authenticateBean.user.primPrin":mobile,
            "authenticateBean.phoneCodeUse":1
        };
        $.ajax({
            url:"/jos/authenticate/phoneCode.action",
            type:"post",
            data:param,
            dataType:"json",
            success: function (data) {
                msgSwitch(data.rCode);
                disableBtn(_self);
            }
        });
});
    /*提交注册按钮*/
    $(".ef-dialog-box").on("click","#register-dialog .login-btn" ,function () {
        var $dialog = $("#register-dialog"),
            mobile = $dialog.find("[name='mobile']").val(),
            password = $dialog.find("[name='password']").val(),
            confirmPassword = $dialog.find("[name='confirm-password']").val(),
            mobileCode = $dialog.find("[name='mobile-code']").val(),
            $checkbox = $dialog.find(".checkbox"),
            $err = $dialog.find(".err");
        if(!$checkbox.hasClass("cur")){
            showMessage($err,"请先同意用户服务协议");
            return;
        }
        var param = {
            "authenticateBean.user.primPrin":mobile,
            "authenticateBean.user.credential":password,
            "authenticateBean.phoneCode":mobileCode
        };
        $.ajax({
            url:"/jos/authenticate/enroll.action",
            type:"post",
            data:param,
            dataType:"json",
            success: function (data) {
                console.log(data);
            }
        });

    });

    $(document).on("click","#login-button", function () {
        $(".ef-dialog").hide();
        $("#login-dialog").show();
    });
    $(document).on("click","#register-button", function () {
        $(".ef-dialog").hide();
        $("#register-dialog").show();
    });
    /*页面顶部*/
    $(document).on("hover",".navi-top", function (e) {
        var obj = {},$box = $(this).find(".navi-box");
        if($(this).hasClass("cur")){
            obj.bottom = "100%";
            $(this).removeClass("cur");
        }else{
            obj.bottom = "0%";
            $(this).addClass("cur");
        }
        $box.stop().animate(obj,"fast");
    });
    var scrollTop = 0;
    $(window).on("scroll", function (e) {
        var scroll = $(window).scrollTop();
        var obj = {};
        //滚轮向上滑
        if(scroll > scrollTop){
            obj.bottom = "0%";
        //滚轮向下滑
        }else{
            obj.bottom = "100%";
        }
        $(".navi-top .navi-box").stop().animate(obj,"fast", function () {
            scrollTop = scroll;
        });
    });
    /*页面搜索框*/
    $(".search-box .search").on("click", function (e) {
        e.stopPropagation();
        var obj={},$parent = $(this).parent().parent();
        if($parent.hasClass("cur")){
            obj.width = "20px";
            $parent.removeClass("cur");
        }else{
            obj.width = "197px";
            $parent.addClass("cur");
        }
        $parent.stop().animate(obj,"fast");
    });
    /*input框*/
    $(document).on("focus",".placeholder",function () {
        var _self = $(this).get(0);
        if(_self.value == _self.defaultValue){
            _self.value = "";
            $(this).removeClass("cur");
        }
    }).on("blur", function () {
        var _self = $(this).get(0);
        if($.trim(_self.value) == ""){
            _self.value = _self.defaultValue;
            $(this).addClass("cur");
        }
    });
    /*checkbox*/
    $(document).on("click",".checkbox,.checkbox2", function (e) {
        e.stopPropagation();
        $(this).toggleClass("cur");
    });
});


/*轮播*/
function Slide(obj){
    var config = {
        loopPlay:false,/*循环播放*/
        scrollNumber:1,/*每次切换个数 <= 显示个数*/
        autoPlay:false,/*自动播放*/
        slideBtn:false,/*侧边按钮*/
        navigateBtn:false,/*导航按钮*/
        navigateBtnType:"number",/*导航按钮类型 number or icon*/
        direction:"left",/*切换方向 left top*/
        switchType:"scroll",/*切换方式 滚动或者渐隐渐变 scroll/shade */
        animateTime:1500,/*动画时间*/
        toggleTime:3000,/*每次动画的间隔*/
        beforeCallback:undefined,/*前置函数*/
        switchCallback:undefined/*动画回调函数*/
    }
    config = $.extend(config,obj);
    this.loopPlay = config.loopPlay;
    this.scrollNumber = config.scrollNumber;
    this.autoPlay = config.autoPlay;
    this.slideBtn = config.slideBtn;
    this.navigateBtn = config.navigateBtn;
    this.navigateBtnType = config.navigateBtnType;
    this.direction = config.direction;
    this.switchType = config.switchType;
    this.animateTime = config.animateTime;
    this.toggleTime = config.toggleTime;
    this.beforeCallback = config.beforeCallback;
    this.switchCallback = config.switchCallback;
}
Slide.prototype = {
    playIndex:0,
    timer:undefined,
    attrName:"margin-left",
    wrapHtml:"<div class='ef-maskbox'></div>",
    btnHtml:"<a href='##' class='ef-slidebtn pre'><i class='arr'></i></a><a href='##' class='ef-slidebtn next'><i class='arr'></i></a>",
    start: function () {
        this.slideBox = $(".ef-slide");
        this.ulDom = this.slideBox.find("ul");
        /*前置函数*/
        if(typeof this.beforeCallback === "function"){
            try{
                this.beforeCallback();
            }catch(e){
                console.log(e.message);
            }
        }
        this.liDom = this.getLis();
        /*设置遮罩*/
        this.ulDom.wrap(this.wrapHtml);
        this.maskBox = this.slideBox.find(".ef-maskbox");

        if(this.switchType == "scroll"){
            /*显示个数*/
            this.showNumber = this.getShowNumber();
            /*判断是否循环播放*/
            if(this.showNumber == 1){
                /*克隆首尾Li标签*/
                this.cloneLiByLoopPlay();
            }
            /*设置ul宽度*/
            /*初始化ul的位置marginleft/margintop*/
            this.setUlCss();
        }else{
            this.liDom.first().css("z-index",2);
        }
        /*绑定事件*/
        this.bind();
        /*判断是否为自动播放*/
        if(this.autoPlay){
            this.setTimefunc();
        }
    },
    startSwitchCallback: function () {
        if(typeof this.switchCallback === "function"){
            try{
                this.switchCallback();
            }catch(e){
                console.log(e.message);
            }
        }
    },
    toggleNaviBtn: function (playIndex) {
        this.naviBtn.eq(playIndex).addClass("cur").siblings().removeClass("cur");
    },
    appendNaviBtn: function () {
        var length = this.getLiLength();
        if(this.showNumber == 1){
            length -= 2;
        }
        var arr = [];
        if(this.navigateBtnType == "number"){
            for(var i = 0;i < length;i++){
                if(i == 0){
                    arr.push("<a href='##' class='ef-navigationicon cur' data-playIndex = '"+i+"'>"+(i+1)+"</a>");
                }else{
                    arr.push("<a href='##' class='ef-navigationicon' data-playIndex = '"+i+"'>"+(i+1)+"</a>");
                }
            }
        }else{
            for(var i = 0;i < length;i++){
                if(i == 0){
                    arr.push("<a href='##' class='ef-navigationicon cur' data-playIndex = '"+i+"'></a>");
                }else{
                    arr.push("<a href='##' class='ef-navigationicon' data-playIndex = '"+i+"'></a>");
                }
            }
        }
        this.slideBox.append("<div class='ef-navigationbox'>"+arr.join("")+"</div>");
        this.naviBtn = this.slideBox.find(".ef-navigationicon");
    },/*只支持单页的无缝循环轮播*/
    autoSlideRight: function () {
        this.playIndex++;
        if(this.playIndex + 1 == this.getLiLength()){
            this.playIndex = 0;
            this.setUlMargin();
            this.playIndex++;
        }
    },
    clickShadeAnimate: function (playIndex) {
        var _self = this;
        if(playIndex >= this.getLiLength()){
            playIndex = 0;
        }else if(playIndex < 0){
            playIndex = this.getLiLength() - 1;
        }
        /*点击往上浮动，原先的往下沉*/
        this.liDom.eq(playIndex).css({
            "z-index":2,
            opacity:0
        });
        this.liDom.eq(this.playIndex).css("z-index",1);
        this.toggleNaviBtn(playIndex);
        this.liDom.eq(playIndex).animate({
            opacity:1
        },this.animateTime, function () {
            _self.liDom.eq(_self.playIndex).css("z-index",0);
            /*重置playIndex*/
            _self.playIndex = playIndex;
            _self.startSwitchCallback();
        });

    },
    shadeAnimate: function () {
        var _self = this;
        var preIndex = this.playIndex - 1;
        var nextIndex = this.playIndex + 1;
        if(this.playIndex == 0){
            preIndex = this.getLiLength() - 1;
        }
        if(this.playIndex == this.getLiLength() - 1){
            nextIndex = 0;
        }
        this.liDom.eq(this.playIndex).css({
            "z-index":2
        });
        this.liDom.eq(preIndex).css({
            "z-index":0,
            opacity:1
        });
        this.liDom.eq(nextIndex).css({
            "z-index":0,
            opacity:1
        });
        this.liDom.eq(nextIndex).css("z-index",1);
        _self.toggleNaviBtn(nextIndex);
        this.liDom.eq(this.playIndex).stop(true,true).animate({
            opacity:0
        },_self.animateTime, function () {
            _self.playIndex++;
            if(_self.playIndex == _self.getLiLength()){
                _self.playIndex = 0;
            }
            _self.startSwitchCallback();
        });
    },
    slideLeft: function () {
        if(this.showNumber > 1){
            var playIndex = 0;
            if(this.isBegin()){/*刚好到顶部*/
                if(this.loopPlay){
                    playIndex = this.getLiLength() - this.showNumber;
                }else{
                    playIndex = 0;
                }
            }else if(this.prePageEnough()){
                playIndex = 0;
            }else{
                playIndex = this.playIndex - this.scrollNumber;
            }
            this.playIndex = playIndex;
        }else{
            this.playIndex--;
            if(this.playIndex < -1){
                this.playIndex = this.getLiLength() - 3;
                this.setUlMargin();
                this.playIndex--;
            }
        }
    },
    slideRight: function () {
        var playIndex = 0;
        if(this.isEnd()){/*刚好最后一页*/
            if(this.loopPlay){
                playIndex = 0;
            }else{
                playIndex = this.getLiLength() - this.showNumber;
            }
        }else if(this.nextPageEnough()){/*不足最后一页*/
            playIndex = this.getLiLength() - this.showNumber;
        }else{
            playIndex = this.playIndex + this.scrollNumber;
        }
        this.playIndex = playIndex;
    },
    isBegin: function () {
        return this.playIndex == 0;
    },
    isEnd: function () {
        return this.playIndex + this.showNumber == this.getLiLength();
    },
    nextPageEnough: function () {
        /*false 不足一页*/
        return this.playIndex + this.showNumber + this.scrollNumber > this.getLiLength();
    },
    prePageEnough: function () {
        return this.playIndex - this.scrollNumber < 0;
    },
    getShowNumber: function () {
        if(this.direction =="left"){
            return Math.floor(this.maskBox.width()/this.getLiWidth());
        }
        if(this.direction == "top"){
            return Math.floor(this.maskBox.height()/this.getLiHeight());
        }
    },
    getLiHeight: function () {
        return this.liDom.height();
    },
    getLiWidth: function () {
        return this.liDom.width();
    },
    getLiLength: function () {
        return this.liDom.length;
    },
    getLis: function () {
        return this.ulDom.find("li");
    },
    getUlMargin: function () {
        var playIndex = this.playIndex;
        if(this.showNumber == 1){
            playIndex++;
        }
        if(this.direction == "left"){
            return -playIndex * this.getLiWidth();
        }
        if(this.direction === "top"){
            return -playIndex * this.getLiHeight();
        }
    },
    setUlMargin: function () {
        var margin = this.getUlMargin() + "px";
        var attrName = "margin-left";
        if(this.direction=="top"){
            attrName = "margin-top";
        }
        this.ulDom.css(attrName,margin);
    },
    setUlCss: function () {
        var value = this.liDom.length * this.getLiWidth() + "px";
        var attr = "width";
        if(this.direction == "top"){
            attr = "height";
            value = this.liDom.length * this.getLiHeight() + "px";
        }
        this.ulDom.css(attr,value);
        this.setUlMargin();
    },
    cloneLiByLoopPlay: function () {
        /*循环播放的时候，只有显示一个元素的时候，才会实现无缝轮播，其他的则是从开头或者结尾循环*/
        var $first = this.liDom.first().clone();
        var $last = this.liDom.last().clone();
        this.ulDom.prepend($last).append($first);
        /*重置li节点集合*/
        this.liDom = this.getLis();
    },
    setTimefunc: function () {
        var _self = this;
        this.timer = setInterval(function () {
            if(_self.switchType == "scroll"){
                if(_self.showNumber>1){
                    _self.slideRight();
                }else{
                    _self.autoSlideRight();
                }
                _self.slideAnimate();
            }else{
                _self.shadeAnimate();
            }
        },_self.toggleTime);
    },
    slideAnimate: function () {
        var _self = this;
        var margin = _self.getUlMargin()+"px";
        var obj = {};
        if(this.direction == "left"){
            obj = {
                "margin-left":margin
            };
        }else{
            obj = {
                "margin-top":margin
            };
        }
        var playIndex = this.playIndex;
        if(this.showNumber == 1 && playIndex >= this.getLiLength()-2){
            playIndex = 0;
        }
        this.toggleNaviBtn(playIndex);
        this.ulDom.stop(true,true).animate(obj, function () {
            _self.startSwitchCallback();
        });

    },
    bind: function () {
        var _self = this;
        /*鼠标hover，停止自动播放*/
        (function (isAutoPlay) {
            if(!isAutoPlay){
                return;
            }
            _self.slideBox.on("hover", function () {
                if($(this).hasClass("cur")){
                    $(this).removeClass("cur");
                    _self.setTimefunc();
                }else{
                    $(this).addClass("cur");
                    if(_self.timer){
                        clearInterval(_self.timer);
                        _self.timer = undefined;
                    }
                }
            });
        })(this.autoPlay);

        /*侧边按钮*/
        (function (isBtn) {
            if(!isBtn){
                return;
            }
            _self.slideBox.append(_self.btnHtml);
            var $pre = _self.slideBox.find(".pre");
            var $next = _self.slideBox.find(".next");
            $pre.on("click", function () {
                if(_self.switchType == "scroll"){
                    _self.slideLeft();
                    _self.slideAnimate();
                }else{
                    _self.clickShadeAnimate(_self.playIndex-1);
                }

            });
            $next.on("click", function () {
                if(_self.switchType == "scroll"){
                    if(_self.showNumber > 1){
                        _self.slideRight();
                    }else{
                        /*无缝循环自动播放*/
                        _self.autoSlideRight();
                    }
                    _self.slideAnimate();
                }else{
                    _self.clickShadeAnimate(_self.playIndex+1);
                }
            });
        })(this.slideBtn);

        /*导航按钮*/
        (function (isNavigateBtn) {
            if(!isNavigateBtn){
                return;
            }
            /*插入html*/
            _self.appendNaviBtn();
            _self.naviBtn.on("click", function () {
                var playIndex = parseInt($(this).attr("data-playindex"));
                if(_self.switchType == "scroll"){/*左右切换或者上下切换*/
                    _self.playIndex = playIndex;
                    /*当前点击按钮位于最后一页内，则显示最后一页*/
                    if(playIndex + _self.showNumber >= _self.getLiLength()){
                        _self.playIndex = _self.getLiLength() - _self.showNumber;
                    }
                    _self.slideAnimate();
                }else{
                    _self.clickShadeAnimate(playIndex);
                }
            });
        })(this.navigateBtn);
    }
}
function Select(obj){
    var config = {};
    config = $.extend(config,obj);
    this.callback = config.callback;
    this.start();
}
Select.prototype = {
    constructorDom: function () {
        var html = "<div class='ef-selectHead'><span class='ef-selectText'></span><i class='ef-selectIcon'></i></div>";
        var arr = [];
        this.options.each(function () {
            var value = $(this).attr("value");
            var text = $(this).text();
            arr.push("<li data-value='"+value+"'>"+text+"</li>");
        });
        html += "<ul>"+arr.join("")+"</ul>";
        this.selectBox.prepend(html);
    },
    initSelect: function () {
        var index = this.selectDom.get(0).selectedIndex;
        var $select = this.options.eq(index);
        var value = $select.attr("value");
        var text = $select.text();
        this.setHeadText(text,value);
    },
    triggerChange: function () {
        try{
            this.selectDom.change();
        }catch(e){
            console.log(e.message);
        }
    },
    setHeadText: function (text,value) {
        this.selectText.text(text).attr("data-value",value);
    },
    bind: function () {
        if(typeof this.callback === "function"){
            this.selectDom.on("change", this.callback);
        }
        var _self = this;
        this.selectHead.on("click", function () {
            $(this).toggleClass("cur");
            if($(this).hasClass("cur")){
                _self.ul.slideDown("fast");
            }else{
                _self.ul.slideUp("fast");
            }
        });
        this.ul.on("click","li",function () {
            var value = $(this).attr("data-value");
            var text = $(this).text();
            _self.setHeadText(text,value);
            _self.selectHead.click();
            _self.selectDom.val(value);
            _self.triggerChange();
        });
    },
    start: function () {
        this.selectDom = $(".ef-select").hide();
        this.selectBox = this.selectDom.wrap("<div class='ef-selectBox'></div>").parent();
        this.options = this.selectDom.find("option");
        /*构建dom结构*/
        this.constructorDom();
        this.selectText = this.selectBox.find(".ef-selectText");
        this.ul = this.selectBox.find("ul");
        this.selectHead = this.selectBox.find(".ef-selectHead");
        this.initSelect();
        this.bind();
    }
};







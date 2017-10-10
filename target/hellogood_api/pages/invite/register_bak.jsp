<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/6/25
  Time: 15:36
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type">
    <meta content="text/html; charset=utf-8">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <link rel="Bookmark" href="favicon.ico" type="image/x-icon">
    <meta charset="utf-8">
    <title>易约，您的专属约会管家</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="format-detection" content="email=no">
    <link rel="stylesheet" type="text/css" href="css/reset.css" />
    <link rel="stylesheet" type="text/css" href="css/index.css" />
    <link rel="stylesheet" type="text/css" href="css/flat-ui.min.css" />
    <link rel="stylesheet" type="text/css" href="css/animations.css" />

</head>
<body style="background-color:#2b2b2b;">
<%
    String id = "";
    String inviteType = "";
    id = request.getParameter("id");
    if (id != null) {
        inviteType = id.substring(id.length() - 1);
        id = id.substring(0, id.length() - 1);
    }
%>
<div class="page page-1-1 page-current one">
    <div class="wrap">
        <img class="img_1 pt-page-moveFromTopFade" src="images/logo.png" />

        <div class="img_2" style="width:82%;margin:auto;">

            <div class="ip_box phone">
                <div class="baliu"></div>
                <input maxlength="11" placeholder="输入手机号" style="float:right;width:72%;text-indent:1em;background-color:#FFF;opacity:0.55;border-radius:5px; outline:none;border:0;color:#2b2b2b;font-size:16px;" id="mobile">
                <input type="hidden" id="inviteUid" value="<%=id%>">
                <input type="hidden" id="inviteType" value="<%=inviteType%>">
            </div>

            <div class="ip_box mima">
                <input placeholder="设置密码" type="password" style="width:100%;text-indent:1em;background-color:#FFF;opacity:0.55;border-radius:5px; outline:none;border:0;color:#2b2b2b;font-size:16px;" id="password">
            </div>
            <div class="ip_box mima">
                <select id="sex" style="width:100%;text-indent:1em;background-color:#FFF;opacity:0.55;border-radius:5px; outline:none;border:0;color:#2b2b2b;font-size:16px;">
                    <option value="">选择性别</option>
                    <option value="男">男</option>
                    <option value="女">女</option>
                </select>
            </div>
            <div class="ip_box">
                <div class="yzmhq">获取验证码</div>
                <input id="smsCode" placeholder="输入验证码" style="float:right;width:72%;text-indent:1em;background-color:#FFF;opacity:0.55;border-radius:5px; outline:none;border:0;color:#2b2b2b;font-size:16px;">
            </div>

        </div>

        <a href="javascript:void(0)"><div id="register" class="zcbtn">注册</div></a>

        <div class="text_1"><img src="images/img_1text.png"></div>


        <img class="img_3 pt-page-moveIconUp" src="images/icon_up.png" />
    </div>
</div>

<div class="page page-2-1 hide">
    <div class="wrap">
        <img class="img_1 hide pt-page-moveFromTop" src="images/img_2tag.png">
        <img class="img_6 hide pt-page-moveIconUp" src="images/icon_up.png" />
    </div>
</div>

<div class="page page-3-1 hide">
    <div class="wrap">
        <img class="img_1 hide pt-page-moveFromBottom" src="images/img_3tag.png">
        <img class="img_6 hide pt-page-moveIconUp" src="images/icon_up.png" />
    </div>
</div>

<div class="page page-4-1 hide">
    <div class="wrap">

        <img class="img_1 hide pt-page-moveFromRight" src="images/img_4_tag.png">
        <a class="xiazai" href=""><img class="img_2 hide pt-page-moveFromBottom" src="images/img_4btn.png"></a>
        <img class="img_3 hide pt-page-moveFromBottomFade" src="images/img_4bottomtext.png">
    </div>
</div>
</div>
<script src="js/zepto.min.js"></script>
<script src="js/touch.js"></script>
<script src="js/register.js"></script>
<script src="js/jquery-2.1.4.min.js"></script>

<script>
    $(function(){
        var ipgao = $(".page-1-1").height()*0.072;
        $("input").height(ipgao+"px");
        $("input").css("lineHeight",ipgao+"px");
        $("select").height(ipgao+"px");
        $("select").css("lineHeight",ipgao+"px");
        $(".yzmhq").height(ipgao+"px");
        $(".yzmhq").css("lineHeight",ipgao+"px");
        $(".yzmhq").css("backgroundSize","auto 100%");
        $(".baliu").height(ipgao+"px");
        $(".baliu").css("backgroundSize","auto 100%");
        $(".zcbtn").height(ipgao+"px");
        $(".zcbtn").css("lineHeight",ipgao+"px");
        $(".ip_box").height(ipgao+"px");
    });
</script>


<script type="text/javascript">
    function checkPlatform(){

        if(/android/i.test(navigator.userAgent)){

            $(".xiazai").attr("href","http://ws.hellogood.com.cn/app/Appointment.apk");//这是Android平台下浏览器

        }

        if(/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)){

            $(".xiazai").attr("href","https://itunes.apple.com/cn/app/yi-yue-yue-hui/id1003136897?mt=8");//这是iOS平台下浏览器

        }
    }
    $(document).ready(function(){

        //alert(navigator.platform);

        checkPlatform();

    });

</script>
<script type="text/javascript">
    window.onload = function(){
        if(isWeiXin()){
            $(".xiazai").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment&g_f=991653")
            $(".ios").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment&g_f=991653")
            $(".and").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment")
        }else{
            $(".ios").attr("href","https://itunes.apple.com/cn/app/yi-yue-yue-hui/id1003136897?mt=8")
            $(".and").attr("href","http://ws.hellogood.com.cn/app/Appointment.apk")

        }
    }
    function isWeiXin(){
        var ua = window.navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i) == 'micromessenger'){
            return true;
        }else{
            return false;
        }
    }
</script>
</body>
</html>

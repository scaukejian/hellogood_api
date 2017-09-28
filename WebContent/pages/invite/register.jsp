<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<%
    String id = "";
    String inviteType = "";
    id = request.getParameter("id");
    if (id != null) {
        inviteType = id.substring(id.length() - 1);
        id = id.substring(0, id.length() - 1);
    }
    String path=request.getContextPath();
%>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
<meta name="format-detection" content="telephone=no">
<meta name="format-detection" content="email=no">
<link rel="shortcut icon" href="favicon.png" type="image/x-icon">
<link rel="Bookmark" href="favicon.png" type="image/x-icon">
<title>易悦—中国精英轻奢生活服务平台</title>
<style>
	body{ padding:0; margin: 0; }
	div{ position:relative;}
	.background_image{ width:100%; }
	/*.content{*/
		/*background-image:url(images/white-back.jpg) ;*/
		/*background-repeat:repeat;*/
		/*width:90%;margin-left:5%; margin-top:-20%; */
		/*background-color:#ffffff; padding-bottom:10%;*/
		/*border-top-left-radius:0.4em;*/
		/*border-top-right-radius:0.4em;*/
		/*border-bottom-right-radius:0.4em;*/
		/*border-bottom-left-radius:0.4em;}*/
	input{ 
		margin-left:10%; 
		border-radius: 5px;
		border:1px solid #868686;
		padding-left:6%;
		letter-spacing:1px;
		height:30px; line-height: 25px;
		opacity:0.8;
		border-style:none;
	}
	input:focus {
	 border: 1px solid #fe595f;
	}
	#register{
		 margin-top:12%;width:80%;
		 margin-left:10%; text-align:center; 
		 line-height:30px;
		/*color:#ffffff; */
		 /*background-color:#fe595f;*/
		 /*border-top-left-radius:0.2em;*/
		 /*border-top-right-radius:0.2em;*/
		 /*border-bottom-right-radius:0.2em;*/
		 /*border-bottom-left-radius:0.2em;*/
	}
	#download{
		margin-top:2%;width:80%;
		margin-left:10%; text-align:center;
		line-height:30px;
		/*color:#ffffff;*/
		/*background-color:#fe595f;*/
		/*border-top-left-radius:0.2em;*/
		/*border-top-right-radius:0.2em;*/
		/*border-bottom-right-radius:0.2em;*/
		/*border-bottom-left-radius:0.2em;*/
	}
	#nameDiv{
		margin-top:15%;
		width:80%;
		margin-left:10%;
		text-align:center;
		line-height:30px;
	}
</style>
</head>

<body>
	<div class="background_image"><img src="images/background1.jpg" width="100%"/></div>
    <div style="margin-top:-160%;  z-index:99999; margin-left:40%;"  id="header-logo">
		<!--
		  style="color:#ccc;border-top-left-radius:0.8em;border-top-right-radius:0.8em;border-bottom-right-radius:0.8em;border-bottom-left-radius:0.8em;"
		 -->
        	<img id="header" src="images/logo.png" width="35%" style="margin-top: 12%"/>
            <div class="user-name" id="user-name" style=" margin-top:1%; margin-left:-2.5%;text-align:center; width:35%; font-size:15px;">
            	 <%--<font style="font-size:15px;font-weight:900;">易悦</font>--%>
            </div>
    </div>
    
    <div class="content">
    	<div style="width:100%; text-align:center; padding-top:5%; font-weight: 100;">
			<img  src="images/title-font.png" width="75%"></div>
    	<div id="register-success">	
	    	<div style="width:100%; padding-top:8%;">
	    		<input style=" width:75%;" id="mobile" maxlength="11" placeholder="输入手机号码"/>
	    		<input type="hidden" id="inviteUid" value="<%=id%>"/>
		        <input  type="hidden" id="inviteType" value="<%=inviteType%>"/>
	    	</div>
	       <div style="width:100%; padding-top:2%;">
	       		<input style=" width:75%;" id="password" type="password"  placeholder="输入密码" />
	       </div>
	        <div style="width:100%;padding-top:2%;">
	            <input style=" width:40%;" id="smsCode" maxlength="10"  placeholder="输入验证码" /> 
	            <div style="margin-left:72%;margin-top:-8%; width:25%; color:#f3f4f5;" class="yzmhq"> 获取验证码</div>
	        </div>
	       <div style="width:80%; padding-top:8%; margin-left:10%;  color:#3e3e3e;">
	       		<input type="hidden" name="sex" id="sex">
	       		<img id="select_male" onclick="changeSex('male')" style="width: 7%;opacity:0.8;" src="images/uncheck.png"/>&nbsp;
                <span style="font-size: 15px;color:#f3f4f5;vertical-align: super;" onclick="changeSex('male')" >男</span>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <img id="select_female" onclick="changeSex('female')" style="width: 7%;opacity:0.8;" src="images/uncheck.png"/>
                &nbsp;
                <span style="font-size: 15px;vertical-align: super;color:#f3f4f5;" onclick="changeSex('female')">女</span>
	        </div> 
        </div>
        <div id="registerDiv" >
        	<img id="register"  src="images/register-btn.png" >
        </div>
		<div id="downloadDiv" >
			<a class="footer" >
			<img id="download"  src="images/downloadApp.png" >
			</a>
		</div>
		<div id="nameDiv" >
			<img id="name" width="11%"  src="images/name.png" >
		</div>
	</div>
    
    <div id="download-app" style=" position:fixed; left:0;right:0;bottom:-1%;top:0 auto;width:100%; z-index:999999; margin-top:8%; display:none;">
    	<a class="footer" >
    	<img  src="images/download.png" width="100%"/>
    	</a>
    </div>
    <div style="margin-bottom:10%; margin-top:13%;">
    	<img src="images/loading.gif" data-original="images/footer.jpg" width="100%" />
    </div>
</body>
<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/jquery.lazyload.min.js"></script>
<script type="text/javascript">
 window.onload=function(){

	 $('img').lazyload({
		 placeholder : "images/loading.gif",
		 effect: "fadeIn"
	 });

	 var height = $("#header-logo").height()*0.72;
	 $("#header").height(height+"px");
	 var inputHeight=$("#mobile").height()*0.60;
	 $("input").css("font-size",inputHeight+"px");
	 $(".yzmhq").css("font-size",inputHeight*0.8+"px");
	 $(".sex").css("font-size",inputHeight+"px");
	 if(isWeiXin()){
	        $(".footer").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment&g_f=991653")
	        $(".ios").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment&g_f=991653")
	        $(".and").attr("href","http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment")
	    }else{
	        $(".ios").attr("href","https://itunes.apple.com/cn/app/yi-yue-yue-hui/id1003136897?mt=8")
	        $(".and").attr("href","http://ws.hellogood.com.cn/app/Appointment.apk")
	    }
	    initSex();
	 //   initHeadFlag();
		
	};
	
	//初始化头像
	function initHeadFlag(){
		var userId=$("#inviteUid").val();
		if(userId == 'null' ||userId==undefined || userId == ''){
			$("#header").html("<img class='header' src='images/hellogood_logo.png'/>");
			$("#register").html("立即注册");
			return;
		}
		 //var  param={"userId":userId};
		var url = '/hellogood_api/auth/getHeadFlag/'+userId+'.do';
		$.ajax({
	        type: 'GET',
	        url: url,
			success: function (result) {
				 if (result.status == 'error' || result.status == 'failed') {
	                    alert(result.message);
	                    return;
	              }
				 if(result.data != null){
					$("#header").html("<img class='header' src='/hellogood_api/userDatum/download.do?fileName="+result.data.imgName+"'/>");
					if(result.user.sex == '女'){
						$("#userName").html(result.user.firstName+"女士");
					}else{
						$("#userName").html(result.user.firstName+"先生");
					}
				 }
			}
		});
	}
	
	
	function isWeiXin(){
	    var ua = window.navigator.userAgent.toLowerCase();
	    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
	        return true;
	    }else{
	        return false;
	    }
	}
	function initSex(){
		//默认性别选择
	    // $('#sex').val('男');
	    // $('#select_male').attr('src','images/man.png');
	     //$('#select_female').attr('src','images/woman1.png');
	}

    function checkPlatform(){
        if(/android/i.test(navigator.userAgent)){
            $(".footer").attr("href","http://ws.hellogood.com.cn/app/Appointment.apk");//这是Android平台下浏览器
        }
        if(/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)){
            $(".footer").attr("href","https://itunes.apple.com/cn/app/yi-yue-yue-hui/id1003136897?mt=8");//这是iOS平台下浏览器
        }
    }
    
    $(document).ready(function(){
        checkPlatform();
    });
    
	//判断滑动到的位置
	window.onscroll=function(){
		var height= window.scrollY;
			if(height >60){
				$("#download-app").css("display","block");
			}else{
				$("#download-app").css("display","none");
			}
	};
	function changeSex(type){
		 if(type == 'male'){
	            $('#sex').val('男');
	            $('#select_male').attr('src','images/checked.png');
	            $('#select_female').attr('src','images/uncheck.png');
	      }else{
	            $('#sex').val('女');
	            $('#select_male').attr('src','images/uncheck.png');
	            $('#select_female').attr('src', 'images/checked.png');
	     }
	}
	
	$(function(){
		
		$("input").focusin(function(){
			$(this).css("border","2px solid #fe595f;");
		});
		
		$("input").focusout(function(){
			$(this).css("border","2px solid #ccc;");
		});
		//时间
		var wait=60;
		function time(o) {
			if (wait == 0) {
				$(o).text("获取验证码");
				$(o).css("color","#FCFCFC");
				$(o).click(function(){time(this);});
				wait = 60;
			} else {
				$(o).unbind("click");
				$(o).css("color","#FCFCFC")
				$(o).text("重新获取" + wait + "s");
				wait--;
				setTimeout(function() {time(o)},1000);
			}
		}
		/**
		 * 验证
		 * @param type
		 * @returns {boolean}
		 */
		var validParam = function (type) {
		    if ($('#mobile').val() == '') {
		        alert("请输入手机号码");
		        $('#mobile').focus();
		        return false;
		    }
	
		    if(!/^(1\d{10})$/.test($('#mobile').val())){
		        alert("请输入正确手机号码");
		        $('#mobile').focus();
		        return false;
		    }
	
		    //注册
		    if (type == 'register') {
		        if ($('#password').val() == '') {
		            alert('请填写密码');
		            $('#password').focus();
		            return false;
		        }
		        if ($('#smsCode').val() == '') {
		            alert('请填写验证码');
		            $('#smsCode').focus();
		            return false;
		        }
		    }
		    return true;
		}

		 /**
		 * 发送短信
		 * */
		$(".yzmhq").click(function () {
			if (!validParam('code')) {
				return;
			}
			time(this);
			var mobile = $.trim($('#mobile').val());
			var url = '/hellogood_api/sms/code.do?type=register&mobile=' + mobile;
			$.ajax({
				type: 'GET',
				url: url,
				success: function (result) {
					if (result.status == 'error' || result.status == 'failed') {
						alert(result.message);
						wait=0;
						return;
					}
					alert('短信发送成功');
				},
				error: function () {
					alert('服务器繁忙，请稍后再试');
					return;
				}
			});
		});

	    var register = function(){
	    	var sex=$("#sex").val();
	    	if(sex == ''){
	    		alert("请选择性别");return;
	    	}
	        if (!validParam('register')) {
	            return;
	        }
	        var url = '/hellogood_api/auth/register.do';
	        var param = {
	            'mobile': $('#mobile').val(), 'password': $('#password').val(),
	            'sex': sex,'smsCode': $('#smsCode').val(),
	            'clientType': 'invite'};
	        if($("#inviteUid").val() != '' && $("#inviteUid").val() != 'null'){
	        	param.inviteUid=$("#inviteUid").val() ;
	        }
	        if( $('#inviteType').val() != '' && $("#inviteUid").val() != 'null'){
	        	param.inviteType =$('#inviteType').val();
	        }
	        //防止重复提交
	        $('#register').unbind('click', register);
	        $.ajax({
	            type: 'POST',
	            dataType: 'json',
	            contentType: 'application/json',
	            url: url,
	            data: JSON.stringify(param),
	            success: function (result) {
	                if (result.status == 'error' || result.status == 'failed') {
	                    alert(result.message);
	                    return;
	                }
	               // alert('注册成功,请下载app登录');
	              //  window.location.href = '/hellogood_api/pages/invite/download.jsp';
	              $("#register-success").html("<img style='margin-top:20%;margin-bottom:48%;opacity:0.8;' src='images/register-success.jpg' width='100%'/>");
					$('#registerDiv').html('');
					// $("#registerDiv").html("<a href='http://a.app.qq.com/o/simple.jsp?pkgname=com.hellogood.appointment&g_f=991653'><img style='margin-left:10%;' src='images/app-download-btn.jpg' width='80%'/></a>")
	          	 },
	            error: function () {
	                alert('服务器繁忙，请稍后再试');
	                return;
	            },
	            complete : function(){
	                $('#register').bind('click', register);
	            }
	        });
	    }

	    /**
	     * 注册
	     */
	    $('#register').bind('click', register);	
	});

</script>
</html>

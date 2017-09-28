(function(){

    var now = { row:1, col:1 }, last = { row:0, col:0};
    const towards = { up:1, right:2, down:3, left:4};
    var isAnimating = false;

    var height = $(window).height()-($(window).height()*0.25);


    $(".tag_04_bg").css("height",height+"px")



    s=window.innerHeight/500;
    ss=250*(1-s);

    $('.wrap').css('-webkit-transform','scale('+s+','+s+') translate(0px,-'+ss+'px)');

    document.addEventListener('touchmove',function(event){
        event.preventDefault(); },false);

    $(document).swipeUp(function(){
        if (isAnimating) return;
        last.row = now.row;
        last.col = now.col;
        if (last.row != 4) { now.row = last.row+1; now.col = 1; pageMove(towards.up);}
    })

    $(document).swipeDown(function(){
        if (isAnimating) return;
        last.row = now.row;
        last.col = now.col;
        if (last.row!=1) { now.row = last.row-1; now.col = 1; pageMove(towards.down);}
    })



    function pageMove(tw){
        var lastPage = ".page-"+last.row+"-"+last.col,
            nowPage = ".page-"+now.row+"-"+now.col;

        switch(tw) {
            case towards.up:
                outClass = 'pt-page-moveToTop';
                inClass = 'pt-page-moveFromBottom';
                break;
            case towards.right:
                outClass = 'pt-page-moveToRight';
                inClass = 'pt-page-moveFromLeft';
                break;
            case towards.down:
                outClass = 'pt-page-moveToBottom';
                inClass = 'pt-page-moveFromTop';
                break;
            case towards.left:
                outClass = 'pt-page-moveToLeft';
                inClass = 'pt-page-moveFromRight';
                break;
        }
        isAnimating = true;
        $(nowPage).removeClass("hide");

        $(lastPage).addClass(outClass);
        $(nowPage).addClass(inClass);

        if(nowPage == '.page-1-1'){
            $(nowPage).find("img").removeClass("hide");
        }

        setTimeout(function(){
            $(lastPage).removeClass('page-current');
            $(lastPage).removeClass(outClass);
            $(lastPage).addClass("hide");
            $(lastPage).find("img").addClass("hide");

            $(nowPage).addClass('page-current');
            $(nowPage).removeClass(inClass);
            $(nowPage).find("img").removeClass("hide");

            isAnimating = false;
        },600);
    }

})();

var wait=60;
function time(o) {
    if (wait == 0) {
        $(o).text("获取验证码");
        $(o).css("color","#e7e7e7");
        $(o).click(function(){
            time(this);
        });
        wait = 60;
    } else {
        $(o).unbind("click");
        $(o).css("color","#ededed")
        $(o).text("重新获取" + wait + "s");
        wait--;
        setTimeout(function() {
                time(o)
            },
            1000)
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
        if ($('#sex').val() == '') {
            alert('请填写性别');
            $('#sex').focus();
            return false;
        }
        if ($('#smsCode').val() == '') {
            alert('请填写验证码');
            $('#smsCode').focus();
            return false;
        }
        if($("#inviteUid").val() == ''){
            alert('邀请人不能为空');
            return false;
        }
        if($('#inviteType').val() == ''){
            alert('分享类型不能为空');
            return false;
        }
    }
    return true;
}

$(function () {



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
        if (!validParam('register')) {
            return;
        }
        var url = '/hellogood_api/auth/register.do';
        var param = {
            'mobile': $('#mobile').val(), 'password': $('#password').val(),
            'sex': $('#sex').val(),'smsCode': $('#smsCode').val(),
            'clientType': 'invite', 'inviteUid':$("#inviteUid").val(),
            'inviteType' : $('#inviteType').val()};
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
                alert('注册成功');
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
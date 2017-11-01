package com.hellogood.utils;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.NotyPopLoadTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 个推工具类
 * Created by KJ on 2017/10/26.
 */
public class AppPush {

    //定义常量, appId、appKey、masterSecret
    private static String appId = StaticFileUtil.getProperty("getui", "appId");
    private static String appKey = StaticFileUtil.getProperty("getui", "appKey");
    private static String masterSecret = StaticFileUtil.getProperty("getui", "masterSecret");
    private static String host = StaticFileUtil.getProperty("getui", "host");
    private static String CID = "6fc7b17b193bb61af6e615fd45cb0130";
    private static String CID1 = "6fc7b17b193bb61af6e615fd45cb0130";
    private static String CID2 = "6fc7b17b193bb61af6e615fd45cb0130";

    /**
     * 按照需求给指定用户列表发送通知
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin_pushList_needDetails", "true");
        // 配置返回每个别名及其对应cid的用户状态，可选
        // System.setProperty("gexin_pushList_needAliasDetails", "true");
        IGtPush push = new IGtPush(appKey, masterSecret);
        // 通知透传模板
        NotificationTemplate template = notificationTemplate(appId, appKey, "今天还有3条计划未完成哟", "您好XXX,","今天还有3条计划未完成哟","test.logo","asdsd");
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List targets = new ArrayList();
        Target target1 = new Target();
        Target target2 = new Target();
        target1.setAppId(appId);
        target1.setClientId(CID1);
        //     target1.setAlias(Alias1);
        target2.setAppId(appId);
        target2.setClientId(CID2);
        //     target2.setAlias(Alias2);
        targets.add(target1);
        targets.add(target2);
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
        System.out.println(ret.getResponse().toString());
    }

    /**
     * 测试单条推送
     */
    public void testSinglePush() {
        // https连接
        IGtPush push = new IGtPush(appKey, masterSecret);
        // 此处true为https域名，false为http，默认为false。Java语言推荐使用此方式
        // IGtPush push = new IGtPush(host, appkey, master);
        // host为域名，根据域名区分是http协议/https协议
        LinkTemplate template = linkTemplate(appId, appKey);
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        message.setPushNetWorkType(0); // 可选，判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(CID);
        // 用户别名推送，cid和用户别名只能2者选其一
        // String alias = "个";
        // target.setAlias(alias);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
    }

    /**
     * 点击通知打开应用模板
     *
     * @param appId
     * @param appkey
     * @return
     */
    public static NotificationTemplate notificationTemplate(String appId, String appkey, String content, String title, String text
            , String logo, String logoUrl) {
        NotificationTemplate template = new NotificationTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appkey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(1);
        template.setTransmissionContent(content);
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        template.setTitle(title);
        template.setText(text);
        // 配置通知栏图标
        template.setLogo(logo);
        // 配置通知栏网络图标
        template.setLogoUrl(logoUrl);
        return template;
    }

    /**
     * 点击通知打开网页链接
     *
     * @param appId
     * @param appKey
     * @return
     */
    public static LinkTemplate linkTemplate(String appId, String appKey) {
        LinkTemplate template = new LinkTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 设置通知栏标题与内容
        template.setTitle("请输入通知栏标题");
        template.setText("请输入通知栏内容");
        // 配置通知栏图标
        template.setLogo("icon.png");
        // 配置通知栏网络图标
        template.setLogoUrl("");
        // 设置打开的网址地址
        template.setUrl("http://www.baidu.com");
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }

    /**
     * 点击通知下载文件
     *
     * @param appId
     * @param appKey
     * @return
     */
    public static NotyPopLoadTemplate notyPopLoadTemplate(String appId, String appKey) {
        NotyPopLoadTemplate template = new NotyPopLoadTemplate();
        // 设置APPID与APPKEY
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 配置通知栏网络图标
        template.setLogoUrl("");
        // 设置弹框标题与内容
        template.setPopTitle("弹框标题");
        template.setPopContent("弹框内容");
        // 设置弹框显示的图片
        template.setPopImage("");
        template.setPopButton1("下载");
        template.setPopButton2("取消");
        // 设置下载标题
        template.setLoadTitle("下载标题");
        template.setLoadIcon("file://icon.png");
        //设置下载地址
        template.setLoadUrl("http://gdown.baidu.com/data/wisegame/80bab73f82cc29bf/shoujibaidu_16788496.apk");
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }

    /**
     * 透传消息模板
     *
     * @return
     */
    public static TransmissionTemplate transmissionTemplate() {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent("请输入需要透传的内容");
        // 设置定时展示时间
        // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
        return template;
    }
}

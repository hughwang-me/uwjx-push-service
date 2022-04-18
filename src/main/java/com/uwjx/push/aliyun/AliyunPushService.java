package com.uwjx.push.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class AliyunPushService implements InitializingBean {

    @Autowired
    AliyunAppConfig aliyunAppConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.warn("阿里云配置:{}", new Gson().toJson(aliyunAppConfig));
        run();
    }

    public void run() {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunAppConfig.getAccessKeyId(), aliyunAppConfig.getAccessSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        PushRequest request = new PushRequest();
//        request.setRegionId("cn-hangzhou");
        request.setAppKey(aliyunAppConfig.getAppKey());
        request.setPushType("NOTICE");
        request.setDeviceType("ALL");
        request.setTarget("DEVICE");
        request.setTargetValue("f53f1131aec84b85a9a21eb5ef7cad7a");

        Random random = new Random();
        // 推送配置。
        request.setTitle("ALi Push Title : " + random.nextInt(10086)); // 消息的标题。
        request.setBody("Ali Push Body : " + random.nextInt(10086));  // 消息的内容。
        // 推送配置：Android。
        request.setAndroidNotifyType("BOTH");//通知的提醒方式。VIBRATE：震动、SOUND：声音、BOTH：声音和震动、NONE：静音。
        request.setAndroidOpenType("APPLICATION"); //点击通知后动作。APPLICATION：打开应用、ACTIVITY：打开AndroidActivity、URL：打开URL、NONE：无跳转。
        // 指定notificaitonchannel id。
        request.setAndroidNotificationChannel("uwjx-push-channel");

        request.setPushType("NOTICE");
// 额外参数
        request.setAndroidExtParameters("{\"k1\":\"android\",\"k2\":\"v2\"}");
// 设置辅助弹窗打开Activity，填写Activity类名，需包名+类名
        request.setAndroidPopupActivity("com.uwjx.aliyun.push.PopupPushActivity.java");
// 设置辅助弹窗通知标题
        request.setAndroidPopupTitle("辅助弹窗标题" + random.nextInt(9527));
// 设置辅助弹窗通知内容
        request.setAndroidPopupBody("辅助弹窗内容" + random.nextInt(9527));
// 72小时后消息失效, 不会再发送
        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 72 * 3600 * 1000));
        request.setExpireTime(expireTime);
// 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
        request.setStoreOffline(true);
//推送消息类型时，设置true，设备离线时会自动把消息转成辅助通道的通知
        request.setAndroidRemind(true);

        try {

            log.warn("推送对象 : {}", new Gson().toJson(request));

            PushResponse response = client.getAcsResponse(request);
            log.warn("推送结果 : {}", new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }


}

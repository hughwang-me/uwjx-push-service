package com.uwjx.push.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        request.setTargetValue("8d91cb85091e4915b0f3831618fb907e");

        Random random = new Random();
        // 推送配置。
        request.setTitle("ALi Push Title : " + random.nextInt(10086)); // 消息的标题。
        request.setBody("Ali Push Body : " + random.nextInt(10086));  // 消息的内容。
        // 推送配置：Android。
        request.setAndroidNotifyType("BOTH");//通知的提醒方式。VIBRATE：震动、SOUND：声音、BOTH：声音和震动、NONE：静音。
        request.setAndroidOpenType("APPLICATION"); //点击通知后动作。APPLICATION：打开应用、ACTIVITY：打开AndroidActivity、URL：打开URL、NONE：无跳转。
        // 指定notificaitonchannel id。
        request.setAndroidNotificationChannel("uwjx-push-channel");

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

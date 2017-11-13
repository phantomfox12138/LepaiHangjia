package com.junjingit.lphj;

import android.app.Application;

import com.vondear.rxtools.RxTool;
import com.yanzhenjie.nohttp.NoHttp;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by niufan on 17/11/3.
 */

public class LPApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        
        RxTool.init(this);
        NoHttp.initialize(this);
        
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(
                this);
        
        JPushInterface.setPushNotificationBuilder(1, builder);

//        UMShareAPI.get(this);
//
//        PlatformConfig.setWeixin("wx94901c6b9d3d5bf9",
//                "236858250ac25ab71205ab34de663911");
//        PlatformConfig.setQQZone("101422269", "b112d3af703fa52787d28911318b0234");
//
//        PlatformConfig.setSinaWeibo("2923309086",
//                "5d8d1faba72f04daacb5ce8645927e21",
//                "http://www.qxswt.com/");
//
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59b10f06");
//
//        UMShareConfig umconfig = new UMShareConfig();
//        umconfig.isNeedAuthOnGetUserInfo(true);
//        umconfig.isOpenShareEditActivity(true);
//        umconfig.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
//        umconfig.setFacebookAuthType(UMShareConfig.AUTH_TYPE_SSO);
//        umconfig.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);
//
//        Config.DEBUG = false;
    }
}

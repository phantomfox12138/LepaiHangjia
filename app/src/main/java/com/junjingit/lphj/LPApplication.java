package com.junjingit.lphj;

import android.app.Application;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
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
        
        UMShareAPI.get(this);
        
        PlatformConfig.setWeixin("wx86aadf1da6986c24",
                "983e8796ef5881e1cdbe096f2eff4ca6");
        PlatformConfig.setQQZone("1106451895", "OP15fjHdQ6cfU3zf");
        
        PlatformConfig.setSinaWeibo("2231573333",
                "37ee520e3c7495f9a7085e06f5481db1",
                "");
        
        UMShareConfig umconfig = new UMShareConfig();
        umconfig.isNeedAuthOnGetUserInfo(true);
        umconfig.isOpenShareEditActivity(true);
        umconfig.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        umconfig.setFacebookAuthType(UMShareConfig.AUTH_TYPE_SSO);
        umconfig.setShareToLinkedInFriendScope(UMShareConfig.LINKED_IN_FRIEND_SCOPE_ANYONE);
        
        Config.DEBUG = false;
    }
}

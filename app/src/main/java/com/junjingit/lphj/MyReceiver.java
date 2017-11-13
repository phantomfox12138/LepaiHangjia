package com.junjingit.lphj;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by niufan on 17/11/7.
 */

public class MyReceiver extends BroadcastReceiver
{
    private static final String TAG = "MyReceiver";
    
    public static final String EXTRA_DATA = "param_url";
    
    public static final String FROM_RECEIVER = "from_receiver";
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //接收Registration Id
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
        {
            Log.d(TAG, "接收Registration Id");
        }
        //接收到推送下来的自定义消息
        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
        {
            Log.d(TAG, "接收到推送下来的自定义消息");
        }
        //接收到推送下来的通知
        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
        {
            Log.d(TAG, "接收到推送下来的通知");
        }
        //用户点击打开了通知
        else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
        {
            Log.d(TAG, "用户点击打开了通知");
            
            String extraStr = intent.getStringExtra(JPushInterface.EXTRA_EXTRA);
            Intent toWeb = null;
            
            String url = "";
            try
            {
                JSONObject jsonObject = new JSONObject(extraStr);
                
                if (jsonObject.has("URL"))
                {
                    url = jsonObject.getString("URL");
                }
                
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            
            Log.d(TAG, "extra = " + url);
            
            //            if (isRunningForeground(context))
            //            {
            toWeb = new Intent(context, WebActivity.class);
            toWeb.putExtra(EXTRA_DATA, url);
            toWeb.putExtra(FROM_RECEIVER, "receiver");
            //            }
            //            else
            //            {
            //                toWeb = new Intent(context, WelcomeActivity.class);
            //                toWeb.putExtra(EXTRA_DATA, url);
            //            }
            
            toWeb.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            
            context.startActivity(toWeb);
        }
        //用户收到到RICH PUSH CALLBACK
        else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
        {
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            Log.d(TAG, "用户收到到RICH PUSH CALLBACK");
        }
        //connected state change
        else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
        {
            Log.d(TAG, "connected state change");
        }
        
    }
    
    private boolean isRunningForeground(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(context.getPackageName()))
        {
            return true;
        }
        
        return false;
    }
}

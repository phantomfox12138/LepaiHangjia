package com.junjingit.lphj.alipay;

import java.util.Map;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

/**
 * Created by niufan on 17/5/5.
 */

public class Alipay
{
    private Activity activity;
    
    private Handler mHandler;
    
    private static final int SDK_PAY_FLAG = 1;
    
    public Alipay(Activity activity, Handler handler)
    {
        this.activity = activity;
        this.mHandler = handler;
    }
    
    public Alipay(Activity activity)
    {
        this.activity = activity;
    }
    
    public void payV2(final String orderInfo)
    {
        Runnable payRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    
}
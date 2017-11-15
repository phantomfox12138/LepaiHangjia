package com.junjingit.lphj;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.junjingit.lphj.alipay.Alipay;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtools.RxSPTool;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by niufan on 17/11/3.
 */

public class JSInterface
{
    private static final String TAG = "JSInterface";
    
    private RequestQueue mQueue = NoHttp.newRequestQueue();
    
    private Context mContext;
    
    private boolean mPushAliasFlag = false;
    
    private Handler mHandler;
    
    private EasyPopup mPoupup;
    
    private WebView mWebView;
    
    private UMAuthListener umAuthListener;
    
    public void setUmAuthListener(UMAuthListener umAuthListener)
    {
        this.umAuthListener = umAuthListener;
    }
    
    public void setmPoupup(EasyPopup mPoupup)
    {
        this.mPoupup = mPoupup;
    }
    
    public void setmWebView(WebView mWebView)
    {
        this.mWebView = mWebView;
    }
    
    private TagAliasCallback mAliasCallback = new TagAliasCallback()
    {
        @Override
        public void gotResult(int code, String alias, Set<String> tags)
        {
            switch (code)
            {
                case 0:
                    
                    mPushAliasFlag = true;
                    
                    break;
            }
        }
    };
    
    public JSInterface(Context context)
    {
        this.mContext = context;
    }
    
    public void setmHandler(Handler mHandler)
    {
        this.mHandler = mHandler;
    }
    
    @JavascriptInterface
    public void saveToken(String key, String id)
    {
        RxSPTool.putString(mContext, key, id);
    }
    
    @JavascriptInterface
    public void setAlias(String alias)
    {
        if (!mPushAliasFlag)
        {
            JPushInterface.setAliasAndTags(mContext.getApplicationContext(),
                    alias,
                    null,
                    mAliasCallback);
        }
    }
    
    @JavascriptInterface
    public void clearAlias()
    {
        JPushInterface.setAliasAndTags(mContext.getApplicationContext(),
                "",
                null,
                mAliasCallback);
    }
    
    //    @JavascriptInterface
    //    public void openNewWindow(String url)
    //    {
    //        Intent intent = new Intent("com.junjing.water.main");
    //        intent.putExtra(WEB_URL, url);
    //        intent.putExtra(MyReceiver.EXTRA_DATA, mData);
    //
    //        mContext.startActivity(intent);
    //        finish();
    //
    //    }
    
    @JavascriptInterface
    public String getToken(String key)
    {
        return RxSPTool.getString(mContext, key);
    }
    
    @JavascriptInterface
    public void clearToken(String key)
    {
        RxSPTool.clearPreference(mContext, key, "userId");
    }
    
    @JavascriptInterface
    public void alipayout(String orderInfo, String userId)
    {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest("http://lepai.chuyuxuan.com/wap/doAppPay.html",
                RequestMethod.POST);
        
        request.add("payment_id", orderInfo);
        request.add("pay_type", "alipayApp");
        request.add("user_id", userId);
        request.add("device", "android");
        
        mQueue.add(0, request, new OnResponseListener<JSONObject>()
        {
            @Override
            public void onStart(int what)
            {
                
            }
            
            @Override
            public void onSucceed(int what, Response<JSONObject> response)
            {
                
                Log.d(TAG, "resp = " + response.get().toString());
                
                //                AliPayTools.aliPay(mContext, "2015123101057074",//支付宝分配的APP_ID
                //                        true,//是否是 RSA2 加密
                //                        RSA_PRIVATE,// RSA 或 RSA2 字符串
                //                        new AliPayModel(orderId,//订单ID (唯一)
                //                                money,//价格
                //                                name,//商品名称
                //                                detail)//商品描述详情 (用于显示在 支付宝 的交易记录里)
                //                );
                
                JSONObject jsonObject = response.get();
                if (jsonObject.has("data"))
                {
                    try
                    {
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data.has("orderString"))
                        {
                            String orderString = data.getString("orderString");
                            Alipay alipay = new Alipay((Activity) mContext);
                            alipay.payV2(orderString);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                
            }
            
            @Override
            public void onFailed(int what, Response<JSONObject> response)
            {
                Log.d(TAG, "onFailed");
            }
            
            @Override
            public void onFinish(int what)
            {
                
            }
        });
    }
    
    @JavascriptInterface
    public void shareTo(String type, String title, String desc, String url)
    {
        mPoupup.showAtAnchorView(mWebView,
                VerticalGravity.ALIGN_BOTTOM,
                HorizontalGravity.CENTER);
        
        Message msg = new Message();
        
        switch (type)
        {
            case "image":
                
                msg.obj = desc + "," + url;
                msg.what = 0x123;
                
                break;
            
            case "text":
                
                msg.obj = desc;
                msg.what = 0x456;
                break;
            
            case "link":
                
                msg.obj = title + "," + desc + "," + url;
                msg.what = 0x789;
                break;
        }
        
        mHandler.sendMessage(msg);
    }
    
    @JavascriptInterface
    public void wechatLogin()
    {
        UMShareAPI.get(mContext).getPlatformInfo((Activity) mContext,
                SHARE_MEDIA.WEIXIN,
                umAuthListener);
    }
}

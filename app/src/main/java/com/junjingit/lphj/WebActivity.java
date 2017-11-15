package com.junjingit.lphj;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zyyoona7.lib.EasyPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebActivity extends AppCompatActivity implements UMAuthListener
{
    public static final String PARAM_URL = "param_url";
    
    private WebView mWebView;
    
    private EasyPopup mSharePopup;
    
    private RequestQueue mQueue = NoHttp.newRequestQueue();
    
    private UMShareListener umShareListener = new UMShareListener()
    {
        @Override
        public void onStart(SHARE_MEDIA share_media)
        {
            
        }
        
        @Override
        public void onResult(SHARE_MEDIA share_media)
        {
            
        }
        
        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable)
        {
            
        }
        
        @Override
        public void onCancel(SHARE_MEDIA share_media)
        {
            
        }
    };
    
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            
            switch (msg.what)
            {
            //图片
                case 0x123:
                    
                    String content = (String) msg.obj;
                    String[] strs = content.split(",");
                    
                    break;
                //文本
                case 0x456:
                    break;
                
                //链接
                case 0x789:
                    break;
            }
        }
    };
    
    //    private UMShareListener mShareListener = new UMShareListener()
    //    {
    //        @Override
    //        public void onStart(SHARE_MEDIA share_media)
    //        {
    //
    //        }
    //
    //        @Override
    //        public void onResult(SHARE_MEDIA share_media)
    //        {
    //            ToastUtils.showLong("分享成功");
    //        }
    //
    //        @Override
    //        public void onError(SHARE_MEDIA share_media, Throwable throwable)
    //        {
    //            Log.d(TAG, "share = " + throwable.toString());
    //
    //            ToastUtils.showLong("throwable.toString()");
    //
    //            ToastUtils.showLong("分享失败");
    //        }
    //
    //        @Override
    //        public void onCancel(SHARE_MEDIA share_media)
    //        {
    //
    //        }
    //    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        
        String url = getIntent().getStringExtra(PARAM_URL);
        
        initView(url);
    }
    
    private void initPopup()
    {
        mSharePopup = new EasyPopup(this);
        mSharePopup.setDimValue(0.5f);
        mSharePopup.setDimColor(Color.BLACK);
        mSharePopup.setDimView(mWebView);
        mSharePopup.setFocusAndOutsideEnable(true);
        mSharePopup.setContentView(R.layout.popup_content_layout);
        
        ShareGridAdapter adapter = new ShareGridAdapter(this);
        adapter.setList(initShareList());
        
        GridView shareGrid = mSharePopup.getView(R.id.share_grid);
        shareGrid.setAdapter(adapter);
        
        shareGrid.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                    int position, long l)
            {
                switch (position)
                {
                    case 0:
                        break;
                    
                    case 1:
                        break;
                    
                    case 2:
                        break;
                    
                    case 3:
                        break;
                    
                    case 4:
                        break;
                    
                    case 5:
                        break;
                }
                
                mSharePopup.dismiss();
            }
        });
        
        mSharePopup.createPopup();
    }
    
    private void initView(String url)
    {
        mWebView = findViewById(R.id.webview);
        
        initPopup();
        
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
        
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                //                if (newProgress == 100)
                //                {
                //                    mProgressBar.setVisibility(GONE);
                //                }
                //                else
                //                {
                //                    if (mProgressBar.getVisibility() == GONE)
                //                        mProgressBar.setVisibility(VISIBLE);
                //                    mProgressBar.setProgress(newProgress);
                //                }
                super.onProgressChanged(view, newProgress);
            }
        });
        
        WebSettings webSettings = mWebView.getSettings();
        
        JSInterface jsInterface = new JSInterface(this);
        jsInterface.setmWebView(mWebView);
        jsInterface.setUmAuthListener(this);
        jsInterface.setmHandler(mHandler);
        jsInterface.setmPoupup(mSharePopup);
        
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(jsInterface, "JSInterface");
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        
        String from = getIntent().getStringExtra(MyReceiver.FROM_RECEIVER);
        
        if (TextUtils.isEmpty(from))
        {
            mWebView.loadUrl(TextUtils.isEmpty(url) ? "http://lepai.chuyuxuan.com/wap?device=android"
                    : url + "&device=android");
        }
        else
        {
            mWebView.loadUrl(url);
        }
        
    }
    
    private List<ShareModel> initShareList()
    {
        List<ShareModel> shareList = new ArrayList<>();
        
        ShareModel wechat = new ShareModel();
        wechat.shareName = "微信好友";
        wechat.shareIconId = R.mipmap.wechat;
        shareList.add(wechat);
        
        ShareModel pyq = new ShareModel();
        pyq.shareName = "朋友圈";
        pyq.shareIconId = R.mipmap.pyq;
        shareList.add(pyq);
        
        ShareModel qq = new ShareModel();
        qq.shareName = "QQ好友";
        qq.shareIconId = R.mipmap.qq;
        shareList.add(qq);
        
        ShareModel qzone = new ShareModel();
        qzone.shareName = "QQ空间";
        qzone.shareIconId = R.mipmap.qzone;
        shareList.add(qzone);
        
        ShareModel weibo = new ShareModel();
        weibo.shareName = "新浪微博";
        weibo.shareIconId = R.mipmap.wb;
        shareList.add(weibo);
        
        ShareModel zfb = new ShareModel();
        zfb.shareName = "支付宝";
        zfb.shareIconId = R.drawable.umeng_socialize_alipay;
        shareList.add(zfb);
        
        return shareList;
    }
    
    private void shareTo(SHARE_MEDIA platform, String title)
    {
        //TODO：构建分享对象（暂无应用logo）
        //        UMImage thumb = new UMImage(this, R.mipmap.logo_fx);
        //        UMWeb web = new UMWeb("url");
        //        web.setThumb(thumb);
        //        web.setDescription("");
        //        web.setTitle(title);
        //        new ShareAction(this).withMedia(web)
        //                .setPlatform(platform)
        //                .setCallback(umShareListener)
        //                .share();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    public void onStart(SHARE_MEDIA share_media)
    {
    }
    
    @Override
    public void onComplete(SHARE_MEDIA share_media, int i,
            Map<String, String> map)
    {
        String temp = "";
        for (String key : map.keySet())
        {
            temp = temp + key + " : " + map.get(key) + "\n";
            
            //            Log.d(TAG, temp);
        }
        
        final String tencentId = map.get("unionid");
        
        String nickname = map.get("screen_name");
        String client = "wx";
        String userLogo = map.get("profile_image_url");
        
        //TODO: 登录完成后，将unionid传给服务器
        Request<JSONObject> request = NoHttp.createJsonObjectRequest("",
                RequestMethod.POST);
        //add param
        request.add("", "");
        
        mQueue.add(0, request, new OnResponseListener<JSONObject>()
        {
            @Override
            public void onStart(int what)
            {
                
            }
            
            @Override
            public void onSucceed(int what, Response<JSONObject> response)
            {
                
            }
            
            @Override
            public void onFailed(int what, Response<JSONObject> response)
            {
                
            }
            
            @Override
            public void onFinish(int what)
            {
                
            }
        });
        
        //        UserLogic.getInstance(LoginActivity.this)
        //                .requestQQUnionId(map.get("access_token"),
        //                        new OnResponseListener<String>()
        //                        {
        //                            @Override
        //                            public void onStart(int what)
        //                            {
        //
        //                            }
        //
        //                            @Override
        //                            public void onSucceed(int what,
        //                                    Response<String> response)
        //                            {
        //                                String jsonObject = response.get();
        //
        //                                Log.d(TAG, "tencentId = " + tencentId);
        //                            }
        //
        //                            @Override
        //                            public void onFailed(int what,
        //                                    Response<String> response)
        //                            {
        //                                response.getException();
        //                            }
        //
        //                            @Override
        //                            public void onFinish(int what)
        //                            {
        //
        //                            }
        //                        });
        
        //        UserLogic.getInstance(LoginActivity.this)
        //                .requestThirdPartLogin(tencentId,
        //                        client,
        //                        nickname,
        //                        userLogo,
        //                        new OnResponseListener<JSONObject>()
        //                        {
        //                            @Override
        //                            public void onStart(int what)
        //                            {
        //
        //                            }
        //
        //                            @Override
        //                            public void onSucceed(int what,
        //                                    Response<JSONObject> response)
        //                            {
        //                                JSONObject jsonObject = response.get();
        //
        //                                try
        //                                {
        //                                    if (JsonUtil.parseThirdPart(jsonObject, mSp))
        //                                    {
        //                                        ToastUtils.showLong("登录成功");
        //                                        finish();
        //                                    }
        //                                }
        //                                catch (JSONException e)
        //                                {
        //                                    e.printStackTrace();
        //
        //                                    ToastUtils.showLong("登录失败");
        //                                }
        //                            }
        //
        //                            @Override
        //                            public void onFailed(int what,
        //                                    Response<JSONObject> response)
        //                            {
        //                                ToastUtils.showLong("登录失败");
        //                            }
        //
        //                            @Override
        //                            public void onFinish(int what)
        //                            {
        //
        //                            }
        //                        });
    }
    
    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable)
    {
        
    }
    
    @Override
    public void onCancel(SHARE_MEDIA share_media, int i)
    {
        
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }
    
}

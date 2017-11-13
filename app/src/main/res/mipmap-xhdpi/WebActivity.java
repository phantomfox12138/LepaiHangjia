package com.ppzx.qxswt.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.ppzx.qxswt.R;
import com.ppzx.qxswt.common.FusionAction;
import com.ppzx.qxswt.common.HttpHelper;
import com.ppzx.qxswt.http.logic.HomeLogic;
import com.ppzx.qxswt.model.ShareModel;
import com.ppzx.qxswt.util.JsonUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xander.panel.XanderPanel;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

public class WebActivity extends AppCompatActivity
{
    private static final String TAG = "WebActivity";
    
    private NumberProgressBar mProgressBar;
    
    private WebView mWebView;
    
    private TextView mTitle;
    
    private View mBack;
    
    private View mShareBtn;
    
    private XanderPanel.Builder mBuilder;
    
    private XanderPanel mXanderPanel;
    
    private String userId;
    
    private String title;
    
    private String type;
    
    private String shareTitle;
    
    private String shareIntro;
    
    private boolean isFollow;
    
    private String url_id;
    
    private String mDescription;
    
    private View mCollectionView;
    
    private ImageView mCollection;
    
    private ProgressDialog mProgressDialog;
    
    private String companyId;
    
    private SharedPreferences mConfigPerference;
    
    private String hotWordType;
    
    private String mCompanyId;
    
    private String personalUrl;
    
    private UMShareListener mShareListener = new UMShareListener()
    {
        @Override
        public void onStart(SHARE_MEDIA share_media)
        {
            
        }
        
        @Override
        public void onResult(SHARE_MEDIA share_media)
        {
            ToastUtils.showLong("分享成功");
        }
        
        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable)
        {
            Log.d(TAG, "share = " + throwable.toString());
            
            ToastUtils.showLong("throwable.toString()");
            
            ToastUtils.showLong("分享失败");
        }
        
        @Override
        public void onCancel(SHARE_MEDIA share_media)
        {
            
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        
        mConfigPerference = getSharedPreferences(FusionAction.SP_NAME,
                MODE_PRIVATE);
        
        Utils.init(this);
        
        initView();
    }
    
    private void initView()
    {
        String sharedable = getIntent().getStringExtra(FusionAction.WebExtra.SHAREDABLE);
        url_id = getIntent().getStringExtra(FusionAction.WebExtra.URL);
        isFollow = getIntent().getBooleanExtra(FusionAction.WebExtra.IS_FOLLOW,
                false);
        companyId = getIntent().getStringExtra(FusionAction.WebExtra.COMPANY_ID);
        shareTitle = getIntent().getStringExtra(FusionAction.WebExtra.SHARE_TITLE);
        shareIntro = getIntent().getStringExtra(FusionAction.WebExtra.SHARE_INTRO);
        hotWordType = getIntent().getStringExtra(FusionAction.WebExtra.HOT_WORD_TYPE);
        
        personalUrl = getIntent().getStringExtra(FusionAction.WebExtra.PERSONAL_URL);
        
        boolean focusable = getIntent().getBooleanExtra(FusionAction.WebExtra.FOCUSABLE,
                false);
        url_id = TextUtils.isEmpty(url_id) ? companyId : url_id;
        
        title = getIntent().getStringExtra(FusionAction.WebExtra.TITLE);
        type = getIntent().getStringExtra(FusionAction.WebExtra.TYPE);
        String iconUrl = getIntent().getStringExtra(FusionAction.WebExtra.ICON_URL);
        String description = getIntent().getStringExtra(FusionAction.WebExtra.DESCRIPTION);
        
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在加载中，请稍后");
        
        mProgressBar = (NumberProgressBar) findViewById(R.id.web_progressbar);
        mWebView = (WebView) findViewById(R.id.web_view);
        mTitle = (TextView) findViewById(R.id.title);
        mBack = findViewById(R.id.web_title_back);
        mShareBtn = findViewById(R.id.share_btn);
        mCollectionView = findViewById(R.id.collection_btn);
        mCollection = (ImageView) findViewById(R.id.collection_btn);
        
        //        mCollectionView.setVisibility(View.GONE);
        
        mCollectionView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                
                //                if (isFollow)
                //                {
                //                    return;
                //                }
                
                userId = mConfigPerference.getString(FusionAction.ConfigParam.USER_ID,
                        null);
                
                if (StringUtils.isEmpty(userId))
                {
                    startActivity(new Intent(FusionAction.LOGIN_ACTION));
                    return;
                }
                
                HomeLogic.getInstance(com.ppzx.qxswt.ui.WebActivity.this)
                        .requestAddOrDelFocus(userId,
                                StringUtils.isEmpty(companyId) ? url_id
                                        : companyId,
                                isFollow ? 0 : 1,
                                new OnResponseListener<JSONObject>()
                                {
                                    @Override
                                    public void onStart(int what)
                                    {
                                        mProgressDialog.show();
                                    }
                                    
                                    @Override
                                    public void onSucceed(int what,
                                            Response<JSONObject> response)
                                    {
                                        JSONObject jsonObject = response.get();
                                        
                                        int type = isFollow ? 0 : 1;
                                        try
                                        {
                                            if (JsonUtil.parseAddOrDeleteFocus(jsonObject))
                                            {
                                                mProgressDialog.dismiss();
                                                mCollection.setImageResource(type == 0 ? R.mipmap.gz
                                                        : R.mipmap.gz_sel);
                                                
                                                ToastUtils.showLong(type == 0 ? "取消关注成功"
                                                        : "关注成功");
                                                
                                                isFollow = !(type == 0);
                                            }
                                            else
                                            {
                                                ToastUtils.showLong(type == 0 ? "取消关注失败"
                                                        : "关注失败");
                                                mProgressDialog.dismiss();
                                                
                                            }
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                            mCollection.setImageResource(R.mipmap.gz);
                                            ToastUtils.showLong("操作失败");
                                            mProgressDialog.dismiss();
                                        }
                                    }
                                    
                                    @Override
                                    public void onFailed(int what,
                                            Response<JSONObject> response)
                                    {
                                        int type = (Integer) response.request()
                                                .getParamKeyValues()
                                                .getValue(HttpHelper.UserRequestParam.TYPE,
                                                        3);
                                        
                                        mProgressDialog.dismiss();
                                        mCollection.setImageResource(R.mipmap.gz);
                                        ToastUtils.showLong(type == 0 ? "取消关注失败"
                                                : "关注失败");
                                        
                                        mProgressDialog.dismiss();
                                    }
                                    
                                    @Override
                                    public void onFinish(int what)
                                    {
                                    }
                                });
                
            }
        });
        
        mTitle.setText(title);
        
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JSInterface(), "JSInterface");
        
        mShareBtn.setVisibility(Boolean.valueOf(sharedable) ? View.VISIBLE
                : View.INVISIBLE);
        mCollectionView.setVisibility(focusable ? VISIBLE : GONE);
        mBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        
        mShareBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mXanderPanel.show();
            }
        });
        
        mWebView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                    WebResourceRequest request)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(view.getUrl());
                return true;
            }
        });
        
        String url = "";
        
        SharedPreferences sp = getSharedPreferences(FusionAction.SP_NAME,
                MODE_PRIVATE);
        userId = sp.getString(FusionAction.ConfigParam.USER_ID, "");
        
        if (!TextUtils.isEmpty(type))
        {
            
            if (type.equals("about"))
            {
                url = HttpHelper.WEB_ABOUT_URL;
                mCollectionView.setVisibility(GONE);
            }
            if (type.equals("sp"))
            {
                url = HttpHelper.WEB_PERSON_URL + companyId;
            }
            if (!TextUtils.isEmpty(userId))
            {
                if (type.equals("company"))
                {
                    url = HttpHelper.WEB_COMPANY_DETAIL_URL + url_id + "?"
                            + HttpHelper.UserRequestParam.USER_ID + "="
                            + userId;
                    //                    mCollectionView.setVisibility(VISIBLE);
                }
                if (type.equals("report"))
                {
                    url = HttpHelper.WEB_REPORT_DETAIL_URL + url_id + "?"
                            + HttpHelper.UserRequestParam.USER_ID + "="
                            + userId;
                    mCollectionView.setVisibility(GONE);
                }
                if (type.equals("search"))
                {
                    url = url_id + "?" + HttpHelper.UserRequestParam.USER_ID
                            + "=" + userId;
                    //                    mCollectionView.setVisibility(VISIBLE);
                }
                
                if (StringUtils.isNotEmpty(userId))
                {
                    HomeLogic.getInstance(this)
                            .requestCompanyFollow(userId,
                                    StringUtils.isEmpty(companyId) ? url_id
                                            : companyId,
                                    new OnResponseListener<JSONObject>()
                                    {
                                        @Override
                                        public void onStart(int what)
                                        {
                                            
                                        }
                                        
                                        @Override
                                        public void onSucceed(int what,
                                                Response<JSONObject> response)
                                        {
                                            JSONObject jsonObject = response.get();
                                            
                                            try
                                            {
                                                isFollow = JsonUtil.parseCompanyIsFollow(jsonObject);
                                                mCollection.setImageResource(isFollow ? R.mipmap.gz_sel
                                                        : R.mipmap.gz);
                                                
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        
                                        @Override
                                        public void onFailed(int what,
                                                Response<JSONObject> response)
                                        {
                                            
                                        }
                                        
                                        @Override
                                        public void onFinish(int what)
                                        {
                                            
                                        }
                                    });
                }
                
            }
            else
            {
                if (type.equals("company"))
                {
                    url = HttpHelper.WEB_COMPANY_DETAIL_URL + url_id;
                }
                if (type.equals("report"))
                {
                    url = HttpHelper.WEB_REPORT_DETAIL_URL + url_id;
                    mCollectionView.setVisibility(GONE);
                }
                if (type.equals("search"))
                {
                    url = url_id;
                }
            }
        }
        
        mWebView.loadUrl(url);
        
        mWebView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                if (newProgress == 100)
                {
                    mProgressBar.setVisibility(GONE);
                }
                else
                {
                    if (mProgressBar.getVisibility() == GONE)
                        mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        
        mBuilder = new XanderPanel.Builder(this);
        mBuilder.setCanceledOnTouchOutside(true);
        mBuilder.setGravity(Gravity.BOTTOM);
        
        View shareSheet = initShareSheet(type,
                url_id,
                title,
                iconUrl,
                description);
        
        mBuilder.setView(shareSheet);
        mXanderPanel = mBuilder.create();
    }
    
    private View initShareSheet(final String type, final String url_id,
            final String companyName, final String iconUrl,
            final String description)
    {
        View shareSheet = LayoutInflater.from(this)
                .inflate(R.layout.share_layout, null);
        GridView sheet = shareSheet.findViewById(R.id.share_grid);
        ShareGridAdapter adapter = new ShareGridAdapter(this);
        adapter.setList(initShareList());
        sheet.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                    int position, long l)
            {
                
                switch (position)
                {
                    case 0:
                        ShareWeb(type,
                                url_id,
                                companyName,
                                SHARE_MEDIA.WEIXIN,
                                description);
                        break;
                    
                    case 1:
                        ShareWeb(type,
                                url_id,
                                companyName,
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                description);
                        break;
                    
                    case 2:
                        ShareWeb(type,
                                url_id,
                                companyName,
                                SHARE_MEDIA.QQ,
                                description);
                        break;
                    
                    case 3:
                        ShareWeb(type,
                                url_id,
                                companyName,
                                SHARE_MEDIA.QZONE,
                                description);
                        break;
                    
                    case 4:
                        ShareWeb(type,
                                url_id,
                                companyName,
                                SHARE_MEDIA.SINA,
                                description);
                        break;
                    
                    case 5:
                        ShareWeb(type,
                                url_id,
                                companyName,
                                SHARE_MEDIA.SMS,
                                description);
                        break;
                }
                
            }
        });
        sheet.setAdapter(adapter);
        
        return shareSheet;
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
        
        ShareModel sms = new ShareModel();
        sms.shareName = "短信";
        sms.shareIconId = R.mipmap.dx;
        shareList.add(sms);
        
        return shareList;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    
    private void ShareWeb(String type, String url_id, String companyName,
            SHARE_MEDIA platform, String description)
    {
        if (type.equals("company"))
        {
            UMImage thumb = new UMImage(this, R.mipmap.logo_fx);
            UMWeb web = new UMWeb(HttpHelper.WEB_COMPANY_URL + url_id);
            web.setThumb(thumb);
            web.setDescription("点击查看" + companyName + "更多详细信息");
            web.setTitle(TextUtils.isEmpty(shareTitle) ? companyName
                    : shareIntro);
            new ShareAction(this).withMedia(web)
                    .setPlatform(platform)
                    .setCallback(mShareListener)
                    .share();
        }
        if (type.equals("search"))
        {
            UMImage thumb = new UMImage(this, R.mipmap.logo_fx);
            UMWeb web = new UMWeb(
                    TextUtils.isEmpty(personalUrl) ? (HttpHelper.WEB_COMPANY_URL + companyId)
                            : personalUrl);
            web.setThumb(thumb);
            web.setDescription("点击查看" + companyName + "更多详细信息");
            web.setTitle(TextUtils.isEmpty(shareTitle) ? companyName
                    : shareIntro);
            new ShareAction(this).withMedia(web)
                    .setPlatform(platform)
                    .setCallback(mShareListener)
                    .share();
        }
        
        if (type.equals("report"))
        {
            UMImage thumb = new UMImage(this, R.mipmap.logo_fx);
            UMWeb web = new UMWeb(HttpHelper.WEB_REPORT_URL + url_id);
            web.setThumb(thumb);
            web.setDescription(description);
            web.setTitle(companyName);
            new ShareAction(this).withMedia(web)
                    .setPlatform(platform)
                    .setCallback(mShareListener)
                    .share();
        }
        
        if (type.equals("sp"))
        {
            UMImage thumb = new UMImage(this, R.mipmap.android_logo);
            UMWeb web = new UMWeb(HttpHelper.WEB_PERSON_URL + companyId);
            web.setThumb(thumb);
            web.setDescription(title);
            web.setTitle(title);
            new ShareAction(this).withMedia(web)
                    .setPlatform(platform)
                    .setCallback(mShareListener)
                    .share();
        }
        
    }
    
    public class JSInterface
    {
        @JavascriptInterface
        public void toBusinessInfo(String url)
        {
            Intent toScrollWeb = new Intent(FusionAction.SCROLL_WEB_ACTION);
            toScrollWeb.putExtra(FusionAction.WebExtra.IS_FOLLOW, isFollow);
            toScrollWeb.putExtra(FusionAction.WebExtra.COMPANY_ID,
                    TextUtils.isEmpty(companyId) ? mCompanyId : companyId);
            toScrollWeb.putExtra(FusionAction.WebExtra.TITLE, title);
            toScrollWeb.putExtra(FusionAction.WebExtra.SHAREDABLE, "true");
            toScrollWeb.putExtra(FusionAction.WebExtra.URL, url);
            toScrollWeb.putExtra(FusionAction.WebExtra.TYPE, "company");
            toScrollWeb.putExtra(FusionAction.WebExtra.FOCUSABLE, false);
            startActivity(toScrollWeb);
        }
        
        @JavascriptInterface
        public void openBrowser(String url)
        {
            Intent toBrowser = new Intent();
            toBrowser.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            toBrowser.setData(content_url);
            startActivity(toBrowser);
        }
        
        @JavascriptInterface
        public void toCompanyNewsList(int id)
        {
            Intent toReportList = new Intent(FusionAction.REPORT_LIST_ACTION);
            toReportList.putExtra(FusionAction.WebExtra.COMPANY_ID, id);
            toReportList.putExtra(FusionAction.WebExtra.TITLE, title);
            toReportList.putExtra(FusionAction.WebExtra.TYPE, type);
            
            startActivity(toReportList);
        }
        
        @JavascriptInterface
        public void toCompanyInfo(String title, String companyId, String url)
        {
            mCompanyId = companyId;
            
            Intent toWeb = new Intent(FusionAction.WEB_ACTION);
            
            toWeb.putExtra(FusionAction.WebExtra.TITLE, title);
            toWeb.putExtra(FusionAction.WebExtra.COMPANY_ID, companyId);
            toWeb.putExtra(FusionAction.WebExtra.TYPE, "company");
            toWeb.putExtra(FusionAction.WebExtra.SHAREDABLE, "true");
            toWeb.putExtra(FusionAction.WebExtra.FOCUSABLE, true);
            
            startActivity(toWeb);
        }
        
        @JavascriptInterface
        public void toMediaInfo(String id, String title, String description)
        {
            Intent toWeb = new Intent(FusionAction.WEB_ACTION);
            
            toWeb.putExtra(FusionAction.WebExtra.TITLE, title);
            toWeb.putExtra(FusionAction.WebExtra.URL, id);
            toWeb.putExtra(FusionAction.WebExtra.DESCRIPTION, description);
            toWeb.putExtra(FusionAction.WebExtra.TYPE, "report");
            
            startActivity(toWeb);
        }
        
        @JavascriptInterface
        public void setReportDescription(String description)
        {
            mDescription = description;
        }
        
        //string '图片类型',int 发布图片的用户id,string '当前点击的图片url')
        @JavascriptInterface
        public void openPreview(int position, String imgUrls)
        {
            Intent toPerview = new Intent(FusionAction.IMAGE_PERVIEW_ACTION);
            
            toPerview.putExtra(FusionAction.WebExtra.IMAGE_URLS, imgUrls);
            toPerview.putExtra(FusionAction.WebExtra.IMAGE_POSITION, position);
            
            startActivity(toPerview);
            
        }
        
        @JavascriptInterface
        public void toPhoneCallTask(final String phone)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                
                Log.d(TAG, "state = " + Environment.getExternalStorageState());
                
                PermissionListener listener = new PermissionListener()
                {
                    @Override
                    public void onSucceed(int requestCode,
                            List<String> grantedPermissions)
                    {
                        // Successfully.
                        if (requestCode == 200)
                        {
                            Log.d(TAG, "permission allowed");
                            if (phone != null && phone.trim().length() > 0)
                            {
                                //这里"tel:"+电话号码 是固定格式，系统一看是以"tel:"开头的，就知道后面应该是电话号码。
                                Intent intent = new Intent(Intent.ACTION_CALL,
                                        Uri.parse("tel:" + phone.trim()));
                                startActivity(intent);//调用上面这个intent实现拨号
                            }
                            else
                            {
                                ToastUtils.showLong("电话号码不能为空");
                            }
                        }
                    }
                    
                    @Override
                    public void onFailed(int requestCode,
                            List<String> deniedPermissions)
                    {
                        // Failure.
                        if (requestCode == 200)
                        {
                            Log.d(TAG, "permission denied");
                        }
                    }
                };
                
                if (AndPermission.hasPermission(com.ppzx.qxswt.ui.WebActivity.this,
                        Manifest.permission.CALL_PHONE))
                {
                    if (phone != null && phone.trim().length() > 0)
                    {
                        //这里"tel:"+电话号码 是固定格式，系统一看是以"tel:"开头的，就知道后面应该是电话号码。
                        Intent intent = new Intent(Intent.ACTION_CALL,
                                Uri.parse("tel:" + phone.trim()));
                        startActivity(intent);//调用上面这个intent实现拨号
                    }
                    else
                    {
                        ToastUtils.showLong("电话号码不能为空");
                    }
                }
                else
                {
                    AndPermission.with(com.ppzx.qxswt.ui.WebActivity.this)
                            .requestCode(200)
                            .permission(Manifest.permission.CALL_PHONE)
                            .callback(listener)
                            .start();
                }
            }
        }
        
        @JavascriptInterface
        public void toSpCompanyInfo(String title, String id, String url)
        {
            mCompanyId = id;
            
            Intent toWeb = new Intent(FusionAction.WEB_ACTION);
            
            toWeb.putExtra(FusionAction.WebExtra.TITLE, title);
            toWeb.putExtra(FusionAction.WebExtra.COMPANY_ID, id);
            toWeb.putExtra(FusionAction.WebExtra.TYPE, "sp");
            toWeb.putExtra(FusionAction.WebExtra.SHAREDABLE, "true");
            toWeb.putExtra(FusionAction.WebExtra.FOCUSABLE, false);
            
            startActivity(toWeb);
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        mWebView.reload();
        
        HomeLogic.getInstance(this).requestCompanyFollow(userId,
                TextUtils.isEmpty(companyId) ? url_id : companyId,
                new OnResponseListener<JSONObject>()
                {
                    @Override
                    public void onStart(int what)
                    {
                        
                    }
                    
                    @Override
                    public void onSucceed(int what,
                            Response<JSONObject> response)
                    {
                        JSONObject jsonObject = response.get();
                        
                        try
                        {
                            isFollow = JsonUtil.parseCompanyIsFollow(jsonObject);
                            mCollection.setImageResource(isFollow ? R.mipmap.gz_sel
                                    : R.mipmap.gz);
                            
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
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
    }
    
    @Override
    public void onBackPressed()
    {
        if (mWebView.canGoBack())
        {
            mWebView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}

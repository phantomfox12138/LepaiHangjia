package com.junjingit.lphj;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity
{
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new Thread()
        {
            
            @Override
            public void run()
            {
                super.run();
                
                try
                {
                    sleep(2500);
                    
                    if (!AndPermission.hasPermission(WelcomeActivity.this,
                            Manifest.permission.READ_PHONE_STATE))
                    {
                        AndPermission.with(WelcomeActivity.this)
                                .permission(Manifest.permission.READ_PHONE_STATE)
                                .callback(new PermissionListener()
                                {
                                    @Override
                                    public void onSucceed(
                                            int requestCode,
                                            @NonNull List<String> grantPermissions)
                                    {
                                        
                                    }
                                    
                                    @Override
                                    public void onFailed(
                                            int requestCode,
                                            @NonNull List<String> deniedPermissions)
                                    {
                                        
                                    }
                                })
                                .start();
                    }
                    
                    startActivity(new Intent("com.junjing.lphj.WEB_ACTION"));
                    finish();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

package com.magispec.shield.wxapi;

import com.magispec.shield.activity.WXLoginActivity;
import com.magispec.shield.constantDefinitions.Constant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{  
    private IWXAPI api;  
    public static BaseResp resp = null; 
   
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        //获取IWXAPI实例
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);   
       
        api.handleIntent(getIntent(), this);
    }
    // 微信发送请求到第三方应用时，会回调到该方法  
    @Override  
    public void onReq(BaseReq req) {  
        finish();  
    }
    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法  
    @Override  
    public void onResp(BaseResp resp) {
    	System.out.println("我执行了返回");    	
        String result = "";  
        if (resp != null) {  
        WXLoginActivity.resp = resp;   
        }  
        switch(resp.errCode) {  
            case BaseResp.ErrCode.ERR_OK:  
                result ="登陆成功";  
               // Toast.makeText(this, result, Toast.LENGTH_LONG).show();  
                finish();  
                break;  
            case BaseResp.ErrCode.ERR_USER_CANCEL:  
                result = "发送取消";  
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();  
                finish();  
                break;  
            case BaseResp.ErrCode.ERR_AUTH_DENIED:  
                result = "发送被拒绝";  
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();  
                finish();  
                break;  
            default:  
                result = "发送返回";  
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();  
                finish();  
                break;  
        }  
    } 
    @Override  
    protected void onNewIntent(Intent intent) {  
        super.onNewIntent(intent);  
        setIntent(intent);  
        api.handleIntent(intent, this);  
        finish();  
    }
  
    
    
    
}  
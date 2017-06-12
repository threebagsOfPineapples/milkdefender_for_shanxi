package com.magispec.shield.service;

import java.io.InputStream;

import com.magispec.shield.application.CrashHandler;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Application;
import android.os.Vibrator;

import okio.Buffer;

public class BaseApplicaton extends Application {
    private static BaseApplicaton context;
    public Vibrator mVibrator;
    public CrashHandler handler;
    private String CER = "-----BEGIN CERTIFICATE-----\n" +
            "MIICkTCCAfoCCQCOVljopdR53DANBgkqhkiG9w0BAQUFADCBjDELMAkGA1UEBhMCODYxCzAJBgNV\n" +
            "BAgMAkJKMQswCQYDVQQHDAJCSjERMA8GA1UECgwITWFnaXNwZWMxDDAKBgNVBAsMA0RldjEWMBQG\n" +
            "A1UEAwwNMTIzLjU2LjIyOS41MDEqMCgGCSqGSIb3DQEJARYbbWluZ3hpbmcuemhhbmdAbWFnaXNw\n" +
            "ZWMuY29tMB4XDTE2MDMxMTA4MzYxN1oXDTE3MDMxMTA4MzYxN1owgYwxCzAJBgNVBAYTAjg2MQsw\n" +
            "CQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxETAPBgNVBAoMCE1hZ2lzcGVjMQwwCgYDVQQLDANEZXYx\n" +
            "FjAUBgNVBAMMDTEyMy41Ni4yMjkuNTAxKjAoBgkqhkiG9w0BCQEWG21pbmd4aW5nLnpoYW5nQG1h\n" +
            "Z2lzcGVjLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA4ZtLhVDDVqXqBijQgpvfupv0\n" +
            "yqy9Vu9QYR2bJ/PfBpfdlxeyUnt7dLEDT+YsSPwEBydWvvTjtDPvDq+lzci1HK9m0Q453Uo1w8SN\n" +
            "KX9bDyxhf2Hdkt3udvuvy8x7qVyBGlHs2aLs40CFaZW9cX1s9DodJtPJQy10vXkl8LoKqt8CAwEA\n" +
            "ATANBgkqhkiG9w0BAQUFAAOBgQCzYbmZQTZfTdoOMj9kQyxa+MDBC9tJWo2VLSwuyxucT7xBQ8CQ\n" +
            "iBswDX7zwi+eZ9rSS/I1bXHcuseDMrP5koUk2NiunxU8Izz6L1RCrvXlqxm0Md1M6leY8c70W0wf\n" +
            "wRY2AAmWAEBXRCCBXUjbBVOZpDXTu5odnOYbTMjI+UYeKg==\n" +
            "-----END CERTIFICATE-----";
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // handler.init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        //   CrashHandler crashHandler = CrashHandler.getInstance();
        //    crashHandler.init(getApplicationContext());
        OkHttpUtils.getInstance().setCertificates(new InputStream[]{
                new Buffer()
                        .writeUtf8(CER)
                        .inputStream()});
    }
    public synchronized static BaseApplicaton getInstance() {
        return context;
    }
    public static BaseApplicaton getAppContext() {
        return context;
    }
/*    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println("uncaughtException");
        System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
}
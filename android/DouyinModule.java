package com.touhaolicai;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bytedance.sdk.open.aweme.CommonConstants;
import com.bytedance.sdk.open.aweme.authorize.model.Authorization;
import com.bytedance.sdk.open.aweme.common.handler.IApiEventHandler;
import com.bytedance.sdk.open.aweme.common.model.BaseReq;
import com.bytedance.sdk.open.aweme.common.model.BaseResp;
import com.bytedance.sdk.open.douyin.DouYinOpenApiFactory;
import com.bytedance.sdk.open.douyin.DouYinOpenConfig;
import com.bytedance.sdk.open.douyin.api.DouYinOpenApi;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.theweflex.react.WeChatModule;

import java.util.ArrayList;


public class DouyinModule extends ReactContextBaseJavaModule implements IApiEventHandler {
    @NonNull
    @Override
    public String getName() {
        return "DouYin";
    }
    DouYinOpenApi douyinOpenApi;

    public DouyinModule(ReactApplicationContext context) {
        super(context);
    }






    @ReactMethod
    public void registerDyApp() {

        douyinOpenApi = DouYinOpenApiFactory.create(this.getReactApplicationContext().getCurrentActivity());
        douyinOpenApi.handleIntent(this.getReactApplicationContext().getCurrentActivity().getIntent(), this);
    }


    @ReactMethod
    public void sendAuthRequest(String scope, String state, Callback callback) {

        Authorization.Request request = new Authorization.Request();
        request.scope = scope;                          // 用户授权时必选权限
        request.state = state;                                 // 用于保持请求和回调的状态，授权请求后原样带回给第三方。

    Boolean bl=  douyinOpenApi.authorize(request);


        callback.invoke(null, bl);
    }

    private static ArrayList<DouyinModule> modules = new ArrayList<>();
    @Override
    public void initialize() {
        super.initialize();
        modules.add(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        if (douyinOpenApi != null) {
            douyinOpenApi = null;
        }
        modules.remove(this);
    }
    public static void handleIntent(Intent intent) {
        for (DouyinModule mod : modules) {
            mod.douyinOpenApi.handleIntent(intent, mod);
        }
    }
    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        WritableMap map = Arguments.createMap();
        // 授权成功可以获得authCode
        if (resp.getType() == CommonConstants.ModeType.SEND_AUTH_RESPONSE) {
            Authorization.Response response = (Authorization.Response) resp;
            if (resp.isSuccess()) {

                map.putString("authCode",response.authCode);

            }
        }

        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("DouYin_Resp", map);


    }

    @Override
    public void onErrorIntent(@Nullable Intent intent) {

    }
}

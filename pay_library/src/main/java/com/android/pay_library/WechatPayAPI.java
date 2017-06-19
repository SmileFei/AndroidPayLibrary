package com.android.pay_library;

/**
 * 微信支付API
 * <p>
 * 使用:
 * <p>
 * WechatPayAPI.getInstance().sendPayReq(wechatPayReq);
 * <p>
 * Created by Kennen on 2017/6/19.
 */
public class WechatPayAPI {

    /**
     * 获取微信支付API
     */
    private static final Object mLock = new Object();
    private static WechatPayAPI mInstance;

    public static WechatPayAPI getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new WechatPayAPI();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送微信支付请求
     *
     * @param wechatPayReq
     */
    public void sendPayReq(WechatPayReq wechatPayReq) {
        wechatPayReq.send();
    }
}

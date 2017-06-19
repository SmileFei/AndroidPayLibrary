package com.android.pay_library;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.pay_library.alipay.PayResult;

/**
 * 支付宝支付请求
 * <p>
 * 后台进行支付宝的基本配置后返回给前端进行调起
 * <p>
 * Created by Kennen on 2017/6/19.
 */
public class AliPayFromServer {

    /**
     * ali pay sdk flag
     */
    private static final int SDK_PAY_FLAG = 1;

    private Activity mActivity;
    private Handler mHandler;

    public AliPayFromServer() {
        super();
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);

                        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                        String resultInfo = payResult.getResult();

                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {

                            if (mOnAliPayListener != null)
                                mOnAliPayListener.onPaySuccess(resultInfo);
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                                if (mOnAliPayListener != null)
                                    mOnAliPayListener.onPayConfirmimg(resultInfo);

                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                                if (mOnAliPayListener != null)
                                    mOnAliPayListener.onPayFailure(resultInfo);
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
            }

        };
    }


    /**
     * 发起支付
     *
     * @param payInfoServer 后台返回配置信息
     */
    public void send(String payInfoServer) {

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = payInfoServer;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static class Builder {
        //上下文
        private Activity activity;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }

        public AliPayFromServer create() {
            AliPayFromServer aliPayReq = new AliPayFromServer();
            aliPayReq.mActivity = this.activity;
            return aliPayReq;
        }

    }

    //支付宝支付监听
    private OnAliPayListener mOnAliPayListener;

    public AliPayFromServer setOnAliPayListener(OnAliPayListener onAliPayListener) {
        this.mOnAliPayListener = onAliPayListener;
        return this;
    }

    /**
     * 支付宝支付监听
     *
     * @author Administrator
     */
    public interface OnAliPayListener {
        public void onPaySuccess(String resultInfo);

        public void onPayFailure(String resultInfo);

        public void onPayConfirmimg(String resultInfo);
    }
}

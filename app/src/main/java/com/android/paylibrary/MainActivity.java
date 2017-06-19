package com.android.paylibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.pay_library.AliPayAPI;
import com.android.pay_library.AliPayFromServer;
import com.android.pay_library.AliPayReq;
import com.android.pay_library.AliPayReq2;
import com.android.pay_library.PayAPI;
import com.android.pay_library.WechatPayReq;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final String payInfoServer = "alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017060607432267&biz_content=%7B%22body%22%3A%22%E7%94%B5%E8%B4%B9%E5%85%85%E5%80%BC%22%2C%22subject%22%3A%22%E7%94%B5%E8%B4%B9%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%22170619000015%22%2C%22total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22seller_id%22%3A%22guanpengfei%40gomoretech.com%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fgomoredev.natapp4.cc%2Fpalmmall-server%2Frest%2Fpayment%2FalipayCallBack&sign=ftzg1c2uJm1qzLLpIsilsN3%2B8xY8DFd748bfWT0JNDB3o2OfnGkCZ7nzXk3JQIPh7lv96g1gw%2FBIe0%2BZbWp32Q9lgIozrHWy2GIAG%2FyE8IEqGNS3JdjjdrcfuO3M3Ivt%2Fw4NmdIVWFz%2FbllwpAWeRavDPyM0SCf%2FvhO70QZF4KcY3ipg8wzwu5Fq4vdQiyTI996U7yE0IRVQmQYjrrxDNOpnKhodXBT%2FGksXQnJa0DFqSIP3FcWrtdkq%2FnN15XVjXicGl8oiSVnOgqpQkcIa%2B7Px3WcvPvC%2BmY99gC2IMZvaM6z1rVFU%2FoK8u%2Bo%2BG75HG%2BikElx3xOt2c2FOwOzTCQ%3D%3D&sign_type=RSA2&timestamp=2017-06-19+21%3A09%3A34&version=1.0&sign=ftzg1c2uJm1qzLLpIsilsN3%2B8xY8DFd748bfWT0JNDB3o2OfnGkCZ7nzXk3JQIPh7lv96g1gw%2FBIe0%2BZbWp32Q9lgIozrHWy2GIAG%2FyE8IEqGNS3JdjjdrcfuO3M3Ivt%2Fw4NmdIVWFz%2FbllwpAWeRavDPyM0SCf%2FvhO70QZF4KcY3ipg8wzwu5Fq4vdQiyTI996U7yE0IRVQmQYjrrxDNOpnKhodXBT%2FGksXQnJa0DFqSIP3FcWrtdkq%2FnN15XVjXicGl8oiSVnOgqpQkcIa%2B7Px3WcvPvC%2BmY99gC2IMZvaM6z1rVFU%2FoK8u%2Bo%2BG75HG%2BikElx3xOt2c2FOwOzTCQ%3D%3D";
        findViewById(R.id.btn_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AliPayFromServer alipay = new AliPayFromServer.Builder().
                        with(MainActivity.this).
                        create().
                        setOnAliPayListener(new AliPayFromServer.OnAliPayListener() {
                            @Override
                            public void onPaySuccess(String resultInfo) {
                            }

                            @Override
                            public void onPayFailure(String resultInfo) {
                            }

                            @Override
                            public void onPayConfirmimg(String resultInfo) {
                            }
                        });
                alipay.send(payInfoServer);
            }
        });

        findViewById(R.id.btn_wechart_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testWechatPay();
            }
        });

    }


    /**
     * 微信支付Test
     */
    public void testWechatPay() {
        String appid = "";
        String partnerid = "";
        String prepayid = "";
        String noncestr = "";
        String timestamp = "";
        String sign = "";
        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                .with(this) //activity实例
                .setAppId(appid) //微信支付AppID
                .setPartnerId(partnerid)//微信支付商户号
                .setPrepayId(prepayid)//预支付码
//								.setPackageValue(wechatPayReq.get)//"Sign=WXPay"
                .setNonceStr(noncestr)
                .setTimeStamp(timestamp)//时间戳
                .setSign(sign)//签名
                .create();

        PayAPI.getInstance().sendPayRequest(wechatPayReq);
//								.setOnWechatPayListener(new OnWechatPayListener() {
//
//									@Override
//									public void onPaySuccess(int errorCode) {
//										ToastUtil.show(mContext, "支付成功" + errorCode);
//
//									}
//
//									@Override
//									public void onPayFailure(int errorCode) {
//										ToastUtil.show(mContext, "支付失败" + errorCode);
//
//									}
//								});
//        WechatPayAPI.getInstance().sendPayReq(wechatPayReq);

        PayAPI.getInstance().sendPayRequest(wechatPayReq);

    }


    /**
     * 支付宝支付测试
     */
    public void testAliPay() {
        String rsa_private = "";
        String rsa_public = "";
        String partner = "";
        String seller = "";

        Activity activity = this;
        String outTradeNo = "";
        String price = "";
        String orderSubject = "";
        String orderBody = "";
        String callbackUrl = "";

        AliPayAPI.Config config = new AliPayAPI.Config.Builder()
                .setRsaPrivate(rsa_private) //设置私钥
                .setRsaPublic(rsa_public)//设置公钥
                .setPartner(partner) //设置商户
                .setSeller(seller) //设置商户收款账号
                .create();

        AliPayReq aliPayReq = new AliPayReq.Builder()
                .with(activity)//Activity实例
                .apply(config)//支付宝支付通用配置
                .setOutTradeNo(outTradeNo)//设置唯一订单号
                .setPrice(price)//设置订单价格
                .setSubject(orderSubject)//设置订单标题
                .setBody(orderBody)//设置订单内容 订单详情
                .setCallbackUrl(callbackUrl)//设置回调地址
                .create();//
        AliPayAPI.getInstance().apply(config).sendPayReq(aliPayReq);

        PayAPI.getInstance().sendPayRequest(aliPayReq);
    }


    /**
     * 安全的支付宝支付测试
     */
    public void testAliPaySafely() {
        String partner = "";
        String seller = "";

        Activity activity = this;
        String outTradeNo = "";
        String price = "";
        String orderSubject = "";
        String orderBody = "";
        String callbackUrl = "";


        String rawAliOrderInfo = new AliPayReq2.AliOrderInfo()
                .setPartner(partner) //商户PID || 签约合作者身份ID
                .setSeller(seller)  // 商户收款账号 || 签约卖家支付宝账号
                .setOutTradeNo(outTradeNo) //设置唯一订单号
                .setSubject(orderSubject) //设置订单标题
                .setBody(orderBody) //设置订单内容
                .setPrice(price) //设置订单价格
                .setCallbackUrl(callbackUrl) //设置回调链接
                .createOrderInfo(); //创建支付宝支付订单信息


        //TODO 这里需要从服务器获取用商户私钥签名之后的订单信息
        String signAliOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);

        AliPayReq2 aliPayReq = new AliPayReq2.Builder()
                .with(activity)//Activity实例
                .setRawAliPayOrderInfo(rawAliOrderInfo)//set the ali pay order info
                .setSignedAliPayOrderInfo(signAliOrderInfo) //set the signed ali pay order info
                .create()//
                .setOnAliPayListener(null);//
        AliPayAPI.getInstance().sendPayReq(aliPayReq);

        PayAPI.getInstance().sendPayRequest(aliPayReq);
    }

    /**
     * 获取签名后的支付宝订单信息  (用商户私钥RSA加密之后的订单信息)
     *
     * @param rawAliOrderInfo
     * @return
     */
    private String getSignAliOrderInfoFromServer(String rawAliOrderInfo) {
        return null;
    }

}

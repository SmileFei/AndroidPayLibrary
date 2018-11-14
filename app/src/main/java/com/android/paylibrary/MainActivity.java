package com.android.paylibrary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.pay_library.AliPayFromServer;
import com.android.pay_library.PayAPI;
import com.android.pay_library.WechatPayReq;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {


    //mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
    private final String mMode = "01";
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";

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
                weChatPay();
            }
        });

        findViewById(R.id.btn_union_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getAcpTNThread().start();
            }
        });
    }

    public void weChatPay() {
        String AppId = "";       //微信支付AppID
        String PartnerId = "";   //微信支付商户号
        String PrepayId = "";    //预支付码
        String NonceStr = "";    //时间戳
        String PackageStr = "";
        String TimeStamp = "";
        String Sign = "";        //签名


        //1.创建微信支付请求
        WechatPayReq wechatPayReq = new WechatPayReq.Builder()
                .with(this) //activity实例
                .setAppId(AppId)
                .setPartnerId(PartnerId)
                .setPrepayId(PrepayId)
                .setNonceStr(NonceStr)
                .setPackageValue(PackageStr)
                .setTimeStamp(TimeStamp)
                .setSign(Sign)
                .create();
        //2.发送微信支付请求
        PayAPI.getInstance().sendPayRequest(wechatPayReq);
    }


    class getAcpTNThread extends Thread {
        @Override
        public void run() {
            super.run();
            String tn = null;
            InputStream is;
            try {
                String url = TN_URL_01;
                URL myURL = new URL(url);
                URLConnection ucon = myURL.openConnection();
                ucon.setConnectTimeout(120000);
                is = ucon.getInputStream();
                int i = -1;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((i = is.read()) != -1) {
                    baos.write(i);
                }
                tn = baos.toString();
                is.close();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = mHandler.obtainMessage();
            msg.obj = tn;
            mHandler.sendMessage(msg);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String tn = "";
            if (msg.obj == null || ((String) msg.obj).length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("错误提示");
                builder.setMessage("网络连接失败,请重试!");
                builder.setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            } else {
                tn = (String) msg.obj;
                //步骤2：通过银联工具类启动支付插件
                UPPayAssistEx.startPay(MainActivity.this, null, null, tn, mMode);
            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 处理银联手机支付控件返回的支付结果
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 此处的verify建议送去商户后台做验签
                    // 如要放在手机端验，则代码必须支持更新证书
                    boolean ret = verify(dataOrg, sign, mMode);
                    if (ret) {
                        // 验签成功，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验签失败
                        msg = "支付失败！";
                    }
                } catch (JSONException e) {
                }
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }
}

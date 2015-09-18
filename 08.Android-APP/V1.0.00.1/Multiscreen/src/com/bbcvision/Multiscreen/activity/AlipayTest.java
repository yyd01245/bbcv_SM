package com.bbcvision.Multiscreen.activity;/**
 * @Title:
 * @Description:
 * @Author: Nestor bbcvision.com 
 * @Date: 2014-11-10 11:03 
 * @Version V1.0
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.bbcvision.Multiscreen.R;
import com.bbcvision.Multiscreen.configs.AliPayKeys;
import com.bbcvision.Multiscreen.tools.alipaytools.Result;
import com.bbcvision.Multiscreen.tools.alipaytools.Rsa;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.message.PushAgent;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kysx_100 on 14/11/10.
 */
public class AlipayTest extends BaseActivity {
    public static final String TAG = "alipay-sdk";

    private static final int RQF_PAY = 1;

    private static final int RQF_LOGIN = 2;
    private Product product;

    @Override
    protected void initView() {
        super.initView();
        PushAgent.getInstance(this).onAppStart();
        setContentView(R.layout.activity_alipayt);
        ViewUtils.inject(this);
        initProducts();
    }

    @OnClick(R.id.bt_alipay)
    public void clickPay(View v) {
        topay();
    }

    // 增加商品
    private void initProducts() {
        if (sProducts != null)
            return;
        ArrayList<Product> products = new ArrayList<Product>();
        product = new Product();
        product.subject = "大话西游";
        product.body = "好看";
        product.price = "200.00";
        products.add(product);

        sProducts = new Product[products.size()];
        products.toArray(sProducts);

    }

    private String getNewOrderInfo(int position) {
        StringBuilder sb = new StringBuilder();
        // 签约的支付宝账号对应的支付宝唯一用户号。以 2088 开头的 16 位纯数字组成。
        sb.append("partner=\"");
        sb.append(AliPayKeys.DEFAULT_PARTNER);

        // 商户网站唯一订单号
        sb.append("\"&out_trade_no=\"");
        sb.append(getOutTradeNo());
        // 商品名称
        sb.append("\"&subject=\"");
        sb.append(sProducts[position].subject);
        // 商品详情
        sb.append("\"&body=\"");
        sb.append(sProducts[position].body);
        // 总金额
        sb.append("\"&total_fee=\"");
        sb.append(sProducts[position].price.replace("一口价:", ""));
        // 服务器异步通知页面路径(可空)
        //sb.append("\"&notify_url=\"");
        //sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));// 网址需要做URL编码
        // 接口名称。固定值。
        sb.append("\"&service=\"mobile.securitypay.pay");
        // 商户网站使用的编码格式，固定为 utf-8。
        sb.append("\"&_input_charset=\"UTF-8");
        // 签名类型，目前仅支持 RSA。
        //sb.append("\"&sign_typ=\"RSA");
        sb.append("\"&return_url=\"");
        sb.append(URLEncoder.encode("http://m.alipay.com"));
        // 支付类型。默认值为：1 （商品购买）。
        sb.append("\"&payment_type=\"1");
        // 卖家支付宝账号（邮箱或手机号码格式）或其对应的支付宝唯一用户号（以 2088 开头的纯 16 位数字）。
        sb.append("\"&seller_id=\"");
        sb.append(AliPayKeys.DEFAULT_SELLER);

        // 商品展示地址 如果show_url值为空，可不传
        // sb.append("\"&show_url=\"");
        // 未付款交易的超时时间
        sb.append("\"&it_b_pay=\"1m");
        sb.append("\"");

        return new String(sb);
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        Log.d(TAG, "outTradeNo: " + key);
        return key;
    }

    // TODO 商品信息写成bean
    public static class Product {
        public String subject;
        public String body;
        public String price;
    }

    public static Product[] sProducts;

    private void topay(){
        try {
            Log.i("ExternalPartner", "onItemClick");
            String info = getNewOrderInfo(0);
            //签名 请参见“9 签名机制”。
            String sign = Rsa.sign(info, AliPayKeys.PRIVATE);
            sign = URLEncoder.encode(sign);
            info += "&sign=\"" + sign + "\"&" + getSignType();
            Log.i("ExternalPartner", "start pay");
            // start the pay.
            Log.i(TAG, "info = " + info);

            final String orderInfo = info;
            new Thread() {
                public void run() {
                    AliPay alipay = new AliPay(AlipayTest.this, mHandler);

                    //设置为沙箱模式，不设置默认为线上环境
                    //alipay.setSandBox(true);

                    String result = alipay.pay(orderInfo);

                    Log.i(TAG, "result = " + result);
                    Message msg = new Message();
                    msg.what = RQF_PAY;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            }.start();

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    /**
     * 签名类型，目前仅支持 RSA。
     * @return
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Result result = new Result((String) msg.obj);

            switch (msg.what) {
                case RQF_PAY:
                case RQF_LOGIN: {
                    Toast.makeText(AlipayTest.this, result.getResult(),
                            Toast.LENGTH_SHORT).show();

                }
                break;
                default:
                    break;
            }
        };
    };

}

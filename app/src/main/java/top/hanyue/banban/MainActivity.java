package top.hanyue.banban;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import cn.stormbirds.stpaydemo.R;
import top.hanyue.banban.utils.PermissionHelper;
import top.hanyue.banban.utils.PermissionInterface;
import cn.stormbirds.stpaylib.PayType;
import cn.stormbirds.stpaylib.bean.OrderRequest;
import cn.stormbirds.stpaylib.PayResult;
import cn.stormbirds.stpaylib.STPayCallback;
import cn.stormbirds.stpaylib.STPaySDK;

public class MainActivity extends AppCompatActivity implements STPayCallback ,PermissionInterface {
    private PermissionHelper mPermissionHelper;

    private EditText amountEditText;
    private EditText useridEditText;
    private EditText time_expireEditText;
    private String currentAmount = "";
    private ImageButton ibWechat;
    private ImageButton ibAlipay;
    private ImageButton ibLKL;
    protected double amount = 0.0;
    protected String userid = "";
    protected long time_expire ;
    private int channel = 0;
    private Button btn_ok;

    private PayType curType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionHelper = new PermissionHelper(this,this);

        STPaySDK.INSTANCE.setTest(false);

        btn_ok = (Button) findViewById(R.id.bt_ok);
        ImageView ivWX = (ImageView) findViewById(R.id.iv_wx);
        ivWX.setImageResource(R.drawable.wx);
        ImageView ivAli = (ImageView) findViewById(R.id.iv_ali);
        ivAli.setImageResource(R.drawable.ali);


        // select channel button
        ibWechat = (ImageButton) findViewById(R.id.ibWechat);
        ibAlipay = (ImageButton) findViewById(R.id.ibAlipay);
        onChannelClick(ibWechat);

        useridEditText = (EditText) findViewById(R.id.edit_userid);
        amountEditText = (EditText) findViewById(R.id.et_right);
        time_expireEditText = (EditText) findViewById(R.id.edit_time_expire);

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(currentAmount)) {
                    amountEditText.removeTextChangedListener(this);
                    String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
                    String cleanString = s.toString().replaceAll(replaceable, "");

                    if (cleanString.equals("") || new BigDecimal(cleanString).toString().equals("0")) {
                        amountEditText.setText(null);
                    } else {
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance(Locale.CHINA).format((parsed / 100));
                        currentAmount = formatted;
                        amountEditText.setText(formatted);
                        amountEditText.setSelection(formatted.length());
                    }
                    amountEditText.addTextChangedListener(this);
                }
            }
        });
    }

    private boolean checkInputValid(String amountText) {
        return !(null == amountText || amountText.length() == 0);
    }

    private double parseInputTxt(String amountText) {
        String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
        String cleanString = amountText.replaceAll(replaceable, "");
        return Double.valueOf(new BigDecimal(cleanString).toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)){
            //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onCharge(View v){
        String amountText = amountEditText.getText().toString();
        userid = useridEditText.getText().toString();
        time_expire=Long.parseLong(time_expireEditText.getText().toString())* 1000+System.currentTimeMillis();
        if (checkInputValid(amountText)) {
            amount = parseInputTxt(amountText);
            amount /= 100;
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setPayId(Integer.valueOf(userid));
            orderRequest.setBody("商品详情");
            orderRequest.setPrice(BigDecimal.valueOf(amount));
            orderRequest.setTimeExpire(time_expire);
            orderRequest.setTitle("商品标题");
            orderRequest.setUserId(userid);

            STPaySDK.INSTANCE.pay(MainActivity.this, orderRequest, curType, MainActivity.this);
        }
    }

    public void onChannelClick(View v) {
        switch (v.getId()) {
            case R.id.ibAlipay:
                channel = 0;
                curType = PayType.aliPay;
                ibAlipay.setBackgroundResource(R.drawable.selected);
                ibWechat.setBackgroundResource(R.drawable.unselected);
                ibLKL.setBackgroundResource(R.drawable.unselected);
                break;

            case R.id.ibWechat:
                channel = 1;
                curType = PayType.wxPay;
                ibAlipay.setBackgroundResource(R.drawable.unselected);
                ibWechat.setBackgroundResource(R.drawable.selected);
                ibLKL.setBackgroundResource(R.drawable.unselected);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPayFinished(PayResult payResult) {
        final PayResult result = payResult;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getPermissionsRequestCode() {
        return 10000;
    }

    @Override
    public String[] getPermissions() {
        //设置该界面所需的全部权限
        return new String[]{
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {
        finish();
    }
}

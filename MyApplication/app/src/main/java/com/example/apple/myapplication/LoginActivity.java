package com.example.apple.myapplication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.myapplication.util.MyException;
import com.example.apple.myapplication.util.MySpider;

public class LoginActivity extends AppCompatActivity {

    private MySpider s;
    private EditText username;
    private EditText password;
    private EditText verifyCode;
    private AppCompatImageView verifyImage;
    @SuppressWarnings("deprecation")
    private ProgressDialog dialog2;

    public void about(View v) {
        Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    //检查输入合法性
    private boolean check() {
        if(username.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("唉");
            dialog.setMessage("请输入学号");
            dialog.setPositiveButton("噢", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //设置焦点
                    username.setFocusable(true);
                }
            });
            dialog.show();
        } else if(password.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("唉");
            dialog.setMessage("请输入密码");
            dialog.setPositiveButton("噢", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //设置焦点
                    password.setFocusable(true);
                }
            });
            dialog.show();
        } else if(verifyCode.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("唉");
            dialog.setMessage("请输入验证码");
            dialog.setPositiveButton("噢", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //设置焦点
                    verifyCode.setFocusable(true);
                }
            });
            dialog.show();
        } else {
            return true;
        }
        return false;
    }

    //显示验证码
    private void setVerifyImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(this.getApplicationContext().getFilesDir().toString()+"/verify_code.jpeg");
        verifyImage.setImageBitmap(bitmap);
    }

    //重新获取并刷新验证码
    public void updateVerifyImage() {
        try {
            //重新获取验证码
            s.redownloadVerifyCode(LoginActivity.this.getApplicationContext());
            //显示验证码
            setVerifyImage();
        } catch(Exception e) {
            //异常处理：也许有异常
            Toast.makeText(LoginActivity.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //监听器：刷新验证码
    View.OnClickListener updateVerifyImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            updateVerifyImage();
        }
    };

    //绑定处理事件：登陆按钮
    public void login(View view) {
        //检查输入合法性
        if(!check())
            return;
        try {
            //noinspection deprecation
            dialog2 = ProgressDialog.show(LoginActivity.this, "提示", "正在登陆中");
            new Thread(null, new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        dialog2.dismiss();
                    } catch (Exception e) {
                        //异常处理：进度条崩了
                        AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
                        dialog.setTitle("惊不惊喜");
                        dialog.setMessage("进度条崩了");
                        dialog.setPositiveButton("噢", null);
                        dialog.show();
                    }
                }
            }).start();
            //登陆
            s.login(username.getText().toString(), password.getText().toString(), verifyCode.getText().toString());
            //进度条
            //页面跳转：具体信息页面
            Intent intent = new Intent(LoginActivity.this,InfoActivity.class);
            intent.putExtra("s",s);
            startActivity(intent);
        } catch(MyException e) {
            dialog2.dismiss();
            //异常处理：验证码不正确, 密码不匹配, 用户名不存在
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("惊不惊喜");
            dialog.setMessage(e.getMessage());
            dialog.setPositiveButton("噢", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //更新验证码
                    ((AppCompatImageView)findViewById(R.id.verify_image)).callOnClick();
                }
            });
            dialog.show();
        } catch(Exception e) {
            dialog2.dismiss();
            //异常处理：其他
            String msg = e.getMessage();
            if(msg.contains("Network is unreachable"))
                msg = "网络没连接（"+msg+"）";
            else if(msg.contains("error fetching URL"))
                msg = "网络不太好，多登陆几次试试（"+msg+"）";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    //初始化
    private void init() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        verifyCode = (EditText) findViewById(R.id.verify_code);
        verifyImage = (AppCompatImageView) findViewById(R.id.verify_image);
        verifyImage.setOnClickListener(updateVerifyImage);

        //设置网络线程
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        try {
            //初始化连接, 获取验证码
            s = new MySpider(getApplicationContext());
            //显示验证码
            setVerifyImage();
        } catch(java.net.ConnectException e) {
            //异常处理：网络没有连接
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
            dialog.setTitle("警告");
            dialog.setMessage("网断啦！");
            dialog.setPositiveButton("噢", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //退出程序
                    finish();
                }
            });
            dialog.show();
        } catch(Exception e) {
            //异常处理：其他
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }
}

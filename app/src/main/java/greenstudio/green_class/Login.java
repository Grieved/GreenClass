package greenstudio.green_class;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.CookieManager;
import java.net.CookiePolicy;

import greenstudio.green_class.Infomation.MsgInfo;
import greenstudio.green_class.Infomation.URLInfo;
import greenstudio.green_class.Utils.AppManager;
import greenstudio.green_class.Utils.HttpPostUtil;
import greenstudio.green_class.Utils.PersistentCookieStore;


public class Login extends AppCompatActivity {
    private EditText usr_name, passwd;
    private Button login;
    private HttpPostUtil util;
    private CheckBox savepass_flag;
    private SharedPreferences preferences;
    private ProgressDialog progressdialog;
    private SharedPreferences.Editor editor;
    private LoginHandler logHandler = new LoginHandler();
    private static String loginUrl = URLInfo.LOG_URL;
    public static OkHttpClient client;
    public static String usr;
    public static String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        AppManager.getAppManager().addActivity(this);
        usr_name = (EditText) findViewById(R.id.usr_name);
        passwd = (EditText) findViewById(R.id.passwd);
        savepass_flag = (CheckBox) findViewById(R.id.save_pass);
        login = (Button) findViewById(R.id.login);
        preferences = getSharedPreferences("user", MODE_PRIVATE);
        editor = preferences.edit();
        usr = preferences.getString("name", "");
        pass = preferences.getString("pass", "");
        usr_name.setText(usr);
        passwd.setText(pass);
        if (pass != null && !"".equals(pass)) {
            savepass_flag.setChecked(true);
        }
        client = new OkHttpClient();
        client.setCookieHandler(new CookieManager(new PersistentCookieStore(getApplicationContext()), CookiePolicy.ACCEPT_ALL));

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         progressdialog = ProgressDialog.show(Login.this, "请等待...", "正在为您登陆...");
                                         usr = usr_name.getText().toString();
                                         pass = passwd.getText().toString();
                                         RequestBody body = new FormEncodingBuilder().add("yhm", usr)
                                                 .add("mm", pass).add("yzm", "").build();
                                         util = new HttpPostUtil(logHandler, client, loginUrl, body, MsgInfo.LOG_MSG);
                                         util.start();
                                     }
                                 }

        );
    }


    //处理器
    class LoginHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int flag = msg.what;
            switch (flag) {
                case MsgInfo.LOG_MSG:
                    String info = (String) msg.obj;
                    editor.putString("name", usr);
                    Document doc = Jsoup.parse(info);
                    String title = doc.getElementsByTag("title").get(0).text();
                    if (title.equals("警告提示")) {
                        Toast.makeText(Login.this, "用户名或密码错误,请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        if (title.equals("本科教学管理与服务平台")) {
                            if (savepass_flag.isChecked()) {
                                editor.putString("pass", pass);
                            } else {
                                editor.remove("pass");
                            }
                            Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            Intent log_pass = new Intent(Login.this, MainActivity.class);
                            log_pass.putExtra("info", info);
                            startActivity(log_pass);
                        } else if (title.equals("登陆超时")) {
                            Toast.makeText(Login.this, "连接超时,请重新登陆", Toast.LENGTH_SHORT).show();
                        }

                    }
                    editor.commit();
                    break;
                case MsgInfo.ERROR_MSG:
                    Toast.makeText(Login.this, "连接超时", Toast.LENGTH_SHORT).show();
                    break;
            }
            progressdialog.dismiss();//解除进度条
        }
    }
}
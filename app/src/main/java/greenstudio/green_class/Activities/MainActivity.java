package greenstudio.green_class.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import greenstudio.green_class.Infomation.MsgInfo;
import greenstudio.green_class.Infomation.URLInfo;
import greenstudio.green_class.Infomation.USER_Info;
import greenstudio.green_class.R;
import greenstudio.green_class.Utils.AppManager;
import greenstudio.green_class.Utils.HttpBitmapUtil;
import greenstudio.green_class.Utils.HttpGetUtil;
import greenstudio.green_class.Utils.HttpPostUtil;
import greenstudio.green_class.fragments.Course_Display;
import greenstudio.green_class.fragments.Course_Search;
import greenstudio.green_class.fragments.Grade_Search;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String ymm;
    private String xmm;
    private String mmqr;
    private HttpPostUtil util;
    private TextView title_name, title_class, title_id, title_xf, title_zxf, now_progress;
    private ProgressBar title_progress;
    private ImageView title_header;
    private HttpGetUtil getUtil;
    private HttpBitmapUtil bitmapUtil;
    private ProgressDialog progressdialog;
    private AlertDialog selfdialog, usr_do_dialog;
    private InfoHandler handler = new InfoHandler();
    private Grade_Search grade_search;
    private Course_Display course_display;
    private Course_Search course_search;
    private Fragment isFragment;
    private OkHttpClient client;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MsgInfo.HEAD_MSG:
                    title_header = (ImageView) findViewById(R.id.title_header);
                    USER_Info.user_header = (Bitmap) msg.obj;
                    title_header.setImageBitmap(USER_Info.user_header);
                    break;
                case MsgInfo.INFO_MSG:
                    String info = (String) msg.obj;
                    try {
                        Document doc = Jsoup.parse(info);
                        USER_Info.user_name = doc.getElementsByTag("h4").get(0).text();
                        USER_Info.user_class = doc.getElementsByTag("p").get(0).text();
                        USER_Info.user_xf = doc.getElementsByTag("p").get(1).text();
                        USER_Info.user_total_xf = doc.getElementsByTag("p").get(2).text();
                        int zxf = Integer.parseInt(USER_Info.user_total_xf.substring(USER_Info.user_total_xf.indexOf(" ") + 1, USER_Info.user_total_xf.length() - 1));
                        int xf = Integer.parseInt(USER_Info.user_xf.substring(USER_Info.user_xf.indexOf(" ") + 1, USER_Info.user_xf.length() - 1));
                        USER_Info.user_progress = xf * 100 / zxf;
                        title_progress = (ProgressBar) findViewById(R.id.title_progress);
                        now_progress = (TextView) findViewById(R.id.progress_now);
                        title_class = (TextView) findViewById(R.id.title_class_user);
                        title_id = (TextView) findViewById(R.id.title_number);
                        title_name = (TextView) findViewById(R.id.title_name);
                        title_xf = (TextView) findViewById(R.id.now_credit);
                        title_zxf = (TextView) findViewById(R.id.title_credit);
                    } finally {
                        title_zxf.setText(USER_Info.user_total_xf);
                        title_xf.setText(USER_Info.user_xf);
                        title_id.setText(USER_Info.user_id);
                        title_class.setText(USER_Info.user_class);
                        title_name.setText(USER_Info.user_name);
                        now_progress.setText(USER_Info.user_progress + "%");
                        title_progress.setProgress(USER_Info.user_progress);
                    }
                    break;
                case MsgInfo.ERROR_MSG:
                    Toast.makeText(MainActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        AppManager.getAppManager().addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment(savedInstanceState);
        client = Login.client;
        USER_Info.user_id = Login.usr;
        String Info_url = URLInfo.INFO_URL + USER_Info.user_id;
        getUtil = new HttpGetUtil(mhandler, client, Info_url, MsgInfo.INFO_MSG);
        getUtil.start();
        String Head_url = URLInfo.HEAD_URL + USER_Info.user_id + "&zplx=rxhzp";
        bitmapUtil = new HttpBitmapUtil(mhandler, client, Head_url, MsgInfo.HEAD_MSG);
        bitmapUtil.start();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_course) {
            // Handle the camera action
            if (course_display == null) {
                course_display = new Course_Display();
            }
            switchContent(isFragment, course_display);

        } else if (id == R.id.nav_search_course) {
            if (course_search == null) {
                course_search = new Course_Search();
            }
            switchContent(isFragment, course_search);
        } else if (id == R.id.nav_search_grade) {
            if (grade_search == null) {
                grade_search = new Grade_Search();
            }
            switchContent(isFragment, grade_search);
        } else if (id == R.id.nav_change_pass) {
            //创建view从当前activity获取loginactivity
            View view;
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.xgmm, null);

            final EditText username = (EditText) view.findViewById(R.id.txt_username);
            final EditText password = (EditText) view.findViewById(R.id.txt_password);
            final EditText password_con = (EditText) view.findViewById(R.id.txt_password_con);
            AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
            ad.setView(view);
            ad.setTitle("密码修改");
            selfdialog = ad.create();
            selfdialog.setButton("确认修改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //获取输入框的用户名密码

                    ymm = username.getText().toString();
                    xmm = password.getText().toString();
                    mmqr = password_con.getText().toString();

                    //提交的时候弹出一个进度条对话框，当处理完毕关闭
                    progressdialog = ProgressDialog.show(MainActivity.this, "请等待...", "正在为您修改密码...");
                    if (!ymm.equals(Login.pass)) {
                        Toast.makeText(MainActivity.this, "原始密码输入错误", Toast.LENGTH_SHORT).show();
                    } else if (!xmm.equals(mmqr) || xmm.equals("")) {
                        Toast.makeText(MainActivity.this, "请确认两次密码输入一致且不为空", Toast.LENGTH_SHORT).show();
                    } else if (xmm.equals(ymm)) {
                        Toast.makeText(MainActivity.this, "新密码要与原始密码不同", Toast.LENGTH_SHORT).show();
                    } else {

                        RequestBody body = new FormEncodingBuilder().add("doType", "save")
                                .add("ymm", ymm).add("mm", xmm).add("yhmmdj", "3").add("nmm", xmm).build();
                        util = new HttpPostUtil(handler, client, URLInfo.MMXG_URL + Login.usr, body, MsgInfo.MMXG_MSG);
                        util.start();
                        selfdialog.cancel();
                    }
                    progressdialog.cancel();
                }
            });
            selfdialog.setButton2("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selfdialog.cancel();
                }
            });
            selfdialog.show();

        } else if (id == R.id.nav_change_usr) {
            usr_do_dialog = new AlertDialog.Builder(MainActivity.this).setIcon(
                    android.R.drawable.btn_star).setTitle("切换用户").setMessage(
                    "要给我换个主人咩？").setPositiveButton("是的",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }).setNegativeButton("不不", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    usr_do_dialog.dismiss();
                }
            }).create();
            usr_do_dialog.show();

        } else if (id == R.id.nav_exit) {
            usr_do_dialog = new AlertDialog.Builder(MainActivity.this).setIcon(
                    android.R.drawable.btn_star).setTitle("退出应用").setMessage(
                    "确定要离开了咩？").setPositiveButton("再见...",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            AppManager.getAppManager().AppExit(MainActivity.this);
                        }
                    }).setNegativeButton("再逛一会儿", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    usr_do_dialog.dismiss();
                }
            }).create();
            usr_do_dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void initFragment(Bundle savedInstanceState) {
        //判断activity是否重建，如果不是，则不需要重新建立fragment.
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            course_display = new Course_Display();
            ft.add(R.id.contents, course_display, "display");
            isFragment = course_display;
            ft.replace(R.id.contents, course_display).commit();
        }
    }


    public void switchContent(Fragment from, Fragment to) {
        if (isFragment != to) {
            isFragment = to;
            FragmentManager fm = getSupportFragmentManager();
            //添加渐隐渐现的动画
            FragmentTransaction ft = fm.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                ft.hide(from).add(R.id.contents, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                ft.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    class InfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            progressdialog.dismiss();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int flag = msg.what;
            switch (flag) {
                case MsgInfo.MMXG_MSG:
                    Toast.makeText(MainActivity.this, "密码修改成功,请退出出应用重新登录", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    break;
                case MsgInfo.ERROR_MSG:
                    Toast.makeText(MainActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

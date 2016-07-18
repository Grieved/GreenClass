package greenstudio.green_class.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import greenstudio.green_class.Utils.HttpPostUtil;
import greenstudio.green_class.Login;
import greenstudio.green_class.Infomation.MsgInfo;
import greenstudio.green_class.R;
import greenstudio.green_class.Infomation.URLInfo;

/**
 * Created by wangyu on 7/18/16.
 */
public class Grade_Search extends Fragment {
    private String[] XNS = {"2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", ""};
    private String[] XQS = {"3", "12", "16", ""};
    private String XN;
    private String XQ;
    private String[] XN_list = {"2011-2012", "2012-2013", "2013-2014", "2014-2015", "2015-2016", "2016-2017", "2017-2018", "2018-2019", "2019-2020", "2020-2021", "全部"};
    private String[] XQ_list = {"第1学期", "第二学期", "第三学期", "全部"};
    private ArrayAdapter<String> XN_adapter;
    private ArrayAdapter<String> XQ_adapter;
    private static int total_xs;
    private static double total_gpa;
    private static double gpa;
    private Context context;
    private boolean getGrade_flag;
    private List<Map<String, String>> group_list;
    private ExpandableListView listView;
    private Grade_Adapter adapter;
    private List<List<Map<String, String>>> item_list;
    private Spinner XQ_spinner, XN_spinner;
    private Button search_grade, cal_gpa;
    private TextView gpa_show;
    private Handler uiHandler;
    private HttpPostUtil util;
    private OkHttpClient client = Login.client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View grade_view = inflater.inflate(R.layout.grade_search, null);
        context = getContext();
        return grade_view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        group_list = new ArrayList<>();
        item_list = new ArrayList<>();
        listView = (ExpandableListView) view.findViewById(R.id.grade_list);
        XQ_spinner = (Spinner) view.findViewById(R.id.xqs);
        XN_spinner = (Spinner) view.findViewById(R.id.xns);
        gpa_show = (TextView) view.findViewById(R.id.show_gpa);
        cal_gpa = (Button) view.findViewById(R.id.GPA);
        search_grade = (Button) view.findViewById(R.id.do_search);
        XQ_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, XQ_list);
        XN_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, XN_list);
        XQ_spinner.setAdapter(XQ_adapter);
        XN_spinner.setAdapter(XN_adapter);
        XN_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                XN = XNS[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        XQ_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                XQ = XQS[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cal_gpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigDecimal bd = new BigDecimal(gpa);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                gpa_show.setText(bd + "");
            }
        });

        search_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody formbody = new FormEncodingBuilder()
                        .add("xnm", XN)
                        .add("xqm", XQ)
                        .add("_search", "false")
                        .add("nd", String.valueOf(System.currentTimeMillis()))
                        .add("queryModel.showCount", "150")
                        .add("queryModel.currentPage", "1")
                        .add("queryModel.sortName", "")
                        .add("queryModel.sortOrder", "asc")
                        .add("time", "1")
                        .build();

                util = new HttpPostUtil(uiHandler, client, URLInfo.CJXX_URL + Login.usr, formbody, MsgInfo.GRADE_MSG);
                util.start();
            }
        });


        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MsgInfo.GRADE_MSG:
                        String grade_info = (String) msg.obj;
                        try {
                            Document doc = Jsoup.parse(grade_info);
                            String title = doc.getElementsByTag("title").get(0).text();
                            if (title.equals("登录超时")) {
                                Toast.makeText(context, "连接超时,请重新登陆", Toast.LENGTH_SHORT).show();
                            }
                            Intent i = context.getPackageManager()
                                    .getLaunchIntentForPackage(context.getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } catch (Exception e) {

                        }
                        try {
                            group_list.clear();
                            item_list.clear();
                            total_xs = 0;
                            total_gpa = 0.0;
                            getGrade_flag = true;
                            ReadJSON(grade_info);
                            if (!getGrade_flag) {
                                Toast.makeText(context, "暂无成绩可供查询", Toast.LENGTH_SHORT).show();
                            }

                            gpa = total_gpa / total_xs;
                            adapter = new Grade_Adapter(context, group_list, item_list);
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case MsgInfo.ERROR_MSG:
                        Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

    }

    public void ReadJSON(String jsonString) throws JSONException {
        Map<String, String> group_map;
        List<Map<String, String>> item_res;
        Map<String, String> item_map;
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        String kcxz;
        double item_xf;
        double item_gpa;
        if (jsonArray.length() < 1) {
            getGrade_flag = false;
        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                group_map = new HashMap<>();
                item_map = new HashMap<>();
                item_res = new ArrayList<>();
                group_map.put("name", jsonObject.getString("kcmc"));
                group_map.put("grade", jsonObject.getString("cj"));
                item_map.put("gpa", jsonObject.getString("jd"));
                item_map.put("xf", jsonObject.getString("xf"));
                item_map.put("xz", jsonObject.getString("kcxzmc"));
                item_map.put("dm", jsonObject.getString("kch"));
                item_map.put("xy", jsonObject.getString("kkbmmc"));
                item_map.put("ksxz", jsonObject.getString("ksxz"));
                kcxz = jsonObject.getString("kcxzmc");
                item_xf = Double.parseDouble(jsonObject.getString("xf"));
                item_gpa = Double.parseDouble(jsonObject.getString("jd"));
                if (kcxz.equals("必修")) {
                    total_xs += item_xf;
                    total_gpa += item_xf * item_gpa;
                }
                item_res.add(item_map);
                item_list.add(item_res);
                group_list.add(group_map);
            }
        }
    }

}

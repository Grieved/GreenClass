package greenstudio.green_class.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import greenstudio.green_class.Activities.Login;
import greenstudio.green_class.Adapters.CourseSearchItemAdapter;
import greenstudio.green_class.CourseDisData.CourseSearchData;
import greenstudio.green_class.Infomation.MsgInfo;
import greenstudio.green_class.Infomation.Search_Info;
import greenstudio.green_class.Infomation.URLInfo;
import greenstudio.green_class.Infomation.USER_Info;
import greenstudio.green_class.R;
import greenstudio.green_class.Utils.HttpPostUtil;

public class Course_Search extends Fragment {
    private String XN;
    private String XQ;
    private Spinner xq_course, xn_course;
    private ListView week_show, course_item;
    private ArrayAdapter weekAdapter;
    private CourseSearchItemAdapter searchItemAdapter;
    private Button search_course, add_toCourse;
    private AlertDialog importCourse;
    private List<CourseBean> total_course, mon_course, tue_course, wed_course, thu_course, fri_course, sat_course, sun_course;
    private List<List<CourseBean>> weekCourse;
    private boolean getCourse_flag;
    private CourseSearchData data = Course_Display.data;
    private Handler uiHandler;
    private Context context;
    private HttpPostUtil postUtil;
    private ArrayAdapter<String> XN_Search_adapter;
    private ArrayAdapter<String> XQ_Search_adapter;
    private OkHttpClient client = Login.client;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View courseSearch = inflater.inflate(R.layout.course_search, null);
        context = getContext();
        return courseSearch;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        xq_course = (Spinner) view.findViewById(R.id.xqs_course);
        xn_course = (Spinner) view.findViewById(R.id.xns_course);
        search_course = (Button) view.findViewById(R.id.Search);
        add_toCourse = (Button) view.findViewById(R.id.add_toCourse);
        week_show = (ListView) view.findViewById(R.id.course_week);
        course_item = (ListView) view.findViewById(R.id.course_item);
        weekAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, Search_Info.WEEK_list);
        week_show.setAdapter(weekAdapter);
        getCourse_flag = true;
        total_course = new ArrayList<>();
        mon_course = new ArrayList<>();
        tue_course = new ArrayList<>();
        wed_course = new ArrayList<>();
        thu_course = new ArrayList<>();
        fri_course = new ArrayList<>();
        sat_course = new ArrayList<>();
        sun_course = new ArrayList<>();
        weekCourse = new ArrayList<>();
        weekCourse.add(mon_course);
        weekCourse.add(tue_course);
        weekCourse.add(wed_course);
        weekCourse.add(thu_course);
        weekCourse.add(fri_course);
        weekCourse.add(sat_course);
        weekCourse.add(sun_course);
        XQ_Search_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Search_Info.XQ_list);
        XN_Search_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Search_Info.XN_list);
        xq_course.setAdapter(XQ_Search_adapter);
        xn_course.setAdapter(XN_Search_adapter);
        week_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<CourseBean> now_list = weekCourse.get(i);
                searchItemAdapter = new CourseSearchItemAdapter(context, now_list);
                course_item.setAdapter(searchItemAdapter);
            }
        });
        add_toCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importCourse = new AlertDialog.Builder(context).setIcon(
                        android.R.drawable.btn_star).setTitle("课表导入").setMessage(
                        "确定将当前课表导入课程表咩??").setPositiveButton("是滴",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                if (total_course.size() < 1) {
                                    Toast.makeText(context, "当前没有课表可以导入...", Toast.LENGTH_SHORT).show();
                                } else {
                                    data.clear();
                                    for (CourseBean bean : total_course) {
                                        data.AddCourseInfo(bean);
                                    }
                                    Toast.makeText(context, "导入新课表成功,重新登录刷新课表.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("点错啦", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        importCourse.dismiss();
                    }
                }).create();
                importCourse.show();
            }
        });
        xn_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                XN = Search_Info.XNS[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        xq_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                XQ = Search_Info.XQS[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        search_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody formbody = new FormEncodingBuilder()
                        .add("xnm", XN)
                        .add("xqm", XQ)
                        .build();
                postUtil = new HttpPostUtil(uiHandler, client, URLInfo.COURSE_URL + USER_Info.user_id, formbody, MsgInfo.COURSE_SEARCH_MSG);
                postUtil.start();
            }
        });
        uiHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MsgInfo.UPDATE_COURSE_SEARCH_MSG:
                        List<CourseBean> new_list = (List<CourseBean>) msg.obj;
                        searchItemAdapter = new CourseSearchItemAdapter(context, new_list);
                        course_item.setAdapter(searchItemAdapter);
                        break;
                    case MsgInfo.COURSE_SEARCH_MSG:
                        String course_info = (String) msg.obj;
                        try {
                            Document doc = Jsoup.parse(course_info);
                            String title = doc.getElementsByTag("title").get(0).text();
                            if (title.equals("登录超时")) {
                                Toast.makeText(context, "连接超时,请重新登陆", Toast.LENGTH_SHORT).show();
                            }
                            Intent i = context.getPackageManager()
                                    .getLaunchIntentForPackage(context.getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            for (List<CourseBean> list : weekCourse) {
                                list.clear();
                            }
                            total_course.clear();
                            getCourse_flag = true;
                            ReadJSON(course_info);
                            if (!getCourse_flag) {
                                Toast.makeText(context, "暂无课表可供查询", Toast.LENGTH_SHORT).show();
                            }
                            searchItemAdapter = new CourseSearchItemAdapter(context, mon_course);
                            course_item.setAdapter(searchItemAdapter);
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
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("kbList");
        String course_day;
        String c_name;
        String c_weeks;
        String c_num;
        String c_building;
        String c_place;
        String c_teacher;
        CourseBean courseBean;
        if (jsonArray.length() < 1) {
            getCourse_flag = false;
        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                c_weeks = jsonObject.getString("zcd");
                c_name = jsonObject.getString("kcmc");
                c_num = jsonObject.getString("jcs");
                c_building = jsonObject.getString("xqmc");
                c_place = jsonObject.getString("cdmc");
                c_teacher = jsonObject.getString("xm");
                course_day = jsonObject.getString("xqjmc");
                courseBean = new CourseBean();
                courseBean.setCourseName(c_name);
                courseBean.setCourseNum(c_num);
                courseBean.setCourseBuilding(c_building);
                courseBean.setCoursePlace(c_place);
                courseBean.setCourseTeacher(c_teacher);
                courseBean.setCourseWeeks(c_weeks);
                for (int j = 0; j < 7; j++) {
                    if (course_day.equals(Search_Info.DAY_list[j])) {
                        courseBean.setCourseDay(j + 1);
                        weekCourse.get(j).add(courseBean);
                    }
                }
                total_course.add(courseBean);
            }
        }
    }
}

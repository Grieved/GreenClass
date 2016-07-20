package greenstudio.green_class.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import greenstudio.green_class.CourseDisData.CourseSearchData;
import greenstudio.green_class.Infomation.Search_Info;
import greenstudio.green_class.R;
import greenstudio.green_class.Utils.CourseAdd;

/**
 * Created by wangyu on 7/18/16.
 */
public class Course_Display extends Fragment {

    public static CourseSearchData data;
    /**
     * 第一个无内容的格子
     */
    protected TextView empty;
    /**
     * 星期一的格子
     */
    protected TextView monColum;
    /**
     * 星期二的格子
     */
    protected TextView tueColum;
    /**
     * 星期三的格子
     */
    protected TextView wedColum;
    /**
     * 星期四的格子
     */
    protected TextView thrusColum;
    /**
     * 星期五的格子
     */
    protected TextView friColum;
    /**
     * 星期六的格子
     */
    protected TextView satColum;
    /**
     * 星期日的格子
     */
    protected TextView sunColum;
    /**
     * 课程表body部分布局
     */
    protected RelativeLayout course_table_layout;
    /**
     * 屏幕宽度
     **/
    protected int screenWidth;
    /**
     * 课程格子平均宽度
     **/
    protected int aveWidth;
    List<TextView> course_lists;
    private Context context;
    private Spinner week_spinner;
    private ArrayAdapter<String> week_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View course_dis = inflater.inflate(R.layout.course_dis, null);
        context = getContext();
        course_lists = new ArrayList<>();
        data = new CourseSearchData(context);
        return course_dis;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //获得列头的控件
        week_spinner = (Spinner) view.findViewById(R.id.week_show);
        week_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, Search_Info.Week_list);
        week_spinner.setAdapter(week_adapter);
        empty = (TextView) view.findViewById(R.id.test_empty);
        monColum = (TextView) view.findViewById(R.id.test_monday_course);
        tueColum = (TextView) view.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) view.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) view.findViewById(R.id.test_thursday_course);
        friColum = (TextView) view.findViewById(R.id.test_friday_course);
        satColum = (TextView) view.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) view.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) view.findViewById(R.id.course_rl);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        final int aveWidth = width / 8;
        //第一个空白格子设置为25宽
        empty.setWidth(aveWidth * 3 / 4);
        monColum.setWidth(aveWidth * 33 / 32 + 1);
        tueColum.setWidth(aveWidth * 33 / 32 + 1);
        wedColum.setWidth(aveWidth * 33 / 32 + 1);
        thrusColum.setWidth(aveWidth * 33 / 32 + 1);
        friColum.setWidth(aveWidth * 33 / 32 + 1);
        satColum.setWidth(aveWidth * 33 / 32 + 1);
        sunColum.setWidth(aveWidth * 33 / 32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        int height = dm.heightPixels;
        final int gridHeight = height / 12;
        //设置课表界面
        //动态生成12 * maxCourseNum个textview
        for (int i = 1; i <= 12; i++) {

            for (int j = 1; j <= 8; j++) {

                TextView tx = new TextView(context);
                tx.setId((i - 1) * 8 + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if (j < 8)
                    tx.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.course_text_view_bg));
                else
                    tx.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.course_table_last_colum));
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth * 33 / 32 + 1,
                        gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //字体样式
                tx.setTextAppearance(context, R.style.courseTableText);
//                如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    tx.setText(String.valueOf(i));
                    rp.width = aveWidth * 3 / 4;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                } else {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    tx.setText("");
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }
        week_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for (TextView course : CourseAdd.course_lists) {
                    course.setVisibility(View.GONE);
                }
                course_lists = CourseAdd.addCoursesByWeeks(context, data, i, course_table_layout, aveWidth, gridHeight);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}

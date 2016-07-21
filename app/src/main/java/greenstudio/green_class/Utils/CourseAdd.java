package greenstudio.green_class.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import greenstudio.green_class.CourseDisData.CourseQueryInfo;
import greenstudio.green_class.CourseDisData.CourseSearchData;
import greenstudio.green_class.Infomation.Search_Info;

/**
 * Created by wangyu on 7/20/16.
 */
public class CourseAdd {
    public static List<TextView> course_lists = new ArrayList<>();

    public static List<TextView> addCoursesByWeeks(Context context, CourseSearchData data, int week, RelativeLayout course_table_layout, int aveWidth, int gridHeight) {
        course_lists.clear();
        List<CourseQueryInfo> exportCourses = data.queryWeekCourses(week);
        for (CourseQueryInfo queryInfo : exportCourses) {
            String content = queryInfo.getCourseName() + "\n" + queryInfo.getCoursePlace() + "\n" + queryInfo.getCourseTeacher();
            int begin = queryInfo.getCourseBegin();
            int day = queryInfo.getCourseDay();
            int length = queryInfo.getCourseLength();
            addCourse(context, course_table_layout, aveWidth, gridHeight, content, length, begin, day);
        }
        return null;
    }

    public static void addCourse(Context context, RelativeLayout course_table_layout, int aveWidth, int gridHeight, String content, int length, int begin, int day) {
        TextView courseInfo = new TextView(context);
        course_lists.add(courseInfo);
        courseInfo.setText(content);
        //该textview的高度根据其节数的跨度来设置
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                aveWidth * 31 / 32,
                (gridHeight - 5) * length);
        //textview的位置由课程开始节数和上课的时间（day of week）确定
        rlp.topMargin = 5 + (begin-1) * gridHeight;
        rlp.leftMargin = day;
        // 偏移由这节课是星期几决定
        rlp.addRule(RelativeLayout.RIGHT_OF, day);
        //字体剧中
        courseInfo.setGravity(Gravity.CENTER);
        // 设置一种背景
        Random random = new Random();
        int s = random.nextInt(5);
        courseInfo.setBackgroundResource(Search_Info.background[s]);
        courseInfo.setTextSize(12);
        courseInfo.setLayoutParams(rlp);
        courseInfo.setTextColor(Color.WHITE);
        //设置不透明度
        courseInfo.getBackground().setAlpha(222);
        course_table_layout.addView(courseInfo);
    }
}

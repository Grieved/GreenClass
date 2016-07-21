package greenstudio.green_class.CourseDisData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import greenstudio.green_class.fragments.CourseBean;

/**
 * Created by wangyu on 7/20/16.
 */
public class CourseSearchData {
    private CourseDatabaseHelper mysqlite;
    private CourseImportInfo disInfo;
    private SQLiteDatabase db;

    public CourseSearchData(Context context) {
        mysqlite = new CourseDatabaseHelper(context);
        db = mysqlite.getWritableDatabase();
    }

    public void AddCourseInfo(CourseBean courseBean) {
        disInfo = SearchDataFormat.FormatSearchToDis(courseBean);
        if (disInfo.getWeek_xz() == 2) {
            for (int week = disInfo.getCourseBeginWeek(); week <= disInfo.getCourseEndWeek(); week++) {
                db.execSQL("insert into stuCourses(courseWeek, courseName, coursePlace, courseLength,courseBegin,courseDay,courseTeacher) "
                                + "values(?,?,?,?,?,?,?)",
                        new Object[]{week, disInfo.getCourseName(), disInfo.getCoursePlace(), disInfo.getCourseLength(),
                                disInfo.getCourseBegin(), disInfo.getCourseDay(), disInfo.getCourseTeacher()});
            }
        } else {
            for (int week = disInfo.getCourseBeginWeek(); week <= disInfo.getCourseEndWeek(); week++) {
                if (week % 2 == disInfo.getWeek_xz()) {
                    db.execSQL("insert into stuCourses(courseWeek, courseName, coursePlace, courseLength,courseBegin,courseDay,courseTeacher) "
                                    + "values(?,?,?,?,?,?,?)",
                            new Object[]{week, disInfo.getCourseName(), disInfo.getCoursePlace(), disInfo.getCourseLength(),
                                    disInfo.getCourseBegin(), disInfo.getCourseDay(), disInfo.getCourseTeacher()});
                }
            }
        }
    }

    public Cursor queryByWeek(int week) {
        Cursor c = db.rawQuery("SELECT * FROM stuCourses WHERE courseWeek = ?", new String[]{String.valueOf(week)});
        return c;
    }

    public List<CourseQueryInfo> queryWeekCourses(int week) {
        List<CourseQueryInfo> weekCourses = new ArrayList<>();
        Cursor c = queryByWeek(week);
        while (c.moveToNext()) {
            CourseQueryInfo queryInfo = new CourseQueryInfo();
            queryInfo.setCourseDay(c.getInt(c.getColumnIndex("courseDay")));
            queryInfo.setCourseBegin(c.getInt(c.getColumnIndex("courseBegin")));
            queryInfo.setCourseTeacher(c.getString(c.getColumnIndex("courseTeacher")));
            queryInfo.setCourseLength(c.getInt(c.getColumnIndex("courseLength")));
            queryInfo.setCourseName(c.getString(c.getColumnIndex("courseName")));
            queryInfo.setCoursePlace(c.getString(c.getColumnIndex("coursePlace")));
            weekCourses.add(queryInfo);
        }

        c.close();
        return weekCourses;
    }

    public void clear() {
        db.execSQL("delete from stuCourses");
    }

    public void closedb() {
        db.close();
    }


}

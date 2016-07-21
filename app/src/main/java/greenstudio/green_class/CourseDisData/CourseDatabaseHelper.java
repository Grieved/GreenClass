package greenstudio.green_class.CourseDisData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangyu on 7/20/16.
 */
public class CourseDatabaseHelper extends SQLiteOpenHelper {

    final String CREATE_SQL = "create table if not exists stuCourses(courseWeek integer,courseName text,coursePlace text,courseLength integer,courseBegin integer,courseDay integer , courseTeacher text)";

    public CourseDatabaseHelper(Context context) {
        super(context, "stuCourses.db", null, 1);
    }


    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

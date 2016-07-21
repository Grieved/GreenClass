package greenstudio.green_class.CourseDisData;

/**
 * Created by wangyu on 7/20/16.
 */
public class CourseImportInfo {
    private int courseBeginWeek;
    private int courseEndWeek;
    private int week_xz;//表示单双周 双周0 单周1 全周2
    private String courseName;
    private String coursePlace;
    private int courseLength;
    private int courseBegin;
    private int courseDay;
    private String courseTeacher;

    public int getWeek_xz() {
        return week_xz;
    }

    public void setWeek_xz(int week_xz) {
        this.week_xz = week_xz;
    }

    public int getCourseLength() {
        return courseLength;
    }

    public void setCourseLength(int courseLength) {
        this.courseLength = courseLength;
    }

    public int getCourseBeginWeek() {
        return courseBeginWeek;
    }

    public void setCourseBeginWeek(int courseBeginWeek) {
        this.courseBeginWeek = courseBeginWeek;
    }

    public int getCourseEndWeek() {
        return courseEndWeek;
    }

    public void setCourseEndWeek(int courseEndWeek) {
        this.courseEndWeek = courseEndWeek;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoursePlace() {
        return coursePlace;
    }

    public void setCoursePlace(String coursePlace) {
        this.coursePlace = coursePlace;
    }

    public int getCourseBegin() {
        return courseBegin;
    }

    public void setCourseBegin(int courseBegin) {
        this.courseBegin = courseBegin;
    }

    public int getCourseDay() {
        return courseDay;
    }

    public void setCourseDay(int courseDay) {
        this.courseDay = courseDay;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

}

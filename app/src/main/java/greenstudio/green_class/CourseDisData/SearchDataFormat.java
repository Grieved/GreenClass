package greenstudio.green_class.CourseDisData;

import greenstudio.green_class.fragments.CourseBean;

/**
 * Created by wangyu on 7/20/16.
 */
public class SearchDataFormat {
    public static CourseImportInfo FormatSearchToDis(CourseBean courseBean) {
        CourseImportInfo disInfo;
        disInfo = new CourseImportInfo();
        String courseWeeks = courseBean.getCourseWeeks();
        String courseNum = courseBean.getCourseNum();
        int BeginWeek = Integer.parseInt(courseWeeks.substring(0, courseWeeks.indexOf("-")));
        int EndWeek = Integer.parseInt(courseWeeks.substring(courseWeeks.indexOf("-") + 1, courseWeeks.indexOf("周")));
        String weekFlag = courseWeeks.substring(courseWeeks.indexOf("周") + 1, courseWeeks.length());
        int BeginClass = Integer.parseInt(courseNum.substring(0, courseNum.indexOf("-")));
        int EndClass = Integer.parseInt(courseNum.substring(courseNum.indexOf("-") + 1, courseNum.length()));
        disInfo.setCourseName(courseBean.getCourseName());
        disInfo.setCoursePlace("@" + courseBean.getCourseBuilding() + "\n" + courseBean.getCoursePlace());
        disInfo.setCourseTeacher(courseBean.getCourseTeacher());
        disInfo.setCourseBeginWeek(BeginWeek);
        disInfo.setCourseEndWeek(EndWeek);
        disInfo.setCourseLength(EndClass - BeginClass + 1);
        disInfo.setCourseBegin(BeginClass);
        disInfo.setCourseDay(courseBean.getCourseDay());
        if (weekFlag.equals("")) {
            disInfo.setWeek_xz(2);
        } else if (weekFlag.equals("(单)")) {
            disInfo.setWeek_xz(1);
        } else if (weekFlag.equals("(双)")) {
            disInfo.setWeek_xz(0);
        }
        return disInfo;
    }
}

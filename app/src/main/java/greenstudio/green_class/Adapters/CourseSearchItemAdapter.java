package greenstudio.green_class.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import greenstudio.green_class.R;
import greenstudio.green_class.fragments.CourseBean;

public class CourseSearchItemAdapter extends BaseAdapter {

    private List<CourseBean> objects;

    private LayoutInflater layoutInflater;

    public CourseSearchItemAdapter(Context context, List<CourseBean> objects) {
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public CourseBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.course_search_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((CourseBean) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(CourseBean bean, ViewHolder holder) {
        //TODO implement
        holder.courseName.setText(bean.getCourseName());
        holder.courseBuilding.setText(bean.getCourseBuilding());
        holder.courseNum.setText(bean.getCourseNum());
        holder.coursePlace.setText(bean.getCoursePlace());
        holder.courseTeacher.setText(bean.getCourseTeacher());
        holder.courseWeeks.setText(bean.getCourseWeeks());

    }

    protected class ViewHolder {
        private TextView courseName;
        private TextView courseWeeks;
        private TextView courseNum;
        private TextView courseBuilding;
        private TextView coursePlace;
        private TextView courseTeacher;

        public ViewHolder(View view) {
            courseName = (TextView) view.findViewById(R.id.course_name);
            courseWeeks = (TextView) view.findViewById(R.id.course_weeks);
            courseNum = (TextView) view.findViewById(R.id.course_num);
            courseBuilding = (TextView) view.findViewById(R.id.course_building);
            coursePlace = (TextView) view.findViewById(R.id.course_place);
            courseTeacher = (TextView) view.findViewById(R.id.course_teacher);
        }
    }
}

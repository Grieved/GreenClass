package greenstudio.green_class.fragments;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import greenstudio.green_class.R;

/**
 * Created by wangyu on 7/18/16.
 */
public class Grade_Adapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Map<String, String>> group_list;
    private List<List<Map<String, String>>> item_list;

    public Grade_Adapter(Context context, List<Map<String, String>> group_list, List<List<Map<String, String>>> item_list) {
        this.context = context;
        this.group_list = group_list;
        this.item_list = item_list;
    }

    /**
     * 获取组的个数
     *
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupCount()
     */
    @Override
    public int getGroupCount() {
        return group_list.size();
    }

    /**
     * 获取指定组中的子元素个数
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildrenCount(int)
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return item_list.get(groupPosition).size();
    }

    /**
     * 获取指定组中的数据
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroup(int)
     */
    @Override
    public Object getGroup(int groupPosition) {
        return group_list.get(groupPosition);
    }

    /**
     * 获取指定组中的指定子元素数据。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChild(int, int)
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return item_list.get(groupPosition).get(childPosition);
    }

    /**
     * 获取指定组的ID，这个组ID必须是唯一的
     *
     * @param groupPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupId(int)
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取指定组中的指定子元素ID
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#getChildId(int, int)
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 组和子元素是否持有稳定的ID,也就是底层数据的改变不会影响到它们。
     *
     * @return
     * @see android.widget.ExpandableListAdapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 获取显示指定组的视图对象
     *
     * @param groupPosition 组位置
     * @param isExpanded    该组是展开状态还是伸缩状态
     * @param convertView   重用已有的视图对象
     * @param parent        返回的视图对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getGroupView(int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grade_group, null);
            groupHolder = new GroupHolder();
            groupHolder.courseName = (TextView) convertView.findViewById(R.id.course_name);
            groupHolder.courseGrade = (TextView) convertView.findViewById(R.id.course_grade);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.courseName.setText(group_list.get(groupPosition).get("name"));
        groupHolder.courseGrade.setText(group_list.get(groupPosition).get("grade"));
        return convertView;
    }

    /**
     * 获取一个视图对象，显示指定组中的指定子元素数据。
     *
     * @param groupPosition 组位置
     * @param childPosition 子元素位置
     * @param isLastChild   子元素是否处于组中的最后一个
     * @param convertView   重用已有的视图(View)对象
     * @param parent        返回的视图(View)对象始终依附于的视图组
     * @return
     * @see android.widget.ExpandableListAdapter#getChildView(int, int, boolean, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grade_child, null);
            itemHolder = new ItemHolder();
            itemHolder.courseGpa = (TextView) convertView.findViewById(R.id.course_gpa);
            itemHolder.courseXF = (TextView) convertView.findViewById(R.id.courde_xf);
            itemHolder.courseXZ = (TextView) convertView.findViewById(R.id.course_xz);
            itemHolder.courseDm = (TextView) convertView.findViewById(R.id.course_dm);
            itemHolder.courseXy = (TextView) convertView.findViewById(R.id.course_xy);
            itemHolder.ksXz = (TextView) convertView.findViewById(R.id.ksxz);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.courseGpa.setText(item_list.get(groupPosition).get(childPosition).get("gpa"));
        itemHolder.courseXF.setText(item_list.get(groupPosition).get(childPosition).get("xf"));
        itemHolder.courseXZ.setText(item_list.get(groupPosition).get(childPosition).get("xz"));
        itemHolder.courseDm.setText(item_list.get(groupPosition).get(childPosition).get("dm"));
        itemHolder.courseXy.setText(item_list.get(groupPosition).get(childPosition).get("xy"));
        itemHolder.ksXz.setText(item_list.get(groupPosition).get(childPosition).get("ksxz"));
        return convertView;
    }

    /**
     * 是否选中指定位置上的子元素。
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see android.widget.ExpandableListAdapter#isChildSelectable(int, int)
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}

class GroupHolder {
    public TextView courseName;
    public TextView courseGrade;
}

class ItemHolder {
    public TextView courseXZ;
    public TextView courseGpa;
    public TextView courseXF;
    public TextView courseDm;
    public TextView courseXy;
    public TextView ksXz;
}

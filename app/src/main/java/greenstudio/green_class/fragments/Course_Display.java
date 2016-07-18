package greenstudio.green_class.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import greenstudio.green_class.R;

/**
 * Created by wangyu on 7/18/16.
 */
public class Course_Display extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View course_dis = inflater.inflate(R.layout.course_dis, null);
        return course_dis;
    }
}

package greenstudio.green_class.fragments;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import greenstudio.green_class.Infomation.USER_Info;
import greenstudio.green_class.R;

/**
 * Created by wangyu on 7/18/16.
 */
public class Course_Search extends Fragment {
    private ImageView imageView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                imageView.setImageBitmap(USER_Info.user_header);
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View course_search = inflater.inflate(R.layout.course_search, null);
        final Button button = (Button) course_search.findViewById(R.id.hh);
        imageView = (ImageView) course_search.findViewById(R.id.imageView11);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }

        });
        return course_search;
    }
}

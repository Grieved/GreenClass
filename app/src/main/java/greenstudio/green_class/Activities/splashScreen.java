package greenstudio.green_class.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import greenstudio.green_class.R;
import greenstudio.green_class.Utils.AppManager;

/**
 * Created by W on 2016/7/17.
 */
public class splashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_layout);
        AppManager.getAppManager().addActivity(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }

}

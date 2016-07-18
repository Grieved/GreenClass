package greenstudio.green_class.Utils;

import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import greenstudio.green_class.Infomation.MsgInfo;

/**
 * Created by wangyu on 7/18/16.
 */
public class HttpGetUtil extends Thread {

    private Handler handler;
    private OkHttpClient client;
    private String Url;
    private int msg;

    public HttpGetUtil(Handler handler, OkHttpClient client, String url, int msg) {
        this.handler = handler;
        this.client = client;
        Url = url;
        this.msg = msg;
        postAsyn();
    }

    @Override
    public void run() {
        postAsyn();
    }

    public void postAsyn() {
        Request request = new Request.Builder().url(Url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(MsgInfo.ERROR_MSG);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message message = handler.obtainMessage();
                message.obj = response.body().string();
                message.what = msg;
                message.sendToTarget();
            }
        });
    }
}

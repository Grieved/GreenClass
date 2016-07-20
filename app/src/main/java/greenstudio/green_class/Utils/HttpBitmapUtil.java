package greenstudio.green_class.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

import greenstudio.green_class.Infomation.MsgInfo;

public class HttpBitmapUtil extends Thread {

    private Handler handler;
    private OkHttpClient client;
    private String Url;
    private int msg;

    public HttpBitmapUtil(Handler handler, OkHttpClient client, String url, int msg) {
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
                InputStream is = response.body().byteStream();
                Bitmap bm = BitmapFactory.decodeStream(is);
                Message message = handler.obtainMessage();
                message.obj = bm;
                message.what = msg;
                message.sendToTarget();
            }
        });
    }
}

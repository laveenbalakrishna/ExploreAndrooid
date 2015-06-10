package com.example.laveen.exploreandrooid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;



public class TestingActivity extends ActionBarActivity {

    Button idleButton;
    Downloader task;
    ImageView Iview;
    ImageView circularView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);


        idleButton = (Button) findViewById(R.id.idle_button);
        Iview = (ImageView) findViewById(R.id.image);
        circularView = (ImageView) findViewById(R.id.imageView);

        task = new Downloader(Iview);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://i.ndtvimg.com/i/2015-06/ashton-carter-carter-reuters-240_240x180_81433398015.jpg");

        InputStream stream;
        //  stream.re
    }

    public void onActiveClick(View view) {
        idleButton.performClick();

    }

    public void onIdleClick(View view) {
        task = new Downloader(Iview);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://i.ndtvimg.com/i/2015-06/ashton-carter-carter-reuters-240_240x180_81433398015.jpg");
    }

    static class Downloader extends AsyncTask<String, Void, Bitmap> {
        WeakReference<ImageView> ref;

        public Downloader(ImageView view) {
            ref = new WeakReference<>(view);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (ref != null && bitmap != null && ref.get() != null)
                ref.get().setImageBitmap(bitmap);
        }

        private Bitmap downloadImage(String url) {
            Log.d("TREAD", "NAME: " + Thread.currentThread().getName());
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            HttpGet request = new HttpGet(url);
            HttpResponse response;
            Bitmap bitmap = null;
            InputStream stream = null;
            try {
                response = client.execute(request);
                stream = response.getEntity().getContent();
                bitmap = BitmapFactory.decodeStream(stream);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                client.close();
            }
            return bitmap;
        }
    }


}

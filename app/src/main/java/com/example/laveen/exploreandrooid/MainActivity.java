package com.example.laveen.exploreandrooid;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


public class MainActivity extends ActionBarActivity {

    RecyclerView list;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
    }

    public void doLongTask(View view) {
        new DownloadTask(textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new TaskInfo("LONG TASK COMPLETE", 60000));
    }

    public void doShortTask(View view) {
        new DownloadTask(textView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new TaskInfo("SHORT TASK COMPLETE", 30000));
    }

    static class TaskInfo {
        String data;
        Integer duration;

        public TaskInfo(String ms, int time) {
            data = ms;
            duration = time;
        }
    }

    static class DownloadTask extends AsyncTask<TaskInfo, Void, TaskInfo> {
        WeakReference<TextView> view;

        public DownloadTask(TextView view1) {
            view = new WeakReference<>(view1);
        }

        @Override
        protected TaskInfo doInBackground(TaskInfo... integers) {
            try {
                Thread.sleep(integers[0].duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return integers[0];
        }

        @Override
        protected void onPostExecute(TaskInfo aVoid) {
            TextView view1 = view.get();
            view1.setText(aVoid.data);
        }
    }

}

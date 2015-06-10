package com.example.laveen.exploreandrooid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class C extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
    }

    public void launchSingleTask(View v){
        Intent intent = new Intent(this,SingleTask.class);
        startActivity(intent);

    }

}

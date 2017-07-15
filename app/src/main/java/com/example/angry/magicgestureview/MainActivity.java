package com.example.angry.magicgestureview;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String TAG = "MAgic_Act";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MagicGestureView m = new MagicGestureView(this);
        ArrayList<View> data = new ArrayList<>();

        for (int i=0;i<10;i++){
            TextView v = new TextView(this);
            v.setText("" + i);
            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            data.add(v);
        }
        Log.d(TAG, "onCreate: size = " + data.size());

        m.setBackgroundColor(Color.RED);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        m.setLayoutParams(lp);
        m.init(this,3,3,data);

        LinearLayout ll = (LinearLayout) findViewById(R.id.main_ll);
        ll.setGravity(Gravity.CENTER);
        ll.addView(m);

    }
}

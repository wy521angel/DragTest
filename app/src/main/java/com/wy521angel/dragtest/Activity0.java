package com.wy521angel.dragtest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by wy521angel on 16/8/1.
 */
public class Activity0 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int label = getIntent().getIntExtra("label", -1);
        switch (label) {
            case 0:
                setContentView(R.layout.activity_for_listener);
                break;
            case 1:
                setContentView(R.layout.activity_for_helper);
                break;
        }
    }
}

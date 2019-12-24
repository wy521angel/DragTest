package com.wy521angel.dragtest;

import android.app.Activity;
import android.os.Bundle;

public class DragListenerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int label = getIntent().getIntExtra("label", -1);
        switch (label) {
            case 0:
                setContentView(R.layout.activity_for_listener_demo0);
                break;
            case 1:
                setContentView(R.layout.drag_to_collect);
                break;
        }
    }
}


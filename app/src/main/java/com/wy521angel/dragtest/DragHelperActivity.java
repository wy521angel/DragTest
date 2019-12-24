package com.wy521angel.dragtest;

import android.app.Activity;
import android.os.Bundle;

public class DragHelperActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int label = getIntent().getIntExtra("label", -1);
        switch (label) {
            case 0:
                setContentView(R.layout.activity_for_helper_demo);
                break;
            case 1:
                setContentView(R.layout.drag_up_down);
                break;
            case 2:
                setContentView(R.layout.activity_for_simple_drag_menu);
                break;
//            case 2:
//                setContentView(R.layout.activity_for_drag2);
//                break;
        }

    }
}


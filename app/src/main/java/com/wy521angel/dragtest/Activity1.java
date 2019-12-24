package com.wy521angel.dragtest;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by wy521angel on 16/8/2.
 */
public class Activity1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int label = getIntent().getIntExtra("label", -1);
        switch (label) {
            case 0:
                setContentView(R.layout.activity_for_helper_demo0);
                break;
//            case 0:
//                setContentView(R.layout.activity_for_drag0);
//                break;
//            case 1:
//                setContentView(R.layout.activity_for_drag1);
//                break;
//            case 2:
//                setContentView(R.layout.activity_for_drag2);
//                break;
        }

    }
}


package com.wy521angel.dragtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DragHelperActivity extends Activity implements View.OnClickListener {
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
            case 3:
                setContentView(R.layout.activity_for_drag_3_textview);
                break;
            case 4:
                setContentView(R.layout.activity_for_drag_3_button);
                findViewById(R.id.btn0).setOnClickListener(this);
                findViewById(R.id.btn1).setOnClickListener(this);
                findViewById(R.id.btn2).setOnClickListener(this);
                findViewById(R.id.btn3).setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn0:
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
                Toast.makeText(DragHelperActivity.this, ((Button) v).getText().toString(),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}


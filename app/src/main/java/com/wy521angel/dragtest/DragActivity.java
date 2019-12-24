package com.wy521angel.dragtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by wy521angel on 16/8/1.
 */
public class DragActivity extends Activity {

    private String[] labels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int label = getIntent().getIntExtra("label", -1);
        setContentView(R.layout.activity_for_drag);
        switch (label) {
            case 0:
                labels = new String[]{"OnDragListener的简单拖拽",
                        "OnDragListener拖拽并传递数据（类似购物车、删除应用的拖拽效果）"};
                break;
            case 1:
                labels = new String[]{"ViewDragHelper的类似dragListener拖拽（没有完成位移）",
                        "ViewDragHelper方法简单实例",
                        "ViewDragHelper详解",
                        "ViewDragHelper使用Button测试",
                        "ViewDragHelper实战，自己打造DrawerLayout"};
                break;
        }
        ListView demoList = findViewById(R.id.demoList);
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, labels);
        demoList.setAdapter(adapter);
        demoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (label) {
                    case 0:
                        gotoListenerDemoPage(position);
                        break;
                    case 1:
                        gotoHelperDemoPage(position);
                        break;
                }
            }
        });

    }

    private void gotoListenerDemoPage(int position) {
        int value = -1;
        switch (position) {
            case 0:
                value = 0;
                break;
            case 1:
                value = 1;
                break;
        }
        startActivity(new Intent(this, DragListenerActivity.class).putExtra("label", value));
    }

    private void gotoHelperDemoPage(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, Activity1.class).putExtra("label", 0));
                break;
            case 1:
                startActivity(new Intent(this, Activity1.class).putExtra("label", 1));
                break;
            case 2:
                startActivity(new Intent(this, Activity1.class).putExtra("label", 2));
                break;
            case 3:
                startActivity(new Intent(this, Activity1.class).putExtra("label", 3));
                break;
            case 4:
                startActivity(new Intent(this, Activity1.class));
                break;
        }
    }
}

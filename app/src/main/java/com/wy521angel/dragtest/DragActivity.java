package com.wy521angel.dragtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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
                        "ViewDragHelper的上下拖拽及惯性拖拽测试",
                        "ViewDragHelper侧滑菜单简单实例",
                        "ViewDragHelper的3个TextView功能演示",
                        "ViewDragHelper的3个TextView替换Button测试",
                        "ViewDragHelper编写DrawerLayout"};
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
        startActivity(new Intent(this, DragListenerActivity.class).putExtra("label", position));
    }

    private void gotoHelperDemoPage(int position) {
        startActivity(new Intent(this, DragHelperActivity.class).putExtra("label", position));
    }
}

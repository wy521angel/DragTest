package com.wy521angel.dragtest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

public class LeftMenuFragment extends ListFragment {

    private static final int SIZE_MENU_ITEM = 3;
    private MenuItem[] mItems = new MenuItem[SIZE_MENU_ITEM];
    private LeftMenuAdapter mAdapter;

    private LayoutInflater mInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = LayoutInflater.from(getActivity());
        MenuItem menuItem = null;
        for (int i = 0; i < SIZE_MENU_ITEM; i++) {
            menuItem = new MenuItem(getResources().getStringArray(R.array.array_left_menu)[i], false, R.drawable.music_36px, R.drawable.music_36px_light);
            mItems[i] = menuItem;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mMenuItemSelectedListener != null) {
            mMenuItemSelectedListener.menuItemSelected(((MenuItem) getListAdapter().getItem(position)).text);
        }
        mAdapter.setSelected(position);
    }

    //选择回调的接口
    public interface OnMenuItemSelectedListener {
        void menuItemSelected(String title);
    }

    private OnMenuItemSelectedListener mMenuItemSelectedListener;

    public void setOnMenuItemSelectedListener(OnMenuItemSelectedListener menuItemSelectedListener) {
        this.mMenuItemSelectedListener = menuItemSelectedListener;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(0xffffffff);
        setListAdapter(mAdapter = new LeftMenuAdapter(getActivity(), mItems));
    }

    public class MenuItem {
        public MenuItem(String text, boolean isSelected, int icon, int iconSelected) {
            this.text = text;
            this.isSelected = isSelected;
            this.icon = icon;
            this.iconSelected = iconSelected;
        }

        boolean isSelected;
        String text;
        int icon;
        int iconSelected;
    }

    public class LeftMenuAdapter extends ArrayAdapter<MenuItem> {
        private LayoutInflater mInflater;
        private int mSelected;

        public LeftMenuAdapter(Context context, MenuItem[] objects) {
            super(context, -1, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_left_menu, parent, false);
            }

            ImageView iv = (ImageView) convertView.findViewById(R.id.id_item_icon);
            TextView title = (TextView) convertView.findViewById(R.id.id_item_title);
            title.setText(getItem(position).text);
            iv.setImageResource(getItem(position).icon);
            convertView.setBackgroundColor(Color.TRANSPARENT);

            if (position == mSelected) {
                iv.setImageResource(getItem(position).iconSelected);
                convertView.setBackgroundColor(Color.parseColor("#44009688"));
            }
            return convertView;
        }

        public void setSelected(int position) {
            this.mSelected = position;
            notifyDataSetChanged();
        }
    }
}

package com.example.leeseonwoo.ycc3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter{
    List<ListViewItem> listViewItemList = new ArrayList<>();
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItem(int foodImage, String foodName) {
        ListViewItem item = new ListViewItem();
        item.setFood_image(foodImage);
        item.setFood_name(foodName);

        listViewItemList.add(item);
    }



    public void removeItem (ListViewItem item){
        listViewItemList.remove(item);
    }

    @Override //ListView에 뿌려질 item을 제공한다.
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }

        //현재 선택된 View에 연결되는 객체 생성
        ImageView food_image = (ImageView)view.findViewById(R.id.food_image);
        TextView food_name = (TextView)view.findViewById(R.id.food_name);
        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.layout_bg);
        food_image.setBackground(drawable);
        food_image.setClipToOutline(true);

        ListViewItem item = listViewItemList.get(i);
        //DBHelper = new DatabaseOpenHelper(context);
        //db = DBHelper.getWritableDatabase();

        //현재 선택된 View에 데이터 삽입
        food_image.setImageResource(item.getFood_image());
        food_name.setText(item.getFood_name());

        return view;

    }
    public void clear(){
        listViewItemList.clear();
    }


}

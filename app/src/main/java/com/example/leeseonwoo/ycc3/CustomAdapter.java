package com.example.leeseonwoo.ycc3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

    public void addItem(int foodImage, String foodName, String ID) {
        ListViewItem item = new ListViewItem();
        item.setFood_image(foodImage);
        item.setFood_name(foodName);
        item.setID(ID);

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
        final ImageView book_mark = (ImageView)view.findViewById(R.id.book_mark);
        TextView back = (TextView)view.findViewById(R.id.textView16);
        FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.list_frame);
        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.layout_bg);
        frameLayout.setBackground(drawable);
        frameLayout.setClipToOutline(true);
        final ListViewItem item = listViewItemList.get(i);
        DBHelper = new DatabaseOpenHelper(context);
        db = DBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select food_name from Bookmark where ID = '"+item.getID()+"' and food_name = '"+item.getFood_name()+"'",null);
        if(cursor.getCount()!=0){
            book_mark.setImageResource(R.drawable.btn_star_on);
        }
        else{
            book_mark.setImageResource(R.drawable.btn_star_off);
        }
        cursor.close();
        book_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper = new DatabaseOpenHelper(context);
                db = DBHelper.getWritableDatabase();
                Cursor cursor = db.rawQuery("select food_name from Bookmark where ID = '"+item.getID()+"' and food_name = '"+item.getFood_name()+"'",null);
                if(cursor.getCount()!=0){
                    db.execSQL("delete from Bookmark where ID = '"+item.getID()+"' and food_name = '"+item.getFood_name()+"'");
                    book_mark.setImageResource(R.drawable.btn_star_off);
                }
                else if(!item.getID().equals("unknown")){
                    db.execSQL("insert into Bookmark (ID, food_name, ImgID) VALUES ('"+item.getID()+"', '"+item.getFood_name()+"', "+item.getFood_image()+")");
                    book_mark.setImageResource(R.drawable.btn_star_on);
                }
                else {
                    Toast.makeText(context, "즐겨찾기를 하려면 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

        Bitmap orgImage = BitmapFactory.decodeResource(context.getResources(), item.getFood_image());
        Bitmap color = Bitmap.createScaledBitmap(orgImage, 1, 1, true);
        int rgb = color.getPixel(0,0);
        int alpha = Color.alpha(rgb);
        int red = Color.red(rgb);
        int green = Color.green(rgb);
        int blue = Color.blue(rgb);
        Log.d("back color",String.valueOf(alpha)+", "+String.valueOf(red)+", "+String.valueOf(green)+", "+String.valueOf(blue));
        back.setBackgroundColor(Color.rgb(red,green,blue));
        food_image.setImageResource(item.getFood_image());
        food_name.setText(item.getFood_name());
        db.close();
        return view;

    }
    public void clear(){
        listViewItemList.clear();
    }
}

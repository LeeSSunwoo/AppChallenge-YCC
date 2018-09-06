package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class FoodListActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;

    CustomAdapter customAdapter = new CustomAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        int colorText = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
        ActionBar bar = getSupportActionBar();
        Spannable text = new SpannableString(bar.getTitle());
        text.setSpan(new ForegroundColorSpan(colorText), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        bar.setTitle(text);
        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();
        customAdapter.clear();
        ListView listView = (ListView)findViewById(R.id.listview);
        Intent food_type = getIntent();
        String type = food_type.getStringExtra("type");

        Cursor cursor = db.rawQuery("select * from FoodDATA where type = '"+type+"'",null);
        for(int i = 0;i<cursor.getCount();i++){
            cursor.moveToNext();
            customAdapter.addItem(cursor.getInt(cursor.getColumnIndex("ImgID")),cursor.getString(cursor.getColumnIndex("food_name")));
        }
        customAdapter.notifyDataSetChanged();
        listView.setAdapter(customAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), FoodActivity.class);
                intent.putExtra("food_name",item.getFood_name());
                startActivity(intent);
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                customAdapter.clear();//toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

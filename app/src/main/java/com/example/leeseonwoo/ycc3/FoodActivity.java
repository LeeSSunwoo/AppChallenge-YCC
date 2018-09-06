package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FoodActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        int colorText = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
        ActionBar bar = getSupportActionBar();
        Spannable text = new SpannableString(bar.getTitle());
        text.setSpan(new ForegroundColorSpan(colorText), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        bar.setTitle(text);

        Intent intent = getIntent();
        String food_name = intent.getStringExtra("food_name");

        TextView food = (TextView)findViewById(R.id.textView34);
        TextView main = (TextView)findViewById(R.id.textView37);
        Button btn = (Button)findViewById(R.id.button5);

        final Cursor cursor = db.rawQuery("select * from FoodDATA where food_name = '"+food_name+"'",null);
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndex("food_name"));
        food.setText(name);
        String _main = cursor.getString(cursor.getColumnIndex("material"));
        String[] ss = _main.split(", ");
        _main = "";
        for(String wo : ss){
            _main = _main + wo+"\n";
        }
        main.setText(_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),RecipeActivity.class);
                intent1.putExtra("food_name", name);
                intent1.putExtra("number",0);
                startActivity(intent1);
                cursor.close();
                finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

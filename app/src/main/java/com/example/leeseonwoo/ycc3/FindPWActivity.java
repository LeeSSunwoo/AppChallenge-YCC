package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPWActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        final EditText edit_id = (EditText)findViewById(R.id.edit_ID);
        final EditText edit_birth = (EditText)findViewById(R.id.edit_birth);
        Button btn = (Button)findViewById(R.id.find_id);
        Button btn_sub = (Button)findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindIDActivity.class);
                startActivity(intent);
            }
        });

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edit_id.getText().toString();
                String birth = edit_birth.getText().toString();
                if(!id.isEmpty() && !birth.isEmpty()) {
                    Cursor cursor = db.rawQuery("select * from UserDATA where ID = '"+id+"'and Number = '"+birth+"'",null);
                    cursor.moveToFirst();
                    if(cursor.getCount()==1) {
                        Intent intent = new Intent(getApplicationContext(), ResetpwActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        cursor.close();
                        finish();
                    }
                    else Toast.makeText(FindPWActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(FindPWActivity.this, "내용을 전부 기입해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

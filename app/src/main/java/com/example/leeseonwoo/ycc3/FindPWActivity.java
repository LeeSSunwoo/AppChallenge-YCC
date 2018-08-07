package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();



        final EditText edit_id = (EditText)findViewById(R.id.edit_ID);
        final EditText edit_email = (EditText)findViewById(R.id.edit_email);
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
                String email = edit_email.getText().toString();
                String birth = edit_birth.getText().toString();
                if(!id.isEmpty() && !email.isEmpty() && !birth.isEmpty()) {
                    Cursor cursor = db.rawQuery("select * from UserDATA where ID = '"+id+"' and Email = '"+email+"' and Number = '"+birth+"'",null);
                    cursor.moveToFirst();
                    if(cursor.getCount()==1) {
                        Intent intent = new Intent(getApplicationContext(), ResetpwActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
                    }
                    else Toast.makeText(FindPWActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(FindPWActivity.this, "내용을 전부 기입해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
}
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetpwActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        int colorText = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
        ActionBar bar = getSupportActionBar();
        Spannable text = new SpannableString(bar.getTitle());
        text.setSpan(new ForegroundColorSpan(colorText), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        bar.setTitle(text);

        final EditText edit_PW = (EditText) findViewById(R.id.resetPW);
        final EditText edit_ch = (EditText) findViewById(R.id.PWcheck);
        Button btn_reset = (Button) findViewById(R.id.reset);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PW = edit_PW.getText().toString();
                String PW2 = edit_ch.getText().toString();
                if(!PW.isEmpty() && !PW2.isEmpty()) {
                    if (PW.equals(PW2)) {
                        db.execSQL("update UserDATA set Password = '" + PW + "' where ID = '" + id + "'");
                        Toast.makeText(ResetpwActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(ResetpwActivity.this, "일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ResetpwActivity.this, "내용을 전부 기입해주세요.", Toast.LENGTH_SHORT).show();
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

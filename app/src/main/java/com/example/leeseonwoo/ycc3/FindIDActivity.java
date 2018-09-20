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
import android.widget.TextView;
import android.widget.Toast;

public class FindIDActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        final EditText editName = (EditText)findViewById(R.id.name_edit);
        final EditText editBirth = (EditText)findViewById(R.id.birth);
        Button btn = (Button)findViewById(R.id.button);
        final TextView result = (TextView)findViewById(R.id.result);
        Button btn2 = (Button)findViewById(R.id.button4);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindPWActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _name = editName.getText().toString();
                String number = editBirth.getText().toString();
                if(!_name.isEmpty() && !number.isEmpty()){
                    Cursor cursor = db.rawQuery("select * from UserDATA where Name = '"+_name+"'and Number = '"+number+"'",null);
                    cursor.moveToFirst();
                    if(cursor.getCount() == 1){
                        String name = "당신의 아이디는 "+cursor.getString(cursor.getColumnIndex("ID"))+" 입니다.";
                        result.setText(name);
                        cursor.close();
                    }
                    else Toast.makeText(FindIDActivity.this, "회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FindIDActivity.this, "내용을 전부 기입해주세요.", Toast.LENGTH_SHORT).show();
                }
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

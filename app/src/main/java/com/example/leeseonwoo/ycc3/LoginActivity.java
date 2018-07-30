package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();
        //Reset();
        Cursor loginState = db.rawQuery("select * from UserDATA where Login = '"+true+"'",null);
        if(loginState.getCount() == 1){
            loginState.moveToFirst();
            Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
            intent.putExtra("gender", loginState.getInt(loginState.getColumnIndex("gender")));
            intent.putExtra("name", loginState.getString(loginState.getColumnIndex("Name")));
            intent.putExtra("email",loginState.getString(loginState.getColumnIndex("Email")));
            intent.putExtra("ID",loginState.getString(loginState.getColumnIndex("ID")));
            startActivity(intent);
            finish();
        }

        final Button login = (Button) findViewById(R.id.login_btn);
        Button register = (Button) findViewById(R.id.register_btn);
        final EditText ID = (EditText) findViewById(R.id.editText);
        final EditText PW = (EditText) findViewById(R.id.editText2);
        Button resetBtn = (Button)findViewById(R.id.btn_reset);
        final CheckBox checkBox = (CheckBox)findViewById(R.id.usinglogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = ID.getText().toString();
                String pw = PW.getText().toString();
                Cursor cursor = db.rawQuery("select * from UserDATA where ID = '" + id + "'", null);
                if (cursor.getCount() == 1) {
                    cursor.moveToFirst();

                    if (cursor.getString(cursor.getColumnIndex("Password")).equals(pw)) {

                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("gender", cursor.getInt(cursor.getColumnIndex("gender")));
                        intent.putExtra("name", cursor.getString(cursor.getColumnIndex("Name")));
                        intent.putExtra("email",cursor.getString(cursor.getColumnIndex("Email")));
                        intent.putExtra("ID",cursor.getString(cursor.getColumnIndex("ID")));
                        db.execSQL("update UserDATA set Login = '"+String.valueOf(checkBox.isChecked())+"' where ID = '"+id+"'");
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("db count", String.valueOf(cursor.getCount()));
                    Toast.makeText(LoginActivity.this, "회원정보가 없습니다 ID 와 Password를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), registerActivity.class);
                startActivity(intent);
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reset();
            }
        });
    }

    public void Reset(){
        db.execSQL("drop table UserDATA");
        db.execSQL("create table UserDATA (_id integer PRIMARY KEY autoincrement, ID text, Password text, Name text, Number text, Email text, gender integer, Login text);");
    }
}

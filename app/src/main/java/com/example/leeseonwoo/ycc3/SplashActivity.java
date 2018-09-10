package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startLoading();

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent1 = new Intent(getApplicationContext(), Main2Activity.class);
                Cursor loginState = db.rawQuery("select * from UserDATA where Login = '"+true+"'",null);
                if(loginState.getCount() == 1){
                    Log.d("iasdjfaosdjfal","사용자 있음");
                    loginState.moveToFirst();
                    intent1 = new Intent(getApplicationContext(), Main2Activity.class);
                    intent1.putExtra("gender", loginState.getInt(loginState.getColumnIndex("gender")));
                    intent1.putExtra("name", loginState.getString(loginState.getColumnIndex("Name")));
                    intent1.putExtra("ID",loginState.getString(loginState.getColumnIndex("ID")));
                    Log.d("sended data : ",String.valueOf(loginState.getInt(loginState.getColumnIndex("gender")))+" "+loginState.getString(loginState.getColumnIndex("Name"))
                    +loginState.getString(loginState.getColumnIndex("ID")));
                    startActivity(intent1);
                    finish();
                }
                else{intent1.putExtra("gender", R.drawable.user);
                intent1.putExtra("name", "새로운 요리사");
                intent1.putExtra("ID","unknown");
                startActivity(intent1);
                finish();}
            }
        }, 2000);
    }
}

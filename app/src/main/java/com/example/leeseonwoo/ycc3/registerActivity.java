package com.example.leeseonwoo.ycc3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    RadioButton man, woman;
    boolean check = false;
    String id;
    public static final int MAN = R.drawable.man;
    public static final int WOMAN = R.drawable.woman;
    EditText ID, PW, PW2, name, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        ActionBar ab = getSupportActionBar() ;

        ab.setIcon(R.drawable.yccb) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;

        ID = (EditText)findViewById(R.id.editText_ID);
        PW = (EditText)findViewById(R.id.editText_PW);
        PW2 = (EditText)findViewById(R.id.editText_PW2);
        name = (EditText)findViewById(R.id.editText_Name);
        num = (EditText)findViewById(R.id.editText_Number);
        man = (RadioButton)findViewById(R.id.radioButton);
        woman = (RadioButton)findViewById(R.id.radioButton2);
        Button overlap = (Button)findViewById(R.id.btn_check);
        Button submit = (Button)findViewById(R.id.btn_submit);

        overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = ID.getText().toString();
                Log.d("id", id);
                if(id.isEmpty()) {
                    Toast.makeText(registerActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    check = false;
                }else {
                    if (overlapCheck(id) == 1) {
                        Toast.makeText(registerActivity.this, "중복된 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        check = false;
                    } else if(overlapCheck(id)==0){
                        Toast.makeText(registerActivity.this, "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        check = true;
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = PW.getText().toString();
                String pw2 = PW2.getText().toString();
                String Name = name.getText().toString();
                String Num = num.getText().toString();

                Log.d("현재 회원 정보", id+", "+pw+", "+Name+", "+Num);
                boolean pwCheck = false;
                int gender = 0;
                if(check){}
                else{
                    Toast.makeText(registerActivity.this, "아이디 중복을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pw.isEmpty() && !pw2.isEmpty()) {
                    if (pw.equals(pw2)) pwCheck = true;
                    else {
                        pwCheck = false;
                        Toast.makeText(registerActivity.this, "비밀번호와 비밀번호확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(registerActivity.this, "비밀번호와 비밀번호 확인을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    pwCheck = false;
                    return;
                }
                if(!pwCheck){
                    return;
                }
                if(!Name.isEmpty()){}
                else {
                    Toast.makeText(registerActivity.this, "이름을 확인해주세요. 필수 기재 항목입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!man.isChecked() && !woman.isChecked()){
                    Toast.makeText(registerActivity.this, "성별을 골라주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if (man.isChecked()) gender = MAN;
                    if (woman.isChecked()) gender = WOMAN;
                }

                if(Num.isEmpty()){
                    Toast.makeText(registerActivity.this, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!id.equals(ID.getText().toString())){
                    Toast.makeText(registerActivity.this, "아이디 중복확인을 다시 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.execSQL("insert into UserDATA (ID, Password, Name, Number, gender, Login) values('"+id+"', '"+pw+"', '"+Name+"', '"+Num+"', "+gender+", '"+false+"');");
                Log.w("add data","db 추가됨  : '"+id+"', '"+pw+"', '"+Name+"', '"+Num+"', '"+gender+", '"+false+"'");
                finish();
            }
        });
    }

    public int overlapCheck(String _id){
        Cursor cursor = db.rawQuery("select * from UserDATA where ID = '"+_id+"'",null);
        return cursor.getCount();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

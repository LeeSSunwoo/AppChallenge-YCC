package com.example.leeseonwoo.ycc3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by leeseonwoo on 2018. 7. 26..
 */

public class MyInfomation extends Fragment{

    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String ID;
    TextView name, gender, age, email;
    EditText pw, pw2;
    public static final int MAN = R.drawable.man;
    public static final int WOMAN = R.drawable.woman;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myinformation, container, false);

        DBHelper = new DatabaseOpenHelper(getContext());
        db = DBHelper.getWritableDatabase();

        Bundle extra = getArguments();
        ID = extra.getString("ID");

        Log.d("id2 : ", ID);

        pw = (EditText)view.findViewById(R.id.editText4);
        pw2 = (EditText)view.findViewById(R.id.editText5);
        Button btn = (Button)view.findViewById(R.id.button3);
        name = (TextView)view.findViewById(R.id.name);
        gender = (TextView)view.findViewById(R.id.gender);
        age = (TextView)view.findViewById(R.id.age);
        email = (TextView)view.findViewById(R.id.email);

        Cursor cursor = db.rawQuery("select * from UserDATA where ID = '"+ID+"'", null);
        cursor.moveToFirst();
        name.setText(cursor.getString(cursor.getColumnIndex("Name")));
        if(cursor.getString(cursor.getColumnIndex("gender")).equals(String.valueOf(MAN)))
            gender.setText("남자");
        else
            gender.setText("여자");
        age.setText(cursor.getString(cursor.getColumnIndex("Number")));
        email.setText(cursor.getString(cursor.getColumnIndex("Email")));
        cursor.close();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PW,PW2;
                PW = pw.getText().toString();
                PW2 = pw2.getText().toString();
                if(!PW.isEmpty()&&!PW2.isEmpty()){
                    if(PW.equals(PW2)){
                        db.execSQL("update UserDATA set Password = '"+PW+"' where ID = '"+ID+"'");
                        Toast.makeText(getContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
    }
}

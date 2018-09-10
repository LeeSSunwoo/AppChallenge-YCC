package com.example.leeseonwoo.ycc3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditData extends Fragment {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String ID;
    public static final int MAN = R.drawable.man;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_data, container, false);

        DBHelper = new DatabaseOpenHelper(getContext());
        db = DBHelper.getWritableDatabase();

        Bundle extra1 = getArguments();
        ID = extra1.getString("ID");

        Cursor cursor = db.rawQuery("select Number, gender from UserDATA where ID = '"+ID+"'",null);
        cursor.moveToFirst();
        final EditText edit_name = (EditText)view.findViewById(R.id.name_edit);
        final EditText edit_pw = (EditText)view.findViewById(R.id.editText4);
        final EditText edit_pw2 = (EditText)view.findViewById(R.id.editText5);
        TextView gender = (TextView)view.findViewById(R.id.textView5);
        TextView age = (TextView)view.findViewById(R.id.age_edit);
        Button btn = (Button)view.findViewById(R.id.button_confirm);

        if(cursor.getInt(cursor.getColumnIndex("gender"))==MAN) {
            gender.setText("남자");
        }else{
            gender.setText("여자");
        }
        age.setText(cursor.getString(cursor.getColumnIndex("Number")));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PW,PW2,name;
                PW = edit_pw.getText().toString();
                PW2 = edit_pw2.getText().toString();
                name = edit_name.getText().toString();
                if(name.isEmpty() && PW.isEmpty() && PW2.isEmpty()){}
                else if(!PW.isEmpty()&&!PW2.isEmpty()){
                    if(PW.equals(PW2)){
                        db.execSQL("update UserDATA set Password = '"+PW+"' where ID = '"+ID+"'");
                        if(!name.isEmpty()){
                            db.execSQL("update UserDATA set Name = '"+name+"' where ID = '"+ID+"'");
                        }
                        Toast.makeText(getContext(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();

                    }else{
                        Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }else if(!name.isEmpty()){
                    db.execSQL("update UserDATA set Name = '"+name+"' where ID = '"+ID+"'");
                    Toast.makeText(getContext(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
        });
        cursor.close();
        return view;
    }
}

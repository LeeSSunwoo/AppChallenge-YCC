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
import android.widget.Toast;

public class before_check extends Fragment{
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String ID;
    EditText pw;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.before_check1, container, false);

        DBHelper = new DatabaseOpenHelper(getContext());
        db = DBHelper.getWritableDatabase();

        Bundle extra = getArguments();
        ID = extra.getString("ID");

        Log.d("id : ", ID);

        pw = (EditText)view.findViewById(R.id.editText3);
        Button btn = (Button)view.findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PW = pw.getText().toString();

                Cursor cursor = db.rawQuery("select ID from UserDATA where Password = '"+PW+"'", null);
                cursor.moveToFirst();
                if(ID.equals(cursor.getString(cursor.getColumnIndex("ID")))){
                    Fragment fragment = new MyInfomation();
                    Bundle bundle = new Bundle();
                    bundle.putString("ID",ID);
                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.myinfo_frame, fragment);
                    fragmentTransaction.commit();
                }
                else{
                    Toast.makeText(getContext(),"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });


        return view;
    }
}

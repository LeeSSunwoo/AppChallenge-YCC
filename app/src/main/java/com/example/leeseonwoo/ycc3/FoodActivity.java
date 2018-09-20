package com.example.leeseonwoo.ycc3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class FoodActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    String name;
    LinearLayout material;
    private BluetoothSPP bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bt = new BluetoothSPP(this);
        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        material = (LinearLayout)findViewById(R.id.material);
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.food_backg);

        Intent intent = getIntent();
        String food_name = intent.getStringExtra("food_name");

        TextView food = (TextView)findViewById(R.id.textView34);
        //TextView main = (TextView)findViewById(R.id.textView37);
        final TextView weight = (TextView)findViewById(R.id.textView21);
        final TextView weight_ = (TextView)findViewById(R.id.textView27);
        Button btn = (Button)findViewById(R.id.button5);
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout2);

        final Cursor cursor = db.rawQuery("select food_name, material from FoodDATA where food_name = '"+food_name+"'",null);
        cursor.moveToFirst();
        name = cursor.getString(cursor.getColumnIndex("food_name"));
        food.setText(name);
        String _main = cursor.getString(cursor.getColumnIndex("material"));
        final String[] ss = _main.split(", ");
        String[] main = new String[ss.length];
        for (int k=0;k<main.length;k++){
            main[k] = "";
        }
        for (int i=0; i<main.length; i++){
            String[] sub = ss[i].split("!");
            for(int j=0;j<sub.length;j++){
                if(sub[j].contains("?")){
                    String[] sub_ = sub[j].split("\\?");
                    sub[j] = sub_[0];
                }
                main[i] += sub[j];
            }
        }
        cursor.close();
        //_main = "";
        //for(String wo : ss){
        //    _main = _main + wo+"\n";
        //}
        //main.setText(_main);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final TextView[] textViews = new TextView[ss.length];
        for (int i = 0; i<ss.length; i++){
            textViews[i] = new TextView(this);
            textViews[i].setText(main[i]);
            textViews[i].setTextSize(18);
            textViews[i].setTextColor(Color.BLACK);
            //textViews[i].setWidth();
            //textViews[i].setId(Integer.parseInt(""));
            lp.gravity = Gravity.CENTER;
            textViews[i].setLayoutParams(lp);
            material.addView(textViews[i]);
            final int finalI = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                String s = ss[finalI];
                String[] main = s.split("!");
                @Override
                public void onClick(View view) {
                    for (int i=0;i<main.length;i++){
                        Log.d("asdfasdfasdfasdfasdf",s);
                        if(main[i].contains("?")){
                            String[] we = main[i].split("\\?");
                            weight_.setText(we[0]);
                        }
                    }
                    Toast.makeText(FoodActivity.this, s, Toast.LENGTH_SHORT).show();
                    if(frameLayout.getVisibility() == View.INVISIBLE && s.contains("g"))
                        frameLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frameLayout.getVisibility() == View.VISIBLE)
                    frameLayout.setVisibility(View.INVISIBLE);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),RecipeActivity.class);
                intent1.putExtra("food_name", name);
                startActivity(intent1);
                cursor.close();
                finish();
                Log.d("check finish","food is ended");
            }
        });

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            //finish();
        }

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                String[] rs = message.split(".");
                weight.setText(rs[0]);
                double rate = Double.parseDouble(rs[0])/Double.parseDouble(String.valueOf(ss[0].split("!")[1].split("\\?")));
                String[] result = new String[ss.length];
                for(int k=0;k<ss.length;k++){
                    result[k]="";
                }
                for(int i=0; i<ss.length;i++){

                    String[] aa = ss[i].split("!");
                    for(int j=0; j<aa.length;j++){
                        if(aa[j].contains("?")){
                            double sss = Double.parseDouble(String.valueOf(aa[j].split("\\?")));
                            sss = sss*rate;
                            aa[j] = String.valueOf(sss).split(".")[0];
                        }
                        result[i] += aa[j];
                    }
                    textViews[i].setText(result[i]);
                }
            }
        });

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    public void setup() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

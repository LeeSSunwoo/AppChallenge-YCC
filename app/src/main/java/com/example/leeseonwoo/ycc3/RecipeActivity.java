package com.example.leeseonwoo.ycc3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    CountDownTimer countDownTimer;
    TextView timeo;
    private TextToSpeech tts;
    int num,prenum, nextnum;
    String food_name,ID;
    String[] m =new String[2];
    String[] ss;
    TextView textView;
    ImageView pre_Img, next_Img;
    int endnum,ImgID;
    Button start_timer;
    Spinner spinner,spinner2;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();

        Intent intent = getIntent();
        ImgID = intent.getIntExtra("ImgID",0);
        ID = intent.getStringExtra("ID");
        food_name = intent.getStringExtra("food_name");
        num = 0;
        Cursor food = db.rawQuery("select * from FoodDATA where food_name = '"+food_name+"'",null);
        food.moveToFirst();
        String recipe = food.getString(food.getColumnIndex("process"));
        food.close();

        ss = recipe.split("\n");
        endnum = ss.length-1;
        setContentView(R.layout.activity_recipe);
        textView = (TextView)findViewById(R.id.textView38);
        textView.setText(ss[num]);
        prenum = num-1;
        nextnum = num+1;
        pre_Img = (ImageView)findViewById(R.id.imageView8);
        next_Img = (ImageView)findViewById(R.id.imageView7);

        pre_Img.setVisibility(View.INVISIBLE);

        next_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    num = nextnum;
                    SetNext(num);
            }
        });

        pre_Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    num = prenum;
                    SetPrevious(num);
            }
        });

        Button btn = (Button)findViewById(R.id.button6);
        timeo = (TextView)findViewById(R.id.textView40);
        Button btn_timer = (Button)findViewById(R.id.button7);
        start_timer = (Button)findViewById(R.id.button8);
        Button stop = (Button)findViewById(R.id.button10);
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);

        String[] arr = getResources().getStringArray(R.array.time);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arr);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter);
        spinner.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        spinner2.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                m[0] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                m[0] = "00";
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                m[1] = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                m[0] = "00";
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num==endnum && !ID.equals("unknown")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(RecipeActivity.this);
                    builder.setIcon(R.drawable.btn_star_on);
                    builder.setTitle("즐겨찾기");
                    builder.setMessage("이 요리가 마음에 드셨다면 즐겨찾기 하세요!");
                    builder.setPositiveButton("마음에 들어요!", dialogListener);
                    builder.setNegativeButton("별로였어요.", dialogListener);

                    alertDialog=builder.create();
                    alertDialog.show();
                }else{
                    finish();
                }
            }
        });

        btn_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frameLayout.getVisibility() == View.INVISIBLE)
                    frameLayout.setVisibility(View.VISIBLE);
                else
                    frameLayout.setVisibility(View.INVISIBLE);
            }
        });

        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start_timer.getText().toString().equals("타이머 시작")) {
                    start_timer.setText("일시정지");
                    int sec = Integer.parseInt(m[0]) * 60 + Integer.parseInt(m[1]);
                    Toast.makeText(RecipeActivity.this, String.valueOf(sec), Toast.LENGTH_SHORT).show();
                    countDownTimer(sec);
                    countDownTimer.start();
                    spinner.setSelection(0);
                    spinner2.setSelection(0);
                }
                else if(start_timer.getText().toString().equals("일시정지")){
                    start_timer.setText("타이머 시작");
                    String[] time = timeo.getText().toString().split(" : ");
                    m[0]=time[0];
                    m[1]=time[1];
                    countDownTimer.cancel();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                timeo.setText("00 : 00");
                start_timer.setText("타이머 시작");
                m[0]="00";
                m[1]="00";
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DBHelper = new DatabaseOpenHelper(getApplicationContext());
            db = DBHelper.getWritableDatabase();
            if(which==DialogInterface.BUTTON_POSITIVE) {
                db.execSQL("insert into Bookmark (ID, food_name, ImgID) VALUES ('" + ID + "', '" + food_name + "', " + ImgID + ")");
                db.close();
            }
            finish();
        }
    };

    private void SetPrevious(int num){
        textView.setText(ss[num]);
        prenum = num - 1;
        nextnum = num + 1;
        if(prenum==-1) {
            pre_Img.setVisibility(View.INVISIBLE);
        }
        next_Img.setVisibility(View.VISIBLE);
        Log.d("recipe finish","sdffsfsffdsewdfgdsfgfdsfvbfd");
    }

    private void SetNext(int num){
        textView.setText(ss[num]);
        prenum = num - 1;
        nextnum = num + 1;
        if(nextnum == endnum+1) {
            next_Img.setVisibility(View.INVISIBLE);
        }
        pre_Img.setVisibility(View.VISIBLE);
    }

    private int se;
    public void countDownTimer(int sec){
        se = sec;
        countDownTimer = new CountDownTimer((sec)*1000,1000) {
            @Override
            public void onTick(long l) {
                timeo.setText( String.format( "%1$02d" , se/60 )+" : "+String.format( "%1$02d" , se%60 ));
                se--;
            }

            @Override
            public void onFinish() {
                timeo.setText("00 : 00");
                start_timer.setText("타이머 시작");
                Toast.makeText(RecipeActivity.this, "끝!", Toast.LENGTH_SHORT).show();
            }

            
        };
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{//toolbar의 back키 눌렀을 때 동작
                if(num>0) {
                    num=prenum;
                    SetPrevious(num);
                }
                else
                {db.close();finish();}
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(num>0) {
            num=prenum;
            SetPrevious(num);

        }
        else {
            db.close();
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
        db.close();
    }
}

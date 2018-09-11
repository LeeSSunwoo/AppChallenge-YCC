package com.example.leeseonwoo.ycc3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import static android.speech.tts.TextToSpeech.ERROR;


import java.util.Locale;

public class RecipeActivity extends AppCompatActivity {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    CountDownTimer countDownTimer;
    TextView timeo;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();
        Intent intent = getIntent();
        final String food_name = intent.getStringExtra("food_name");
        final int num = intent.getIntExtra("number",0);
        Cursor food = db.rawQuery("select * from FoodDATA where food_name = '"+food_name+"'",null);
        food.moveToFirst();
        String recipe = food.getString(food.getColumnIndex("process"));
        food.close();
        String[] ss = recipe.split("\n");
        final int endnum = ss.length-1;
        setContentView(R.layout.activity_recipe);
        TextView textView = (TextView)findViewById(R.id.textView38);
        textView.setText(ss[num]);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        tts.speak(ss[num],TextToSpeech.QUEUE_FLUSH,null);
        Button btn = (Button)findViewById(R.id.button6);
        final EditText time = (EditText)findViewById(R.id.editText8);
        timeo = (TextView)findViewById(R.id.textView40);
        Button btn_timer = (Button)findViewById(R.id.button7);
        Button start_timer = (Button)findViewById(R.id.button8);
        final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num==endnum){
                    finish();
                }else{
                    Intent next = new Intent(getApplicationContext(),RecipeActivity.class);
                    next.putExtra("food_name",food_name);
                    next.putExtra("number",num+1);
                    startActivity(next);
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
                String aaa = time.getText().toString();
                String[] m = aaa.split(":");
                int sec = Integer.parseInt(m[0])*60+Integer.parseInt(m[1]);
                Toast.makeText(RecipeActivity.this, String.valueOf(sec), Toast.LENGTH_SHORT).show();
                countDownTimer(sec);
                countDownTimer.start();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);



        int colorText = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
        ActionBar bar = getSupportActionBar();
        Spannable text = new SpannableString(bar.getTitle());
        text.setSpan(new ForegroundColorSpan(colorText), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        bar.setTitle(text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


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
                Toast.makeText(RecipeActivity.this, "끝!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{//toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }
}

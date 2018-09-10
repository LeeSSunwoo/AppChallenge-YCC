package com.example.leeseonwoo.ycc3;

import android.support.constraint.ConstraintLayout;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.Random;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    Intent intent;
    String ID;
    CustomAdapter customAdapter = new CustomAdapter();
    SearchView searchView;
    TextView nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GridView gridView = (GridView)findViewById(R.id.random_reco);
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView3);
        GradientDrawable drawable = (GradientDrawable)getDrawable(R.drawable.layout_bg);
        imageView2.setBackground(drawable);
        imageView2.setClipToOutline(true);

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();
//Reset();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ImageView imageView = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView);
        nickname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name_edit2);
        TextView email_text = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textView);
        intent = getIntent();

        int gender = intent.getExtras().getInt("gender");
        String name = intent.getExtras().getString("name");
        ID = intent.getExtras().getString("ID");
        Log.d("received data : ",String.valueOf(gender)+", "+name);
        imageView.setImageResource(gender);
        nickname.setText(name);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ID.equals("unknown")){
                    Toast.makeText(getApplicationContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent3 = new Intent(getApplicationContext(), MyinfoActivity.class);
                    intent3.putExtra( "ID", ID);
                    startActivity(intent3);
                }
            }
        });

        MenuItem item = (MenuItem)navigationView.getMenu().findItem(R.id.nav_logout);
        if(ID.equals("unknown")) {
            item.setTitle("로그인");
            item.setIcon(R.drawable.lock);
            email_text.setText("로그인이 필요합니다.");
        }
        else{
            item.setTitle("로그아웃");
            item.setIcon(R.drawable.unlock);
            email_text.setText("");
        }
        Random random = new Random();
        int[] random1 = new int[5];
        for (int x = 0; x < 5; x++){
            random1[x] = random.nextInt(12)+1;

            for(int j = 0;j<x;j++){
                if(random1[x] == random1[j]){
                    x--;
                    break;
                }
            }
        }
        for (int i = 0;i<5;i++) {
            Cursor cursor_rand = db.rawQuery("select * from FoodDATA where _id = "+String.valueOf(random1[i]),null);
            cursor_rand.moveToFirst();
            customAdapter.addItem(cursor_rand.getInt(cursor_rand.getColumnIndex("ImgID")),cursor_rand.getString(cursor_rand.getColumnIndex("food_name")));
        }
        customAdapter.notifyDataSetChanged();
        gridView.setAdapter(customAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(i);
                String name = item.getFood_name();
                Intent Gotofood = new Intent(getApplicationContext(),FoodActivity.class);
                Gotofood.putExtra("food_name", name);
                startActivity(Gotofood);
            }
        });

        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.content);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery("",false);
                searchView.setIconified(true);
            }
        });
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Cursor cursor = db.rawQuery("select food_name,ImgID from FoodDATA where food_name like '%"+query+"%' order by _id",null);
            cursor.moveToFirst();
            int capa = cursor.getCount();
            if(capa <= 0){
                Toast.makeText(Main2Activity.this, "그 음식은 아직 주문이 안 들어왔습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
            String[] food_list = new String[capa];
            int[] img_list = new int[capa];
            for (int i = 0;i<capa;i++){
                food_list[i] = cursor.getString(cursor.getColumnIndex("food_name"));
                img_list[i] = cursor.getInt(cursor.getColumnIndex("ImgID"));
                cursor.moveToNext();

            }
            cursor.close();
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("food_name",food_list);
            intent.putExtra("img_list",img_list);
            startActivity(intent);
            searchView.setQuery("",false);
            searchView.setIconified(true);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("원하시는 요리를 입력하세요.");
        searchView.setOnQueryTextListener(queryTextListener);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_info) {
            if(ID.equals("unknown")){
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                intent = new Intent(getApplicationContext(), MyinfoActivity.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
            }

        } else if (id == R.id.k_food) {
            Intent food_info = new Intent(getApplicationContext(),FoodListActivity.class);
            food_info.putExtra("type","한식");
            startActivity(food_info);
        } else if (id == R.id.u_food) {
            Intent food_info = new Intent(getApplicationContext(),FoodListActivity.class);
            food_info.putExtra("type","양식");
            startActivity(food_info);
        } else if (id == R.id.c_food) {
            Intent food_info = new Intent(getApplicationContext(),FoodListActivity.class);
            food_info.putExtra("type","중식");
            startActivity(food_info);
        } else if (id == R.id.j_food) {
            Intent food_info = new Intent(getApplicationContext(),FoodListActivity.class);
            food_info.putExtra("type","일식");
            startActivity(food_info);
        } else if (id == R.id.nav_share) {
            Intent bt = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(bt);
        } else if (id == R.id.nav_logout){
            if(!ID.equals("unknown")) {
                db.execSQL("update UserDATA set Login = 'false' where ID = '" + ID + "'");
            }

            Intent toLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(toLogin);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void Reset(){
        db.execSQL("drop table UserDATA");
        db.execSQL("drop table FoodDATA");
        db.execSQL("create table UserDATA (_id integer PRIMARY KEY autoincrement, ID text, Password text, Name text, Number text, gender integer, Login text);");
        db.execSQL("CREATE TABLE FoodDATA (_id integer PRIMARY KEY autoincrement," + "  type varchar(2) DEFAULT NULL," + "  food_name varchar(8) DEFAULT NULL," + "  amount varchar(3) DEFAULT NULL," + "  material varchar(299) DEFAULT NULL," + "  process varchar(569) DEFAULT NULL" + ",ImgID bigint);");
        db.execSQL("INSERT INTO FoodDATA (type, food_name, amount, material, process, ImgID) VALUES\n" +
                "('한식,면', '잡채', '4인분', '당면 100g, 돼지고기(돼지고기 등심) 50g, 시금치 80g, 표고버섯 1개(30g), 당근 1/5개(40g), 양파 1/6개(30g), (양념) 식용유 1큰술(15ml), 깨 1작은술(3g), 소금 약간, 시금치 양념(참기름 약간, 소금 약간), 돼지고기 양념(간장 1큰술(15g), 설탕 1작은술(3g), 참기름 약간, 대파(다진 대파) 1/2작은술(2g), 마늘(다진 마늘) 1/2작은술(2g), 청주 1작은술(5ml), 후춧가루(후춧가루 약간)), 당면 양념(간장 1큰술(15ml), 설탕 1/2큰술(5g), 참기름 약간)\\n', '1. 당면은 찬물에 1시간 정도 불린다. 끓는 물에 당면을 넣고 2분간 삶은 후 체에 밭쳐 물기를 뺀다. 돼지고기는 돼지고기 양념에 버무린 후 10분간 재운다.\n2. 시금치는 끓는 소금물에 담가 데친 후 물기를 꼭 짠 후 소금, 참기름에 무친다.\n3. 양파, 당근, 표고버섯은 5cm길이로 가늘게 채 썬다. 달군 팬에 식용유를 두르고 양파, 당근, 표고버섯을 넣고 소금을 약간 넣어 볶아 접시에 덜어둔다. 돼지고기는 달군 팬에 식용유를 두르고 볶는다.\n4. 팬에 당면 양념 재료와 당면을 넣고 양념이 졸아들 때까지 볶는다.\n5. 양파, 당근, 표고버섯, 돼지고기, 시금치를 함께 넣고 볶은 후 깨를 뿌린다.',"+R.drawable.japche+"),\n" +
                "('한식', '불고기', '4인분', '소고기(등심 또는 안심) 500g, 양파 200g(2개), 대파 20g(1대), 팽이버섯 50g, 곁들임 채소(상추외 잎채소 100g, 풋고추 2개, 깐마늘 5쪽), 쌈장 42g(2TS), 불고기 양념장: 간장 72g(4TS), 후춧가루 0.6g(1/4ts), 배즙 30g(2TS), 생강즙 16g(1TS), 설탕 48g(4TS), 물엿 38g(2TS), 다진 파 14g(1TS), 다진 마늘 8g(1/2TS), 깨소금 3g(1/2TS), 참기름 13g(1TS)', '1. 소고기는 등심이나 안심의 연한 부분을 골라 얇게 썰어 준비한다.\n2. 소고기를 배즙과 설탕, 생강즙에 버무려 재어 놓은 다음, 나머지 양념 재료를 넣고 간이 고루 베게 주물러 30분 이상 재어 둔다.\n3. 대파는 0.5cm 두께로 어슷 썰고, 양파는 0.3cm 두께로 채 썬다. 팽이버섯은 밑동을 잘라 준비한다.\n4. 뜨겁게 달군 팬에 고기와 준비한 채소를 넣고 볶는다.\n5. 곁들임 채소(상추, 깻잎 등의 잎 채소, 풋고추, 마늘)를 준비하여 쌈장과 함께 곁들인다.',"+R.drawable.bulgogi+"),\n" +
                "('한식,국', '미역국', '4인분', '미역 5줌(20g), 쇠고기(양지머리) 120g, 물 8컵(1,600ml), 재래간장 1과 1/2큰술(22ml), 마늘(다진 마늘) 1큰술(10g), 소금 작은술(3g), 참기름 작은술(5ml)', '1. 마른 미역은 찬물에 담가 10분간 불린뒤 찬물에 바락바락 씻어 거품이 나오지 않을 때까지 헹군다.\n2. 물기를 꼭 짠 후 적당한 크기로 자른 후 재래간장 1/2큰술을 넣고 조물조물 무친다.\n3. 쇠고기는 한입 크기로 썬 후 달군 냄비에 참기름을 두르고 쇠고기, 마늘을 넣어 볶다가 쇠고기가 거의 익으면 미역을 넣고 볶는다.\n4. 3번에 물을 넣고 한소끔 끓인다. 재래간장과 소금으로 간하고 더 끓인다. (물 대신 쌀뜨물을 넣으면 더욱 구수하고 맛있는 미역국을 만들 수 있다.)',"+R.drawable.miyukguk+"),\n" +
                "('양식,면', '토마토 스파게티', '4인분', '스파게티400g, 올리브유120g, 마늘4쪽, 양파120g, 방울토마토40g, 토마토홀(캔) 800g, 소금, 후추, 바질 4줄기, 이탈리아파슬리 4줄기, 그라나 파다노가루 40g\\n', '1. 양파와 마늘은 잘게 다지고, 바질은 장식용으로 일부 남기고 나머지는 파슬리와 함께 잘게 다진다.\n2. 방울토마토는 4등분으로 잘라놓고, 토마토 홀은 손으로 주물러 으깬다.\n3. 끓는 물에 소금을 넣고 스파게티를 9분간 삶는다.\n4. 팬에 올리브유를 두르고 양파와 마늘을 볶다가 방울토마토와 으깬 토마토를 넣고 10분 정도 은근하게 끓여 소금, 후추로 간하고 다진 바질과 파슬리를 넣는다.\n5. 소스의 농도가 맞으면 삶은 면을 건져 넣고 앞뒤로 흔들며 잘 섞어 접시에 담고 그라나치즈가루와 바질잎으로 장식한다.',"+R.drawable.supagatti+"),\n" +
                "('양식,면', '크림 스파게티', '1인분', '스파게티 100g, 베이컨 40g, 양파 20g, 브로콜리 25g, 밀가루 8g, 버터 15g, 우유 100ml, 물 100ml, 치킨스톡 4g, 올리브유 28ml, 소금(소금 약간), 파슬리가루(파슬리가루 약간), 후춧가루(후춧가루 약간)', '1. 약불의 팬에 버터를 넣어 완전히 녹으면 밀가루를 넣어 뭉치지 않게 볶아주고, 치킨스톡을 녹인 육수를 부어가며 뭉치지 않게 재빨리 저어가며 후루루 끓인다.(약불에서 눌지 않게 볶아주어야 한다.)\n2. 우유를 넣어 눌지 않도록 저어가며 한소끔 끓이면 불을 끈다.\n3. 스파게티면의 양의 3배 정도 되는 끓는 물에 소금을 약간 넣어 부채꼴 모양으로 펼쳐 넣어 8분가량 삶아 낸 후 채반에 받쳐 물기를 제거한 후 올리브유 28g에 버무려 둔다.(올리브유에 삶은 스파게티를 버무려 주면 덜 불는다.)\n4. 팬에 베이컨을 넣고 볶아 기름이 많이 생기면 불을 끄고 키친타월에 받쳐 기름기를 제거한다.\n5. 잘게 다진 양파는 올리브유 28g을 두른 팬에서 볶다가 작게 잘라둔 브로콜리를 넣어 소금간을 약간하고 살짝 볶아 준비한다.(치킨스톡을 사용하지 않고 그냥 물을 사용할 경우에는 크림소스에 소금간을 해야 한다.)\n6. 양파와 브로콜리를 볶은 팬에 삶은 스파게티와 기름을 제거한 베이컨을 넣고 크림소스를 부어 버무린 후 후춧가루와 파슬리가루를 뿌려 완성한다.(기호에 따라 양송이버섯이나 감자 등의 야채를 첨가하여도 좋다.)\\n',"+R.drawable.cream+"),\n" +
                "('양식,구이', '스테이크', '2인분', '채끝등심 600g, 양송이 3개, 토마토 1/2개, 양파 1/2개, 호박 1/3개, 가지 1/3개, 버터 10g, 소금 약간, 후추 약간, 로즈마리 5g', '1. 채끝의 핏기를 뺀 후 소금과 후추로 간을 해준다.\n2. 가지, 호박, 양파 토마토를 동글하게 썰어준다.\n3. 양송이는 밑 기둥을 짤라주시고 4등분한다.\n4. 버터 10g를 두른 팬에 로즈마리를 넣어 향을 내준후 고기를 투하한다.\n5. 고기를 식히는 동안 양파, 가지, 호박, 토마토, 양송이를 구워 가니쉬를 만들어준다.\n6. 고기와 가니쉬를 플레이팅을 하면 완성.',"+R.drawable.stake+"),\n" +
                "('일식', '생선초밥', '4인분', '초밥용 생선회(종류는 기호에 따라) 800g, 쌀 3컵, 다시마 1조각, 고추냉이(와사비) 30g, 환만식초 180ml, 설탕 120ml, 천일염 60ml, 다시마 1조각, 레몬 1/4개, 간장(양조간장) 30ml, 고추냉이(와사비) 5g', '1. 쌀을 씻어 냄비에 담고, 물을 조금 적게 잡아 다시마 한 조각을 넣어 밥을 합니다.\n2. 식초, 소금을 냄비에 넣고 섞은 다음 설탕을 넣고 약한 불에서 설탕이 녹을 때까지 가열하고 불을 끕니다.\n3. ②에 다시마 1조각을 넣고 그대로 식힙니다. 단촛물이 다 식으면 레몬즙을 짜서 넣어주세요.(밥이 뜨거울 경우, 바람을 불어 식히며 비벼주세요.)\n4. 밥에 단촛물을 넣고 주걱을 세워서 밥알이 상하지 않게 비벼 준비합니다.\n5. 손질된 생선살들을 준비해주세요.\n6. 밥 1스푼을 오른손으로 쥐어 뭉치고, 준비된 초밥용 생선살을 왼손 위에 올린 후 생선살 가운데에 검지로 와사비를 콕 찍듯이 발라줍니다.\n7. 생선살 위에 뭉친 밥을 올리고, 오른손으로 눌러 밥의 모양을 잡아준 뒤 뒤집어줍니다.\n8. 접시에 초밥들을 색을 맞춰 담고, 와사비 간장을 곁들여 내주세요.',"+R.drawable.susi+"),\n" +
                "('일식', '규카츠', '1인분', '소고기 두덩어리, 빵가루 1컵, 밀가루 반컵, 계란 3개, 튀김용 기름 1리터, 소금1작은술, 후추1작은술, 꿀 2큰술, 아스파라거스, 간장 (와사비), 양배추4/1, 케찹 + 마요네즈', '1. 소고기 힘줄 부분을 칼집 내어 풀어줍니다.\n2. 칼집낸 소고기를 고기망치로 두들겨 넓게 펴줍니다.\n3. 소금, 후추를 뿌립니다.\n4. 밀가루, 계란, 빵가루 순서로 튀김옷을 입힙니다.\n5. 기름온도가 160~170도가 되면 튀김옷을 입힌 소고기를 겉면이 바삭해지도록 튀겨줍니다.\n6. 달궈진 팬에 후추와 소금을 살짝 뿌리고 적채와 아스파라거스를 볶습니다.\n7. 간장과 와사비를 섞어 소스를 만들면 완성입니다.',"+R.drawable.gukach+"),\n" +
                "('일식,면', '미소라멘', '1인분', '라면사리 1인분, 삼겹살 1줄, 계란 1개, 숙주 1줌, 실파 조금, 간장 2큰술, 설탕 1큰술, 청주 1큰술, 다시마육수 500ml, 미소된장 1큰술, 다진마늘 1큰술, 후추 조금', '1. 다시마를 끓여서 다시마 육수를 만들어준다. 물은 졸아들기 때문에 넉넉하게 넣어준다.\n2. 분량의 육수재료를 넣고 한소끔 끓여준다.\n3. 반숙 계란을 만들기 위해 찬물에 계란을 넣고 끓이다가 물이 끓으면 4분 후에 계란을 꺼내준다.\n4. 실파를 작게 썰어준다.\n5. 숙주는 끓는 물에 잠깐 넣었다가 바로 빼준다.\n6. 삽결살을 굽는다. 고기가 촉촉해지기위해 구을 때 물을 뿌려준다.\n7. 간장, 맛술, 설탕을 섞은 소스를 삼겹살에 붓고 졸여준다.\n8. 면을 삶아준다.\n9. 그릇에 면을 담고 육수를 부은 뒤 삼겹살, 계란, 파를 넣어주면 완성. ',"+R.drawable.ramen+"),\n" +
                "('중식,면', '짜장면', '4인분', '양파 1개, 당근 1/3개, 감자 1개, 쥬티니 호박 1/4개, 양배추 2장, 대파 1대, 돼지고기 등심(카레용) 150g, 칼국수면 4인분, 짜장 분말 100g, 물600ml, 청주 2T, 다진 마늘 1/2T, 소금, 후춧가루', '1. 청주, 다진 마늘, 소금, 후춧가루를 약간 섞어서 돼지고기 밑간 재료를 만듭니다.\n2. 돼지고기에 밑간 재료를 넣고 10분 정도 재웁니다.\n3. 파는 얇게 썰고 양파, 당근, 쥬키니 호박, 감자, 양배추는 1.5cm~2cm 크기로 썹니다.\n4. 냄비에 식용유를 두른 뒤 파를 먼저 볶습니다.\n5. 양파와 돼지고기를 넣고 볶다가 당근과 감자를 넣고 볶습니다.\n6. 어느 전도 익으면 양배추와 쥬키니 호박을 넣고, 소금과 후춧가루로 밑간을 합니다.\n7. 물을 넣고 약한 불에서 끓이다가 재료가 다 익으면 짜장 분말을 넣고 걸쭉해질 때가지만 끓입니다.\n8. 끓는 물에 칼국수면을 약 5분 전도 삶습니다.\n9. 찬물에 면을 비벼서 헹군 후 체에 밭쳐 물기를 뺍니다.\n10. 칼국수면을 그릇에 담은 후 짜장 소스를 뿌리면 완성입니다.',"+R.drawable.jjajangmyun+"),\n" +
                "('중식,면', '짬뽕', '1인분', '파기름 3큰술, 간장 1큰술, 배추 20g, 양상추 20g, 당근 10g, 홍새우 10g, 홍합 30g, 소금 반 큰술, 고추가루 2큰술, 파 5g, 생강 5g, 홍합육수 700ml', '1. 배추, 양배추, 당근을 손질합니다.\n2. 약한 불에 기름을 달구고 파, 생강을 넣고 볶다가 파, 생강 향이 날 때쯤 생강은 건져냅니다.\n3. 건새우를 넣고 볶다가 새우 향이 날 때즘 불을 세게 합니다.\n4. 손질한 배추, 양배추, 당근을 넣고 볶습니다.\n5. 간장 한 큰술을 넣고 채소 숨이 죽을 때까지 볶습니다.\n6. 고춧가루 2큰술, 홍합 육수 1큰술을 넣고 볶습니다.\n7. 홍합을 넣고 같이 볶다가 육수 700ml를 넣고 끓입니다.\n8. 면은 밑간을 하기 위해 끓는 물에 소금을 넣고 면을 삶습니다.\n9. 물이 끓고 면이 떠오르면 차가운 물을 붓습니다.\n10. 면을 건져 찬물에 헹궈주고 국물이 뽀얗게 우러나면 면 위에 부으면 완성입니다.\\n',"+R.drawable.jjambbong+"),\n" +
                "('중식', '마파두부', '2인분', '두부 한모, 다진마늘 두큰술, 다진돼지고기 1/4컵, 송송 썬 대파 1/4컵, 다진양파 반컵, 고춧가루 1/3컵, 설탕 1/3컵, 간장 한큰술, 된장 반큰술, 물 한컵, 전분물 두큰술', '1. 두부 한 모를 깍둑썬다.\n2. 식용유를 두른 팬에 다진 마늘, 다진 돼지고기, 다진 양파, 대파를 넣고 먼저 볶아준다.\n3. 고기가 익으면 고춧가루, 설탕, 간장, 된장, 물을 넣어 양념장을 만든다.\n4. 만들어진 양념장에 깍둑 썬 두부를 넣고 끓이다가 전분물을 넣고 농도가 잡힐 때 까지 빨리 섞은 뒤 가스불을 끈다. (전분물을 넣은 상태에서 오랫동안 끓이면 전분물끼리 뭉치기 때문에 가스불을 꺼줘야 합니다.)\n5. 참기름을 한바퀴 둘러주면 완성.\\n',"+R.drawable.mapadubu+");");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!ID.equals("unknown")) {
            Cursor cursor = db.rawQuery("select Name from UserDATA where ID = '" + ID + "'", null);
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
            if (!nickname.getText().toString().equals(name)) {
                nickname.setText(name);
            }
        }
    }
}

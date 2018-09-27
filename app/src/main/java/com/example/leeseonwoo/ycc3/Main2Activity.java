package com.example.leeseonwoo.ycc3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.support.constraint.ConstraintLayout;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.Random;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseOpenHelper DBHelper;
    SQLiteDatabase db;
    Intent intent;
    String ID;
    CustomAdapter customAdapter = new CustomAdapter();
    CustomAdapter3[] customAdapter3 = new CustomAdapter3[3];
    SearchView searchView;
    TextView nickname;
    private BluetoothSPP bt;
    TextView weight;
    TextView[] list_title;
    ListView[] listView_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bt = new BluetoothSPP(this);
        weight = (TextView)findViewById(R.id.weight_text);
         //Initializing
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar() ;

        /*ab.setIcon(R.drawable.yccb) ;
        ab.setDisplayUseLogoEnabled(true) ;
        ab.setDisplayShowHomeEnabled(true) ;*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.main_layout);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        GridView gridView = (GridView)findViewById(R.id.random_reco);
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView3);
        GradientDrawable drawable = (GradientDrawable)getDrawable(R.drawable.layout_bg);
        imageView2.setBackground(drawable);
        imageView2.setClipToOutline(true);

        /*if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            Toast.makeText(this, "블루투스 사용가능", Toast.LENGTH_SHORT).show();
        }*/
//        Toast.makeText(this, String.valueOf(bt.isBluetoothEnabled()), Toast.LENGTH_SHORT).show();

        DBHelper = new DatabaseOpenHelper(getApplicationContext());
        db = DBHelper.getWritableDatabase();
        //Reset();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
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
        ImageView login_nav = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.nav_login);
        list_title = new TextView[3];
        listView_ = new ListView[3];
        list_title[0] = (TextView)findViewById(R.id.textView27);
        list_title[1] = (TextView)findViewById(R.id.textView28);
        list_title[2] = (TextView)findViewById(R.id.textView29);
        listView_[0] = (ListView)findViewById(R.id.list1);
        listView_[1] = (ListView)findViewById(R.id.list2);
        listView_[2] = (ListView)findViewById(R.id.list3);
        LinearLayout linearLayout1 = (LinearLayout)findViewById(R.id.linear1);
        LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.linear2);
        LinearLayout linearLayout3 = (LinearLayout)findViewById(R.id.linear3);
        LinearLayout linearLayout4 = (LinearLayout)findViewById(R.id.linear4);
        linearLayout1.setBackground(drawable);
        linearLayout1.setClipToOutline(true);
        linearLayout2.setBackground(drawable);
        linearLayout2.setClipToOutline(true);
        linearLayout3.setBackground(drawable);
        linearLayout3.setClipToOutline(true);
        gridView.setBackground(drawable);
        gridView.setClipToOutline(true);


        String[] type = new String[] {"밥", "빵", "면", "고기", "생선", "국", "반찬", "튀김", "스프"};
        intent = getIntent();

        int gender = intent.getExtras().getInt("gender");
        String name = intent.getExtras().getString("name");
        ID = intent.getExtras().getString("ID");
        Log.d("received data : ",String.valueOf(gender)+", "+name);
        imageView.setImageResource(gender);
        nickname.setText(name);
        nickname.setTextSize(20);
        MenuItem item = (MenuItem) navigationView.getMenu().findItem(0);

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

        login_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(ID.equals("unknown")){
                    Intent intent_login = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent_login);
                    finish();
                //}else{

                //}
            }
        });

        //MenuItem item = (MenuItem)navigationView.getMenu().findItem(R.id.nav_logout);
        if(ID.equals("unknown")) {

            login_nav.setImageResource(R.drawable.lock);

        }
        else{
            login_nav.setImageResource(R.drawable.unlock);

        }
        Random random = new Random();
        int[] random1 = new int[5];
        int[] random2 = new int[3];
        for (int x = 0; x < 5; x++){
            random1[x] = random.nextInt(12)+1;

            for(int j = 0;j<x;j++){
                if(random1[x] == random1[j]){
                    x--;
                    break;
                }
            }
        }
        for (int x = 0; x < 3; x++){
            random2[x] = random.nextInt(8)+1;

            for(int j = 0;j<x;j++){
                if(random2[x] == random2[j]){
                    x--;
                    break;
                }
            }
        }
        for (int i = 0;i<5;i++) {
            Cursor cursor_rand = db.rawQuery("select * from FoodDATA where _id = "+String.valueOf(random1[i]),null);
            cursor_rand.moveToFirst();
            customAdapter.addItem(cursor_rand.getInt(cursor_rand.getColumnIndex("ImgID")),cursor_rand.getString(cursor_rand.getColumnIndex("food_name")),ID);
        }
        customAdapter.notifyDataSetChanged();
        gridView.setAdapter(customAdapter);
        int a;
        for(int i=0;i<3;i++){
            customAdapter3[i] = new CustomAdapter3();
            list_title[i].setText(type[random2[i]]);
            Log.d("type check",type[random2[i]]);
            Cursor cursor;
            if(type[random2[i]].equals("국")){
                cursor = db.rawQuery("select * from FoodDATA where type like '%"+type[random2[i]]+"%' or type like '%찌개%'",null);
            }
            else{
                cursor = db.rawQuery("select * from FoodDATA where type like '%"+type[random2[i]]+"%'",null);
            }
            cursor.moveToFirst();
            if(type[random2[i]].equals("스프")||type[random2[i]].equals("빵")||type[random2[i]].equals("생선")) a = 1;
            else if(type[random2[i]].equals("튀김")) a=2;
            else a=3;
            for(int j=0;j<a;j++){
                customAdapter3[i].addItem(cursor.getInt(cursor.getColumnIndex("ImgID")),cursor.getString(cursor.getColumnIndex("food_name")),ID);
                cursor.moveToNext();
            }
            listView_[i].setAdapter(customAdapter3[i]);
            cursor.close();
        }

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

        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                Toast.makeText(Main2Activity.this, "수신됨", Toast.LENGTH_SHORT).show();
                //String[] rs = message.split(".");
                weight.setText(message);
            }
        });
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "연결되었습니다."
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "연결을 해제하였습니다.", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "연결을 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        for(int l = 0;l<3;l++) {
            listView_[l].setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(i);
                    String name = item.getFood_name();
                    Intent Gotofood = new Intent(getApplicationContext(), FoodActivity.class);
                    Gotofood.putExtra("food_name", name);
                    startActivity(Gotofood);
                }
            });
        }
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
            intent.putExtra("ID",ID);
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
            searchView.setQuery("",false);
            searchView.setIconified(true);
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
        Intent food_info = new Intent(getApplicationContext(), FoodListActivity.class);
        String type = "";
        if (id == R.id.nav_book) {
            if (ID.equals("unknown")) {
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                type = "즐찾";
            }
        } else if (id == R.id.k_food) {
            type = "한식";
        } else if (id == R.id.u_food) {
            type = "양식";
        } else if (id == R.id.c_food) {
            type = "중식";
        } else if (id == R.id.j_food) {
            type = "일식";
        } else if (id == R.id.nav_share) {
            Intent bt = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(bt);
            return true;
        } else if (id == R.id.noodle) {
            type = "면";
        } else if (id == R.id.bob) {
            type = "밥";
        } else if (id == R.id.meat) {
            type = "고기";
        } else if (id == R.id.gook_jjigea) {
            type = "국/찌개";
        } else if (id == R.id.banchan) {
            type = "반찬";
        } else if (id == R.id.sangsun) {
            type = "생선";
        } else if (id == R.id.tuigim_ryu) {
            type = "튀김";
        } else if (id == R.id.bread) {
            type = "빵";
        } else if (id == R.id.soup_ryu) {
            type = "스프";
        }
        food_info.putExtra("type",type);
        food_info.putExtra("ID",ID);
        startActivity(food_info);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    public void Reset(){
        db.execSQL("drop table if exists UserDATA");
        db.execSQL("drop table if exists FoodDATA");
        db.execSQL("drop table if exists Bookmark");
        db.execSQL("create table if not exists Bookmark (_id integer PRIMARY KEY autoincrement, ID text, food_name text, ImgID bigint)");
        db.execSQL("create table if not exists UserDATA (_id integer PRIMARY KEY autoincrement, ID text, Password text, Name text, Number text, gender integer, Login text);");
        db.execSQL("CREATE TABLE if not exists FoodDATA (_id integer PRIMARY KEY autoincrement," + "  type varchar(2) DEFAULT NULL," + "  food_name varchar(8) DEFAULT NULL," + "  amount varchar(3) DEFAULT NULL," + "  material varchar(299) DEFAULT NULL," + "  process varchar(569) DEFAULT NULL" + ",ImgID bigint);");
        db.execSQL("INSERT INTO FoodDATA (type, food_name, amount, material, process, ImgID) VALUES\n" +
                "('한식,면,반찬', '잡채', '4인분', '당면(!250?!g), 물 !6?!컵, 당근 !1?!개, 다진마늘 !1?!작은술(약 !5?!g), 소금 !1/2?!작은술(약 !2.5?!g), 깨소금 !1/2?!작은술(약 !2.5?!g), 소고기(등심) !120?!g, 양파 !1?!개, 파 !3?!쪽, 참기름 !1?!작은술(약 !5?!g), 후추 !1/4?!작은술(약 !0.6?!g)', '1. 끓는 물에 당면을 10-12분 정도 부드럽고 투명해질때까지 삶아준다.\n2. 양파랑 당근은 길게 채썰고 파는 5cm 길이로 잘라준다.\n3. 삶아진 당면을 체에 걸러서 물기를 빼준 후 2-3번 가위로 잘라준다.\n4. 고기는 후라이팬에 기름을 두르고 가늘게 썰어서 다진마늘, 소금, 후추를 넣고 볶아준다.\n5. 후라이팬에 기름을 두르고 양파, 당근, 파를 소금과 후추로 간을 하면서 각각 따로 볶아준다.\n6. 삶은 당면을 큰 그릇에 담고 볶은 고기와 볶은 야채를 넣고 골고루 무친다.\n7. 완성된 잡채 그릇에 담으면 완성.',"+R.drawable.japche+"),"+
                "('한식,고기,반찬', '불고기', '4인분', '소고기(등심 또는 안심) !500?!g, 양파 !2?!개(!200?!g), 대파 !1?!대(!20?!g), 팽이버섯(!50?!g), 곁들임 채소(상추외 잎채소 !100?!g, 풋고추 !2?!개, 깐마늘 !5?!쪽), 쌈장 !2?!TS(!42?!g), 불고기 양념장: 간장 !4?!TS(!72?!g), 후춧가루 !1.4?!ts(!0.6?!g), 배즙 !2?!TS(!30?!g), 생강즙 !1?!TS(!16?!g), 설탕 !4?!TS(!48?!g), 물엿 !2?!TS(!38?!g), 다진 파 !1?!TS(!14?!g), 다진 마늘 !1/2?!TS(!8?!g), 깨소금 !1/2?!TS(!3?!g), 참기름 !1?!TS(!13?!g)', '1. 소고기는 등심이나 안심의 연한 부분을 골라 얇게 썰어 준비한다.\n2. 소고기를 배즙과 설탕, 생강즙에 버무려 재어 놓은 다음, 나머지 양념 재료를 넣고 간이 고루 베게 주물러 30분 이상 재어 둔다.\n3. 대파는 0.5cm 두께로 어슷 썰고, 양파는 0.3cm 두께로 채 썬다. 팽이버섯은 밑동을 잘라 준비한다.\n4. 뜨겁게 달군 팬에 고기와 준비한 채소를 넣고 볶는다.\n5. 곁들임 채소(상추, 깻잎 등의 잎 채소, 풋고추, 마늘)를 준비하여 쌈장과 함께 곁들이면 완성.',"+R.drawable.bulgogi+"),"+
                "('한식,국', 'm미역국', '4인분', '마른 미역(!10?!g), 소고기(양지머리) !100?!g, 물 !1?!L(!1000?!g), 국간장 !1?!큰술(약 !15?!g), 소금 !1?!큰술(약 !15?!g), 참기름 !1?!큰술(약 !13?!g), 다진마늘 !1?!큰술(약 !15?!g)', '1. 마른 미역 10g을 물에 담궈 불리고, 불린 미역은 물기를 빼고 먹기 좋은 크기(5~7cm)로 잘라 준다.\n2. 양지머리는 물에 담궈 핏물을 빼고 먹기 좋은 크기(2cmx4cm)로 잘라 준다.\n3. 후라이팬이나 냄비에 참기름 1큰술을 두르고 양지머리를 넣고 같이 3분 정도 익을 때까지 볶아 준다.\n4. 잘라놓은 불린 미역을 양지머리와 함께 중불에 살살 볶아준다. 센불에 볶으면 팬에 눌러 붙으므로 주의한다.\n5. 볶은 미역과 양지머리에 물을 붓고 센불로 끓여 준다. \n6. 국이 끊으면 국간장과 소금, 다진 마늘을 넣고 20분 정도 끓여주면 맛있는 미역국이 완성.',"+R.drawable.miyukguk+"),"+
                "('한식,찌개', '된장찌개', '1인분', '물 !200?!ml(!200?!g), 애호박 !1/3?!개, 양파!1/2?!개, 청양고추!3?!개, 다진마늘 !1?!큰술(약 !15?!g), 대파 !1/2?!쪽, 된장 !2?!큰술(약 !30?!g), 고춧가루 !1?!큰술(약 !15?!g), 다시멸치 !5?!마리, 두부 !1/2?!모', '1. 애호박, 양파, 청양고추를 먹기좋은 크기로 썰어 다진마늘과 함께 뚝배기에 담아준다.\n2. 뚝배기에 된장 2큰술을 넣고 물을 부어준다.\n3. 고춧가루 1큰술을 넣고 다시멸치 5마리를 넣어 끓여준다.\n4. 보글보글 끓으면 멸치를 건져내고 두부와 대파를 넣고 2분정도만 더 끓여주면 완성(거품은 그때그때 걷어 주는게 좋다).',"+R.drawable.doinjangjjigea+"),"+
                "('한식,반찬', '두부조림', '1인분', '두부 !1?!모, 청양고추 !1?!개, 대파 !1/2?!쪽, 양파 !1/2?!개, 간장 !3?!큰술(약 !45?!g), 고춧가루 !1?!큰술(약 !15?!g), 다진마늘 !1?!큰술(약 !15?!g), 참기름 !1?!큰술(약 !13?!g), 설탕 !1?!큰술(약 !15?!g), 맛술 !1?!큰술(약 !15?!g)', '1. 두부는 먹기좋은 크기로 자른다.\n2. 준비한 간장, 다진마늘, 맛술, 참기름, 설탕, 고춧가루를 섞어 양념장을 만든다.\n3. 양파, 대파, 청양고추를 먹기좋은 크기로 썬다.\n4. 팬에 기름을 두르고 두부를 살짝 튀기듯이 노릇노릇 구워준다.\n5. 두부가 반 정도 잠길 때까지 물 한 컵 정도의 양을 부어준 뒤, 아까 만든 양념과 양파, 대파, 청양고추를 넣어주고 자작하게 끓인다.\n6. 참기름으로 마무리 하면 완성.',"+R.drawable.dubujorim+"),"+
                "('양식,면', '토마토 스파게티', '1인분', '스파게티면(!100?!g), 토마토(토마토홀 통조림)!550?!g, 물!1?!L(!1000?!g), 양파 !1/4?!개(!50?!g), 마늘 !2?!쪽(!10?!g), 올리브유(!10?!g), 베이컨 !5?!줄(!70?!g), 바질 !10?!장(!5?!g), 후추 약간, 소금 약간\\n', '1. 마늘은 편썰고, 양파는 다진다.\n2. 달군 팬에 올리브유를 두르고 마늘과 양파를 넣고 볶다가 베이컨을 넣고 노릇하게 볶는다.\n3. 토마토홀을 넣고 약한 불에서 저어가며 15분간 끓이다가, 소금과 후추로 간한다.\n4. 끓는 물에 스파게티면을 넣고 포장지에 적힌 시간대로 삶는다. \n5. 체에 받쳐 물기를 뺀다.(삶은 스파게티면에 올리브유를 버무려두면 서로 달라붙는 것을 방지할 수 있다.)\n6. 방금 전에 만든 소스에 스파게티를 넣고 잘 2~3분 정도 볶는다.\n7. 불을 끄고 바질을 넣고 가볍게 버무리면 완성.',"+R.drawable.supagatti+"),"+
                "('양식,면', '크림 스파게티', '1인분', '스파게티(!100?!g), 베이컨(!40?!g), 양파(!20?!g), 브로콜리(!25?!g), 밀가루(!8?!g), 버터(!15?!g), 우유 !100?!ml(!100?!g), 물 !100?!ml(!100?!g), 치킨스톡(!4?!g), 올리브유 !28?!ml(!28?!g), 소금 약간, 파슬리 약간, 후추 약간', '1. 약불의 팬에 버터를 넣어 완전히 녹으면 밀가루를 넣어 뭉치지 않게 볶아주고, 치킨스톡을 녹인 육수를 부어가며 뭉치지 않게 재빨리 저어가며 끓인다.(약불에서 눌지 않게 볶아주어야 한다.)\n2. 우유를 넣어 눌지 않도록 저어가며 한소끔 끓이면 불을 끈다.\n3. 스파게티면의 양의 3배 정도(약 300ml) 되는 끓는 물에 소금을 약간 넣어 부채꼴 모양으로 펼쳐 넣어 8분가량 삶는다.\n4. 채반에 받쳐 물기를 제거한 후 올리브유에 버무려 둔다.(올리브유에 삶은 스파게티를 버무려 주면 덜 불는다.)\n5. 팬에 베이컨을 넣고 볶아 기름이 많이 생기면 불을 끄고 키친타월에 받쳐 기름기를 제거한다.\n6. 잘게 다진 양파는 올리브유를 두른 팬에서 볶다가 작게 잘라둔 브로콜리를 넣어 소금간을 약간하고 살짝 볶아 준비한다.(치킨스톡을 사용하지 않고 그냥 물을 사용할 경우에는 크림소스에 소금간을 해야 한다.)\n6. 양파와 브로콜리를 볶은 팬에 삶은 스파게티와 기름을 제거한 베이컨을 넣고 크림소스를 부어 버무린 후 후추와 파슬리를 뿌리면 완성.(기호에 따라 양송이버섯이나 감자 등의 야채를 첨가하여도 좋다.)\n',"+R.drawable.cream+"),"+
                "('양식,고기', '스테이크', '2인분', '채끝등심(!600?!g), 양송이 !3?!개, 토마토 !1/2?!개, 양파 !1/2?!개, 호박 !1/3?!개, 가지 !1/3?!개, 버터(!10?!g), 소금 약간, 후추 약간, 로즈마리(!5?!g)', '1. 채끝의 핏기를 뺀 후 소금과 후추로 간을 해준다.\n2. 가지, 호박, 양파 토마토를 동글하게 썰어준다.\n3. 양송이는 밑 기둥을 짤라주시고 4등분한다.\n4. 버터 10g를 두른 팬에 로즈마리를 넣어 향을 내준후 고기를 투하한다.\n5. 고기를 식히는 동안 양파, 가지, 호박, 토마토, 양송이를 구워 가니쉬를 만들어준다.\n6. 고기와 가니쉬를 플레이팅을 하면 완성.',"+R.drawable.stake+"),"+
                "('양식,스프', '콘스프', '1인분', '옥수수캔(!120?!g), 우유 !120?!ml(!120?!g), 양파 !1/4?!개, 감자 !1/2?!개, 버터 !1?!큰술(약 !15?!g)', '1. 감자와 양파를 얇게 썰어 준비한다.\n2. 달군 팬에 버터를 녹인 후 감자와 양파를 노릇해지도록 볶는다.\n3. 그 뒤 옥수수콘을 넣고 볶는다.\n4. 볶은 재료와 우유를 믹서에 넣고 곱게 갈아준다.\n5. 냄비에 콘스프를 담고 다시한번 스프가 끓을때까지 끓이면 완성.',"+R.drawable.cornsoup+"),"+
                "('양식,빵', '토마토 치즈 토스트', '1인분', '완숙 토마토 !1?!개, 식빵 !2?!개, 모짜렐라치즈 !1?!컵(약 !200?!g), 케첩 !1?!큰술(약 !15?!g), 파슬리가루 약간', '1. 완숙 토마토를 1cm 두께로 동그랗게 썰어준다.\n2. 식빵 2개를 준비해 한쪽 면에 케첩 각각 발라준다.\n3. 그 위에 모짜렐라치즈를 올리고 그 위에 토마토를 올린다.\n4. 남은 치즈를 마저 올리고, 파슬리가루를 뿌린뒤 예열된 200도 오븐에서 10분간 구워주면 완성.(정 오븐이 없으면 프라이팬을 쓰자.)',"+R.drawable.tomato_cheeze_toast+"),"+
                "('일식,생선,밥', '생선초밥', '4인분', '초밥용 생선회(종류는 기호에 따라) !800?!g, 쌀 !3?!컵(!575?!g), 고추냉이(와사비) !30?!g, 환만식초 !180?!ml(!180?!g), 설탕 !120?!ml(!120?!g), 천일염 !60?!ml(!60?!g), 다시마 !2?!조각, 레몬 !1/4?!개, 간장(양조간장) !30?!ml(!30?!g), 고추냉이(와사비) !5?!g', '1. 쌀을 씻어 냄비에 담고, 물을 조금 적게 잡아 다시마 한 조각을 넣어 밥을 한다.\n2. 식초, 소금을 냄비에 넣고 섞은 다음 설탕을 넣고 약한 불에서 설탕이 녹을 때까지 가열하고 불을 끈다.\n3. 2번에 다시마 1조각을 넣고 그대로 식히고 다 식으면 레몬즙을 짜서 넣어준다.(밥이 뜨거울 경우, 바람을 불어 식히며 비벼준다.)\n4. 밥에 방금 만든 물을 넣고 주걱을 세워서 밥알이 상하지 않게 비벼 준비한다.\n5. 손질된 생선살들을 준비한다.\n6. 밥 1스푼을 오른손으로 쥐어 뭉치고, 준비된 초밥용 생선살을 왼손 위에 올린 후 생선살 가운데에 검지로 와사비를 콕 찍듯이 발라준다.\n7. 생선살 위에 뭉친 밥을 올리고, 오른손으로 눌러 밥의 모양을 잡아준 뒤 뒤집어준다.\n8. 접시에 초밥들을 색을 맞춰 담고, 와사비 간장을 곁들여 내어주면 완성.',"+R.drawable.susi+"),"+
                "('일식,고기,튀김', '규카츠', '2인분', '스테이크용 소고기(!450?!g 두께:2cm), 양배추 !1/8?!개, 계란 !1?!개, 밀가루 !2?!T(약 !30?!g), 빵가루 !1?!컵(약 !200?!g), 양배추 소스재료: 마요네즈 !4?!T(약 !60?!g), 식초 !1?!T(약 !15?!g), 설탕 !5?!g, 후추 !5?!g, 규카츠 소스재료: 돈가스 소스(원하는 만큼), 간장(원하는 만큼), 와사비(원하는 만큼)', '1. 준비한 고기를 직사각형 모양으로 맞춰 썰어준다.\n2. 칼끝으로 힘줄을 끊어주고, 칼날로 앞뒤로 살짝살짝 다져준다.\n3. 고기에 소금과 후추를 살짝 뿌려 10분 정도 재워둔다.\n4. 그동안에 양배추를 최대한 얇게 썰어준다.(그래야 식감이 한층 더 살아난다.)\n5. 썬 양배추는 물에 5분 정도 담가준 다음 체에 받쳐 물기를 빼준다.\n6. 준비한 양배추 소스재료를 섞어서 만들어준다.\n7. 10분 재워둔 고기에 밀가루를 골고루 묻혀주고, 소금을 약간 넣은 계란물을 입혀준다.\n8. 고기에 빵가루를 입혀주고, 기름 200도에 중불에서 1분 40초 정도 튀겨준다.\n9. 튀겨진 규카츠를 1cm 간격으로 썰어준다.\n10. 찍어먹을 돈까스 소스나 간장 소스(기호에 따라 와사비를 넣어도 좋다)만 준비해주면 완성.',"+R.drawable.gukach+"),"+
                "('일식,면', 'm미소라멘', '1인분', '라면사리 !1?!인분(약 !110?!g), 삼겹살 !1?1줄, 계란 !1?!개, 숙주 !1?!줌, 실파 조금, 간장 !2?!큰술(약 !30?!g), 설탕 !1?!큰술(약 !12?!g), 청주 !1?!큰술(약 !15?!g), 다시마육수 !500?!ml(약 !500?!g), 미소된장 !1?!큰술, 다진마늘 !1?!큰술, 후추 조금', '1. 다시마를 끓여서 다시마 육수를 만들어준다.(물은 졸아들기 때문에 넉넉하게 넣어준다.)\n2. 분량의 육수재료를 넣고 한소끔 끓여준다.\n3. 반숙 계란을 만들기 위해 찬물에 계란을 넣고 끓이다가 물이 끓으면 4분 후에 계란을 꺼내준다.\n4. 실파를 작게 썰어준다.\n5. 숙주는 끓는 물에 잠깐 넣었다가 바로 빼준다.\n6. 삽결살을 굽는다.(고기가 촉촉해지기위해 구을 때 물을 뿌려준다.)\n7. 간장, 맛술, 설탕을 섞은 소스를 삼겹살에 붓고 졸여준다.\n8. 면을 익히기위해 약 4분간 삶아준다.\n9. 그릇에 면을 담고 육수를 부은 뒤 삼겹살, 계란, 파를 넣어주면 완성. ',"+R.drawable.ramen+"),"+
                "('일식,국', 'm미소시루(일본 된장국)', '2인분', '무 !150?!g, 미소 !45?!g, 이치반다시 재료: 물 !1?!L(!1000?!g), 다시마 !10?!g, 가츠오부시 !20?!g, 육수 재료: 물 !1?!L, 다시마 !10?!g, 국물용 멸치 !10?!g', '1. 무를 5mm 두께로 막대처럼 썬다.\n2. 이치반다시를 만들기위해 찬물에 20분간 다시마를 불린다.\n3. 불린 다시마와 물을 냄비에 넣고 중간 불로 끓인다.\n4. 냄비 가장자리에 작은 거품이 생기면 다시마를 건진 후 가츠오부시를 넣는다.\n5. 한소끔 끓으면 불을 끄고 바로 가츠오부시를 건져낸다.\n6. 육수를 만들기위해 멸치와 다시마를 물에 넣고 30분간 우린다.\n7. 냄비에 우린 물을 붓고 중간 불에서 끓이고 끓어오르면 거품을 걷어 내며 2~3분간 더 끓인다.\n8. 육수를 약간 식힌 후 체에 거른다.\n9. 냄비에 이치반다시와 멸치 다시마 육수, 무를 넣고 중간 불에서 한소금 끓인다가 약한 불로 줄이고 무를 더 익힌 후 불을 끄고 미소를 푼다.\n10. 먹기 직전에 미소시루를 데워 국구릇에 담으면 완성.',"+R.drawable.misosiru+"),"+
                "('일식,면', '가쓰오우동', '1인분', '우동면 !1?!인분(약 !100?!g), 손질한 새우!5?!마리, 오징어 !50?!g, 바지락살 !50?!g, 청경채 !100?!g, 후추 약간, 우동국물 재료: 가쓰오부시 !3?!g, 파뿌리 !2?!개, 다시마 !1?!조각(4*4cm), 쯔유 !1?!T(약 !15?!g), 물 !400?!ml(!400?!g)', '1. 청경채를 세척 후, 크기에따라 2~3등분 해준다.\n2. 새우, 바지락, 오징어도 세척하여 불순물을 제거한다.\n3. 우동사리는 따로 데치지 않고, 흐르는 뜨거운 물에 헹구어 뭉친면을 풀어 준비한다.\n4. 우동 국물을 만들기위해 먼저 냄비에 물400ml를 넣는다.\n5. 물이 끓으면 파뿌리2개와 다시마 한조각을 넣고, 4분간 끓인 다음 조각들을 건져내고 쯔유로 맛을 낸다.\n6. 이 국물에 해산물을 데치고, 한소끔 끓어오르면 청경채를 넣어 3분간 더 끓여준다.\n7. 불순물이 떠오르면 걸러주고, 면을 넣고 약3~4분간 끓여준다.\n8. 해산물 데친것과 가쓰오부시를 얹어주면 완성.',"+R.drawable.gasso+"),"+
                "('중식,면', '짜장면', '4인분', '양파 !1?!개, 당근 !1/3?!개, 감자 !1?!개, 쥬티니 호박 !1/4?!개, 양배추 !2?!장, 대파 !1?!대, 돼지고기 등심(카레용, !150?!g), 칼국수면 !4?!인분(약 !400?!g), 짜장 분말(!100?!g), 물!600?!ml(!600?!g), 청주 !2?!T(약 !30?!g), 다진 마늘 !1/2?!개, 소금 약간, 후추 약간', '1. 청주, 다진 마늘, 소금, 후추를 약간 섞어서 돼지고기 밑간 재료를 만든다.\n2. 돼지고기에 방금 만든 밑간 재료를 넣고 10분 정도 재운다.\n3. 파는 얇게 썰고 양파, 당근, 쥬키니 호박, 감자, 양배추는 1.5cm~2cm 크기로 썬다.\n4. 냄비에 식용유를 두른 뒤 파를 먼저 볶는다.\n5. 양파와 돼지고기를 넣고 볶다가 당근과 감자를 넣고 같이 볶는다.\n6. 어느 정도 익으면 양배추와 쥬키니 호박을 넣고, 소금과 후추로 밑간을 한다.\n7. 물을 넣고 약한 불에서 끓이다가 재료가 다 익으면 짜장 분말을 넣고 걸쭉해질 때가지만 끓인다.\n8. 끓는 물에 칼국수면을 약 5분 정도 삶는다.\n9. 찬물에 면을 비벼서 헹군 후 체에 밭쳐 물기를 뺀다.\n10. 칼국수면을 그릇에 담은 후 짜장 소스를 뿌리면 완성.',"+R.drawable.jjajangmyun+"),"+
                "('중식,면', '짬뽕', '1인분', '파기름 !3?!큰술(약 !45?!g), 간장 !1?!큰술(약 !15?!g), 배추(!20?!g), 양상추(!20?!g), 당근(!10?!g), 홍새우(!10?!g), 홍합(!30?!g), 소금 !1/2?!큰술(약 !7?!g), 고춧가루 !2?!큰술(약 !25?!g), 파(!5?!g), 생강(!5?!g), 홍합육수 !700?!ml(!700?!g)', '1. 배추, 양배추, 당근을 손질한다.\n2. 약한 불에 기름을 달구고 파, 생강을 넣고 볶다가 파, 생강 향이 날 때쯤 생강은 건져낸다.\n3. 건새우를 넣고 볶다가 새우 향이 날 때즘 불을 세게 한다.\n4. 손질한 배추, 양배추, 당근을 넣고 같이 볶는다.\n5. 간장 한 큰술(약 15g)을 넣고 채소 숨이 죽을 때까지 볶는다.\n6. 고춧가루 2큰술(약 25g), 홍합 육수 1큰술을 넣고 볶는다.\n7. 홍합을 넣고 같이 볶다가 육수 700ml를 넣고 끓인다.\n8. 면은 밑간을 하기 위해 끓는 물에 소금을 넣고 면을 삶는다.\n9. 물이 끓고 면이 떠오르면 차가운 물을 붓는다.\n10. 면을 건져 찬물에 헹궈주고 국물이 뽀얗게 우러나면 면 위에 부으면 완성.',"+R.drawable.jjambbong+"),"+
                "('중식,튀김', '깐풍만두', '2인분', '냉동 만두 !10?!개, 홍고추 !2?!개, 청고추 !2?!개, 대파 !1?!대, 마늘 !3?!쪽, 물 !4?!큰술(약 !60?!g), 식초 !2?!큰술(약 !30?!g), 설탕 !1?!큰술(약 !12?!g), 굴 소스 !1?!큰술(약 !15?!g), 간장 !2?!큰술(약 !30?!g), 식용유 !2?!큰술(약 !30?!g)', '1. 고추를 반으로 갈라 씨를 빼고 작은 네모 모양으로 잘라준다.\n2. 마늘은 편으로 썰고 대파는 송송 썰어준다.\n3. 그릇에 물, 식초, 간장, 설탕, 굴 소스를 넣고 잘 섞어주어 양념장을 만든다.\n4. 달군 팬에 식용유를 두르고 만두를 올린다.\n5. 센 불에서 겉을 노릇하게 익힌 다음 불을 줄여 마저 만두를 익혀준다.\n6. 펜에 기름을 다시 두르고 마늘, 대파, 고추를 넣고 센 불에 볶아준다.\n7. 좀 전에 섞어둔 양념장을 팬에 붓고 반 정도 줄어들 때까지 센 불을 유지하며 끓여준다.\n8. 소스가 바글바글 끟으면 구운 만두를 넣어 양념을 골고루 묻혀주면 완성.',"+R.drawable.gganpungmandu+"),"+
                "('중식,밥', '볶음밥', '1인분', '즉석밥 !1?!개, 계란 !2?!개, 대파 흰부분 !1?1뿌리, 당근 !1/4?!개, 양송이 버섯 !1?!개, 고추 !2?!개, 소금 약간, 포도씨유 !2?!큰술(약 !30?!g)', '1. 대파, 당근, 고추, 양송이 버섯을 잘게 썰어준다.\n2. 팬을 달구고 기름을 살짝 두른 후 계란물을 붓고 익기 전에 저어준다.\n3. 저어주다가 스크램블에그가 만들어지면 다른 그릇에 잠시 옮겨준다.\n4. 다시 팬에 기름을 두르고 대파를 먼저 넣어 파기름을 낸다.\n5. 파향이 올라오면 채소들을 넣고 바짝 볶아준다.\n6. 채소들의 숨이 죽으면 밥을 넣고 고루 볶아준다.\n7. 밥이 다 섞였으면 스크램블에그를 넣어 고루 섞어주면 완성.',"+R.drawable.bokkeumbob+"),"+
                "('중식,밥,반찬', 'm마파두부', '2인분', '두부 !1?!모, 다진마늘 !2?!큰술(약 !30?!g), 다진돼지고기 !1/4?!컵, 썬 대파 !1/4?!컵, 다진양파 !1/2?!컵, 고춧가루 !1/3?!컵, 설탕 !1/3?!컵, 간장 !1?!큰술(약 !15?!g), 된장 !1/2?!큰술(약 !7?!g), 물 !1?!컵, 전분물 !2?!큰술(약 !30?!g)', '1. 두부 한 모를 깍둑썬다.\n2. 식용유를 두른 팬에 다진 마늘, 다진 돼지고기, 다진 양파, 대파를 넣고 먼저 볶아준다.\n3. 고기가 익으면 고춧가루, 설탕, 간장, 된장, 물을 넣어 양념장을 만든다.\n4. 만들어진 양념장에 깍둑 썬 두부를 넣고 끓이다가 전분물을 넣고 농도가 잡힐 때 까지 빨리 섞은 뒤 가스불을 끈다. (전분물을 넣은 상태에서 오랫동안 끓이면 전분물끼리 뭉치기 때문에 가스불을 꺼줘야 한다.)\n5. 참기름을 한바퀴 둘러주면 완성.\n',"+R.drawable.mapadubu+");");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!ID.equals("unknown")) {
            Intent intent01 = getIntent();
            ID = intent01.getStringExtra("ID");
            Log.d("id check",ID);
            Cursor cursor = db.rawQuery("select * from UserDATA where ID = '" + ID + "'", null);
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
            if (!nickname.getText().toString().equals(name)) {
                nickname.setText(name);
                nickname.setTextSize(20);
            }
            customAdapter.notifyDataSetChanged();
        }
    }
    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
        db.close();//블루투스 중지
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

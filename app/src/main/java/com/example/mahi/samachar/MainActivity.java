package com.example.mahi.samachar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    RecyclerView listview,categories_list;
    static ArrayList<Article> articles;
    MyRecyclerAdapter adapter;
    static int pages;
    static int current_page;
    Toolbar toolbar;
    ActionBar actionBar;
    private SearchView searchView;
    public static final int REQUEST_CODE_ADV_SEARCH=1;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean status;
    private Uri uri;
    FloatingActionButton fab_next,fab_previous;
    private RecyclerView.LayoutManager layoutManager;
    public static final String SAVE_KEY="save";
    int[] drawables;
    String[] titles;
    TabLayout tabLayout;
    String type,country,category,keyword;
    TextView tv_error,tv_noData;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    public static boolean savedData;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_activity_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        if(s!=null&&!s.equals("")) {
                            keyword=s;
                            uri=ApiUtil.getQueryUri(s);
                            performSearching(uri);
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );
        ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchClose.setImageResource(R.drawable.ic_search_black_24dp);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:{
                if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(GravityCompat.START);
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            }
            case R.id.action_settings:{
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.action_advance_search:{
                Intent intent = new Intent(this,Main2Activity.class);
                startActivityForResult(intent,REQUEST_CODE_ADV_SEARCH);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawables=new int[]{
                R.drawable.general,
                R.drawable.business,
                R.drawable.entertainment,
                R.drawable.health,
                R.drawable.sports,
                R.drawable.science,
                R.drawable.technology
        };
        titles= new String[]{
                "General",
                "Business",
                "Entertainment",
                "Health",
                "Sports",
                "Science",
                "Technology"
        };
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        tv_error=findViewById(R.id.tv_error);
        tv_noData = findViewById(R.id.tv_no_data);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        listview = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab_next = findViewById(R.id.fab_next_page);
        fab_previous = findViewById(R.id.fab_previous_page);
        tabLayout = findViewById(R.id.tabLayout);
        actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        }
        for(int i=0;i<drawables.length;i++){
            tabLayout.addTab(tabLayout.newTab().
                    setIcon(drawables[i]).
                    setText(titles[i])
            );
        }
        if(getResources().getDisplayMetrics().widthPixels<getResources().getDisplayMetrics().heightPixels) {
            layoutManager = new LinearLayoutManager(this);
        }else{
            layoutManager = new GridLayoutManager(this,2);
        }
        DatabaseOpenHelper openHelper = new DatabaseOpenHelper(this);
        openHelper.getWritableDatabase();

        listview.setLayoutManager(layoutManager);
        current_page=1;
        final Intent intent = getIntent();
        if(intent!=null){
            savedData=getIntent().getBooleanExtra(SAVE_KEY,false);
            if( savedData) {
                showSavedData();
            }else {
                tabLayout.setVisibility(View.VISIBLE);
                type = "";
                country = PreferenceManager.getDefaultSharedPreferences(this).getString("country", "in");
                category = "";
                keyword = "";
                uri = ApiUtil.getQueryUri(type, country, category, keyword);
                swipeRefreshLayout.setEnabled(true);
            }
            performSearching(uri);
        }

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Uri newuri = ApiUtil.getQueryUri(type, country, category, keyword);
                         newuri = newuri.buildUpon().appendQueryParameter("page", String.valueOf(current_page)).build();
                        performSearching(newuri);
                    }
                }
        );

        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        type="top-headlines";
                        category=titles[tab.getPosition()];
                        Uri newuri = ApiUtil.getQueryUri(type,country,category,keyword);
                        performSearching(newuri);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.action_nav_main_news:{
                                tabLayout.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setEnabled(true);
                                TabLayout.Tab tab = tabLayout.getTabAt(0);
                                tab.select();
                                navigationView.setCheckedItem(item.getItemId());
                                type="";
                                country= PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("country","in");
                                category="";
                                keyword="";
                                uri = ApiUtil.getQueryUri(type, country, category, keyword);
                                performSearching(uri);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            }

                            case R.id.action_nav_saved_data:{
                                showSavedData();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            }

                            case R.id.action_nav_settings:{
                                navigationView.setCheckedItem(item.getItemId());
                                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                                MainActivity.this.startActivity(intent);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            }

                            default:{
                                return false;                            }
                        }
                    }
                }
        );
    }

    private void showSavedData() {
        listview.setAdapter(new MySavedDataAdapter(this));
        swipeRefreshLayout.setEnabled(false);
        tabLayout.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.GONE);
    }

    public void performSearching(Uri uri) {
        new MyAsyncTask().execute(uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADV_SEARCH && resultCode==RESULT_OK){
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            Bundle bundle = data.getBundleExtra("query");
            type=bundle.getString(ApiUtil.TYPE,"");
            country=bundle.getString(ApiUtil.COUNTRY,"");
            category=bundle.getString(ApiUtil.CATEGORY,"");
            keyword=bundle.getString(ApiUtil.QUERY_PARAMETER,"");
            if(type.equals("everything")){
                tabLayout.setVisibility(View.GONE);
                actionBar.setTitle(keyword);
            }else{
                tabLayout.dispatchSetSelected(true);
            }
            uri = ApiUtil.getQueryUri(
                    type,
                    country,
                    category,
                    keyword
            );
            current_page=1;
            performSearching(uri);
        }
    }

    public void showProgress(){
        swipeRefreshLayout.setRefreshing(true);
        tv_noData.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);

    }
    public void showlist(){
        swipeRefreshLayout.setRefreshing(false);
        tv_noData.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
        listview.setVisibility(View.VISIBLE);
    }
    public void showError(){
        swipeRefreshLayout.setRefreshing(false);
        tv_noData.setVisibility(View.INVISIBLE);
        tv_error.setVisibility(View.VISIBLE);
        listview.setVisibility(View.VISIBLE);
    }
    public void showDataNotFound(){
        swipeRefreshLayout.setRefreshing(false);
        tv_noData.setVisibility(View.VISIBLE);
        tv_error.setVisibility(View.INVISIBLE);
        listview.setVisibility(View.INVISIBLE);
    }


    public void onClickPagesFab(View view){
        switch (view.getId())
        {
            case R.id.fab_next_page:{
                current_page++;
                Uri newuri = uri.buildUpon().appendQueryParameter("page",String.valueOf(current_page)).build();
                performSearching(newuri);
                break;
            }

            case R.id.fab_previous_page:{
                current_page--;
                Uri newuri=uri.buildUpon().appendQueryParameter("page",String.valueOf(current_page)).build();
                performSearching(newuri);
                break;
            }
        }
    }

    public class MyAsyncTask extends AsyncTask<Uri,Void,String>{

        String result;

        @Override
        protected String doInBackground(Uri... uris) {
            Uri uri = uris[0];
            result=ApiUtil.getJSONresult(uri);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result==null||result.equals("")){
                showError();
            }else {
                try {
                    articles = ApiUtil.getData(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (articles.size() == 0||pages==0) {
                    showDataNotFound();
                } else {
                    adapter = new MyRecyclerAdapter(MainActivity.this, articles);
                    listview.setAdapter(adapter);
                    showlist();

                    if (current_page < pages) {
                        fab_next.setVisibility(View.VISIBLE);
                        fab_next.setClickable(true);
                    } else {
                        fab_next.setVisibility(View.INVISIBLE);
                        fab_next.setClickable(false);
                    }
                    if (current_page > 1) {
                        fab_previous.setVisibility(View.VISIBLE);
                        fab_previous.setClickable(true);
                    } else {
                        fab_previous.setVisibility(View.INVISIBLE);
                        fab_previous.setClickable(false);
                    }
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }
    }
}

package com.example.mahi.samachar;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    String type,country,category,keyword;
    EditText et_category,et_keyword;
    Spinner spinner;
    TextInputLayout til_cat,til_keyword;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        spinner = findViewById(R.id.sp_country);
        et_category = findViewById(R.id.et_category);
        et_keyword = findViewById(R.id.et_keyword);
        til_cat = findViewById(R.id.til_category);
        til_keyword = findViewById(R.id.til_keyword);
        toolbar = findViewById(R.id.toolbar_adv);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.country_name,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String c;
                        c= (String) adapterView.getItemAtPosition(i);
                        if(c.toLowerCase().equals("all")){
                            country="";
                        }else{
                            country=getResources().getStringArray(R.array.country_code)[i];
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        country="";
                    }
                }
        );
    }

    public void onClickRadioButton(View view){
        switch (view.getId()){
            case R.id.rb_everything:{
                type="everything";
                til_cat.setEnabled(false);
                et_category.setEnabled(false);
                et_category.setAlpha(0.3f);
                til_cat.setAlpha(0.3f);
                spinner.setEnabled(false);
                spinner.setEnabled(false);
                spinner.setAlpha(0.3f);
                spinner.setAlpha(0.3f);
                break;
            }

            case R.id.rb_top_headlines:{
                type="top-headlines";
                til_cat.setEnabled(true);
                et_category.setEnabled(true);
                et_category.setAlpha(1f);
                til_cat.setAlpha(1f);
                spinner.setEnabled(true);
                spinner.setEnabled(true);
                spinner.setAlpha(1f);
                spinner.setAlpha(1f);
                break;
            }
        }
    }

    public void onClickButton(View view){
        category = et_category.getText().toString();
        keyword = et_keyword.getText().toString();
        if(country.equals("")&&category.equals("")&&keyword.equals("")){
            Toast.makeText(this,"Please enter details of atlest one more field",Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ApiUtil.TYPE, type == null ? "" : type);
            bundle.putString(ApiUtil.COUNTRY, country == null ? "" : country);
            bundle.putString(ApiUtil.CATEGORY, category == null ? "" : category);
            bundle.putString(ApiUtil.QUERY_PARAMETER, keyword == null ? "" : keyword);
            intent.putExtra("query", bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

package com.example.mahi.samachar;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class ArticleDescriptionActivity extends AppCompatActivity {

    TextView tvTitle,tvAuthor,tvDate,tvTime,tvDescription;
    ImageView imgDesc;
    Article article;
    int position;
    FloatingActionButton fab_save,fab_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_description);
        tvTitle = findViewById(R.id.tv_title_desc);
        tvAuthor = findViewById(R.id.tv_author_desc);
        tvDate = findViewById(R.id.tv_date_desc);
        tvTime = findViewById(R.id.tv_time_desc);
        tvDescription = findViewById(R.id.tv_description_desc);
        imgDesc = findViewById(R.id.img_desc);
        fab_save=findViewById(R.id.fab_save);
        fab_more=findViewById(R.id.fab_more);
        Intent intent = getIntent();
        if(intent!=null)
        {
            article = intent.getParcelableExtra("position");
            if(MainActivity.savedData){
                 fab_save.setVisibility(View.GONE);
            }
        }
        setDisplay();
    }

    public void setDisplay(){
        tvTitle.setText(article.title);
        tvAuthor.setText(article.author.equals("null")?"":article.author);
        tvDescription.setText(article.description);
        tvDate.setText(article.date);
        tvTime.setText(article.time);
        Picasso.get()
                .load(Uri.parse(article.thumbnail))
                .fit()
                .into(imgDesc);
    }

    public void onClickMoreFab(View view){
        switch (view.getId()) {

            case R.id.fab_more: {
                Uri uri = Uri.parse(article.moreUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            }

            case R.id.fab_save:{
                if(DatabaseOpenHelper.insertData(this, article)==-1)
                {
                    Snackbar.make(findViewById(R.id.parent_frame),"Unable to save data ",Snackbar.LENGTH_SHORT)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }else {

                    Snackbar.make(findViewById(R.id.parent_frame),"Data saved successfully ",Snackbar.LENGTH_SHORT)
                            .setAction("OPEN", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ArticleDescriptionActivity.this,MainActivity.class);
                                    intent.putExtra(MainActivity.SAVE_KEY,true);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                }
            }

        }
    }
}

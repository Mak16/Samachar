package com.example.mahi.samachar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MySavedDataAdapter  extends RecyclerView.Adapter<MySavedDataAdapter.MyViewHolder>{

    ArrayList<Article> articles;
    Context context;

    public MySavedDataAdapter(Context context){
        this.context=context;
        SQLiteDatabase database = new DatabaseOpenHelper(context).getReadableDatabase();
        articles = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseOpenHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()){
            Article article = new Article(
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_THUMBNAIL)),
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_TIME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_MORE))
            );
            articles.add(article);
        }
        cursor.close();
        database.close();
    }
    @NonNull
    @Override
    public MySavedDataAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MySavedDataAdapter.MyViewHolder holder, final int position) {
        holder.tvTitle.setText(articles.get(position).title);
        if(articles.get(position).thumbnail==null || articles.get(position).thumbnail==""){
            holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.news));
        }else {
            Picasso.get().load(Uri.parse(articles.get(position).thumbnail))
                    .fit()
                    .into(holder.imageView);
        }
        holder.view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,ArticleDescriptionActivity.class);
                        intent.putExtra("position",articles.get(position));
                        context.startActivity(intent);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ImageView imageView;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_item_article_title);
            imageView = itemView.findViewById(R.id.item_article_img);
            this.view=itemView;
        }
    }

}
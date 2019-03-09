package com.example.mahi.samachar;

import android.content.Context;
import android.content.Intent;
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

public class MyRecyclerAdapter  extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>{

    ArrayList<Article> articles;
    Context context;

    public MyRecyclerAdapter(Context context,ArrayList<Article> arrayList){
        this.context=context;
        this.articles = arrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
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

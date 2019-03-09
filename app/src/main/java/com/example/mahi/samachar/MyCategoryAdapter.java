package com.example.mahi.samachar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCategoryAdapter  extends RecyclerView.Adapter<MyCategoryAdapter.MycategoryViewholder>{

    Drawable drawables[];
    Context context;
    String titles[];

    MyCategoryAdapter(Context context){
        this.context=context;
        drawables=new Drawable[]{
                context.getResources().getDrawable(R.drawable.business),
                context.getResources().getDrawable(R.drawable.entertainment),
                context.getResources().getDrawable(R.drawable.health),
                context.getResources().getDrawable(R.drawable.sports),
                context.getResources().getDrawable(R.drawable.science),
                context.getResources().getDrawable(R.drawable.technology)
        };
        titles = new String[]{
                "Business",
                "Entertainment",
                "Health",
                "Sports",
                "Science",
                "Technology"
        };
    }

    @NonNull
    @Override
    public MycategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.category_items,parent,false);
        return new MycategoryViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MycategoryViewholder holder, int position) {
        holder.category_image.setImageDrawable(drawables[position]);
        holder.category_title.setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return drawables.length;
    }

    public class MycategoryViewholder extends RecyclerView.ViewHolder{

        ImageView category_image;
        TextView category_title;

        public MycategoryViewholder(View itemView) {
            super(itemView);
            category_image=itemView.findViewById(R.id.category_item_img);
            category_title=itemView.findViewById(R.id.category_item_title);
        }

    }
}

package com.example.mywallpaper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mywallpaper.R;
import com.example.mywallpaper.model.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwiperAdapter extends RecyclerView.Adapter<SwiperAdapter.SwiperHolder> {
    private List<Photo> list= new ArrayList<>();
    public static onDataPass dataPass;

    public void setList(List<Photo> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SwiperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swiper_item,parent,false);
        return new SwiperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwiperHolder holder, int position) {
        Random random = new Random();
        int color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));

holder.constraintLayout.setBackgroundColor(color);
        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getUrlL())
                .timeout(6500)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.constraintLayout.setBackgroundColor(Color.TRANSPARENT);
                        return false;
                    }
                })
                .into(holder.imageView);

            holder.clickListener(position);
    }

    @Override
    public int getItemCount() {
        if (list!=null) return list.size();
        return 0;
    }

    class SwiperHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ConstraintLayout constraintLayout;
        private ImageButton saveBtn;
        private Button btnApplyWall;
        public SwiperHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
            constraintLayout=itemView.findViewById(R.id.consLayout);

            saveBtn=itemView.findViewById(R.id.btnSave);
            btnApplyWall=itemView.findViewById(R.id.applyWallpaper);
        }
        private void clickListener(final int position){
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Here we will convert image to bitmap
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    dataPass.onImageSave(position,bitmap);
                }
            });

            btnApplyWall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    dataPass.onApplyImage(position,bitmap);
                }
            });
        }
    }

    public interface  onDataPass{
        void onImageSave(int position, Bitmap bitmap);
        void onApplyImage(int position,Bitmap bitmap);
    }
    public void onDataPass(onDataPass dataPass){
        SwiperAdapter.dataPass =dataPass;
    }
}

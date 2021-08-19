package com.example.mywallpaper.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.mywallpaper.R;
import com.example.mywallpaper.adapter.WallpaperAdapter;
import com.example.mywallpaper.model.Images;
import com.example.mywallpaper.other.EndlessRecyclerViewScrollListener;
import com.example.mywallpaper.other.PaginationScrollListener;
import com.example.mywallpaper.service.ImageInstance;
import com.example.mywallpaper.service.ImageService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String METHOD="flickr.favorites.getList";
    private static final String API_KEY="5ff0ea0dd15a3d08b3762cb1d14934d0";
    private static final String USER_ID="191866964@N06";
    private static final String EXTRAS="views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o";
    private int PER_PAGE=10;
    private int PAGE = 1;
    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage=3;
    private static final String FORMAT = "json";
    private static final int NOJSONCALLBACK=1;
    private EndlessRecyclerViewScrollListener scrollListener;

    private ProgressBar progressBar;
    private RelativeLayout progressLoadMore;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private WallpaperAdapter adapter;
    public static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadImage(PAGE);
        progressDialog.show();
    }


    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progress);
        adapter = new WallpaperAdapter();
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        progressLoadMore = findViewById(R.id.progressLoadMore);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                progressLoadMore.setVisibility(View.VISIBLE);
                loadImage(page);
            }
        };

    }
    private void loadMore(int page) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadImage(page);
                isLoading =false;
                progressBar.setVisibility(View.VISIBLE);
                if (PAGE==totalPage){
                    isLastPage=true;
                }
            }
        },2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.about){
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
        }
        return true;
    }

    private void loadImage(int page){
        Retrofit retrofit = ImageInstance.getInstance(this);
        ImageService imageService = retrofit.create(ImageService.class);
        imageService.getListPhoto(METHOD,API_KEY,USER_ID,EXTRAS,PER_PAGE,page,FORMAT,NOJSONCALLBACK).enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                if (progressLoadMore.getVisibility()==View.VISIBLE){
                    progressLoadMore.setVisibility(View.GONE);
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Images e = response.body();
                if (e == null) return;
                adapter.setList(e.getPhotos().getPhoto());
                Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<Images> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("TAG",""+t.getMessage());
                Toast.makeText(MainActivity.this, "Call API failed", Toast.LENGTH_LONG).show();
            }
        });
    }



}
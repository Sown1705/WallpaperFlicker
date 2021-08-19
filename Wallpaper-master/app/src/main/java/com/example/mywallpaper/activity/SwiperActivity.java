package com.example.mywallpaper.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mywallpaper.R;
import com.example.mywallpaper.adapter.SwiperAdapter;
import com.example.mywallpaper.model.Images;
import com.example.mywallpaper.service.ImageInstance;
import com.example.mywallpaper.service.ImageService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SwiperActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    public static int position, currentPosition;
    private SwiperAdapter adapter;
    private static final String METHOD="flickr.favorites.getList";
    private static final String API_KEY="5ff0ea0dd15a3d08b3762cb1d14934d0";
    private static final String USER_ID="191866964@N06";
    private static final String EXTRAS="views, media, path_alias, url_sq, url_t, url_s, url_q, url_m, url_n, url_z, url_c, url_l, url_o";
    private int PER_PAGE=10;
    private int PAGE = 1;
    private int count =9;
    private static final String FORMAT = "json";
    private static final int NOJSONCALLBACK=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiper);
        init();
        adapter = new SwiperAdapter();
        loadImage(PAGE);
        viewPager.setAdapter(adapter);
        Log.e("position",""+position);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                Log.e("currentPosition",""+currentPosition);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (position==count){
                    count+=10;
                    PAGE++;
                    loadImage(PAGE);
                }
            }
        });

        clickListener();
    }

    private void clickListener() {

        adapter.onDataPass(new SwiperAdapter.onDataPass() {
            @Override
            public void onImageSave(int position, Bitmap bitmap) {
                Dexter.withContext(SwiperActivity.this)
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    saveImage(bitmap);
                                } else {
                                    Toast.makeText(SwiperActivity.this, "Please allow permission", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        }).check();
            }

            @Override
            public void onApplyImage(int position, Bitmap bitmap) {
                WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());

                try {
                    manager.setBitmap(bitmap);
                    Toast.makeText(SwiperActivity.this, "Wallpaper set as background", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SwiperActivity.this, "Failed to set as wallpaper", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //save image
    private void saveImage(Bitmap bitmap) {
        String time = "WallpaperImage" + System.currentTimeMillis();

        String imageName = time + ".png";
        //duong dan anh
        File path = Environment.getExternalStorageDirectory();

        File dir = new File(path + "/DCIM/Wallpaper App");
        boolean is = dir.mkdirs();
        Log.d("result", String.valueOf(is));
        File file = new File(dir, imageName);

        OutputStream out;
        try {
            out = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            //chuyen doi anh thanh png
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            bos.flush();
            bos.close();
            Toast.makeText(this, "Success to save image !", Toast.LENGTH_SHORT).show();
            ;

        } catch (Exception ex) {
            ex.printStackTrace();
            ;
            Toast.makeText(this, "Failed to save image !", Toast.LENGTH_SHORT).show();
            ;
        }
    }

    private void init() {
        viewPager = findViewById(R.id.viewPager);
        position = getIntent().getIntExtra("position", -1);
        Log.e("pos1",""+position);
    }
    //load image
    private void loadImage(int page){
        Retrofit retrofit = ImageInstance.getInstance(this);
        ImageService imageService = retrofit.create(ImageService.class);
        imageService.getListPhoto(METHOD,API_KEY,USER_ID,EXTRAS,PER_PAGE,page,FORMAT,NOJSONCALLBACK).enqueue(new Callback<Images>() {
            @Override
            public void onResponse(Call<Images> call, Response<Images> response) {
                Images e = response.body();
                if (e == null) return;
                adapter.setList(e.getPhotos().getPhoto());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position, true);
                    }
                }, 200);

            }
            @Override
            public void onFailure(Call<Images> call, Throwable t) {
                Log.e("TAG",""+t.getMessage());
            }
        });
    }
}
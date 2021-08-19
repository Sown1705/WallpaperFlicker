package com.example.mywallpaper.service;

import com.example.mywallpaper.model.Images;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageService {
    //https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/Search/ImageSearchAPI?q=taylor%20swift&pageNumber=1&pageSize=10&autoCorrect=true
    //X-RapidAPI-Key: "x-rapidapi-key:0f3958b02bmsh5384da24ab89880p1663a4jsn928eb910a6c5"
    //X-RapidAPI-Host: "x-rapidapi-host:contextualwebsearch-websearch-v1.p.rapidapi.com"

    //https://www.flickr.com/services/rest/?
    // method=flickr.favorites.getList
    // &api_key=5ff0ea0dd15a3d08b3762cb1d14934d0
    // &user_id=191866964%40N06
    // &extras=views%2C+media%2C+path_alias%2C+url_sq%2C+url_t%2C+url_s%2C+url_q%2C+url_m%2C+url_n%2C+url_z%2C+url_c%2C+url_l%2C+url_o
    // &per_page=20
    // &page=1
    // &format=json
    // &nojsoncallback=1

//    @Headers({
//            "x-rapidapi-key:0f3958b02bmsh5384da24ab89880p1663a4jsn928eb910a6c5",
//            "x-rapidapi-host:contextualwebsearch-websearch-v1.p.rapidapi.com"
//    })
    @GET("/services/rest/")
    Call<Images> getListPhoto(@Query("method") String method,
                              @Query("api_key") String api_key,
                              @Query("user_id") String user_id,
                              @Query("extras") String extras,
                              @Query("per_page") int per_page,
                              @Query("page") int page,
                              @Query("format") String format,
                              @Query("nojsoncallback") int nojsoncallback);
}

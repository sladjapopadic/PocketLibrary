package com.example.pocketlibrary;

import com.example.pocketlibrary.data.BooksObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BooksAPI {

    @GET("books/v1/volumes")
    Call<BooksObject> findBooks(@Query("q") String q, @Query("key") String key,@Query("maxResults") int maxResults);
}

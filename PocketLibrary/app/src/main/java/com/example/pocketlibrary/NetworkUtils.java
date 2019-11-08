package com.example.pocketlibrary;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    public static String sessionId;
    public static String apiKey = "AIzaSyCtkc2Ejy1fIIvgbVwrY6jzWFYopIBcEkk";
    public static String baseUrl = "https://www.googleapis.com/";
    public static Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
    public static BooksAPI booksAPI = retrofit.create(BooksAPI.class);
}

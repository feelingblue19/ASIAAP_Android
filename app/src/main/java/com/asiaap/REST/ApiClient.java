package com.asiaap.REST;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.asiaap.LoginAdmin;
import com.asiaap.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://192.168.19.140/8900/public/api/";
//    public static final String BASE_URL = "http://172.17.63.193:81//api/";
    public static Retrofit retrofit = null;

    private static Context applicationContext = MainActivity.getContextOfApplication();

    private static SharedPreferences sp;
    private static final String name = "myShared";
    public static final int mode = Activity.MODE_PRIVATE;
    private static String token;
    private static Context context;

    public static Retrofit getClient() {
            sp = applicationContext.getSharedPreferences(name, mode);
            token = sp.getString("token", "0");

            Log.d("token123", token);

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            }).build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }

}

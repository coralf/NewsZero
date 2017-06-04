package com.newt.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by T on 2017/5/24.
 */

public class HttpUtil {


    public static void sendHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request= new Request.Builder().url(address).build();
        okHttpClient.newCall(request).enqueue(callback);
    }


}

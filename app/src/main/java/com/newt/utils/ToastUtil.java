package com.newt.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by T on 2017/5/24.
 */

public class ToastUtil {

    public static void show(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

}

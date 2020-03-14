package com.bhex.wallet.common.config;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.bhex.tools.utils.LogUtils;

import java.io.File;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 15:39
 */
public class BHFilePath {

    public static String PATH_KEYSTORE;

    /*public static void printfPath(Context context){
        //
        String ex_path_1 = Environment.getDataDirectory().getAbsolutePath();
        LogUtils.d("BHFilePath==>:","ex_path_1=="+ex_path_1);


        File ex_path_2 = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        LogUtils.d("BHFilePath==>:","ex_path_2=="+ex_path_2);

        String path_2 = context.getFilesDir().getAbsolutePath();
        LogUtils.d("BHFilePath==>:","path_2=="+path_2);

        //context.getF

    }*/

    public static void initPath(Context context){
        File ksDir = new File(context.getFilesDir().getAbsolutePath(),"ks");

        /*if(!ksDir.exists()){
            ksDir.mkdirs();
        }*/
        PATH_KEYSTORE = ksDir.getAbsolutePath();
    }
}

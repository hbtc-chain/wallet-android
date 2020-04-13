package com.bhex.tools.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/23
 * Time: 23:40
 */
public class FileUtil {

    /**
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String loadStringByAssets(Context context,String fileName) {

        StringBuilder sb = new StringBuilder();

        try{
            AssetManager am = context.getAssets();

            BufferedReader bf = new BufferedReader(new InputStreamReader(am.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line).append(" ");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}

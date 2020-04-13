package com.bhex.network.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongyanggong
 * on 2019/5/7
 */
public class JsonUtils {

    private static Gson mG;
    private static JsonParser mParser;

    private static void initG() {
        if (mG == null) {
            synchronized (JsonUtils.class) {
                if (mG == null) {
                    mG = new Gson();
                    mParser = new JsonParser();
                }
            }
        }
    }

    public static JsonParser getParser() {
        initG();
        return mParser;
    }

    public static String getAsString(String json, String memberName) {
        initG();
        JsonObject object = mParser.parse(json).getAsJsonObject();
        if (!object.has(memberName))
            return "";
        return object.get(memberName).getAsString();
    }

    public static int getAsInt(String json, String memberName) {
        initG();
        JsonObject object = mParser.parse(json).getAsJsonObject();
        if (!object.has(memberName))
            return 0;
        return object.get(memberName).getAsInt();
    }

    public static String getAsJsonObject(String json, String memberName) {
        initG();
        JsonObject object = mParser.parse(json).getAsJsonObject();
        if (!object.has(memberName))
            return "";
        return object.get(memberName).getAsJsonObject().toString();
    }

    @NonNull
    public static JsonArray getAsJsonArray(String json, String memberName) {
        initG();
        JsonObject object = mParser.parse(json).getAsJsonObject();
        if (!object.has(memberName))
            return new JsonArray();
        return object.get(memberName).getAsJsonArray();
    }

    public static String toJson(Object object) {
        initG();
        return mG.toJson(object);
    }


    public static <T> T fromJson(String json, Class<T> classOfT) {
        initG();
        return mG.fromJson(json, classOfT);
    }

    public static <T> List<T> getListFromJson(String json, String memberName, Class<T> classOfT) {
        initG();
        return getListFromJson(getAsJsonArray(json, memberName).toString(), classOfT);
    }

    public static <T> List<T> getListFromJson(String json, Class<T> classOfT) {
        initG();
        List<T> list = new ArrayList<>();
        JsonArray array = mParser.parse(json).getAsJsonArray();
        for (JsonElement je : array) {
            T item = mG.fromJson(je, classOfT);
            list.add(item);
        }
        return list;
    }

}

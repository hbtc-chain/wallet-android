package com.bhex.tools.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Comparator;

public class JsonSortUtil {

    /*public static  String sortJson(String json){
        //String jsonmsg="{\"name\":\"yjc\",\"age\":25,\"sex\":\"男\"}";
        Map<String,String> map = JSON.parseObject(json,Map.class);
        Map<String, String> resultMap = sortMap(map);
        String result = JSON.toJSONString(resultMap);
        return  result;
    }

    public static Map<String, String> sortMap(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new MapComparator());
        sortMap.putAll(map);
        return sortMap;
    }*/

    public static String sortJson(String raw_json){
        String  res = JSONObject.toJSONString(JSONObject.parseObject(raw_json), SerializerFeature.SortField.MapSortField);
        return  res;
    }

    static class MapComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }

    }
}

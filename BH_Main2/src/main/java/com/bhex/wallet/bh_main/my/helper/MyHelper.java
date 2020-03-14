package com.bhex.wallet.bh_main.my.helper;

import android.content.Context;

import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/12
 * Time: 11:23
 */
public class MyHelper {

    public static List<MyItem> getAllItems(Context context){

        List<MyItem> myItems = new ArrayList<>();

        String [] res = context.getResources().getStringArray(R.array.my_list_item);
        for (int i = 0; i < res.length; i++) {
            MyItem item = new MyItem(res[i],true);
            myItems.add(item);
        }
        return myItems;
    }
}

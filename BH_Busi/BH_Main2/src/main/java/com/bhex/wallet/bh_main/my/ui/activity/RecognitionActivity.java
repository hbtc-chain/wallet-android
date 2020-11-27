package com.bhex.wallet.bh_main.my.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CheckedTextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bhex.network.mvx.base.BaseActivity;
import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.bh_main.R;
import com.bhex.wallet.bh_main.R2;
import com.bhex.wallet.bh_main.my.adapter.RecognitionAdapter;
import com.bhex.wallet.bh_main.my.adapter.SettingAdapter;
import com.bhex.wallet.bh_main.my.ui.item.MyItem;
import com.bhex.wallet.common.config.ARouterConfig;
import com.bhex.wallet.common.manager.MMKVManager;
import com.bhex.wallet.common.utils.SafeUilts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author gongdongyang
 * 2020-5-20 10:07:45
 * 生物识别
 */
@Route(path = ARouterConfig.My.My_Recognition,name = "面容和指纹识别设置")
public class RecognitionActivity extends BaseActivity {

    @Autowired(name="title")
    String title;

    @BindView(R2.id.tv_center_title)
    AppCompatTextView tv_center_title;

    @BindView(R2.id.recycler_setting)
    RecyclerView recycler_setting;

    private RecognitionAdapter mRecognitionAdapter;
    private List<MyItem> mLists;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recognition;
    }

    @Override
    protected void initView() {
        ARouter.getInstance().inject(this);
        //
        tv_center_title.setText(title);

        mLists = new ArrayList<>();
        String []res = getResources().getStringArray(R.array.Recongition_list);
        for (int i = 0; i < res.length; i++) {
            MyItem myItem = new MyItem(i,res[i],false,"");
            mLists.add(myItem);
        }
        mRecognitionAdapter = new RecognitionAdapter(R.layout.item_recogintion,mLists);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_setting.setLayoutManager(llm);
        recycler_setting.setAdapter(mRecognitionAdapter);
    }

    @Override
    protected void addEvent() {
        //
        mRecognitionAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if(view.getId()== com.bhex.wallet.balance.R.id.ck_select){
                CheckedTextView ck = (CheckedTextView) view;
                if(!ck.isChecked()){
                    if(SafeUilts.isFinger(this)){
                        MMKVManager.getInstance().mmkv().encode(BHConstants.FINGER_PWD_KEY,true);
                        ck.toggle();
                    }
                }else{
                    MMKVManager.getInstance().mmkv().remove(BHConstants.FINGER_PWD_KEY);
                    ck.toggle();
                }
            }
        });
    }

}

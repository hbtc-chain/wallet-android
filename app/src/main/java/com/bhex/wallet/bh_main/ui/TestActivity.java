package com.bhex.wallet.bh_main.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.bhex.wallet.R;

public class TestActivity extends AppCompatActivity {
    public Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mToolBar = findViewById(R.id.toolbar);
        if(mToolBar!=null){
            mToolBar.setTitle("");
            setSupportActionBar(mToolBar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}

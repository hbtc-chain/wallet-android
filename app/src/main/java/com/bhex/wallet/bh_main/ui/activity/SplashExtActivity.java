package com.bhex.wallet.bh_main.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import com.airbnb.lottie.LottieAnimationView;
import com.bhex.wallet.R;

public class SplashExtActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    AppCompatTextView bottomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_ext);

        lottieAnimationView = findViewById(R.id.animationView);
        lottieAnimationView.setAnimation("splash/cn.json");
        lottieAnimationView.playAnimation();

        //文字动画
        bottomView = findViewById(R.id.bottomView);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(bottomView,"alpha", new float[] { 0F, 1F });
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.setDuration(1800L);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        Runnable runnable = ()->{
            objectAnimator.start();
        };
        double d1 = lottieAnimationView.getDuration();
        bottomView.postDelayed(runnable,0);
    }
}
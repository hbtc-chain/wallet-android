package com.bhex.tools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

import com.bhex.tools.R;
import com.bumptech.glide.Glide;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/26
 * Time: 10:21
 */
public class ImageLoaderUtil {


    public interface ImageLoaderListener{
        public void onImageLoaderListener(Bitmap resource);
    }

    //默认加载
    public static void loadImageView(Context mContext, String path, ImageView mImageView, @DrawableRes int resId) {
        if(TextUtils.isEmpty(path)){
           mImageView.setImageResource(resId);
           return;
        }
        if(!path.equals(mImageView.getTag())){
            mImageView.setTag(null);
            Glide.with(mContext).load(path)
                    .placeholder(resId)
                    .error(resId)
                    .into(mImageView);

            mImageView.setTag(path);
        }
    }
}

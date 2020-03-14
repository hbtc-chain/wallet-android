package com.bhex.lib.uikit.widget.editor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.bhex.lib.uikit.R;


/**
 * Created by gongdongyang on 2018/9/17.
 */

public class EditTextHelper {

    private static Drawable deleteDrawable;
    private static Drawable eyeDrawablePassword;
    private static Drawable eyeDrawableText;

    /**
     * 为EditText添加删除按钮并返回wrapper
     *
     * @param context  上下文
     * @param editText 要添加删除按钮的EditText
     * @return 创建的wrapper
     */
    public static EditTextWrapper getEditTextDeleteIconWrapper(Context context, final EditText editText) {
        final EditTextWrapper wrapper = new EditTextWrapper(editText);
        if (deleteDrawable == null)
            deleteDrawable = ContextCompat.getDrawable(context, R.drawable.base_ic_delete);
        wrapper.setDrawableRight(deleteDrawable);
        wrapper.setOnDrawableRightClickListener(new EditTextWrapper.onDrawableRightClickListener() {
            @Override
            public void onDrawableRightClick() {
                editText.setText("");
            }
        });
        return wrapper;
    }

    /**
     * 为EditText添加切换明码、密码状态按钮
     *
     * @param context  上下文
     * @param editText 要添加切换明码、密码状态按钮的EditText
     * @return 创建的wrapper
     */
    public static EditTextWrapper getEditTextEyeIconWrapper(Context context, final EditText editText) {
        final EditTextWrapper wrapper = new EditTextWrapper(editText);
        if (eyeDrawablePassword == null)
            eyeDrawablePassword = ContextCompat.getDrawable(context, R.drawable.base_ic_eye_password);
        if (eyeDrawableText == null)
            eyeDrawableText = ContextCompat.getDrawable(context, R.drawable.base_ic_eye_text);
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        wrapper.setDrawableRight(eyeDrawablePassword);
        wrapper.setOnDrawableRightClickListener(new EditTextWrapper.onDrawableRightClickListener() {
            @Override
            public void onDrawableRightClick() {
                if (editText.getCompoundDrawables()[2] == eyeDrawablePassword) {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText.setSelection(editText.getText().length());
                    wrapper.setDrawableRight(eyeDrawableText);
                } else {
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText.setSelection(editText.getText().length());
                    wrapper.setDrawableRight(eyeDrawablePassword);
                }
            }
        });
        return wrapper;
    }
}

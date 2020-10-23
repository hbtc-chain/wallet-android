package com.bhex.wallet.common.utils;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import com.bhex.tools.constants.BHConstants;
import com.bhex.tools.utils.LogUtils;
import com.bhex.wallet.common.R;
import com.bhex.wallet.common.manager.MMKVManager;

/**
 * @author gongdongyang
 * 2020-5-20 10:54:56
 */
public class SafeUilts {

    private static final String TAG = "指纹判断";

    /**
     * 是否开启了安全校验
     * @return
     */
    public static boolean isOpenSageVerify(){
        boolean fingerOpen = MMKVManager.getInstance().mmkv().decodeBool(BHConstants.FINGER_PWD_KEY,false);
        //String gesturePwd = SPEx.get(AppData.SPKEY.GESTURE_PWD_KEY,"");
        //开启了指纹或者手势
        return fingerOpen;
    }

    public static boolean isFinger(Context context) {
        FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);
        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, context.getResources().getString(R.string.string_no_fingerprint_permission), Toast.LENGTH_SHORT).show();
            return false;
        }
        LogUtils.d(TAG, "有指纹权限");
        //判断硬件是否支持指纹识别
        if (!manager.isHardwareDetected()) {
            Toast.makeText(context, context.getResources().getString(R.string.fingerprint_no_hardware), Toast.LENGTH_SHORT).show();
            return false;
        }

        LogUtils.d(TAG, "有指纹模块");
        //判断是否有指纹录入
        if (!manager.hasEnrolledFingerprints()) {
            Toast.makeText(context, context.getResources().getString(R.string.fingerprint_create_finger_first_hint), Toast.LENGTH_SHORT).show();
            return false;
        }
        LogUtils.d(TAG, "已录入指纹");
        //判断 是否开启锁屏密码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            KeyguardManager mKeyManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            if (!mKeyManager.isKeyguardSecure()) {
                Toast.makeText(context, context.getResources().getString(R.string.fingerprint_no_lock_screen_pwd), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        LogUtils.d(TAG, "已开启锁屏密码");

        return true;
    }

    /**
     * 是否支持指纹
     * @param context
     * @return
     */
    public static boolean isSupportFinger(Context context) {
        FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);

        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(context, context.getResources().getString(R.string.string_no_fingerprint_permission), Toast.LENGTH_SHORT).show();
            return false;
        }
        LogUtils.d(TAG, "有指纹权限");
        //判断硬件是否支持指纹识别
        if (!manager.isHardwareDetected()) {
//            Toast.makeText(context, context.getResources().getString(R.string.fingerprint_no_hardware), Toast.LENGTH_SHORT).show();
            return false;
        }
        LogUtils.d(TAG, "有指纹模块");
        return true;
    }
}

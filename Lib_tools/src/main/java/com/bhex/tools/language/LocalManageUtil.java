package com.bhex.tools.language;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.bhex.tools.utils.LogUtils;

import java.util.Locale;

/**
 * created by gongdongyang
 * on 2020/2/25
 */
public class LocalManageUtil {

    private final static String TAG = LocalManageUtil.class.getSimpleName();

    /**
     * 赋予Context对象新的属性
     * @param context
     * @param language
     * @return
     */
    public static Context attachBaseContext(Context context, String language) {
        Locale locale = getSetLanguageLocale(context);
        LogUtils.d("MainActivity","locale:"+locale.getLanguage());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            return createConfigurationResources(context, locale.getLanguage());
        }
        applyLanguage(context, locale.getLanguage());
        return  context;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = null;

        if(language.startsWith("zh")){

            locale = Locale.SIMPLIFIED_CHINESE;

        }else if(language.startsWith("en")||language.toLowerCase().startsWith("english")){

            locale = Locale.ENGLISH;

        }

        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    public static void applyLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(newLanguage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocale(locale);
        } else {
            // updateConfiguration
            configuration.locale = locale;
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        }
    }

    /**
     * 获取选择的语言
     * @param context
     * @return
     */
    public static Locale getSetLanguageLocale(Context context) {
        switch (LocalSPUtil.getInstance(context).getSelectLanguage()) {
            case 0:
                Locale locale = getSystemLocale(context);
                if (!locale.getLanguage().startsWith("zh") && !locale.getLanguage().startsWith("en")) {
                    locale = Locale.US;
                }
                return locale;
            case 1:
                return Locale.SIMPLIFIED_CHINESE;
            case 2:
                return Locale.US;
            case 3:
                return Locale.KOREA;
            default:
                return Locale.US;
        }

    }

    public static int getSetLanguageLocaleIndex(Context context) {
        int selectIndex = LocalSPUtil.getInstance(context).getSelectLanguage();
        /*if(selectIndex==0){
            Locale locale = getSystemLocale(context);

        }else{
            return selectIndex;
        }*/
        return selectIndex;
    }

    /**
     * 获取系统语言
     * @param context
     * @return
     */
    public static Locale getSystemLocale(Context context) {
        return LocalSPUtil.getInstance(context).getSystemCurrentLocal(context);
    }

    /**
     * 保存语言设置
     * @param context
     * @param select
     */
    public static void saveSelectLanguage(Context context, int select) {
        LocalSPUtil.getInstance(context).saveLanguage(select);
    }
}

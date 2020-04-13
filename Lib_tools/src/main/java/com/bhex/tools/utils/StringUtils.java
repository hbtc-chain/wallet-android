package com.bhex.tools.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;


import com.bhex.lib.uikit.util.ColorUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/23
 * Time: 18:54
 */
public class StringUtils {

    /**
     * 关键字高亮显示
     *
     * @param context 上下文
     * @param text    需要显示的文字
     * @param target  需要高亮的关键字
     * @param color   高亮颜色
     * @param start   头部增加高亮文字个数
     * @param end     尾部增加高亮文字个数
     * @return 处理完后的结果
     */
    public static SpannableString highlight(Context context, String text, String target,
                                           int colorId , int start, int end) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(target);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            ForegroundColorSpan span = new ForegroundColorSpan(ColorUtil.getColor(context,colorId));
            spannableString.setSpan(span, matcher.start() - start, matcher.end() + end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}

package com.bhex.tools.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.bhex.tools.utils.RegexUtil;

/**
 * @author gongdongyang
 * 2020-5-21 15:29:41
 * 数字3位空格
 */
public class FormatTextWatcher implements TextWatcher {

    private EditText editText;

    private String tag = " ";

    private int unit = 3;

    int beforeTextLength = 0;
    int afterTextLength = 0;
    int location = 0;// 记录光标的位置
    boolean isChanging = false;

    public FormatTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        beforeTextLength = s.length();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextLength = s.length();

        if(isChanging)
            return;
        String text = editText.getText().toString().trim();

        if(RegexUtil.checkNumeric(text) && Double.valueOf(text)<1000){
            return;
        }

        if(beforeTextLength < afterTextLength){// 字符增加
            location = editText.getSelectionEnd();
            setFormatText(s.toString());
            if(location % (unit+1) == 0){
                editText.setSelection(getLocation(location+1));
            }else{
                editText.setSelection(getLocation(location));
            }
        }else if(beforeTextLength > afterTextLength){// 字符减少
            location = editText.getSelectionEnd();
            setFormatText(s.toString());
            if(location % (unit+1) == 0){
                editText.setSelection(getLocation(location-1));
            }else{
                editText.setSelection(getLocation(location));
            }
        }
    }

    private int getLocation(int location){
        if(location<0)
            return 0;
        if(location>afterTextLength){
            return afterTextLength;
        }
        return location;
    }

    private void setFormatText(String str){
        isChanging = true;
        editText.setText(addTag(str));
        isChanging = false;
    }

    /**
     * 加上标识符
     * @param str
     * @return
     */
    private String addTag(String str){
        str = replaceTag(str);
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int strLength = str.length();
        while (index < strLength) {
            int increment = index+unit;
            sb.append(str.subSequence(index, index=(increment>strLength)?strLength:increment));
            if(increment<strLength){
                sb.append(tag);
            }
        }
        return sb.toString();
    }

    /**
     * 清除标识符
     * @param str
     * @return
     */
    public String replaceTag(String str){
        if(str.indexOf(tag)!=-1){
            str=str.replace(tag, "");
        }
        return str;
    }
}

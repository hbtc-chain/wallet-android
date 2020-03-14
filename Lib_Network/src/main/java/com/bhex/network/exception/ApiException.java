package com.bhex.network.exception;

import android.text.TextUtils;

public class ApiException extends Exception{
    private int code;

    private String displayMessage;

    public ApiException(Throwable error, int code) {
        super(error);
        this.code = code;
    }

    public int getCode() { return this.code; }

    public String getDisplayMessage() {
        return TextUtils.isEmpty(this.displayMessage) ? getLocalizedMessage() : this.displayMessage;
    }

    public void setDisplayMessage(String paramString) {
        this.displayMessage = paramString;
    }

}

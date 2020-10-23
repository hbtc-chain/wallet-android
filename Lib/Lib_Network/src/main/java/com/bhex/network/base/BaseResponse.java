package com.bhex.network.base;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class BaseResponse {

    public int code;

    public String message;

    public BaseResponse(){

    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

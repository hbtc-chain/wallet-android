package com.bhex.network.base;

/**
 * created by gongdongyang
 * on 2020/2/25
 */
public class LoadingStatus {

    public static final int ERROR = 2;

    public static final int LOADING = 0;

    public static final int SUCCESS = 1;

    public int code;

    public int loadingStatus;

    public String msg;

    public LoadingStatus() { this.loadingStatus = 0; }

    public LoadingStatus(int status) { this.loadingStatus = status; }

    public LoadingStatus(int code, String message) {
        this.code = code;
        this.msg = message;
        this.loadingStatus = 2;
    }

    public int getCode() { return this.code; }

    public int getLoadingStatus() { return this.loadingStatus; }

    public String getMsg() { return this.msg; }

    public boolean isError() { return (this.loadingStatus == 2); }

    public boolean isLoading() { return (this.loadingStatus == 0); }

    public boolean isSuccess() { return (this.loadingStatus == 1); }
}

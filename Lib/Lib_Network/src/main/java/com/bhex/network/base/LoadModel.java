package com.bhex.network.base;

/**
 * created by gongdongyang
 * on 2020/2/25
 */
public class LoadModel<T> extends LoadingStatus {

    private T data;

    private boolean isRefrash;

    private LoadModel() {
        this.data = null;
        this.loadingStatus = 0;
    }

    public LoadModel(int status) { super(status); }

    public LoadModel(int code, String message, boolean isRefrash) {
        super(code, message);
        this.isRefrash = isRefrash;
    }

    public LoadModel(int code, boolean isRefrash) {
        super(code);
        this.isRefrash = isRefrash;
    }

    public LoadModel(T paramT) {
        this.data = paramT;
        this.loadingStatus = 1;
    }

    public LoadModel(boolean paramBoolean) {
        this.isRefrash = paramBoolean;
        this.loadingStatus = 0;
    }

    public T getData() { return (T)this.data; }

    public boolean isRefrash() { return this.isRefrash; }

    public void setRefrash(boolean paramBoolean) { this.isRefrash = paramBoolean; }

}

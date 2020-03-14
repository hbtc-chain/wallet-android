package com.bhex.network.base;

/**
 * created by gongdongyang
 * on 2020/2/25
 */
public class LoadDataModel<T> extends LoadingStatus {

    private T data;

    public LoadDataModel() {}

    public LoadDataModel(int code, String message) { super(code, message); }

    public LoadDataModel(T t) {
        super(LoadingStatus.SUCCESS);
        this.data = t;
    }

    public T getData() { return (T)this.data; }
}

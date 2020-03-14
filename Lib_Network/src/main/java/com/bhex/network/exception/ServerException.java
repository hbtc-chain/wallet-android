package com.bhex.network.exception;

public class ServerException  extends RuntimeException {
    private int code;

    private String msg;

    public ServerException(int code, String errorMsg) {
        this.code = code;
        this.msg = errorMsg;
    }

    public int getCode() { return this.code; }

    public String getMsg() { return this.msg; }
}

package com.bhex.network.receiver;

/**
 * created by gongdongyang
 * on 2020/2/24
 */
public class NetworkChangeEvent {

    public boolean hasChange;

    public int nowNetWorkStatus;

    public NetworkChangeEvent(boolean hasChange, int nowNetWorkStatus) {
        this.hasChange = hasChange;
        this.nowNetWorkStatus = nowNetWorkStatus;
    }

    public boolean hasNetWork() {
        return (this.nowNetWorkStatus != NetWorkStatusChangeReceiver.Network_Status_None); }
}

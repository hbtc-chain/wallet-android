package com.bhex.wallet.market.event;

import com.bhex.wallet.market.model.H5Sign;

public class H5SignEvent {
    public H5Sign h5Sign;
    public String data;

    public H5SignEvent(H5Sign h5Sign, String data) {
        this.h5Sign = h5Sign;
        this.data = data;
    }

}

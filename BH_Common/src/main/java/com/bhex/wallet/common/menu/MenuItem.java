package com.bhex.wallet.common.menu;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItem implements Parcelable {
    public String title;
    public boolean isSelected;


    public MenuItem(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected MenuItem(Parcel in) {
        this.title = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel source) {
            return new MenuItem(source);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}

package com.bhex.wallet.market.language.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gdy on 2018/12/8
 * Describe:
 */


public class LanguageEntity implements Parcelable {

    private int id;
    private String fullName;
    private String shortName;
    private boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.shortName);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public LanguageEntity() {
    }

    protected LanguageEntity(Parcel in) {
        this.id = in.readInt();
        this.fullName = in.readString();
        this.shortName = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<LanguageEntity> CREATOR = new Creator<LanguageEntity>() {
        @Override
        public LanguageEntity createFromParcel(Parcel source) {
            return new LanguageEntity(source);
        }

        @Override
        public LanguageEntity[] newArray(int size) {
            return new LanguageEntity[size];
        }
    };
}

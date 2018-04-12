package com.pedromoreirareisgmail.pchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Amigo implements Parcelable {


    private long data;

    public Amigo() {
    }

    public Amigo(long data) {
        this.data = data;
    }

    protected Amigo(Parcel in) {
        data = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Amigo> CREATOR = new Creator<Amigo>() {
        @Override
        public Amigo createFromParcel(Parcel in) {
            return new Amigo(in);
        }

        @Override
        public Amigo[] newArray(int size) {
            return new Amigo[size];
        }
    };

    public long getData() {
        return data;
    }
}
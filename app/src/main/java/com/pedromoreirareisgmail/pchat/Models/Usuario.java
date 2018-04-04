package com.pedromoreirareisgmail.pchat.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {


    private String nome;
    private String status;
    private String imagem;
    private String thumbnail;
    private boolean online;
    private long ultima;
    private String device_token;

    public Usuario() {

    }

    public Usuario(String nome, String status, String imagem, String thumbnail, boolean online, long ultima, String device_token) {
        this.nome = nome;
        this.status = status;
        this.imagem = imagem;
        this.thumbnail = thumbnail;
        this.online = online;
        this.ultima = ultima;
        this.device_token = device_token;
    }


    protected Usuario(Parcel in) {
        nome = in.readString();
        status = in.readString();
        imagem = in.readString();
        thumbnail = in.readString();
        online = in.readByte() != 0;
        ultima = in.readLong();
        device_token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(status);
        dest.writeString(imagem);
        dest.writeString(thumbnail);
        dest.writeByte((byte) (online ? 1 : 0));
        dest.writeLong(ultima);
        dest.writeString(device_token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getNome() {
        return nome;
    }

    public String getStatus() {
        return status;
    }

    public String getImagem() {
        return imagem;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Boolean getOnline() {
        return online;
    }

    public long getUltima() {
        return ultima;
    }

    public String getDevice_token() {
        return device_token;
    }

}
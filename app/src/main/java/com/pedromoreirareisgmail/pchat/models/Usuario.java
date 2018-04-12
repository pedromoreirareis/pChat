package com.pedromoreirareisgmail.pchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {

    private String deviceToken;
    private String urlImagem;
    private String nome;
    private boolean online;
    private String status;
    private String urlThumbnail;
    private long ultAcesso;

    public Usuario() {

    }

    public Usuario(String deviceToken, String urlImagem, String nome, boolean online, String status, String urlThumbnail, long ultAcesso) {
        this.deviceToken = deviceToken;
        this.urlImagem = urlImagem;
        this.nome = nome;
        this.online = online;
        this.status = status;
        this.urlThumbnail = urlThumbnail;
        this.ultAcesso = ultAcesso;
    }

    protected Usuario(Parcel in) {
        deviceToken = in.readString();
        urlImagem = in.readString();
        nome = in.readString();
        online = in.readByte() != 0;
        status = in.readString();
        urlThumbnail = in.readString();
        ultAcesso = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceToken);
        dest.writeString(urlImagem);
        dest.writeString(nome);
        dest.writeByte((byte) (online ? 1 : 0));
        dest.writeString(status);
        dest.writeString(urlThumbnail);
        dest.writeLong(ultAcesso);
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public boolean isOnline() {
        return online;
    }

    public String getStatus() {
        return status;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public long getUltAcesso() {
        return ultAcesso;
    }
}
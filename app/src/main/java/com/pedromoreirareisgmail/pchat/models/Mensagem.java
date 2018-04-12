package com.pedromoreirareisgmail.pchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Mensagem implements Parcelable {

    private String mensagem;
    private String urlMensagem;
    private String tipo;
    private boolean lida;
    private String urlThumbnail;
    private long timestamp;
    private String idOrigem;
    private String idDestino;

    public Mensagem(){

    }

    public Mensagem(String mensagem, String urlMensagem, String tipo, boolean lida, String urlThumbnail, long timestamp, String idOrigem, String idDestino) {
        this.mensagem = mensagem;
        this.urlMensagem = urlMensagem;
        this.tipo = tipo;
        this.lida = lida;
        this.urlThumbnail = urlThumbnail;
        this.timestamp = timestamp;
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
    }

    protected Mensagem(Parcel in) {
        mensagem = in.readString();
        urlMensagem = in.readString();
        tipo = in.readString();
        lida = in.readByte() != 0;
        urlThumbnail = in.readString();
        timestamp = in.readLong();
        idOrigem = in.readString();
        idDestino = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mensagem);
        dest.writeString(urlMensagem);
        dest.writeString(tipo);
        dest.writeByte((byte) (lida ? 1 : 0));
        dest.writeString(urlThumbnail);
        dest.writeLong(timestamp);
        dest.writeString(idOrigem);
        dest.writeString(idDestino);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mensagem> CREATOR = new Creator<Mensagem>() {
        @Override
        public Mensagem createFromParcel(Parcel in) {
            return new Mensagem(in);
        }

        @Override
        public Mensagem[] newArray(int size) {
            return new Mensagem[size];
        }
    };

    public String getMensagem() {
        return mensagem;
    }

    public String getUrlMensagem() {
        return urlMensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isLida() {
        return lida;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getIdOrigem() {
        return idOrigem;
    }

    public String getIdDestino() {
        return idDestino;
    }


}
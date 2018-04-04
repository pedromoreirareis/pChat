package com.pedromoreirareisgmail.pchat.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Mensagem implements Parcelable {

    private String mensagem;
    private boolean lida;
    private String tipo;
    private long time;
    private String origem;
    private String thumb;

    public Mensagem() {

    }

    public Mensagem(String mensagem, boolean lida, String tipo, long time, String origem, String thumb) {
        this.mensagem = mensagem;
        this.lida = lida;
        this.tipo = tipo;
        this.time = time;
        this.origem = origem;
        this.thumb = thumb;
    }

    protected Mensagem(Parcel in) {
        mensagem = in.readString();
        lida = in.readByte() != 0;
        tipo = in.readString();
        time = in.readLong();
        origem = in.readString();
        thumb = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mensagem);
        dest.writeByte((byte) (lida ? 1 : 0));
        dest.writeString(tipo);
        dest.writeLong(time);
        dest.writeString(origem);
        dest.writeString(thumb);
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

    public boolean isLida() {
        return lida;
    }

    public String getTipo() {
        return tipo;
    }

    public long getTime() {
        return time;
    }

    public String getOrigem() {
        return origem;
    }

    public String getThumb() {
        return thumb;
    }
}
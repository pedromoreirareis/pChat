package com.pedromoreirareisgmail.pchat.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Solicitacao implements Parcelable {

    private String tipo_solicitacao;

    public Solicitacao(){}

    public Solicitacao(String tipo_solicitacao) {
        this.tipo_solicitacao = tipo_solicitacao;
    }

    protected Solicitacao(Parcel in) {
        tipo_solicitacao = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tipo_solicitacao);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Solicitacao> CREATOR = new Creator<Solicitacao>() {
        @Override
        public Solicitacao createFromParcel(Parcel in) {
            return new Solicitacao(in);
        }

        @Override
        public Solicitacao[] newArray(int size) {
            return new Solicitacao[size];
        }
    };

    public String getTipo_solicitacao() {
        return tipo_solicitacao;
    }
}

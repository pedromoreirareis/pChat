package com.pedromoreirareisgmail.pchat.Fire;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pedromoreirareisgmail.pchat.Utils.Const;

public class Fire {

    private static FirebaseAuth auth;
    private static FirebaseUser usuario;
    private static DatabaseReference refRoot;
    private static DatabaseReference refUsuarios;
    private static DatabaseReference refUsuario;
    private static DatabaseReference refSolicitacoes;
    private static DatabaseReference refAmigo;
    private static DatabaseReference refNotificacoes;
    private static DatabaseReference refAmigos;
    private static StorageReference storageRefRoot;
    private static StorageReference storageRefImgPerfil;
    private static StorageReference storageRefImgThumbnail;
    private static String idUsuario;

    public static void setAuth() {
        Fire.auth = null;
    }

    public static void setUsuario() {
        Fire.usuario = null;
    }

    public static void setIdUsuario() {
        Fire.idUsuario = null;
    }

    public static void setRefRoot() {
        Fire.refRoot = null;
    }

    public static void setRefUsuarios() {
        Fire.refUsuarios = null;
    }

    public static void setRefUsuario() {
        Fire.refUsuario = null;
    }

    public static void setRefSolicitacoes() {
        Fire.refSolicitacoes = null;
    }

    public static void setRefAmigo() {
        Fire.refAmigo = null;
    }

    public static void setRefNotificacoes() {
        Fire.refNotificacoes = null;
    }

    public static void setRefAmigos() {
        Fire.refAmigos = null;
    }

    public static void setStorageRefRoot() {
        Fire.storageRefRoot = null;
    }

    public static void setStorageRefImgPerfil() {
        Fire.storageRefImgPerfil = null;
    }


    public static void setStorageRefImgThumbnail() {
        Fire.storageRefImgThumbnail = null;
    }


    public static FirebaseAuth getAuth() {

        if (auth == null) {

            auth = FirebaseAuth.getInstance();

        }
        return auth;
    }

    public static FirebaseUser getUsuario() {

        if (usuario == null) {

            usuario = getAuth().getCurrentUser();
        }

        return usuario;
    }

    public static String getIdUsuario() {

        if (idUsuario == null) {

            idUsuario = getUsuario().getUid();
        }

        return idUsuario;
    }

    public static DatabaseReference getRefRoot() {

        if (refRoot == null) {

            refRoot = FirebaseDatabase.getInstance().getReference();

        }
        return refRoot;
    }

    public static DatabaseReference getRefUsuario() {

        if (refUsuario == null) {

            refUsuario = getRefRoot().child(Const.PASTA_USUARIOS).child(getIdUsuario());

        }

        return refUsuario;
    }

    public static DatabaseReference getRefUsuarios() {

        if (refUsuarios == null) {

            refUsuarios = getRefRoot().child(Const.PASTA_USUARIOS);

        }
        return refUsuarios;
    }

    public static DatabaseReference getRefSolicitacoes() {

        if (refSolicitacoes == null) {

            refSolicitacoes = getRefRoot().child(Const.PASTA_SOLIC);

        }
        return refSolicitacoes;
    }

    public static DatabaseReference getRefAmigos() {

        if (refAmigos == null) {

            refAmigos = getRefRoot().child(Const.PASTA_AMIGOS);

        }
        return refAmigos;
    }

    public static DatabaseReference getRefNotificacoes() {

        if (refNotificacoes == null) {

            refNotificacoes = getRefRoot().child(Const.PASTA_NOTIF);

        }
        return refNotificacoes;
    }

    public static StorageReference getStorageRefRoot() {

        if (storageRefRoot == null) {

            storageRefRoot = FirebaseStorage.getInstance().getReference();
        }

        return storageRefRoot;
    }

    public static StorageReference getStorageRefImgPerfil() {

        if (storageRefImgPerfil == null) {

            storageRefImgPerfil = getStorageRefRoot().child(Const.S_PASTA_IMAGENS).child(getIdUsuario() + ".jpg");
        }

        return storageRefImgPerfil;
    }

    public static StorageReference getStorageRefImgThumbnail() {

        if (storageRefImgThumbnail == null) {

            storageRefImgThumbnail = getStorageRefRoot().child(Const.S_PASTA_THUMB).child(getIdUsuario() + ".jpg");
        }

        return storageRefImgThumbnail;
    }

}

package com.pedromoreirareisgmail.pchat.fire;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pedromoreirareisgmail.pchat.utils.Const;

public class Fire {



    private static String deviceToken;
    private static FirebaseAuth auth;
    private static FirebaseUser usuario;
    private static DatabaseReference refRoot;
    private static DatabaseReference refUsuarios;
    private static DatabaseReference refUsuario;
    private static DatabaseReference refSolicitacoes;
    private static DatabaseReference refNotificacoesSOL;
    private static DatabaseReference refNotificacoesMSG;
    private static DatabaseReference refAmigos;
    private static DatabaseReference refChat;
    private static DatabaseReference refMensagens;
    private static StorageReference storageRefRoot;
    private static StorageReference storageRefImgPerfil;
    private static StorageReference storageRefImgThumbnail;
    private static StorageReference storageRefImgMsg;
    private static StorageReference storageRefImgMsgThumbnail;
    private static String idUsuario;

    public static void setDeviceToken() {
        Fire.deviceToken = null;
    }

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

    public static void setRefNotificacoesSOL() {
        Fire.refNotificacoesSOL = null;
    }

    public static void setRefNotificacoesMSG() {
        Fire.refNotificacoesMSG = null;
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

    public static void setStorageRefImgMsg() {
        Fire.storageRefImgMsg = null;
    }

    public static void setStorageRefImgMsgThumbnail() {
        Fire.storageRefImgMsgThumbnail = null;
    }

    public static void setRefChat() {
        Fire.refChat = null;
    }

    public static void setRefMensagens() {
        Fire.refMensagens = null;
    }


    public static String getDeviceToken() {

        if(deviceToken == null){

            deviceToken = FirebaseInstanceId.getInstance().getToken();
        }

        return  deviceToken;
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

    public static DatabaseReference getRefNotificacoesSOL() {

        if (refNotificacoesSOL == null) {

            refNotificacoesSOL = getRefRoot().child(Const.PASTA_NOTIF_SOL);

        }
        return refNotificacoesSOL;
    }

    public static DatabaseReference getRefNotificacoesMSG() {

        if (refNotificacoesMSG == null) {

            refNotificacoesMSG = getRefRoot().child(Const.PASTA_NOTIF_MSG);

        }
        return refNotificacoesMSG;
    }

    public static DatabaseReference getRefChat() {

        if (refChat == null) {

            refChat = getRefRoot().child(Const.PASTA_CHATS);

        }
        return refChat;
    }

    public static DatabaseReference getRefMensagens() {

        if (refMensagens == null) {

            refMensagens = getRefRoot().child(Const.PASTA_MENSAGENS);

        }
        return refMensagens;
    }



    public static StorageReference getStorageRefRoot() {

        if (storageRefRoot == null) {

            storageRefRoot = FirebaseStorage.getInstance().getReference();
        }

        return storageRefRoot;
    }

    public static StorageReference getStorageRefImgPerfil() {

        if (storageRefImgPerfil == null) {

            storageRefImgPerfil = getStorageRefRoot().child(Const.S_PASTA_IMG_PERFIL_USUARIO).child(getIdUsuario() + ".jpg");
        }

        return storageRefImgPerfil;
    }

    public static StorageReference getStorageRefImgThumbnail() {

        if (storageRefImgThumbnail == null) {

            storageRefImgThumbnail = getStorageRefRoot().child(Const.S_PASTA_IMG_PERFIL_USUARIO_THUMBNAIL).child(getIdUsuario() + ".jpg");
        }

        return storageRefImgThumbnail;
    }

    public static StorageReference getStorageRefImgMsg() {

        if (storageRefImgMsg == null) {

            storageRefImgMsg = getStorageRefRoot().child(Const.S_PASTA_IMG_MSG_CHATS);
        }

        return storageRefImgMsg;
    }

    public static StorageReference getStorageRefImgMsgThumbnail() {

        if (storageRefImgMsgThumbnail == null) {

            storageRefImgMsgThumbnail = getStorageRefRoot().child(Const.S_PASTA_IMG_MSG_CHATS_THUMBNAIL);
        }

        return storageRefImgMsgThumbnail;
    }

}

package com.pedromoreirareisgmail.pchat.Fire;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.pedromoreirareisgmail.pchat.Utils.Const;

import java.util.HashMap;
import java.util.Map;

public class FireUtils {

    private static boolean isSucesso;

    public static void usuarioOnLine(DatabaseReference refUsuario) {

        refUsuario.child(Const.DB_ON_LINE).setValue(true);
    }

    public static void ultimaVez(DatabaseReference refUsuario) {

        refUsuario.child(Const.DB_ULT_VEZ).setValue(ServerValue.TIMESTAMP);
    }

    public static void usuarioOffLine(DatabaseReference refUsuario) {

        refUsuario.child(Const.DB_ON_LINE).setValue(false);
    }

    public static Map<String,Object> mapAdicionar(String idUsuario, String idConvite ){

        Map<String, Object> mapAdicionar = new HashMap<>();
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite + "/" + Const.SOL_TIPO, Const.SOL_TIPO_ENVIADA);
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario + "/" + Const.SOL_TIPO, Const.SOL_TIPO_RECEBIDA);
        mapAdicionar.put(Const.PASTA_NOTIF + "/" + idConvite + "/" + idPushNotfAmizade(idConvite) , mapNotifAmizade(idUsuario));

        return mapAdicionar;
    }

    public static Map<String,Object> mapCancelar(String idUsuario,String idConvite){

        Map<String, Object> mapCancelar = new HashMap<>();
        mapCancelar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapCancelar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        return mapCancelar;
    }

    public  static Map<String, Object> mapAceitar(String idUsuario,String idConvite){

        Map<String, Object> mapAmigos = new HashMap<>();
        mapAmigos.put(Const.PASTA_AMIGOS + "/" + idUsuario + "/" + idConvite + "/" + Const.DATA, ServerValue.TIMESTAMP);
        mapAmigos.put(Const.PASTA_AMIGOS + "/" + idConvite + "/" + idUsuario + "/" + Const.DATA, ServerValue.TIMESTAMP);

        mapAmigos.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapAmigos.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        return mapAmigos;
    }

    public static Map<String,Object> mapRecusar(String idUsuario, String idConvite){

        Map<String, Object> mapRecusar = new HashMap<>();
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        return mapRecusar;
    }


    public static Map<String,Object> mapDesfazer(String idUsuario, String idConvite){

        Map<String, Object> mapDesfazer = new HashMap<>();
        mapDesfazer.put(Const.PASTA_AMIGOS + "/" + idUsuario + "/" + idConvite, null);
        mapDesfazer.put(Const.PASTA_AMIGOS + "/" + idConvite + "/" + idUsuario, null);

        return mapDesfazer;
    }

    private static Map<String,Object> mapNotifAmizade(String idUsuario){

        Map<String, Object> mapNotificacao = new HashMap<>();
        mapNotificacao.put(Const.NOTIF_ORIGEM, idUsuario);
        mapNotificacao.put(Const.NOTIF_TIPO, Const.NOTIF_SOLICITADA);

        return mapNotificacao;
    }

    private static String idPushNotfAmizade(String idConvite){

        return Fire.getRefNotificacoes().child(idConvite).push().getKey();
    }

    public static void firebaseSetNull(){

        Fire.setAuth();
        Fire.setUsuario();
        Fire.setIdUsuario();

        Fire.setRefAmigo();
        Fire.setRefAmigos();
        Fire.setRefNotificacoes();
        Fire.setRefRoot();
        Fire.setRefSolicitacoes();
        Fire.setRefUsuario();
        Fire.setRefUsuarios();
        Fire.setStorageRefImgPerfil();
        Fire.setStorageRefImgThumbnail();
        Fire.setStorageRefRoot();
    }
}
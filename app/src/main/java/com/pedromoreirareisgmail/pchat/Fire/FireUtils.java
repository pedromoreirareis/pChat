package com.pedromoreirareisgmail.pchat.Fire;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.pedromoreirareisgmail.pchat.Utils.Const;

import java.util.HashMap;

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

    public static boolean recusarAmizade(DatabaseReference refRoot, String idUsuario, String idConvite) {

        isSucesso = false;

        HashMap<String, Object> mapRecusar = new HashMap<>();
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        refRoot.updateChildren(mapRecusar, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                isSucesso = databaseError == null;
            }
        });

        return isSucesso;
    }

    public static boolean adicionarAmigos(DatabaseReference refRoot, String idUsuario, String idConvite ){

        isSucesso = false;

        String idNotif = Fire.getRefNotificacoes().child(idConvite).push().getKey();

        HashMap<String, Object> mapNotificacao = new HashMap<>();
        mapNotificacao.put(Const.NOTIF_ORIGEM, idUsuario);
        mapNotificacao.put(Const.NOTIF_TIPO, Const.NOTIF_SOLICITADA);

        HashMap<String, Object> mapAdicionar = new HashMap<>();
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite + "/" + Const.SOL_TIPO, Const.SOL_TIPO_ENVIADA);
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario + "/" + Const.SOL_TIPO, Const.SOL_TIPO_RECEBIDA);
        mapAdicionar.put(Const.PASTA_NOTIF + "/" + idConvite + "/" + idNotif, mapNotificacao);

        refRoot.updateChildren(mapAdicionar, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                isSucesso = databaseError == null;


            }
        });

        return isSucesso;
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
package com.pedromoreirareisgmail.pchat.Fire;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.pedromoreirareisgmail.pchat.Utils.Const;

import java.util.HashMap;

public class FireUtils {

    private static boolean sucesso = false;

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

        HashMap<String, Object> mapRecusar = new HashMap<>();
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        refRoot.updateChildren(mapRecusar, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                sucesso = databaseError == null;
            }
        });

        return sucesso;
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
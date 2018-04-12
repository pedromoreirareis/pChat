package com.pedromoreirareisgmail.pchat.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pedromoreirareisgmail.pchat.utils.Const;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService{

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        enviarTokenUser(token);
    }


    private void enviarTokenUser(String token){

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Const.PASTA_USUARIOS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Const.USUARIO_DEVICETOKEN)
                    .setValue(token);
        }
    }
}

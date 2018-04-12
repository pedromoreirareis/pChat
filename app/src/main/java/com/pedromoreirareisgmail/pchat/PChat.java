package com.pedromoreirareisgmail.pchat;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class PChat extends Application {

    private DatabaseReference mRefUsuario;

    @Override
    public void onCreate() {
        super.onCreate();

        // FirebaseUtils OffLine
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Picasso OffLine
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));

        Picasso built = builder.build();

        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


        if (Fire.getUsuario() != null) {


            mRefUsuario =  Fire.getRefUsuario();

            mRefUsuario.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null) {

                        if(dataSnapshot.hasChild(Const.USUARIO_ONLINE)) {

                            boolean online = (boolean) dataSnapshot.child(Const.USUARIO_ONLINE).getValue();
                            if (online) {

                                mRefUsuario.child(Const.USUARIO_ONLINE).onDisconnect().setValue(false);
                                mRefUsuario.child(Const.USUARIO_ULT_ACESSO).onDisconnect().setValue(ServerValue.TIMESTAMP);

                                mRefUsuario.child(Const.USUARIO_ONLINE).setValue(true);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
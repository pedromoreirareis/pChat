package com.pedromoreirareisgmail.pchat;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class PChat extends Application {

    private DatabaseReference mRefUsuario;
    private FirebaseAuth mAuth;
    private FirebaseUser mUsuario;
    private String mIdUsuario;


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


        mAuth = FirebaseAuth.getInstance();
        mUsuario = mAuth.getCurrentUser();

        if (mUsuario != null) {

            mIdUsuario = mUsuario.getUid();

            mRefUsuario = FirebaseDatabase.getInstance().getReference()
                    .child(Const.PASTA_USUARIOS).child(mAuth.getCurrentUser().getUid());

            mRefUsuario.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null) {

                        if(dataSnapshot.hasChild(Const.DB_ON_LINE)) {

                            boolean online = (boolean) dataSnapshot.child(Const.DB_ON_LINE).getValue();
                            if (online) {

                                mRefUsuario.child(Const.DB_ON_LINE).onDisconnect().setValue(false);
                                mRefUsuario.child(Const.DB_ULT_VEZ).onDisconnect().setValue(ServerValue.TIMESTAMP);

                                mRefUsuario.child(Const.DB_ON_LINE).setValue(true);
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
package com.pedromoreirareisgmail.pchat;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pedromoreirareisgmail.pchat.adapters.AdapterUsers;
import com.pedromoreirareisgmail.pchat.databinding.ActivityUsersBinding;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.fire.FireUtils;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Const;

import java.util.Objects;

public class UsersActivity extends AppCompatActivity {

    private AdapterUsers mAdapter;
    private DatabaseReference mRefUsuarios;
    private ActivityUsersBinding mBinding;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);

        initViews();
    }

    private void initViews() {

        mContext = UsersActivity.this;

        Toolbar toolbar = (Toolbar) mBinding.toolbarAllUsuarios;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.titulo_usuarios));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = mBinding.rvUsersList;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Fire.getUsuario() != null) {

            FireUtils.usuarioOnLine(Fire.getRefUsuario());
        }

        initFirebase();

        mAdapter = new AdapterUsers(firebaseOptions(), mContext);
        mAdapter.startListening();
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initFirebase() {

        mRefUsuarios = Fire.getRefUsuarios();
        mRefUsuarios.keepSynced(true);
    }

    private FirebaseRecyclerOptions<Usuario> firebaseOptions() {

        Query querySearch = mRefUsuarios.orderByChild(Const.USUARIO_NOME);

        return new FirebaseRecyclerOptions.Builder<Usuario>()
                .setQuery(querySearch, Usuario.class)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        FireUtils.ultimoAcesso(Fire.getRefUsuario());
        mAdapter.stopListening();
    }
}

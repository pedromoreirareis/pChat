package com.pedromoreirareisgmail.pchat.fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.adapters.AdapterFragFriends;
import com.pedromoreirareisgmail.pchat.databinding.FragmentFriendsBinding;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.models.Amigo;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding mBinding;
    private DatabaseReference mRefAmigos;
    private DatabaseReference mRefUsuarios;
    private AdapterFragFriends mAdapter;
    private RecyclerView mReciclerView;
    private Context mContext;

    public FriendsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false);

        initViews();
        initFirebase();

        return mBinding.getRoot();
    }

    private void initViews() {

        mContext = getContext();

        mReciclerView = mBinding.rvFriendsList;
        mReciclerView.setHasFixedSize(true);
        mReciclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initFirebase() {

        mRefAmigos = Fire.getRefAmigos().child(Fire.getIdUsuario());
        mRefAmigos.keepSynced(true);

        mRefUsuarios = Fire.getRefUsuarios();
        mRefUsuarios.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AdapterFragFriends(firebaseOptions(),mContext,mRefUsuarios);

        mReciclerView.setAdapter(mAdapter);

        mAdapter.startListening();
    }

    private FirebaseRecyclerOptions<Amigo> firebaseOptions (){

        return new FirebaseRecyclerOptions.Builder<Amigo>()
                .setQuery(mRefAmigos, Amigo.class)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

}

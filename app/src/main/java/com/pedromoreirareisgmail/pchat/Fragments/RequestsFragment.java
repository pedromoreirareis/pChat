package com.pedromoreirareisgmail.pchat.Fragments;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.pedromoreirareisgmail.pchat.Adapters.AdapterFragRequest;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Models.Solicitacao;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.databinding.FragmentRequestsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private FragmentRequestsBinding mBinding;
    private DatabaseReference mRefSolicitacao;
    private DatabaseReference mRefUsuarios;
    private AdapterFragRequest mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLLManager;
    private String mIdUsuario;
    private Context mContext;


    public RequestsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_requests, container, false);

        initViews();
        initFirebase();

        return mBinding.getRoot();
    }

    private void initViews() {

        mContext = getContext();

        mRecyclerView = mBinding.rvRequestFragList;

        mLLManager = new LinearLayoutManager(mContext);
        mLLManager.setReverseLayout(true);
        mLLManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLLManager);
    }

    private void initFirebase() {

        mIdUsuario = Fire.getIdUsuario();

        mRefUsuarios = Fire.getRefUsuarios();
        mRefUsuarios.keepSynced(true);

        mRefSolicitacao = Fire.getRefSolicitacoes().child(mIdUsuario);
        mRefSolicitacao.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AdapterFragRequest(firebaseOptions(), mContext,mRefUsuarios, mIdUsuario);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.startListening();
    }

    private FirebaseRecyclerOptions<Solicitacao> firebaseOptions(){

        return new FirebaseRecyclerOptions.Builder<Solicitacao>()
                .setQuery(mRefSolicitacao, Solicitacao.class)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

}


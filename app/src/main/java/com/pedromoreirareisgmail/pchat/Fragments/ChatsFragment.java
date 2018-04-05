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
import com.google.firebase.database.Query;
import com.pedromoreirareisgmail.pchat.Adapters.AdapterFragChat;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Models.Chat;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding mBinding;
    private DatabaseReference mRefChat;
    private DatabaseReference mRefMensagens;
    private DatabaseReference mRefUsuarios;
    private AdapterFragChat mAdapter;
    private LinearLayoutManager mLLManager;
    private RecyclerView mRecyclerView;
    private Context mContext;


    public ChatsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);

        initViews();
        initFirebase();

        return mBinding.getRoot();
    }

    private void initViews() {

        mContext = getContext();

        mLLManager = new LinearLayoutManager(mContext);
        mLLManager.setReverseLayout(true);
        mLLManager.setStackFromEnd(true);

        mRecyclerView = mBinding.rvChatFragList;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLLManager);
    }

    private void initFirebase() {
        mRefChat = Fire.getRefChat().child(Fire.getIdUsuario());
        mRefChat.keepSynced(true);

        mRefUsuarios = Fire.getRefUsuarios();
        mRefUsuarios.keepSynced(true);

        mRefMensagens = Fire.getRefMensagens().child(Fire.getIdUsuario());
        mRefMensagens.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AdapterFragChat(firebaseOptions(), mContext, mRefMensagens, mRefUsuarios);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.startListening();
    }

    private FirebaseRecyclerOptions<Chat> firebaseOptions() {

        Query chatQuery = mRefChat.orderByChild(Const.CHAT_MSG_TIME);

        return new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(chatQuery, Chat.class)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

}

package com.pedromoreirareisgmail.pchat;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pedromoreirareisgmail.pchat.Adapters.AdapterMensagem;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Fire.FireUtils;
import com.pedromoreirareisgmail.pchat.Models.Mensagem;
import com.pedromoreirareisgmail.pchat.Utils.Comprimir;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.Utils.GetDateTime;
import com.pedromoreirareisgmail.pchat.Utils.PicassoDownload;
import com.pedromoreirareisgmail.pchat.databinding.ActivityChatBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        EditText.OnEditorActionListener {

    private static final int TOTAL_MENSAGENS_DOWNLOAD = 100;
    private ActivityChatBinding mBinding;
    private DatabaseReference mRefRoot;
    private DatabaseReference mRefAmigo;
    private DatabaseReference mRefMensagensAmigo;
    private TextView mTvBarNome;
    private TextView mTvBarVisualizacao;
    private CircleImageView mCivImagem;
    private EditText mEtMensagem;
    private LinearLayoutManager mLlManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private List<Mensagem> mListMensagens;
    private AdapterMensagem mAdapter;
    private String mIdUsuario;
    private String mIdAmigo;
    private String mNomeAmigo;
    private int mPaginaAtual = 1;
    private int mItemPos = 0;
    private String mUtilmaChave = "";
    private String mPenultimaChave = "";
    private Context mContext;
    private boolean isRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        if (getIntent().hasExtra(Const.INTENT_ID_AMIGO)) {

            mIdAmigo = getIntent().getStringExtra(Const.INTENT_ID_AMIGO);
        }

        if (getIntent().hasExtra(Const.INTENT_NOME_AMIGO)) {

            mNomeAmigo = getIntent().getStringExtra(Const.INTENT_NOME_AMIGO);
        }

        mContext = ChatActivity.this;

        initFirebase();

        initViews();
    }

    private void initFirebase() {

        mIdUsuario = Fire.getIdUsuario();

        mRefRoot = Fire.getRefRoot();
        mRefRoot.keepSynced(true);

        mRefAmigo = Fire.getRefUsuarios().child(mIdAmigo);
        mRefAmigo.keepSynced(true);


        mRefMensagensAmigo = Fire.getRefMensagens().child(mIdUsuario).child(mIdAmigo);
        mRefMensagensAmigo.keepSynced(true);
    }

    private void initViews() {


        Toolbar toolbar = (Toolbar) mBinding.toolbarChat;
        setSupportActionBar(toolbar);

        View actionBarView = getLayoutInflater().inflate(R.layout.chat_custom_bar, null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setCustomView(actionBarView);

        mCivImagem = actionBarView.findViewById(R.id.civ_chat_custom_bar);
        mTvBarNome = actionBarView.findViewById(R.id.tv_chat_custom_bar_nome);
        mTvBarVisualizacao = actionBarView.findViewById(R.id.tv_chat_custom_bar_ultima_visualizacao);

        mListMensagens = new ArrayList<>();
        mAdapter = new AdapterMensagem(mListMensagens, mIdUsuario, mIdAmigo, mContext);

        mSwipeRefresh = mBinding.srlChatRefresh;

        mLlManager = new LinearLayoutManager(mContext);
        mRecyclerView = mBinding.rvChatList;
        mRecyclerView.setLayoutManager(mLlManager);
        mRecyclerView.setAdapter(mAdapter);

        mEtMensagem = mBinding.etChatMensagem;

        mBinding.ivbutChatEnviarMensagem.setOnClickListener(this);
        mBinding.ivbutChatEnviarArqvuivo.setOnClickListener(this);

        mSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Fire.getUsuario() != null) {

            FireUtils.usuarioOnLine(Fire.getRefUsuario());
        }

        recuperarDadosAmigo();

        verificarJaTemChatAmigo();

        carregarMensagens();
    }

    @Override
    public void onRefresh() {

        isRefresh = true;

        mItemPos = 0;
        //carregarMaisMensagens();
        mListMensagens.clear();
        carregarMensagens();
    }

  /*  private void carregarMaisMensagens() {

        Query queryMaisMensagens = mRefMensagensAmigo.orderByKey().endAt(mUtilmaChave).limitToLast(TOTAL_MENSAGENS_DOWNLOAD);
        queryMaisMensagens.keepSynced(true);

        queryMaisMensagens.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);
                String mensagemKey = dataSnapshot.getKey();

                if (!mPenultimaChave.equals(mensagemKey)) {

                    mListMensagens.add(mItemPos++, mensagem);
                } else {

                    mPenultimaChave = mensagemKey;
                }

                if (mItemPos == 1) {

                    mUtilmaChave = mensagemKey;
                }

                mAdapter.notifyDataSetChanged();

                mSwipeRefresh.setRefreshing(false);

                mLlManager.scrollToPositionWithOffset(10, 0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }*/

    private void carregarMensagens() {

        Query queryMensagem = mRefMensagensAmigo.limitToLast(mPaginaAtual * TOTAL_MENSAGENS_DOWNLOAD);
        queryMensagem.keepSynced(true);

        queryMensagem.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Mensagem mensagem = dataSnapshot.getValue(Mensagem.class);

               /* mItemPos++;

                if (mItemPos == 1) {
                    String mensagemKey = dataSnapshot.getKey();

                    mUtilmaChave = mensagemKey;
                    mPenultimaChave = mensagemKey;
                }
                */

                mListMensagens.add(mensagem);
                mAdapter.notifyDataSetChanged();

                mRecyclerView.scrollToPosition(mListMensagens.size() - 1);

                if (isRefresh) {

                    mSwipeRefresh.setRefreshing(false);
                    isRefresh = false;
                }
                mPaginaAtual++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void recuperarDadosAmigo() {

        mTvBarNome.setText(mNomeAmigo);

        mRefAmigo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean onLine = (boolean) dataSnapshot.child(Const.DB_ON_LINE).getValue();

                if (onLine) {

                    mTvBarVisualizacao.setText(getString(R.string.online));

                } else {

                    if (dataSnapshot.hasChild(Const.DB_ULT_VEZ)) {

                        long ultimaVez = (long) dataSnapshot.child(Const.DB_ULT_VEZ).getValue();
                        mTvBarVisualizacao.setText(GetDateTime.getTimeAgo(ultimaVez, mContext));
                    }
                }

                final String urlImagem = dataSnapshot.child(Const.DB_THUMB).getValue().toString();

                if (!urlImagem.equals(Const.DB_REG_THUMB)) {

                    PicassoDownload.civChachePlaceholder(mContext, urlImagem, R.drawable.ic_usuario, mCivImagem);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void verificarJaTemChatAmigo() {

        Fire.getRefChat().child(mIdUsuario).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(mIdAmigo)) {

                    mRefRoot.updateChildren(FireUtils.mapAddUsuariosChat(mIdUsuario, mIdAmigo), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {

                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        FireUtils.ultimaVez(Fire.getRefUsuario());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ivbut_chat_enviar_mensagem:

                enviarMensagem();
                break;

            case R.id.ivbut_chat_enviar_arqvuivo:

                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                salvarImagem(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }

    }

    private void salvarImagem(Uri resultUri) {

        File filePath = new File(resultUri.getPath());

        final byte[] imagem = Comprimir.comprimirImagemNoLA(mContext, filePath, 40);
        final byte[] imagemThumb = Comprimir.comprimirImagemNoLA(mContext, filePath, 1);

        String pushId = FireUtils.pushIdMsg(mIdUsuario, mIdAmigo);

        StorageReference storageImagem = Fire.getStorageRefImgMsg().child(pushId + ".jpg");
        final StorageReference storageImagemThumb = Fire.getStorageRefImgMsgThumbnail().child(pushId + ".jpg");

        storageImagem.putBytes(imagem).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful()) {

                    final String urlImage = task.getResult().getDownloadUrl().toString();

                    UploadTask uploadTask = storageImagemThumb.putBytes(imagemThumb);

                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskThumb) {

                            if (taskThumb.isSuccessful()) {

                                String urlThumb = taskThumb.getResult().getDownloadUrl().toString();

                                mRefRoot.updateChildren(FireUtils.mapEnviarMsgImagem(mIdUsuario, mIdAmigo, urlImage, urlThumb),
                                        new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                if (databaseError != null) {

                                                }
                                            }
                                        });
                            }
                        }
                    });

                }
            }
        });
    }

    private void enviarMensagem() {

        String mensagem = mEtMensagem.getText().toString().trim();

        if (!TextUtils.isEmpty(mensagem)) {

            mEtMensagem.setText("");

            mRefRoot.updateChildren(FireUtils.mapEnviarMsgTexto(mIdUsuario, mIdAmigo, mensagem),
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {

                            }
                        }
                    });
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == R.id.et_chat_mensagem) {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                enviarMensagem();
                return true;
            }
        }

        return false;
    }
}

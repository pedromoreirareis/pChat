package com.pedromoreirareisgmail.pchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Fire.FireUtils;
import com.pedromoreirareisgmail.pchat.Models.Usuario;
import com.pedromoreirareisgmail.pchat.Utils.Buts;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.Utils.PicassoDownload;
import com.pedromoreirareisgmail.pchat.databinding.ActivityProfileBinding;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityProfileBinding mBinding;
    private DatabaseReference mRefUsuarios;
    private DatabaseReference mRefSolicitacoes;
    private DatabaseReference mRefAmigos;
    private DatabaseReference mRefNotificacoes;
    private DatabaseReference mRefRoot;
    private CircleImageView mCivImagem;
    private TextView mTvNome;
    private TextView mTvStatus;
    private Button mButEnviar;
    private Button mButRecusar;
    private ProgressDialog mDialog;
    private String mIdUsuario;
    private String mIdConvite;
    private String mEstadoAtual;
    private Context mContext;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        if (getIntent().hasExtra(Const.INTENT_ID_OUTRO_USUARIO)) {

            mIdConvite = getIntent().getStringExtra(Const.INTENT_ID_OUTRO_USUARIO);
        }

        mEstadoAtual = Const.ESTADO_NAO_AMIGOS;

        initViews();
    }

    private void initViews() {

        mContext = ProfileActivity.this;

        usuario = new Usuario();

        Toolbar toolbar = (Toolbar) mBinding.toolbarProfile;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.titulo_profile));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCivImagem = mBinding.civProfileImagem;
        mTvNome = mBinding.tvProfileNome;
        mTvStatus = mBinding.tvProfileStatus;
        mButEnviar = mBinding.butProfileEnviar;
        mButRecusar = findViewById(R.id.but_profile_recusar);

        mButEnviar.setOnClickListener(this);
        mButRecusar.setOnClickListener(this);

        mButRecusar.setEnabled(false);
        mButRecusar.setVisibility(View.GONE);

        mButEnviar.setEnabled(false);
        mButEnviar.setVisibility(View.GONE);

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(getString(R.string.dialog_msg_por_favor_aguarde));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initFirebase();

        if (Fire.getUsuario() != null) {

            FireUtils.usuarioOnLine(Fire.getRefUsuario());
        }

        recuperarDadosDoUsuario();
    }

    private void initFirebase() {

        mIdUsuario = Fire.getIdUsuario();

        if (mIdConvite.equals(mIdUsuario)) {

            mIdConvite = mIdUsuario;
        }

        mRefSolicitacoes = Fire.getRefSolicitacoes();
        mRefNotificacoes = Fire.getRefNotificacoes();

        mRefUsuarios = Fire.getRefUsuarios();
        mRefUsuarios.keepSynced(true);

        mRefAmigos = Fire.getRefAmigos();
        mRefAmigos.keepSynced(true);

        mRefRoot = Fire.getRefRoot();
    }

    private void recuperarDadosDoUsuario() {

        mDialog.setTitle(getString(R.string.dialog_titulo_carregando_dados));
        mDialog.show();

        mRefUsuarios.child(mIdConvite).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usuario = dataSnapshot.getValue(Usuario.class);

                mTvNome.setText(usuario.getNome());
                mTvStatus.setText(usuario.getStatus());

                if (!usuario.getImagem().equals(Const.DB_REG_IMAGEM)) {

                    PicassoDownload.civChachePlaceholder(mContext,usuario.getImagem(),R.drawable.ic_usuario,mCivImagem);
                }

                verificarSolicitacoesPendentes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void verificarSolicitacoesPendentes() {

        mRefSolicitacoes.child(mIdUsuario)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            if (dataSnapshot.hasChild(mIdConvite)) {

                                String tipo_solicitacao = dataSnapshot.child(mIdConvite).child(Const.SOL_TIPO).getValue().toString();

                                if (tipo_solicitacao.equals(Const.SOL_TIPO_RECEBIDA)) {

                                    // SOLICITAÇÃO RECEBIDA => BUT FICA: ACEITAR AMIZADE E RECUSAR AMIZADE

                                    mEstadoAtual = Const.ESTADO_SOL_RECEBIDA;
                                    Buts.solicitacaoRecebida(ProfileActivity.this, mButEnviar, mButRecusar);
                                    mDialog.dismiss();

                                } else if (tipo_solicitacao.equals(Const.SOL_TIPO_ENVIADA)) {

                                    // SOLICITAÇÃO RECEBIDA => BUT FICA: CANCELAR AMIZADE

                                    mEstadoAtual = Const.ESTADO_SOL_ENVIADA;
                                    Buts.solicitacaoEnviada(ProfileActivity.this, mButEnviar, mButRecusar);
                                    mDialog.dismiss();
                                }

                            } else {


                                mRefAmigos.child(mIdUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild(mIdConvite)) {

                                            // AMIGOS => BUT FICA: DESFAZER AMIZADE

                                            mEstadoAtual = Const.ESTADO_AMIGOS;
                                            Buts.amigos(ProfileActivity.this, mButEnviar);
                                            mDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }

                        if (!mIdUsuario.equals(mIdConvite)) {

                            mButEnviar.setVisibility(View.VISIBLE);
                            mButEnviar.setEnabled(true);
                        }

                        mDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.but_profile_enviar:

                mButEnviar.setEnabled(false);

                // NÃO AMIGOS - ADICIONAR AMIGOS
                if (mEstadoAtual.equals(Const.ESTADO_NAO_AMIGOS)) {

                    adicionarAmigos();
                }

                // CANCELAR SOLICITACAO DE AMIZADE
                if (mEstadoAtual.equals(Const.ESTADO_SOL_ENVIADA)) {

                    cancelarSolicitacao();
                }

                // ACEITAR SOLICITAÇÂO DE AMIZADE
                if (mEstadoAtual.equals(Const.ESTADO_SOL_RECEBIDA)) {

                    aceitarSolicitacao();
                }

                // DESFAZER AMIZADE
                if (mEstadoAtual.equals(Const.ESTADO_AMIGOS)) {

                    desfazerAmizade();
                }

                break;

            case R.id.but_profile_recusar:

                // RECUSAR SOLICITCAO DE AMIZADE
                recusarAmizade();
                break;
        }
    }

    private void recusarAmizade() {

        // ESTA SOLICITACAO RECEBIDA
        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_recusar_amizade));
        mDialog.show();

        boolean sucesso = FireUtils.recusarAmizade(Fire.getRefRoot(),mIdUsuario,mIdConvite);

        if(sucesso){

            // NAO AMIGO => BUT FICA: ADICIONAR AMIGO
            mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
            Buts.recusarAmizade(ProfileActivity.this, mButEnviar, mButRecusar);
            mDialog.dismiss();

        }else{

            mDialog.dismiss();
        }

        /*
        final HashMap<String, Object> mapRecusar = new HashMap<>();
        mapRecusar.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + mIdConvite, null);
        mapRecusar.put(Const.PASTA_SOLIC + "/" + mIdConvite + "/" + mIdUsuario, null);

        mRefRoot.updateChildren(mapRecusar, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    // NAO AMIGO => BUT FICA: ADICIONAR AMIGO
                    mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
                    Buts.recusarAmizade(ProfileActivity.this, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });
        */
    }

    private void desfazerAmizade() {

        // ESTA AMIGOS
        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_desfazer_amizade));
        mDialog.show();

        HashMap<String, Object> mapDesfazer = new HashMap<>();
        mapDesfazer.put(Const.PASTA_AMIGOS + "/" + mIdUsuario + "/" + mIdConvite, null);
        mapDesfazer.put(Const.PASTA_AMIGOS + "/" + mIdConvite + "/" + mIdUsuario, null);

        mRefRoot.updateChildren(mapDesfazer, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    // TERMINA NAO AMIGO => BUT FICA: ADICIONAR AMIGO
                    mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
                    Buts.desfazerAmizade(ProfileActivity.this, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });

    }

    private void aceitarSolicitacao() {

        // COMEÇA SOLICITAÇÃO RECEBIDA - ACEITAR
        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_aceitar_solicitacao));
        mDialog.show();

        HashMap<String, Object> mapAmigos = new HashMap<>();
        mapAmigos.put(Const.PASTA_AMIGOS + "/" + mIdUsuario + "/" + mIdConvite + "/" + Const.DATA, ServerValue.TIMESTAMP);
        mapAmigos.put(Const.PASTA_AMIGOS + "/" + mIdConvite + "/" + mIdUsuario + "/" + Const.DATA, ServerValue.TIMESTAMP);

        mapAmigos.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + mIdConvite, null);
        mapAmigos.put(Const.PASTA_SOLIC + "/" + mIdConvite + "/" + mIdUsuario, null);

        mRefRoot.updateChildren(mapAmigos, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    // TERMINA - AMIGOS => BUT FICA: DESFAZER AMIZADE
                    mEstadoAtual = Const.ESTADO_AMIGOS;
                    Buts.aceitarSolicitacao(ProfileActivity.this, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });

    }

    private void cancelarSolicitacao() {

        // COMEÇA SOLICITACAO ENVIADA
        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_cancelar_solicitacao));
        mDialog.show();

        HashMap<String, Object> mapCancelar = new HashMap<>();
        mapCancelar.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + mIdConvite, null);
        mapCancelar.put(Const.PASTA_SOLIC + "/" + mIdConvite + "/" + mIdUsuario, null);

        mRefRoot.updateChildren(mapCancelar, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    // TERMINA - NAO AMIGO => BUT FICA: ADICIONAR AMIGO
                    mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
                    Buts.cancelarSolicitacao(ProfileActivity.this, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });

    }

    private void adicionarAmigos() {

        //TODO FAZER ESTE AGORA

        // COMEÇA - NÃO AMIGOS
        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_adicionar_amigos));
        mDialog.show();

        String notifId = Fire.getRefNotificacoes().child(mIdConvite).push().getKey();

        HashMap<String, Object> mapNotificacao = new HashMap<>();
        mapNotificacao.put(Const.NOTIF_ORIGEM, mIdUsuario);
        mapNotificacao.put(Const.NOTIF_TIPO, Const.NOTIF_SOLICITADA);

        HashMap<String, Object> mapAdicionar = new HashMap<>();
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + mIdConvite + "/" + Const.SOL_TIPO, Const.SOL_TIPO_ENVIADA);
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + mIdConvite + "/" + mIdUsuario + "/" + Const.SOL_TIPO, Const.SOL_TIPO_RECEBIDA);
        mapAdicionar.put(Const.PASTA_NOTIF + "/" + mIdConvite + "/" + notifId, mapNotificacao);

        mRefRoot.updateChildren(mapAdicionar, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    // TERMINA - SOL ENVIADA => BUT FICA: CANCELA SOL AMIZADE
                    mEstadoAtual = Const.ESTADO_SOL_ENVIADA;
                    Buts.adicionarAmigos(ProfileActivity.this, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mDialog.isShowing()) {

            mDialog.dismiss();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        FireUtils.ultimaVez(Fire.getRefUsuario());
    }

}
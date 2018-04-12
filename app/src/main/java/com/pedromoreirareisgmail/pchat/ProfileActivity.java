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
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.databinding.ActivityProfileBinding;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.fire.FireUtils;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Buts;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.PicassoDownload;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityProfileBinding mBinding;
    private DatabaseReference mRefUsuarios;
    private DatabaseReference mRefSolicitacoes;
    private DatabaseReference mRefAmigos;
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

        } else if (getIntent().hasExtra("from_user_id")) {

            mIdConvite = getIntent().getStringExtra("from_user_id");
        }

        mEstadoAtual = Const.ESTADO_NAO_AMIGOS;

        initViews();
    }

    private void initViews() {

        mContext = ProfileActivity.this;

        usuario = new Usuario();

        Toolbar toolbar = (Toolbar) mBinding.toolbarProfile;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.titulo_profile));
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

                usuario = Objects.requireNonNull(dataSnapshot.getValue(Usuario.class));

                mTvNome.setText(usuario.getNome());
                mTvStatus.setText(usuario.getStatus());

                if (!usuario.getUrlImagem().equals(Const.IMG_PADRAO_IMAGEM)) {

                    PicassoDownload.civChachePlaceholder(mContext, usuario.getUrlImagem(), R.drawable.ic_usuario, mCivImagem);
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

                                String tipo_solicitacao = Objects.requireNonNull(dataSnapshot.child(mIdConvite).child(Const.SOL_TIPO).getValue()).toString();

                                if (tipo_solicitacao.equals(Const.SOL_TIPO_RECEBIDA)) {

                                    // SOLICITAÇÃO RECEBIDA => BUT FICA: ACEITAR AMIZADE E RECUSAR AMIZADE

                                    mEstadoAtual = Const.ESTADO_SOL_RECEBIDA;
                                    Buts.solicitacaoRecebida(mContext, mButEnviar, mButRecusar);
                                    mDialog.dismiss();

                                } else if (tipo_solicitacao.equals(Const.SOL_TIPO_ENVIADA)) {

                                    // SOLICITAÇÃO RECEBIDA => BUT FICA: CANCELAR AMIZADE

                                    mEstadoAtual = Const.ESTADO_SOL_ENVIADA;
                                    Buts.solicitacaoEnviada(mContext, mButEnviar, mButRecusar);
                                    mDialog.dismiss();
                                }

                            }
                        }

                        mRefAmigos.child(mIdUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild(mIdConvite)) {

                                    // AMIGOS => BUT FICA: DESFAZER AMIZADE

                                    mEstadoAtual = Const.ESTADO_AMIGOS;
                                    Buts.amigos(mContext, mButEnviar);
                                    mDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

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


    private void adicionarAmigos() {

        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_adicionar_amigos));
        mDialog.show();

        mRefRoot.updateChildren(FireUtils.mapAdicionar(mIdUsuario, mIdConvite), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    mEstadoAtual = Const.ESTADO_SOL_ENVIADA;
                    Buts.adicionarAmigos(mContext, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });
    }

    private void cancelarSolicitacao() {

        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_cancelar_solicitacao));
        mDialog.show();

        mRefRoot.updateChildren(FireUtils.mapCancelar(mIdUsuario, mIdConvite), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
                    Buts.cancelarSolicitacao(mContext, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });

    }

    private void aceitarSolicitacao() {

        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_aceitar_solicitacao));
        mDialog.show();

        mRefRoot.updateChildren(FireUtils.mapAceitar(mIdUsuario, mIdConvite), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    mEstadoAtual = Const.ESTADO_AMIGOS;
                    Buts.aceitarSolicitacao(mContext, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });
    }

    private void recusarAmizade() {

        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_recusar_amizade));
        mDialog.show();

        mRefRoot.updateChildren(FireUtils.mapRecusar(mIdUsuario, mIdConvite), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
                    Buts.recusarAmizade(mContext, mButEnviar, mButRecusar);
                    mDialog.dismiss();

                } else {

                    mDialog.dismiss();
                }
            }
        });

    }

    private void desfazerAmizade() {

        mDialog.setTitle(getString(R.string.dialog_solicitacoes_titulo_desfazer_amizade));
        mDialog.show();

        mRefRoot.updateChildren(FireUtils.mapDesfazer(mIdUsuario, mIdConvite), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {

                    mEstadoAtual = Const.ESTADO_NAO_AMIGOS;
                    Buts.desfazerAmizade(mContext, mButEnviar, mButRecusar);
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

        FireUtils.ultimoAcesso(Fire.getRefUsuario());
    }

}
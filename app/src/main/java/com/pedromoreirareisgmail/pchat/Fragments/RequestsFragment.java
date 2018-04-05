package com.pedromoreirareisgmail.pchat.Fragments;


import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Fire.FireUtils;
import com.pedromoreirareisgmail.pchat.Models.Solicitacao;
import com.pedromoreirareisgmail.pchat.Models.Usuario;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.Utils.Buts;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.Utils.PicassoDownload;
import com.pedromoreirareisgmail.pchat.databinding.FragmentRequestsBinding;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private FragmentRequestsBinding mBinding;
    private DatabaseReference mRefSolicitacao;
    private DatabaseReference mRefUsuarios;
    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLLManager;
    private String mIdUsuario;
    private ProgressDialog mDialog;
    private Context mContext;
    private Usuario usuario;


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

        usuario = new Usuario();

        mRecyclerView = mBinding.rvRequestFragList;

        mLLManager = new LinearLayoutManager(mContext);
        mLLManager.setReverseLayout(true);
        mLLManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLLManager);

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getString(R.string.dialog_msg_por_favor_aguarde));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
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

        buscarSolicitacoes();

        mAdapter.startListening();
    }

    private void buscarSolicitacoes() {


        FirebaseRecyclerOptions<Solicitacao> opcaoSolicitacao = new FirebaseRecyclerOptions.Builder<Solicitacao>()
                .setQuery(mRefSolicitacao, Solicitacao.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Solicitacao, SolicitacaoViewHolder>(opcaoSolicitacao) {
            @Override
            protected void onBindViewHolder(@NonNull final SolicitacaoViewHolder holder, int position, @NonNull Solicitacao model) {

                final String idConvite = getRef(position).getKey();

                // SOLICITACAO ENVIADA
                if (model.getTipo_solicitacao().equals(Const.SOL_TIPO_ENVIADA)) {

                    holder.setTipo(mContext.getString(R.string.msg_tipo_solicitacao_enviada));

                    mRefUsuarios.child(idConvite).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            usuario = dataSnapshot.getValue(Usuario.class);

                            holder.setNome(usuario.getNome());
                            holder.setStatus(usuario.getStatus());
                            holder.setImagem(usuario.getThumbnail());

                            Buts.fragRequestSolEnviada(holder, mContext);

                            holder.butAceitar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    mDialog.setTitle(mContext.getString(R.string.dialog_solicitacoes_titulo_cancelar_solicitacao));
                                    mDialog.show();

                                    Fire.getRefRoot().updateChildren(FireUtils.mapCancelar(mIdUsuario, idConvite),
                                            new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    if (databaseError == null) {

                                                        mDialog.dismiss();

                                                    } else {

                                                        mDialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    // SOLICITACAO RECEBIDA
                } else {

                    holder.setTipo(mContext.getString(R.string.msg_tipo_solicitacao_recebida));


                    mRefUsuarios.child(idConvite).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Buts.fragRequestSolRecebida(holder, mContext);

                            usuario = dataSnapshot.getValue(Usuario.class);

                            holder.setNome(usuario.getNome());
                            holder.setStatus(usuario.getStatus());
                            holder.setImagem(usuario.getThumbnail());

                            holder.butAceitar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    mDialog.setTitle(mContext.getString(R.string.dialog_solicitacoes_titulo_aceitar_solicitacao));
                                    mDialog.show();

                                    Fire.getRefRoot().updateChildren(FireUtils.mapAceitar(mIdUsuario, idConvite),
                                            new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    if (databaseError == null) {

                                                        mDialog.dismiss();

                                                    } else {

                                                        mDialog.dismiss();
                                                    }
                                                }
                                            });

                                }
                            });

                            holder.butRecusar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    mDialog.setTitle(mContext.getString(R.string.dialog_solicitacoes_titulo_recusar_amizade));
                                    mDialog.show();

                                    Fire.getRefRoot().updateChildren(FireUtils.mapRecusar(mIdUsuario, idConvite),
                                            new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                    if (databaseError == null) {

                                                        mDialog.dismiss();

                                                    } else {

                                                        mDialog.dismiss();
                                                    }
                                                }
                                            });
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @NonNull
            @Override
            public SolicitacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.item_request_layout, parent, false);

                return new SolicitacaoViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.stopListening();
    }

    public class SolicitacaoViewHolder extends RecyclerView.ViewHolder {

        public Button butAceitar;
        public Button butRecusar;
        private CircleImageView civImagem;
        private TextView tvNome;
        private TextView tvStatus;
        private TextView tvTipo;

        private SolicitacaoViewHolder(View itemView) {
            super(itemView);

            civImagem = itemView.findViewById(R.id.civ_request_imagem);
            tvNome = itemView.findViewById(R.id.tv_request_nome);
            tvStatus = itemView.findViewById(R.id.tv_request_status);
            tvTipo = itemView.findViewById(R.id.tv_request_tipo);
            butAceitar = itemView.findViewById(R.id.but_request_aceitar);
            butRecusar = itemView.findViewById(R.id.but_request_recusar);
        }

        public void setNome(String nome) {

            tvNome.setText(nome);
        }

        public void setStatus(String status) {

            tvStatus.setText(status);
        }

        public void setTipo(String tipo) {

            tvTipo.setText(tipo);
        }

        public void setImagem(final String urlImagem) {

            if (!urlImagem.equals(Const.DB_REG_THUMB)) {

                PicassoDownload.civChachePlaceholder(mContext, urlImagem, R.drawable.ic_usuario, civImagem);
            }
        }
    }
}


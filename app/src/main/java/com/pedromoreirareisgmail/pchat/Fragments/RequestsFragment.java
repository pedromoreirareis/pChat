package com.pedromoreirareisgmail.pchat.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.Models.Solicitacao;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.databinding.FragmentRequestsBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private FragmentRequestsBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUsuario;
    private DatabaseReference mRefRoot;
    private DatabaseReference mRefSolicitacao;
    private DatabaseReference mRefUsuarios;
    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLLManager;
    private String mIdUsuario;
    private ProgressDialog mDialog;
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

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage( mContext.getString(R.string.dialog_msg_por_favor_aguarde));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();
        mUsuario = mAuth.getCurrentUser();
        mIdUsuario = mUsuario.getUid();

        mRefRoot = FirebaseDatabase.getInstance().getReference();

        mRefUsuarios = mRefRoot.child(Const.PASTA_USUARIOS);
        mRefUsuarios.keepSynced(true);

        mRefSolicitacao = mRefRoot.child(Const.PASTA_SOLIC).child(mIdUsuario);
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

                if (model.getTipo_solicitacao().equals(Const.SOL_TIPO_ENVIADA)) {

                    holder.setTvTipo(mContext.getString(R.string.msg_tipo_solicitacao_enviada));

                    mRefUsuarios.child(idConvite).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            String nome = dataSnapshot.child(Const.DB_NOME).getValue().toString();
                            String status = dataSnapshot.child(Const.DB_STATUS).getValue().toString();
                            String thumbnail = dataSnapshot.child(Const.DB_THUMB).getValue().toString();

                            holder.setTvNome(nome);
                            holder.setTvStatus(status);
                            holder.setImagem(thumbnail, mContext);


                            holder.butAceitar.setBackgroundResource(R.drawable.but_cancelar_sol);
                            holder.butAceitar.setText(mContext.getString(R.string.but_profile_cancelar_solicitacao));
                            holder.butAceitar.setVisibility(View.VISIBLE);

                            holder.butRecusar.setVisibility(View.GONE);

                            holder.butAceitar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    mDialog.setTitle(mContext.getString(R.string.dialog_solicitacoes_titulo_cancelar_solicitacao));
                                    mDialog.show();

                                    HashMap<String, Object> mapCancelar = new HashMap<>();
                                    mapCancelar.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + idConvite, null);
                                    mapCancelar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + mIdUsuario, null);

                                    mRefRoot.updateChildren(mapCancelar, new DatabaseReference.CompletionListener() {
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


                } else {

                    holder.setTvTipo(mContext.getString(R.string.msg_tipo_solicitacao_recebida));


                    mRefUsuarios.child(idConvite).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String nome = dataSnapshot.child(Const.DB_NOME).getValue().toString();
                            String status = dataSnapshot.child(Const.DB_STATUS).getValue().toString();
                            String thumbnail = dataSnapshot.child(Const.DB_THUMB).getValue().toString();

                            holder.setTvNome(nome);
                            holder.setTvStatus(status);
                            holder.setImagem(thumbnail,mContext);

                            holder.butAceitar.setBackgroundResource(R.drawable.but_aceitar);
                            holder.butAceitar.setText(mContext.getString(R.string.but_profile_aceitar_solicitacao));
                            holder.butAceitar.setVisibility(View.VISIBLE);

                            holder.butAceitar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    mDialog.setTitle(mContext.getString(R.string.dialog_solicitacoes_titulo_aceitar_solicitacao));
                                    mDialog.show();

                                    HashMap<String, Object> mapAmigos = new HashMap<>();
                                    mapAmigos.put(Const.PASTA_AMIGOS + "/" + mIdUsuario + "/" + idConvite + "/" + Const.DATA, ServerValue.TIMESTAMP);
                                    mapAmigos.put(Const.PASTA_AMIGOS + "/" + idConvite + "/" + mIdUsuario + "/" + Const.DATA, ServerValue.TIMESTAMP);

                                    mapAmigos.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + idConvite, null);
                                    mapAmigos.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + mIdUsuario, null);

                                    mRefRoot.updateChildren(mapAmigos, new DatabaseReference.CompletionListener() {
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

                            holder.butRecusar.setBackgroundResource(R.drawable.but_recusar);
                            holder.butRecusar.setText(mContext.getString(R.string.but_profile_recusar_amizade));
                            holder.butRecusar.setVisibility(View.VISIBLE);

                            holder.butRecusar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    mDialog.setTitle(mContext.getString(R.string.dialog_solicitacoes_titulo_recusar_amizade));
                                    mDialog.show();

                                    final HashMap<String, Object> mapRecusar = new HashMap<>();
                                    mapRecusar.put(Const.PASTA_SOLIC + "/" + mIdUsuario + "/" + idConvite, null);
                                    mapRecusar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + mIdUsuario, null);

                                    mRefRoot.updateChildren(mapRecusar, new DatabaseReference.CompletionListener() {
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

        public void setTvNome(String nome) {

            tvNome.setText(nome);
        }

        public void setTvStatus(String status) {

            tvStatus.setText(status);
        }

        public void setTvTipo(String tipo) {

            tvTipo.setText(tipo);
        }

        public void setImagem(final String urlImagem, final Context context) {

            if (!urlImagem.equals(Const.DB_REG_THUMB)) {

                if (!TextUtils.isEmpty(urlImagem)) {
                    Picasso.with(context)
                            .load(urlImagem)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(context.getResources().getDrawable(R.drawable.ic_usuario))
                            .into(civImagem, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                    Picasso.with(context)
                                            .load(urlImagem)
                                            .placeholder(context.getResources().getDrawable(R.drawable.ic_usuario))
                                            .into(civImagem);
                                }
                            });
                }
            }
        }
    }
}

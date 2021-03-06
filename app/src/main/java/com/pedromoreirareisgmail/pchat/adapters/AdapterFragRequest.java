package com.pedromoreirareisgmail.pchat.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
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
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.fire.FireUtils;
import com.pedromoreirareisgmail.pchat.models.Solicitacao;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Buts;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.PicassoDownload;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFragRequest extends FirebaseRecyclerAdapter<Solicitacao, AdapterFragRequest.SolicitacaoViewHolder> {

    private Context mContext;
    private Usuario usuario;
    private ProgressDialog mDialog;
    private DatabaseReference mRefUsuarios;
    private String mIdUsuario;

    public AdapterFragRequest(@NonNull FirebaseRecyclerOptions<Solicitacao> options, Context context, DatabaseReference refUsuarios, String idUsuario) {
        super(options);
        mContext = context;
        mRefUsuarios = refUsuarios;
        mIdUsuario = idUsuario;
        usuario = new Usuario();

        mDialog = new ProgressDialog(mContext);
        mDialog.setMessage(mContext.getString(R.string.dialog_msg_por_favor_aguarde));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }

    @Override
    protected void onBindViewHolder(@NonNull final SolicitacaoViewHolder holder, int position, @NonNull Solicitacao model) {

        final String idConvite = getRef(position).getKey();

        // SOLICITACAO ENVIADA
        if (model.getTipo_solicitacao().equals(Const.SOL_TIPO_ENVIADA)) {

            holder.setTipo(mContext.getString(R.string.msg_tipo_solicitacao_enviada));

            mRefUsuarios.child(idConvite).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    usuario = Objects.requireNonNull(dataSnapshot.getValue(Usuario.class));

                    holder.setNome(usuario.getNome());
                    holder.setStatus(usuario.getStatus());
                    holder.setImagem(usuario.getUrlThumbnail());

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

                    usuario = Objects.requireNonNull(dataSnapshot.getValue(Usuario.class));

                    holder.setNome(usuario.getNome());
                    holder.setStatus(usuario.getStatus());
                    holder.setImagem(usuario.getUrlThumbnail());

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

        private void setNome(String nome) {

            tvNome.setText(nome);
        }

        private void setStatus(String status) {

            tvStatus.setText(status);
        }

        private void setTipo(String tipo) {

            tvTipo.setText(tipo);
        }

        private void setImagem(final String urlImagem) {

            if (!urlImagem.equals(Const.IMG_PADRAO_THUMBNAIL)) {

                PicassoDownload.civChachePlaceholder(mContext, urlImagem, R.drawable.ic_usuario, civImagem);
            }
        }
    }

}

package com.pedromoreirareisgmail.pchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.ChatActivity;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.models.Chat;
import com.pedromoreirareisgmail.pchat.models.Mensagem;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.GetDateTime;
import com.pedromoreirareisgmail.pchat.utils.PicassoDownload;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFragChat extends FirebaseRecyclerAdapter<Chat, AdapterFragChat.ChatViewHolder> {

    private Context mContext;
    private Usuario usuario;
    private DatabaseReference mRefMensagens;
    private DatabaseReference mRefUsuarios;
    private List<Usuario> listUsuario;

    public AdapterFragChat(@NonNull FirebaseRecyclerOptions<Chat> options, Context context, DatabaseReference refMensagens, DatabaseReference refUsuarios) {
        super(options);
        mContext = context;
        mRefMensagens = refMensagens;
        mRefUsuarios = refUsuarios;
        usuario = new Usuario();
        listUsuario = new ArrayList<>();
    }


    @Override
    protected void onBindViewHolder(@NonNull final ChatViewHolder holder,int position, @NonNull Chat chat) {

        final String idAmigo = getRef(position).getKey();

        Query queryUltimaMensagem = mRefMensagens.child(idAmigo).limitToLast(1);

        queryUltimaMensagem.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {

                    Mensagem mensagem = Objects.requireNonNull(dataSnapshot.getValue(Mensagem.class));

                    switch (mensagem.getTipo()) {

                        case Const.MSG_TIPO_TEXT:

                            holder.setMensagem(mensagem.getMensagem());
                            holder.ivFoto.setVisibility(View.GONE);
                            break;

                        case Const.MSG_TIPO_IMAGE:

                            holder.setMensagem(mContext.getString(R.string.msg_foto));
                            holder.ivFoto.setVisibility(View.VISIBLE);
                            break;

                        default:
                            holder.setMensagem(mContext.getString(R.string.msg_nenhuma_msg));
                            holder.ivFoto.setVisibility(View.GONE);
                            break;
                    }

                } else {

                    holder.setMensagem(mContext.getString(R.string.msg_nenhuma_msg));
                    holder.ivFoto.setVisibility(View.GONE);
                }
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

        mRefUsuarios.child(idAmigo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot != null) {

                    usuario = Objects.requireNonNull(dataSnapshot.getValue(Usuario.class));

                    listUsuario.add(usuario);

                    holder.setNome(usuario.getNome());
                    holder.setImagem(usuario.getUrlThumbnail());
                    holder.setOnline(usuario.isOnline(), usuario.getUltAcesso(), mContext);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chatIntent = new Intent(mContext, ChatActivity.class);
                chatIntent.putExtra(Const.INTENT_ID_OUTRO_USUARIO, idAmigo);
                chatIntent.putExtra(Const.INTENT_NOME_OUTRO_USUARIO, holder.tvNome.getText().toString());
                mContext.startActivity(chatIntent);
            }
        });
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_msg_layout, parent, false);

        return new ChatViewHolder(view);
    }


    public class ChatViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civImagem;
        private ImageView ivOnline;
        private TextView tvNome;
        private TextView tvMensagem;
        private TextView tvOnline;
        private ImageView ivFoto;

        private ChatViewHolder(View itemView) {
            super(itemView);

            civImagem = itemView.findViewById(R.id.civ_chat_imagem);
            ivOnline = itemView.findViewById(R.id.iv_chat_online);
            tvNome = itemView.findViewById(R.id.tv_chat_nome);
            tvMensagem = itemView.findViewById(R.id.tv_chat_mensagem);
            tvOnline = itemView.findViewById(R.id.tv_chat_online);
            ivFoto = itemView.findViewById(R.id.iv_chat_foto);
        }

        private void setImagem(final String urlImagem) {

            if (!urlImagem.equals(Const.IMG_PADRAO_THUMBNAIL)) {

                PicassoDownload.civChachePlaceholder(mContext, urlImagem, R.drawable.ic_usuario, civImagem);

            }
        }

        private void setNome(String nome) {

            tvNome.setText(nome);
        }

        private void setMensagem(String mensagem) {

            tvMensagem.setText(mensagem);
        }

        private void setOnline(boolean online, long time, Context context) {

            if (online) {

                ivOnline.setBackgroundResource(R.drawable.online_green);
                tvOnline.setVisibility(View.GONE);

            } else {

                ivOnline.setBackgroundResource(R.drawable.online_red);
                tvOnline.setVisibility(View.VISIBLE);
                tvOnline.setText(GetDateTime.getTimeAgo(time, context));
            }
        }
    }
}

package com.pedromoreirareisgmail.pchat.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromoreirareisgmail.pchat.Models.Mensagem;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdapterMensagem extends RecyclerView.Adapter<AdapterMensagem.MensagemViewHolder> {


    private static final int MSG_RIGHT = 0;
    private static final int MSG_LEFT = 1;
    private static final int IMG_RIGHT = 2;
    private static final int IMG_LEFT = 3;

    private List<Mensagem> mListMensagens;
    private String mIdUsuario;
    private String mIdAmigo;
    private Context mContext;

    public AdapterMensagem(List<Mensagem> list, String idUsuario, String idAmigo, Context context) {
        mListMensagens = list;
        mIdUsuario = idUsuario;
        mIdAmigo = idAmigo;
        mContext = context;
    }

    @NonNull
    @Override
    public MensagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == MSG_RIGHT) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_right_layout, parent, false);
            return new MensagemViewHolder(view);

        } else if (viewType == MSG_LEFT) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_left_layout, parent, false);
            return new MensagemViewHolder(view);

        } else if (viewType == IMG_RIGHT) {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_img_right_layout, parent, false);
            return new MensagemViewHolder(view);

        } else {

            view = LayoutInflater.from(mContext).inflate(R.layout.item_img_left_layout, parent, false);
            return new MensagemViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mListMensagens.get(position);

        if (mensagem.getOrigem().equals(mIdUsuario)) {

            if (mensagem.getTipo().equals(Const.CHAT_MSG_TIPO_TEXT)) {

                return MSG_RIGHT;

            } else {

                return IMG_RIGHT;
            }

        } else if (mensagem.getOrigem().equals(mIdAmigo)) {

            if (mensagem.getTipo().equals(Const.CHAT_MSG_TIPO_TEXT)) {

                return MSG_LEFT;

            } else {

                return IMG_LEFT;
            }
        }

        return 1000;
    }


    @Override
    public void onBindViewHolder(@NonNull final MensagemViewHolder holder, int position) {

        final Mensagem msg = mListMensagens.get(position);

        String idMensagem = msg.getOrigem();

        if (idMensagem.equals(mIdUsuario)) {

            if (msg.getTipo().equals(Const.CHAT_MSG_TIPO_TEXT)) {

                holder.tvMensagem.setText(msg.getMensagem());

            } else if (msg.getTipo().equals(Const.CHAT_MSG_TIPO_IMAGE)) {

                Picasso.with(mContext).load(msg.getThumb()).into(holder.ivImagem);
            }

        } else if (idMensagem.equals(mIdAmigo)) {

            if (msg.getTipo().equals(Const.CHAT_MSG_TIPO_TEXT)) {

                holder.tvMensagem.setText(msg.getMensagem());

            } else if (msg.getTipo().equals(Const.CHAT_MSG_TIPO_IMAGE)) {

                Picasso.with(mContext)
                        .load(msg.getThumb())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.ivImagem, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(mContext)
                                        .load(msg.getThumb())
                                        .into(holder.ivImagem);
                            }
                        });
            }
        }

        holder.tvTime.setText(formatarData(msg.getTime()));

        if (msg.getTipo().equals(Const.CHAT_MSG_TIPO_IMAGE)) {

            holder.ivImagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Picasso.with(mContext)
                            .load(msg.getMensagem())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .into(holder.ivImagem, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                    Picasso.with(mContext)
                                            .load(msg.getMensagem())
                                            .into(holder.ivImagem);
                                }
                            });
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (mListMensagens.size() == 0) {

            return 0;

        } else {

            return mListMensagens.size();
        }
    }

    private String formatarData(long dataLong) {

        return new SimpleDateFormat("dd/MM/yy,  HH:mm", Locale.getDefault()).format(dataLong);
    }

    public class MensagemViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivImagem;
        private TextView tvMensagem;
        private TextView tvTime;

        private MensagemViewHolder(View itemView) {
            super(itemView);

            tvMensagem = itemView.findViewById(R.id.tv_msg_chat_mensagem);
            tvTime = itemView.findViewById(R.id.tv_msg_chat_data);
            ivImagem = itemView.findViewById(R.id.iv_msg_chat_imagem);
        }
    }
}
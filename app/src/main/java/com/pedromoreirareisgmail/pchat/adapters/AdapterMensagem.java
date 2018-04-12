package com.pedromoreirareisgmail.pchat.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.models.Mensagem;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.PicassoDownload;
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

        switch (viewType) {

            case MSG_RIGHT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_right_layout, parent, false);
                return new MensagemViewHolder(view);

            case MSG_LEFT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_msg_left_layout, parent, false);
                return new MensagemViewHolder(view);

            case IMG_RIGHT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_img_right_layout, parent, false);
                return new MensagemViewHolder(view);

            case IMG_LEFT:
                view = LayoutInflater.from(mContext).inflate(R.layout.item_img_left_layout, parent, false);
                return new MensagemViewHolder(view);

            default:
                view = LayoutInflater.from(mContext).inflate(null,parent,false);
                return new MensagemViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mListMensagens.get(position);


        if (mensagem.getIdOrigem().equals(mIdUsuario)) {

            if (mensagem.getTipo().equals(Const.MSG_TIPO_TEXT)) {

                return MSG_RIGHT;

            } else {

                return IMG_RIGHT;
            }

        } else if (mensagem.getIdOrigem().equals(mIdAmigo)) {

            if (mensagem.getTipo().equals(Const.MSG_TIPO_TEXT)) {

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

        String idOrigem = msg.getIdOrigem();

        if (idOrigem.equals(mIdUsuario)) {

            if (msg.isLida()) {

                Drawable imagem = mContext.getResources().getDrawable(R.drawable.ic_check_true);
                imagem.setBounds(0, 0, imagem.getIntrinsicWidth(), imagem.getIntrinsicHeight());
                holder.tvTime.setCompoundDrawables(null, null, imagem, null);

            } else {

                Drawable imagem = mContext.getResources().getDrawable(R.drawable.ic_check_false);
                imagem.setBounds(0, 0, imagem.getIntrinsicWidth(), imagem.getIntrinsicHeight());
                holder.tvTime.setCompoundDrawables(null, null, imagem, null);
            }

            if (msg.getTipo().equals(Const.MSG_TIPO_TEXT)) {

                holder.tvMensagem.setText(msg.getMensagem());

            } else if (msg.getTipo().equals(Const.MSG_TIPO_IMAGE)) {

                Picasso.with(mContext).load(msg.getUrlThumbnail()).into(holder.ivImagem);
            }

        } else if (idOrigem.equals(mIdAmigo)) {

            if (msg.getTipo().equals(Const.MSG_TIPO_TEXT)) {

                holder.tvMensagem.setText(msg.getMensagem());

            } else if (msg.getTipo().equals(Const.MSG_TIPO_IMAGE)) {

                PicassoDownload.ivChache(mContext, msg.getUrlThumbnail(), holder.ivImagem);
            }
        }

        holder.tvTime.setText(formatarData(msg.getTimestamp()));

        if (msg.getTipo().equals(Const.MSG_TIPO_IMAGE)) {

            holder.ivImagem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int height = holder.ivImagem.getHeight();
                    int width = holder.ivImagem.getWidth();

                    PicassoDownload.ivChache(mContext, msg.getUrlMensagem(), holder.ivImagem);

                    holder.ivImagem.setMinimumHeight(height);
                    holder.ivImagem.setMinimumWidth(width);
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

        return new SimpleDateFormat("HH:mm, dd/MM/yy", Locale.getDefault()).format(dataLong);
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
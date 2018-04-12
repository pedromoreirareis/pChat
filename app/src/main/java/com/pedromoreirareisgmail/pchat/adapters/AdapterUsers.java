package com.pedromoreirareisgmail.pchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.pedromoreirareisgmail.pchat.ProfileActivity;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.PicassoDownload;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterUsers extends FirebaseRecyclerAdapter<Usuario, AdapterUsers.UsersViewHolder>{

    private Context mContext;

    public AdapterUsers(@NonNull FirebaseRecyclerOptions<Usuario> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Usuario usuario) {

        holder.setNome(usuario.getNome());
        holder.setStatus(usuario.getStatus());
        holder.setImagem(usuario.getUrlThumbnail());

        final String idUsuarioB = getRef(position).getKey();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentProfile = new Intent(mContext, ProfileActivity.class);
                intentProfile.putExtra(Const.INTENT_ID_OUTRO_USUARIO, idUsuarioB);
                mContext.startActivity(intentProfile);
            }
        });
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_users_layout, parent, false);

        return new UsersViewHolder(view);
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civImagem;
        private TextView tvNome;
        private TextView tvStatus;

        private UsersViewHolder(View itemView) {
            super(itemView);

            civImagem = itemView.findViewById(R.id.civ_users_imagem);
            tvNome = itemView.findViewById(R.id.tv_users_nome);
            tvStatus = itemView.findViewById(R.id.tv_users_status);
        }

        private void setImagem(final String urlImagem) {

            if (!urlImagem.equals(Const.IMG_PADRAO_THUMBNAIL)) {

                PicassoDownload.civChachePlaceholder(mContext, urlImagem, R.drawable.ic_usuario, civImagem);
            }
        }

        private void setNome(String nome) {

            tvNome.setText(nome);
        }

        private void setStatus(String status) {

            tvStatus.setText(status);
        }
    }
}

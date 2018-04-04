package com.pedromoreirareisgmail.pchat.Fragments;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pedromoreirareisgmail.pchat.ChatActivity;
import com.pedromoreirareisgmail.pchat.Models.Amigo;
import com.pedromoreirareisgmail.pchat.ProfileActivity;
import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.databinding.FragmentFriendsBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUsuario;
    private DatabaseReference mRefRoot;
    private DatabaseReference mRefAmigos;
    private DatabaseReference mRefUsuarios;
    private FirebaseRecyclerAdapter mAdapterAmigos;
    private RecyclerView mReciclerView;
    private String mIdUsuario;
    private Context mContext;

    public FriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false);

        initViews();
        initFirebase();

        return mBinding.getRoot();
    }

    private void initViews() {

        mContext = getContext();

        mReciclerView = mBinding.rvFriendsList;
        mReciclerView.setHasFixedSize(true);
        mReciclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();
        mUsuario = mAuth.getCurrentUser();
        mIdUsuario = mUsuario.getUid();

        mRefRoot = FirebaseDatabase.getInstance().getReference();

        mRefAmigos = mRefRoot.child(Const.PASTA_AMIGOS).child(mIdUsuario);
        mRefAmigos.keepSynced(true);

        mRefUsuarios = mRefRoot.child(Const.PASTA_USUARIOS);
        mRefUsuarios.keepSynced(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        buscaAmigos();

        mAdapterAmigos.startListening();
    }

    private void buscaAmigos() {

        FirebaseRecyclerOptions<Amigo> opcoesBuscaAmigos =
                new FirebaseRecyclerOptions.Builder<Amigo>()
                        .setQuery(mRefAmigos, Amigo.class)
                        .build();

        mAdapterAmigos = new FirebaseRecyclerAdapter<Amigo, FriendsViewHolder>(opcoesBuscaAmigos) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder holder, int position, @NonNull Amigo model) {

                final String idAmigo = getRef(position).getKey();

                final long data = model.getData();
                holder.setTvData(data, mContext);

                mRefUsuarios.child(idAmigo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null) {

                            final String nome = dataSnapshot.child(Const.DB_NOME).getValue().toString();
                            String status = dataSnapshot.child(Const.DB_STATUS).getValue().toString();
                            String urlImagem = dataSnapshot.child(Const.DB_THUMB).getValue().toString();

                            if (dataSnapshot.hasChild(Const.DB_ON_LINE)) {

                                boolean online = (boolean) dataSnapshot.child(Const.DB_ON_LINE).getValue();
                                holder.setTvOnline(online, mContext);
                            }

                            holder.setTvNome(nome);
                            holder.setTvStatus(status);
                            holder.setCivImagem(urlImagem, mContext);

                            holder.clContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent chatIntent = new Intent(mContext, ChatActivity.class);
                                    chatIntent.putExtra(Const.INTENT_ID_AMIGO, idAmigo);
                                    chatIntent.putExtra(Const.INTENT_NOME_AMIGO, nome);
                                    startActivity(chatIntent);
                                }
                            });

                            holder.ivMais.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                                    profileIntent.putExtra(Const.INTENT_ID_OUTRO_USUARIO, idAmigo);
                                    startActivity(profileIntent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.item_friend_layout, parent, false);

                return new FriendsViewHolder(view);
            }
        };

        mReciclerView.setAdapter(mAdapterAmigos);
    }


    @Override
    public void onStop() {
        super.onStop();

        mAdapterAmigos.stopListening();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civImagem;
        private TextView tvNome;
        private TextView tvStatus;
        private TextView tvData;
        private ImageView ivOnline;
        private ConstraintLayout clContent;
        private ImageView ivMais;

        private FriendsViewHolder(View itemView) {
            super(itemView);

            tvNome = itemView.findViewById(R.id.tv_friend_nome);
            tvData = itemView.findViewById(R.id.tv_friend_data);
            ivOnline = itemView.findViewById(R.id.iv_friend_online);
            tvStatus = itemView.findViewById(R.id.tv_friend_status);
            civImagem = itemView.findViewById(R.id.civ_friend_imagem);
            clContent = itemView.findViewById(R.id.cl_friend_content);
            ivMais = itemView.findViewById(R.id.ivbut_friend_mais);
        }

        public void setCivImagem(final String urlImagem, final Context context) {

            if (!urlImagem.equals(Const.DB_REG_THUMB)) {

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

        public void setTvNome(String nome) {

            tvNome.setText(nome);
        }

        public void setTvOnline(Boolean online, Context context) {

            if (online) {

                ivOnline.setBackground(context.getResources().getDrawable(R.drawable.online_green));
            } else {

                ivOnline.setBackground(context.getResources().getDrawable(R.drawable.online_red));
            }


        }

        public void setTvStatus(String status) {

            tvStatus.setText(status);
        }

        public void setTvData(long data, Context context) {

            tvData.setText(String.format(context.getString(R.string.msg_amigos_desde),
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(data)));
        }

    }
}

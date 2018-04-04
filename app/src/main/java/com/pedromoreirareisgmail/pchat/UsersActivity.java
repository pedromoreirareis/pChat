package com.pedromoreirareisgmail.pchat;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Fire.FireUtils;
import com.pedromoreirareisgmail.pchat.Models.Usuario;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.Utils.PicassoDownload;
import com.pedromoreirareisgmail.pchat.databinding.ActivityUsersBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter mAdapter;
    private DatabaseReference mRefUsuarios;
    private ActivityUsersBinding mBinding;
    private RecyclerView mRecyclerView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);

        initViews();
    }

    private void initViews() {

        mContext = UsersActivity.this;

        Toolbar toolbar = (Toolbar) mBinding.toolbarAllUsuarios;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.titulo_usuarios));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = mBinding.rvUsersList;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onStart() {
        super.onStart();

        initFirebase();

        if (Fire.getUsuario() != null) {

            FireUtils.usuarioOnLine(Fire.getRefUsuario());
        }

        buscarUsuarios();

        mAdapter.startListening();
    }

    private void initFirebase() {

        mRefUsuarios = Fire.getRefUsuarios();
        mRefUsuarios.keepSynced(true);
    }

    private void buscarUsuarios() {

        FirebaseRecyclerOptions<Usuario> opcoesBuscaUsuarios =
                new FirebaseRecyclerOptions.Builder<Usuario>()
                        .setQuery(mRefUsuarios, Usuario.class)
                        .build();

        mAdapter = new FirebaseRecyclerAdapter<Usuario, UsersViewHolder>(opcoesBuscaUsuarios) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull Usuario usuario) {

                holder.setNome(usuario.getNome());
                holder.setStatus(usuario.getStatus());
                holder.setImagem(usuario.getThumbnail());

                final String idUsuario = getRef(position).getKey();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentProfile = new Intent(mContext, ProfileActivity.class);
                        intentProfile.putExtra(Const.INTENT_ID_OUTRO_USUARIO, idUsuario);
                        startActivity(intentProfile);
                    }
                });
            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.item_users_layout, parent, false);

                return new UsersViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        FireUtils.ultimaVez(Fire.getRefUsuario());
        mAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // TODO: implemnetar Search de usuario - ao digitar nome de usuario e clicar fazer busca

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
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

        public void setImagem(final String urlImagem) {

            if (!urlImagem.equals(Const.DB_REG_THUMB)) {

                PicassoDownload.civChachePlaceholder(mContext, urlImagem, R.drawable.ic_usuario, civImagem);
            }
        }

        public void setNome(String nome) {

            tvNome.setText(nome);
        }

        public void setStatus(String status) {

            tvStatus.setText(status);
        }
    }
}

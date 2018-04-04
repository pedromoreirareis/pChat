package com.pedromoreirareisgmail.pchat;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pedromoreirareisgmail.pchat.Adapters.AdapterPaginas;
import com.pedromoreirareisgmail.pchat.Fire.Fire;
import com.pedromoreirareisgmail.pchat.Fire.FireUtils;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUsuario;
    private DatabaseReference mRefUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        initViews();
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) mBinding.toolbarMain;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        TabLayout tab = mBinding.tabsMain;
        ViewPager pagina = mBinding.paginasMain;
        AdapterPaginas adapterPaginas = new AdapterPaginas(getSupportFragmentManager(), this);

        pagina.setAdapter(adapterPaginas);
        pagina.setCurrentItem(1);
        pagina.setOffscreenPageLimit(2);
        tab.setupWithViewPager(pagina);

        pagina.setClipToPadding(false);
        pagina.setPageMargin(12);
    }


    @Override
    protected void onStart() {
        super.onStart();

        initFirebase();

        if (mUsuario == null) {

            irParaActivityStart();
        } else {

            continueFirebase();

            FireUtils.usuarioOnLine(mRefUsuario);
        }
    }


    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();
        mUsuario = mAuth.getCurrentUser();
    }

    private void continueFirebase() {

        mRefUsuario = FirebaseDatabase.getInstance().getReference().child(Const.PASTA_USUARIOS).child(mUsuario.getUid());
    }

    private void irParaActivityStart() {

        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_menu_configuracoes:

                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_menu_sair:


                if (mUsuario != null) {

                    FireUtils.usuarioOffLine(mRefUsuario);
                    Fire.getAuth().signOut();

                    FireUtils.firebaseSetNull();

                    irParaActivityStart();
                }


                return true;

            case R.id.action_menu_usuarios:

                Intent usuariosIntent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(usuariosIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mUsuario != null) {

            FireUtils.ultimaVez(mRefUsuario);
        }
    }
}

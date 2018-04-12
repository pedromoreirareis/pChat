package com.pedromoreirareisgmail.pchat;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.pedromoreirareisgmail.pchat.adapters.AdapterPaginas;
import com.pedromoreirareisgmail.pchat.databinding.ActivityMainBinding;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.fire.FireUtils;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private ActivityMainBinding mBinding;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

        initViews();
    }

    private void initViews() {

        mContext = MainActivity.this;

        Toolbar toolbar = (Toolbar) mBinding.toolbarMain;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));

        TabLayout tab = mBinding.tabsMain;
        ViewPager pagina = mBinding.paginasMain;
        AdapterPaginas adapterPaginas = new AdapterPaginas(getSupportFragmentManager(), mContext);

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

        if (Fire.getUsuario() == null) {

            irParaActivityStart();
        } else {

            Fire.getAuth().addAuthStateListener(this);

            FireUtils.usuarioOnLine(Fire.getRefUsuario());

            FireUtils.usuarioDeviceAcesso(Fire.getRefUsuario());
        }
    }


    private void irParaActivityStart() {

        Intent startIntent = new Intent(mContext, StartActivity.class);
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

                Intent settingsIntent = new Intent(mContext, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_menu_sair:


                if (Fire.getUsuario() != null) {

                    FireUtils.usuarioOffLine(Fire.getRefUsuario());
                    Fire.getAuth().signOut();
                    FireUtils.firebaseSetNull();

                    irParaActivityStart();
                }

                return true;

            case R.id.action_menu_usuarios:

                Intent usuariosIntent = new Intent(mContext, UsersActivity.class);
                startActivity(usuariosIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (Fire.getUsuario() != null) {

            FireUtils.ultimoAcesso(Fire.getRefUsuario());
            Fire.getAuth().removeAuthStateListener(this);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        if(firebaseAuth.getCurrentUser() == null){

            irParaActivityStart();
        }
    }
}

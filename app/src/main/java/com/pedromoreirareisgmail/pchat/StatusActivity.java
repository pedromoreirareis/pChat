package com.pedromoreirareisgmail.pchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.pedromoreirareisgmail.pchat.databinding.ActivityStatusBinding;
import com.pedromoreirareisgmail.pchat.fire.Fire;
import com.pedromoreirareisgmail.pchat.fire.FireUtils;
import com.pedromoreirareisgmail.pchat.models.Usuario;
import com.pedromoreirareisgmail.pchat.utils.Const;
import com.pedromoreirareisgmail.pchat.utils.Internet;
import com.pedromoreirareisgmail.pchat.utils.Validacoes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityStatusBinding mBinding;
    private EditText mEtStatus;
    private EditText mEtNome;
    private ProgressDialog mDialog;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_status);

        initViews();
    }

    private void initViews() {

        mContext = StatusActivity.this;

        Toolbar toolbar = (Toolbar) mBinding.toolbarStatus;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.titulo_status));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtNome = mBinding.etStatusNome;
        mEtStatus = mBinding.etStatusValor;

        mBinding.butStatusSalvar.setOnClickListener(this);

        if (getIntent() != null) {

            Usuario usuario = getIntent().getParcelableExtra(Const.INTENT_SETTINGS_STATUS);
            mEtNome.setText(usuario.getNome());
            mEtStatus.setText(usuario.getStatus());
        }

        mDialog = new ProgressDialog(mContext);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (Fire.getUsuario() != null) {

            FireUtils.usuarioOnLine(Fire.getRefUsuario());
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.but_status_salvar:

                salvarNovoStatusUsuario();
                break;
        }
    }

    private void salvarNovoStatusUsuario() {

        String status = mEtStatus.getText().toString().trim();
        String nome = mEtNome.getText().toString().trim();

        if (Validacoes.status(mContext, mEtStatus, status)) {

            if (Internet.temInternet(mContext)) {

                mDialog.setTitle(getString(R.string.dialog_titulo_salvando_alteracoes));
                mDialog.setMessage(getString(R.string.dialog_msg_por_favor_aguarde));
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.show();

                Map<String, Object> mapUsuario = new HashMap<>();
                mapUsuario.put(Const.USUARIO_NOME, nome);
                mapUsuario.put(Const.USUARIO_STATUS, status);

                Fire.getRefUsuario().updateChildren(mapUsuario, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if (databaseError == null) {

                            mDialog.dismiss();
                            Toast.makeText(mContext, getString(R.string.toast_alteracoes_sucesso), Toast.LENGTH_LONG).show();
                            finish();
                        } else {

                            mDialog.dismiss();
                            Toast.makeText(mContext, getString(R.string.toast_alteracoes_erro), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        FireUtils.ultimoAcesso(Fire.getRefUsuario());
    }
}

package com.pedromoreirareisgmail.pchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.Utils.Internet;
import com.pedromoreirareisgmail.pchat.Utils.Validacoes;
import com.pedromoreirareisgmail.pchat.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding mBinding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRefUsuario;
    private EditText mEtNome;
    private EditText mEtEmail;
    private EditText mEtSenha;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        initViews();
        initFirebase();
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) mBinding.toolbarRegister;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.titulo_register));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEtNome = mBinding.etRegisterNome;
        mEtEmail = mBinding.etRegisterEmail;
        mEtSenha = mBinding.etRegisterSenha;
        mBinding.butRegisterCriarConta.setOnClickListener(this);

        mDialog = new ProgressDialog(this);
    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.but_register_criar_conta:

                pegarDadosNomeEmailSenha();
                break;
        }
    }

    private void pegarDadosNomeEmailSenha() {

        String nome = mEtNome.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String senha = mEtSenha.getText().toString().trim();

        if (Validacoes.nomeEmailSenha(this, mEtNome, nome, mEtEmail, email, mEtSenha, senha)) {

            if (Internet.temInternet(this)) {

                registrarNovaContaUsuario(nome, email, senha);
            }
        }
    }

    private void registrarNovaContaUsuario(final String nome, String email, String senha) {

        mDialog.setTitle(getString(R.string.dialogo_criar_conta_titulo));
        mDialog.setMessage(getString(R.string.dialogo_criar_conta_msg));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            final FirebaseUser usuario = mAuth.getCurrentUser();
                            if (usuario != null) {

                                mDialog.setTitle(getString(R.string.dialogo_criar_conta_titulo_dados));
                                mDialog.setMessage(getString(R.string.dialogo_criar_conta_msg_dados));

                                String idUsuario = usuario.getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();
                                mRefUsuario = FirebaseDatabase.getInstance().getReference(Const.PASTA_USUARIOS).child(idUsuario);

                                Map<String, Object> usuarioMap = new HashMap<>();
                                usuarioMap.put(Const.DB_NOME, nome);
                                usuarioMap.put(Const.DB_STATUS, Const.DB_REG_STATUS);
                                usuarioMap.put(Const.DB_IMAGEM, Const.DB_REG_IMAGEM);
                                usuarioMap.put(Const.DB_THUMB, Const.DB_REG_THUMB);
                                usuarioMap.put(Const.DB_D_TOKEN, deviceToken);
                                usuarioMap.put(Const.DB_ON_LINE, true);


                                mRefUsuario.setValue(usuarioMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            mDialog.dismiss();
                                            abrirActivityMain();
                                            finish();

                                        } else {

                                            mDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this, getString(R.string.toast_register_erro_criar_conta), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }

                        } else {

                            String erroRegistrar = "";

                            try {

                                if (task.getException() != null) {

                                    throw task.getException();
                                }


                            } catch (FirebaseAuthWeakPasswordException e) {

                                erroRegistrar = getString(R.string.toast_register_erro_criar_conta_senha);

                            } catch (Exception e) {

                                erroRegistrar = getString(R.string.toast_register_erro_criar_conta_msg);
                            }


                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, String.format(getString(R.string.toast_register_erro_criar_conta), erroRegistrar), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void abrirActivityMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }
}

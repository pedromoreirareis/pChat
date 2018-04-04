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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pedromoreirareisgmail.pchat.Utils.Const;
import com.pedromoreirareisgmail.pchat.Utils.Internet;
import com.pedromoreirareisgmail.pchat.Utils.Validacoes;
import com.pedromoreirareisgmail.pchat.databinding.ActivityStartBinding;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityStartBinding mBinding;
    private FirebaseAuth mAuth;
    private DatabaseReference mRefUsuarios;
    private EditText mEtEmail;
    private EditText mEtSenha;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(StartActivity.this, R.layout.activity_start);

        initViews();
        initFirebase();
    }

    private void initViews() {

        Toolbar toolbar = (Toolbar) mBinding.toolbarStart;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        mEtEmail = mBinding.etStartEmail;
        mEtSenha = mBinding.etStartSenha;
        mBinding.tvStartCriarConta.setOnClickListener(this);
        mBinding.butStartEntar.setOnClickListener(this);

        mDialog = new ProgressDialog(this);
    }

    private void initFirebase() {

        mAuth = FirebaseAuth.getInstance();
        mRefUsuarios = FirebaseDatabase.getInstance().getReference().child(Const.PASTA_USUARIOS);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_start_criar_conta:

                Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.but_start_entar:

                pegarDadosEmailSenha();
                break;
        }
    }

    private void pegarDadosEmailSenha() {

        String email = mEtEmail.getText().toString().trim();
        String senha = mEtSenha.getText().toString().trim();

        if (Validacoes.emailSenha(this, mEtEmail, email, mEtSenha, senha)) {

            if (Internet.temInternet(this)) {

                entrarContaUsuario(email, senha);
            }
        }
    }

    private void entrarContaUsuario(String email, String senha) {

        mDialog.setTitle(getString(R.string.dialogo_entrar_titulo));
        mDialog.setMessage(getString(R.string.dialogo_entrar_msg));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String idUsuario = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            Map<String, Object> usuarioMap = new HashMap<>();
                            usuarioMap.put(Const.DB_D_TOKEN, deviceToken);
                            usuarioMap.put(Const.DB_ON_LINE, true);

                            DatabaseReference refUsuario = FirebaseDatabase.getInstance().getReference().child(Const.PASTA_USUARIOS).child(idUsuario);
                            refUsuario.updateChildren(usuarioMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if (databaseError == null) {

                                        mDialog.dismiss();
                                        abrirActivityMain();
                                        finish();
                                    } else {

                                        mDialog.dismiss();
                                        Toast.makeText(StartActivity.this, getString(R.string.toast_start_erro_entrar), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                        } else {

                            mDialog.dismiss();
                            Toast.makeText(StartActivity.this, getString(R.string.toast_start_erro_entrar), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void abrirActivityMain() {

        Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }
}

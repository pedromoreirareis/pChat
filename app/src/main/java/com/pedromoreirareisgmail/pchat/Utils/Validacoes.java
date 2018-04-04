package com.pedromoreirareisgmail.pchat.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.pedromoreirareisgmail.pchat.R;

public class Validacoes {

    public Validacoes() {
    }

    public static boolean emailSenha(Context context, EditText editEmail, String email, EditText editSenha, String senha) {

        if (TextUtils.isEmpty(email)) {

            editEmail.setError(context.getString(R.string.erro_start_email));
            editEmail.requestFocus();
            return false;

        }
        if (TextUtils.isEmpty(senha)) {

            editSenha.setError(context.getString(R.string.erro_start_senha));
            editSenha.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean nomeEmailSenha(Context context, EditText editNome, String nome, EditText editEmail, String email, EditText editSenha, String senha) {

        if (TextUtils.isEmpty(nome)) {

            editNome.setError(context.getString(R.string.erro_register_usuario));
            editNome.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email)) {

            editEmail.setError(context.getString(R.string.erro_register_email));
            editEmail.requestFocus();
            return false;

        }
        if (TextUtils.isEmpty(senha)) {

            editSenha.setError(context.getString(R.string.erro_register_senha));
            editSenha.requestFocus();
            return false;
        }

        return true;
    }

    public static boolean status(Context context, EditText editStatus, String status) {

        if (TextUtils.isEmpty(status)) {

            editStatus.setError(context.getString(R.string.erro_start_email));
            editStatus.requestFocus();
            return false;
        }

        return true;
    }
}
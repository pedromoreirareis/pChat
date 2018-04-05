package com.pedromoreirareisgmail.pchat.Utils;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.pedromoreirareisgmail.pchat.Fragments.RequestsFragment;
import com.pedromoreirareisgmail.pchat.R;

public class Buts {

    public static void adicionarAmigos(Context context, Button butEnviar, Button butRecusar) {

        // SOL ENVIADA => BUT FICA: CANCELA SOL AMIZADE

        butEnviar.setText(context.getString(R.string.but_profile_cancelar_solicitacao));
        butEnviar.setEnabled(true);

        butEnviar.setBackground(context.getDrawable(R.drawable.but_cancelar_sol));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorWhite));

        butRecusar.setEnabled(false);
        butRecusar.setVisibility(View.GONE);
    }

    public static void cancelarSolicitacao(Context context, Button butEnviar, Button butRecusar) {

        // NAO AMIGO => BUT FICA: ADICIONAR AMIGO

        butEnviar.setText(context.getString(R.string.but_profile_adicionar_amigos));
        butEnviar.setEnabled(true);

        butEnviar.setBackground(context.getDrawable(R.drawable.but_adicionar));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorBlack));

        butRecusar.setVisibility(View.GONE);
        butRecusar.setEnabled(false);
    }

    public static void aceitarSolicitacao(Context context, Button butEnviar, Button butRecusar) {

        // AMIGOS => BUT FICA: DESFAZER AMIZADE

        butEnviar.setText(context.getString(R.string.but_profile_desfazer_amizade));
        butEnviar.setEnabled(true);

        butEnviar.setBackground(context.getDrawable(R.drawable.but_desfazer));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorWhite));

        butRecusar.setVisibility(View.GONE);
        butRecusar.setEnabled(false);
    }

    public static void desfazerAmizade(Context context, Button butEnviar, Button butRecusar) {

        // NAO AMIGO => BUT FICA: ADICIONAR AMIGO

        butEnviar.setText(context.getString(R.string.but_profile_adicionar_amigos));
        butEnviar.setEnabled(true);

        butEnviar.setBackground(context.getDrawable(R.drawable.but_adicionar));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorBlack));

        butRecusar.setVisibility(View.GONE);
        butRecusar.setEnabled(false);
    }

    public static void recusarAmizade(Context context, Button butEnviar, Button butRecusar) {

        // NAO AMIGO => BUT FICA: ADICIONAR AMIGO

        butEnviar.setText(context.getString(R.string.but_profile_adicionar_amigos));
        butEnviar.setEnabled(true);

        butEnviar.setBackground(context.getDrawable(R.drawable.but_adicionar));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorBlack));

        butRecusar.setVisibility(View.GONE);
        butRecusar.setEnabled(false);
    }


    public static void solicitacaoRecebida(Context context, Button butEnviar, Button butRecusar) {

        // SOLICITAÇÃO RECEBIDA => BUT FICA: ACEITAR AMIZADE E RECUSAR AMIZADE

        butEnviar.setText(context.getString(R.string.but_profile_aceitar_solicitacao));

        butEnviar.setBackground(context.getDrawable(R.drawable.but_aceitar));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorWhite));

        butEnviar.setVisibility(View.VISIBLE);
        butEnviar.setEnabled(true);

        butRecusar.setVisibility(View.VISIBLE);
        butRecusar.setEnabled(true);
    }

    public static void solicitacaoEnviada(Context context, Button butEnviar, Button butRecusar) {

        // SOLICITAÇÃO RECEBIDA => BUT FICA: CANCELAR AMIZADE

        butEnviar.setText(context.getString(R.string.but_profile_cancelar_solicitacao));

        butEnviar.setBackground(context.getDrawable(R.drawable.but_cancelar_sol));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorWhite));

        butRecusar.setVisibility(View.GONE);
        butRecusar.setEnabled(false);
    }

    public static void amigos(Context context, Button butEnviar) {

        butEnviar.setText(context.getString(R.string.but_profile_desfazer_amizade));

        butEnviar.setBackground(context.getDrawable(R.drawable.but_desfazer));
        butEnviar.setTextColor(context.getResources().getColor(R.color.colorWhite));

        butEnviar.setVisibility(View.VISIBLE);
        butEnviar.setEnabled(true);
    }


    public static void fragRequestSolEnviada(RequestsFragment.SolicitacaoViewHolder holder, Context context){

        holder.butAceitar.setBackgroundResource(R.drawable.but_cancelar_sol);
        holder.butAceitar.setText(context.getString(R.string.but_profile_cancelar_solicitacao));
        holder.butAceitar.setVisibility(View.VISIBLE);

        holder.butRecusar.setVisibility(View.GONE);
    }

    public static void fragRequestSolRecebida(RequestsFragment.SolicitacaoViewHolder holder, Context context){

        holder.butAceitar.setBackgroundResource(R.drawable.but_aceitar);
        holder.butAceitar.setText(context.getString(R.string.but_profile_aceitar_solicitacao));
        holder.butAceitar.setVisibility(View.VISIBLE);

        holder.butRecusar.setBackgroundResource(R.drawable.but_recusar);
        holder.butRecusar.setText(context.getString(R.string.but_profile_recusar_amizade));
        holder.butRecusar.setVisibility(View.VISIBLE);
    }
}
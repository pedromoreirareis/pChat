package com.pedromoreirareisgmail.pchat.fire;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.pedromoreirareisgmail.pchat.utils.Const;

import java.util.HashMap;
import java.util.Map;

public class FireUtils {


    /***************************** ADICIONAR UM USUARIO AOS AMIGOS *********************************/
    public static Map<String, Object> mapAdicionar(String idUsuario, String idConvite) {

        Map<String, Object> mapAdicionar = new HashMap<>();
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite + "/" + Const.SOL_TIPO, Const.SOL_TIPO_ENVIADA);
        mapAdicionar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario + "/" + Const.SOL_TIPO, Const.SOL_TIPO_RECEBIDA);
        mapAdicionar.put(Const.PASTA_NOTIF_SOL + "/" + idConvite + "/" + idPushNotfAmizade(idConvite), mapNotifAmizade(idUsuario));

        return mapAdicionar;
    }

    private static Map<String, Object> mapNotifAmizade(String idUsuario) {

        Map<String, Object> mapNotificacao = new HashMap<>();
        mapNotificacao.put(Const.NOTIF_ORIGEM, idUsuario);
        mapNotificacao.put(Const.NOTIF_TIPO, Const.NOTIF_SOLICITADA);

        return mapNotificacao;
    }

    private static String idPushNotfAmizade(String idConvite) {

        return Fire.getRefNotificacoesSOL().child(idConvite).push().getKey();
    }

    /************************ CANCELAR O ENVIO DE UMA SOLICITAÇÃO DE AMIZADE **********************/
    public static Map<String, Object> mapCancelar(String idUsuario, String idConvite) {

        Map<String, Object> mapCancelar = new HashMap<>();
        mapCancelar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapCancelar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        return mapCancelar;
    }

    /**************************** ACEITAR UMA SOLICITAÇÃO DE AMIZADE ******************************/
    public static Map<String, Object> mapAceitar(String idUsuario, String idConvite) {

        Map<String, Object> mapAmigos = new HashMap<>();
        mapAmigos.put(Const.PASTA_AMIGOS + "/" + idUsuario + "/" + idConvite + "/" + Const.AMIGOS_TIMESTAMP, ServerValue.TIMESTAMP);
        mapAmigos.put(Const.PASTA_AMIGOS + "/" + idConvite + "/" + idUsuario + "/" + Const.AMIGOS_TIMESTAMP, ServerValue.TIMESTAMP);

        mapAmigos.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapAmigos.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        return mapAmigos;
    }

    /**************************** RECUSAR UMA SOLICITAÇÃO DE AMIZADE ******************************/
    public static Map<String, Object> mapRecusar(String idUsuario, String idConvite) {

        Map<String, Object> mapRecusar = new HashMap<>();
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idUsuario + "/" + idConvite, null);
        mapRecusar.put(Const.PASTA_SOLIC + "/" + idConvite + "/" + idUsuario, null);

        return mapRecusar;
    }

    /*********************************** DESFAZER UMA AMIZADE *************************************/
    public static Map<String, Object> mapDesfazer(String idUsuario, String idConvite) {

        Map<String, Object> mapDesfazer = new HashMap<>();
        mapDesfazer.put(Const.PASTA_AMIGOS + "/" + idUsuario + "/" + idConvite, null);
        mapDesfazer.put(Const.PASTA_AMIGOS + "/" + idConvite + "/" + idUsuario, null);

        mapDesfazer.put(Const.PASTA_CHATS + "/" + idConvite + "/" + idUsuario, null);
        mapDesfazer.put(Const.PASTA_CHATS + "/" + idUsuario + "/" + idConvite, null);

        mapDesfazer.put(Const.PASTA_MENSAGENS + "/" + idConvite + "/" + idUsuario, null);
        mapDesfazer.put(Const.PASTA_MENSAGENS + "/" + idUsuario + "/" + idConvite, null);

        return mapDesfazer;
    }

    /******************************* ENVIAR UMA MENSAGEM DE TEXTO *********************************/
    public static Map<String, Object> mapEnviarMsgTexto(String idUsuario, String idAmigo, String mensagem) {

        String rootUsuario = Const.PASTA_MENSAGENS + "/" + idUsuario + "/" + idAmigo + "/";
        String rootAmigo = Const.PASTA_MENSAGENS + "/" + idAmigo + "/" + idUsuario + "/";

        String pushId = pushIdMsg(idUsuario, idAmigo);

        Map<String, Object> mapEnviarMensagem = new HashMap<>();
        mapEnviarMensagem.put(rootUsuario + "/" + pushId, mapMsgTexto(idUsuario, idAmigo, mensagem));
        mapEnviarMensagem.put(rootAmigo + "/" + pushId, mapMsgTexto(idUsuario, idAmigo, mensagem));

        mapEnviarMensagem.put(Const.PASTA_NOTIF_MSG + "/" + idAmigo + "/" + idPushNotfMsg(idAmigo), mapNotifMsg(idUsuario, pushId));

        return mapEnviarMensagem;
    }

    private static Map<String, Object> mapMsgTexto(String idUsuario, String idAmigo, String mensagem) {

        Map<String, Object> mapMensagem = new HashMap<>();
        mapMensagem.put(Const.MSG_TIPO, Const.MSG_TIPO_TEXT);
        mapMensagem.put(Const.MSG_MENSAGEM, mensagem);
        mapMensagem.put(Const.MSG_LIDA, false);
        mapMensagem.put(Const.MSG_ID_ORIGEM, idUsuario);
        mapMensagem.put(Const.MSG_ID_DESTINO, idAmigo);
        mapMensagem.put(Const.MSG_TIMESTAMP, ServerValue.TIMESTAMP);
        mapMensagem.put(Const.MSG_URL_THUMBNAIL, Const.MSG_URL_PADRAO_THUMBNAIL);
        mapMensagem.put(Const.MSG_URL_PADRAO_IMAGEM, Const.MSG_URL_PADRAO_IMAGEM);

        return mapMensagem;
    }

    public static String pushIdMsg(String idUsuario, String idAmigo) {

        DatabaseReference pushMsg = Fire.getRefRoot().child(Const.PASTA_MENSAGENS)
                .child(idUsuario).child(idAmigo).push();

        return pushMsg.getKey();
    }

    private static String idPushNotfMsg(String idAmigo) {

        return Fire.getRefNotificacoesMSG().child(idAmigo).push().getKey();
    }

    private static Map<String, Object> mapNotifMsg(String idUsuario, String pushId) {

        Map<String, Object> mapNotificacaoMSG = new HashMap<>();
        mapNotificacaoMSG.put(Const.NOTIF_ORIGEM, idUsuario);
        mapNotificacaoMSG.put(Const.NOTIF_PUSH_ID, pushId);

        return mapNotificacaoMSG;
    }

    /******************************* ENVIAR UMA MENSAGEM DE IMAGEM *********************************/
    public static Map<String, Object> mapEnviarMsgImagem(String idUsuario, String idAmigo, String urlImagem, String urlThumb) {

        String rootUsuario = Const.PASTA_MENSAGENS + "/" + idUsuario + "/" + idAmigo + "/";
        String rootAmigo = Const.PASTA_MENSAGENS + "/" + idAmigo + "/" + idUsuario + "/";

        String pushId = pushIdMsg(idUsuario, idAmigo);

        Map<String, Object> mapEnviarMensagem = new HashMap<>();
        mapEnviarMensagem.put(rootUsuario + "/" + pushId, mapMsgImagem(idUsuario, idAmigo, urlImagem, urlThumb));
        mapEnviarMensagem.put(rootAmigo + "/" + pushId, mapMsgImagem(idUsuario, idAmigo, urlImagem, urlThumb));

        return mapEnviarMensagem;
    }

    private static Map<String, Object> mapMsgImagem(String idUsuario, String idAmigo, String urlImage, String urlThumb) {

        Map<String, Object> mapMensagemImagem = new HashMap<>();
        mapMensagemImagem.put(Const.MSG_TIPO, Const.MSG_TIPO_IMAGE);
        mapMensagemImagem.put(Const.MSG_MENSAGEM, Const.MSG_TEXT_VAZIO);
        mapMensagemImagem.put(Const.MSG_LIDA, false);
        mapMensagemImagem.put(Const.MSG_URL_IMAGE, urlImage);
        mapMensagemImagem.put(Const.MSG_URL_THUMBNAIL, urlThumb);
        mapMensagemImagem.put(Const.MSG_TIMESTAMP, ServerValue.TIMESTAMP);
        mapMensagemImagem.put(Const.MSG_ID_ORIGEM, idUsuario);
        mapMensagemImagem.put(Const.MSG_ID_DESTINO, idAmigo);

        return mapMensagemImagem;
    }

    /**************************** ADICIONAR UM USUARIO A PASTA CHATS ******************************/
    public static Map<String, Object> mapAddUsuariosChat(String idUsuario, String idAmigo) {

        Map<String, Object> mapDadosChat = new HashMap<>();
        mapDadosChat.put(Const.CHAT_LIDA, false);
        mapDadosChat.put(Const.CHAT_TIMESTAMP, ServerValue.TIMESTAMP);

        Map<String, Object> mapUsuarioChat = new HashMap<>();

        mapUsuarioChat.put(Const.PASTA_CHATS + "/" + idUsuario + "/" + idAmigo, mapDadosChat);
        mapUsuarioChat.put(Const.PASTA_CHATS + "/" + idAmigo + "/" + idUsuario, mapDadosChat);

        return mapUsuarioChat;
    }

    /********************************* STATUS DE ACESSO AO APP ***********************************/
    public static void usuarioOnLine(DatabaseReference refUsuario) {

        refUsuario.child(Const.USUARIO_ONLINE).setValue(true);
    }

    public static void ultimoAcesso(DatabaseReference refUsuario) {

        refUsuario.child(Const.USUARIO_ULT_ACESSO).setValue(ServerValue.TIMESTAMP);
    }

    public static void usuarioOffLine(DatabaseReference refUsuario) {

        refUsuario.child(Const.USUARIO_ONLINE).setValue(false);
    }

    public static void usuarioDeviceAcesso(DatabaseReference refUsuario) {

        refUsuario.child(Const.USUARIO_DEVICETOKEN).setValue(Fire.getDeviceToken());
        refUsuario.child(Const.USUARIO_ULT_ACESSO).setValue(ServerValue.TIMESTAMP);
    }


    /************************** NULL PARA TODAS INSTANCIAS FIREBASE *******************************/
    public static void firebaseSetNull() {

        Fire.setDeviceToken();

        Fire.setAuth();
        Fire.setUsuario();
        Fire.setIdUsuario();

        Fire.setRefAmigos();
        Fire.setRefNotificacoesSOL();
        Fire.setRefNotificacoesMSG();
        Fire.setRefRoot();
        Fire.setRefSolicitacoes();
        Fire.setRefUsuario();
        Fire.setRefUsuarios();
        Fire.setRefChat();
        Fire.setRefMensagens();

        Fire.setStorageRefRoot();
        Fire.setStorageRefImgPerfil();
        Fire.setStorageRefImgThumbnail();
        Fire.setStorageRefImgMsg();
        Fire.setStorageRefImgMsgThumbnail();
    }
}
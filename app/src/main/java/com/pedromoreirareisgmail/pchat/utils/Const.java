package com.pedromoreirareisgmail.pchat.utils;

public class Const {

    /**************************************** PASTAS **********************************************/

    // PASTAS DATABASE
    public static final String PASTA_USUARIOS = "Usuarios";
    public static final String PASTA_SOLIC = "Solicitacoes";
    public static final String PASTA_AMIGOS = "Amigos";
    public static final String PASTA_NOTIF_SOL = "NotifSolAmizade";
    public static final String PASTA_NOTIF_MSG = "NotifMensagens";
    public static final String PASTA_CHATS = "Chats";
    public static final String PASTA_MENSAGENS = "Mensagens";

    // PASTAS STORAGE
    public static final String S_PASTA_IMG_PERFIL_USUARIO = "Img_perfil_usuario";
    public static final String S_PASTA_IMG_PERFIL_USUARIO_THUMBNAIL = "Img_perfil_usuario_thumbnail";
    public static final String S_PASTA_IMG_MSG_CHATS = "Img_msg_chats";
    public static final String S_PASTA_IMG_MSG_CHATS_THUMBNAIL = "Img_msg_chats_thumbnail";



    /**************************************** CAMPOS **********************************************/

    //USUARIO
    public static final String USUARIO_NOME = "nome";
    public static final String USUARIO_STATUS = "status";
    public static final String USUARIO_IMAGEM = "urlImagem";
    public static final String USUARIO_URLTHUMBNAIL = "urlThumbnail";
    public static final String USUARIO_DEVICETOKEN = "deviceToken";
    public static final String USUARIO_ONLINE = "online";
    public static final String USUARIO_ULT_ACESSO = "ultAcesso";

    //AMIGOS
    public static final String AMIGOS_TIMESTAMP = "timestamp";

    // CHAT MENSAGEM
    public static final String MSG_MENSAGEM = "mensagem";
    public static final String MSG_URL_IMAGE = "urlMensagem";
    public static final String MSG_LIDA = "lida";
    public static final String MSG_TIMESTAMP = "timestamp";
    public static final String MSG_ID_ORIGEM = "idOrigem";
    public static final String MSG_ID_DESTINO = "idDestino";
    public static final String MSG_URL_THUMBNAIL = "urlThumbnail";
    public static final String MSG_TIPO = "tipo";

    public static final String MSG_TIPO_TEXT = "text";
    public static final String MSG_TIPO_IMAGE = "image";

    public static final String MSG_URL_PADRAO_THUMBNAIL = "url_thumbnail_padrao";
    public static final String MSG_URL_PADRAO_IMAGEM = "url_imagem_padrao";

    public static final String MSG_TEXT_VAZIO = "";

    // CHAT
    public static final String CHAT_LIDA = "lida";
    public static final String CHAT_TIMESTAMP = "timestamp";

    // PERFIL
    public static final String IMG_PADRAO_IMAGEM = "imagemPadrao";
    public static final String IMG_PADRAO_THUMBNAIL = "thumbnailPadrao";
    public static final String STATUS_PADRAO = "Viva o melhor da vida!";

    // TIPO SOLICITACOES DE AMIZADE
    public static final String SOL_TIPO = "tipo_solicitacao";
    public static final String SOL_TIPO_ENVIADA = "enviada";
    public static final String SOL_TIPO_RECEBIDA = "recebida";

    // ESTADOS DAS SOLICITAÇÕES DE AMIZADE
    public static final String ESTADO_NAO_AMIGOS = "nao_amigos";
    public static final String ESTADO_SOL_ENVIADA = "solicitacao_enviada";
    public static final String ESTADO_SOL_RECEBIDA = "solicitacao_recebida";
    public static final String ESTADO_AMIGOS = "amigos";

    // INTENTS
    /* Este é um usuário que não é o atual da aplicação - Ex: Amigo */
    public static final String INTENT_ID_OUTRO_USUARIO = "id_outro_usuario";
    public static final String INTENT_NOME_OUTRO_USUARIO = "nome_amigo";
    public static final String INTENT_SETTINGS_STATUS = "settings_status";

    // NOTIFICACOES
    public static final String NOTIF_ORIGEM = "origem";
    public static final String NOTIF_PUSH_ID = "notPushId";

    public static final String NOTIF_TIPO = "tipo";
    public static final String NOTIF_SOLICITADA = "solicitada";



}
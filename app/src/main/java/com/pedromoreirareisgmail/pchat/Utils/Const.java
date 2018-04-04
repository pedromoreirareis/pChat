package com.pedromoreirareisgmail.pchat.Utils;

public class Const {

    // BARRA ENDEREÇO
    public static final String ABRIR_PERFIL = "Abrir Perfil";
    public static final String ENVIAR_MENSAGEM = "Enviar Mensagem";

    // PASTAS DATABASE
    public static final String PASTA_USUARIOS = "Usuarios";
    public static final String PASTA_SOLIC = "Solicitacoes";
    public static final String PASTA_AMIGOS = "Amigos";
    public static final String PASTA_NOTIF = "Notificacoes"; // TODO: Alterar nome: notif_sol_amizade
    public static final String PASTA_CHAT = "Chat"; // Mudar para Chats
    public static final String PASTA_MENSAGENS = "Mensagens";

    // CAMPOS DATABASE
    public static final String DB_NOME = "nome";
    public static final String DB_STATUS = "status";
    public static final String DB_IMAGEM = "imagem";
    public static final String DB_THUMB = "thumbnail";
    public static final String DB_D_TOKEN = "device_token";
    public static final String DB_ON_LINE = "online";
    public static final String DB_ULT_VEZ = "ultima";
    public static final String DATA = "data";

    // PASTAS STORAGE
    public static final String S_PASTA_IMAGENS = "Imagens_Perfil"; //TODO: Alterar nome para: img_usuario
    public static final String S_PASTA_THUMB = "Imagens_Thumbnail"; //TODO: Alterar nome para: img_usuario_thumbnail
    public static final String S_PASTA_IMAGENS_MSG = "Imagens_Mensagens"; //TODO: Alterar nome para: img_chat
    public static final String S_PASTA_IMAGENS_MSG_THUMB = "Imagens_Mensagens_thumb"; //TODO: Alterar nome para: img_chat_thumbnail

    // REGISTRO PADRAO DATABASE
    public static final String DB_REG_IMAGEM = "imagem_padrao";
    public static final String DB_REG_THUMB = "thumbnail_padrao";
    public static final String DB_REG_STATUS = "Viva o melhor da vida!";

    // TIPO SOLICITACOES DATABASE
    public static final String SOL_TIPO = "tipo_solicitacao";
    public static final String SOL_TIPO_ENVIADA = "enviada";
    public static final String SOL_TIPO_RECEBIDA = "recebida";

    // ESTADOS SOLICITAÇÃO AMIGOS
    public static final String ESTADO_NAO_AMIGOS = "nao_amigos";
    public static final String ESTADO_SOL_ENVIADA = "solicitacao_enviada";
    public static final String ESTADO_SOL_RECEBIDA = "solicitacao_recebida";
    public static final String ESTADO_AMIGOS = "amigos";

    // INTENTS
    public static final String INTENT_STATUS = "status_usuario";
    public static final String INTENT_NOME = "nome_usuario";
    public static final String INTENT_ID_OUTRO_USUARIO = "id_outro_usuario";
    public static final String INTENT_ID_NOTIFICACAO = "id_outro_usuario";
    public static final String INTENT_ID_AMIGO = "id_amigo";
    public static final String INTENT_NOME_AMIGO = "nome_amigo";

    // NOTIFICACAO
    public static final String NOTIF_ORIGEM = "origem";
    public static final String NOTIF_TIPO = "tipo";
    public static final String NOTIF_SOLICITADA = "solicitada";
    public static final String NOTIF_RECEBIDA = "recebida";

    // CHAT PASTA
    public static final String CHAT_LIDA = "lida";
    public static final String CHAT_TIME = "time";

    // CHAT MENSAGEM
    public static final String CHAT_MSG_MENSAGEM = "mensagem";
    public static final String CHAT_MSG_LIDA = "lida";
    public static final String CHAT_MSG_TIPO = "tipo";
    public static final String CHAT_MSG_TIME = "time";
    public static final String CHAT_MSG_ORIGEM = "origem";
    public static final String CHAT_MSG_THUMB = "thumb";

    public static final String CHAT_MSG_TIPO_TEXT = "text";
    public static final String CHAT_MSG_TIPO_IMAGE = "image";

    public static final String CHAT_MSG_THUMB_PADRAO = "thumb_padrao";

}
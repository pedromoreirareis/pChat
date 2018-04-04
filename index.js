'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


exports.enviarNotificacoes = functions.database.ref('/Notificacoes/{user_id}/{notification_id}').onWrite(event => {

	const user_id = event.params.user_id;
  	const notification_id = event.params.notification_id;

  	console.log('Notificação para enviar para : ', user_id);

  	if(!event.data.val()){

  		return console.log('Notificação foi excluida do banco de dados : ', notification_id);

  	}
  
  	
  	const fromUser = admin.database().ref(`/Notificacoes/${user_id}/${notification_id}`).once('value');

  	return fromUser.then(fromUserResult => {

  			const from_user_id = fromUserResult.val().origem;

  			console.log('Você recebeu uma notificação de : ', from_user_id);

  			const userQuery = admin.database().ref(`/Usuarios/${from_user_id}/nome`).once('value');
  			const deviceToken = admin.database().ref(`/Usuarios/${user_id}/device_token`).once('value');


  			return Promise.all([userQuery, deviceToken]).then( result => {

  				const userName = result[0].val();
  				const token_id = result[1].val();	

  						const payload = {
  							notification: {
  								title : "Solicitação de Amizade",
  								body : `${userName} enviou uma solicitação de amizade.`,
  								icon : "default",
  								click_action : "com.pedromoreirareisgmail.pchat_TARGET_NOTIFICATION"
  							},
  							data : {
  								from_user_id : from_user_id
  							}
  						};

  						return admin.messaging().sendToDevice(token_id, payload).then(response => {

  							console.log('Este é um recurso de notificação');
         					return true

  						});

  			});	

  						
  	});
  	
});

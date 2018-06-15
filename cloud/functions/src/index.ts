import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import topics from './topics';

admin.initializeApp(functions.config().firebase);

exports.registerNewUserOnDatabase = functions.auth.user().onCreate(event => {
    const promises = [];

    let update = false;
    if (!event.displayName) {
        event.displayName = event.phoneNumber;
        update = true
    }
    if (!event.photoURL) {
        event.photoURL = 'https://firebasestorage.googleapis.com/v0/b/fir-demo-16877.appspot.com/o/avatar.jpg?alt=media&token=bc005b42-e18f-4a02-97cb-8d2004954a25';
        update = true
    }
    if (update) {
        promises.push(admin.auth().updateUser(event.uid, {
            displayName: event.displayName,
            photoURL: event.photoURL
        }));
    }

    const id = event.uid;
    const name = event.displayName;
    const email = event.email;
    const photoUrl = event.photoURL;

    console.log('registerNewUserOnDatabase: id=' + id + ', name=' + name + ', email=' + email + ', photoUrl=' + photoUrl);

    promises.push(admin.database().ref('users').child(id).set({
        name: name,
        email: email,
        photoUrl: photoUrl
    }));

    promises.push(admin.messaging().sendToTopic(topics.new_users, {
        notification: {
            title: name,
            text: 'Has just registered'
        }
    }));

    return Promise.all(promises)
});

exports.sendNewMessageNotification = functions.database.ref('chat/{messageId}').onCreate(chatSnap => {
    const message = chatSnap.child('message').val();

    return message ? admin.database()
        .ref('users').child(chatSnap.child('userId').val())
        .once('value', userSnap => {
            const userName = userSnap.child('name').val();

            console.log('sendNewMessageNotification: userName=' + userName + '; message=' + message);

            return admin.messaging().sendToTopic(topics.new_messages, {
                notification: {
                    title: userName,
                    text: message
                }
            })
        }) : null;
});

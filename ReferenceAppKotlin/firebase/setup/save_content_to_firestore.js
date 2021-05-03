const admin = require('./../server/node_modules/firebase-admin');

/**
 * Put service-account-key.json file into firebase/ root folder.
 * It should hold firebase configuration info such as
 * type, project_id, project_key_id etc.
 */
const serviceAccount = require("./../service-account-key.json");

// Replace the value with databaseURL given in firebase admin SDK configuration,
// for example, https://my-app.firebaseio.com.
const dbURL = "REPLACE_ME"

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: dbURL
});

const collectionKey = "content"; //name of the collection

const db = admin.firestore();

content_feed = require('./../../app/src/main/res/raw/api.json')

content_feed.content.forEach(function(obj) {
    var objIdHash = createIdHash(obj.id);

    db.collection(collectionKey).doc(objIdHash).set({
        content_id: obj.id,
        title: obj.name,
        description: obj.description,
        uri: obj.uri,
        videoUri: obj.videoUri,
        thumbnailUri: obj.thumbnailUri,
        backgroundUri: obj.backgroundUri,
        category: obj.category,
        videoType: obj.videoType,
        duration: obj.duration,
        seriesUri: obj.seriesUri,
        seasonUri: obj.seasonUri,
        episodeNumber: obj.episodeNumber,
        seasonNumber: obj.seasonNumber
    }).then(function(docRef) {
        console.log("Document written with ID: ", docRef.id);
    })
    .catch(function(error) {
        console.error("Error adding document: ", error.name, ": ", error.message);
    });
});

/**
 * Replace all slash '/' with underscore '_' characters in content ID since
 * slash '/' is not an allowed character for Firestore collection and document
 * names.
 */
function createIdHash(content_id){
    return content_id.replace(new RegExp('/','g'),"_");
}
## Reference App - Firebase

If FIREBASE_ENABLED is set to false in the app's [build.gradle](app/build.gradle) file (not the
project's file) the app displays a warning when it starts up saying that Firebase is not configured.
In this case account-specific features are disabled. If you change the flag to `true` the message no
longer appears, but you will need to still update the project to use Firebase. Modify
[RemoteAuthClient](app/src/main/java/com/android/tv/reference/auth/RemoteAuthClient.kt) to reflect
your configuration once you set up the firebase instance.

This folder contains a simple Node.js server in the server directory(server) & a web app that implements
OAuth endpoints in the web directory(web) to include Account Linking and run it using a sample web app.
Included is a script(setup/save_content_to_firestore.js) to upload the content catalog to Firebase's Firestore.
The content catalog is used by the app to know what video content to display and play.

This sample application uses the following Firebase features -

1. Auth - It enables authentication for users using email and Google Sign in methods. It is also required
for the Account Linking flow to link a user's Google account with their in-app account.

1. Cloud Functions - The cloud functions are used to serve as the endpoints required for performing the
Account Linking, such as the endpoints for authentication callback, user info and token exchange. These
endpoints act as an OAuth server, refer to this link(https://developers.google.com/identity/account-linking/oauth-linking?oauth=code#implement_your_oauth_server)
for more details. The cloud functions can also be used for defining endpoints for other features in the cloud.

1. Firestore - It is used as the cloud database system for the application. It stores information about the
content catalog in the cloud, and stores data regarding authentication and authorisation of users such as
auth tokens and refresh tokens.

### Firebase Project Setup

1. Go to **Firebase Console** and create a Firebase project.

    * [https://console.firebase.google.com/](https://console.firebase.google.com/)

1. In the project, go to Build > Cloud Firestore > Create database > Start in **production mode**

1. In the project, go to Build > Functions> Get started. Enable Functions.

1. In the project, go to Build > Authentication > Get started > Sign-in method. Enable **Email/Password** and **Google**
 as Sign-in providers.

1. Add your app's SHA1 fingerprint for Google Sign-In

    * [https://firebase.google.com/docs/auth/android/firebaseui#before_you_begin](https://firebase.google.com/docs/auth/android/firebaseui#before_you_begin)

    * Specify your app's SHA-1 fingerprint from the [Settings page](https://console.firebase.google.com/project/_/settings/general/)
    of the Firebase console

    * See [Authenticating Your Client](https://developers.google.com/android/guides/client-auth)
    for details on how to get your app's SHA-1 fingerprint

    * If you do not know your SHA1 fingerprint yet, please skip it for now and return after you have
     signed your APK later

1. Follow the instructions at [firebase setup](https://firebase.google.com/docs/android/setup#manually_add_firebase) to create a project
and obtain a google-services.json file. Download the file and move it to your app module directory at location -

    * `{project_folder}/app/google-services.json`

1. Then add the Firebase SDKs and initialize another for the Web App within the firebase directory following the
instructions at [firebase web app setup](https://firebase.google.com/docs/web/setup#add-sdks-initialize). Obtain a
service-account-key.json file and place that file at location -

    * `{project_folder}/firebase/service-account-key.json.json`

1. Add Firebase to the server.

    * [https://firebase.google.com/docs/admin/setup](https://firebase.google.com/docs/admin/setup)

### Content Catalog in Firestore Setup

To upload the content catalog for this Reference App to Firebase Firestore to
try out server-side features that require the use of the content catalog, initialise
your Firebase project in the *firebase* directory and update the dbUrl value from
the current value of "REPLACE_ME" in the script to the URL corresponding to your
Firebase instance. To find the dbUrl, go to your firebase project in the
Firebase console[https://console.firebase.google.com/](https://console.firebase.google.com/)
and navigate to Project Settings > Service Accounts > Firebase Admin SDK
and the databaseUrl would be present as a field in the Admin SDK configuration snippet.

Then run the following command from within the *firebase* directory -
```
node setup/save_content_to_firestore.js
```
The script reads the list of content that is used in the app and creates nodes in
Firestore for each such content item.

### Server

You would need a [Firebase project](https://firebase.google.com/) to deploy the server implementation on
[Cloud Functions for Firebase](https://firebase.google.com/docs/functions/) and enable Account Linking. While
the cloud functions will be deployed on Firebase, they will also need to be registered as endpoints with Google
for Account Linking to run. See the [Google Account Linking][account-linking] documentation for details.
Note: The Firebase project has a corresponding Google Cloud Console project that will be configured for
Real-time Developer Notifications.

To deploy the backend server endpoints, perform the following steps -
1. Make sure you have installed Node.js, npm, and Firebase CLI

    * [https://firebase.google.com/docs/cli/](https://firebase.google.com/docs/cli/)

1. Run `npm install` to install dependencies

1. Setup your firebase project to be used

    ```
       firebase use --add {your_firebase_project_id}
    ```

1. Install Node packages

    ```
        cd {project_folder}/firebase/server
        npm install
    ```

1. Run `firebase deploy` to deploy your backend to Cloud Functions for Firebase

The src directory(server/src) contains the code for the various endpoints required to
perform Account Linking in the authentication.ts(server/src/controller/functions/authentication.ts)
file. These endpoints are for authentication callback, token exchange and revocation and user
info. See the [Google Account Linking][account-linking] documentation for more details on these
endpoints. The corresponding tests for these methods can be found in the authentication.test.ts(server/test/authentication.test.ts)
file in the test directory(server/test). To run these tests, update the project ID, database URL and
storage bucket replacing them from the current given value of "REPLACE_ME" to the values corresponding
to your Firebase project. These can be found in the google-services.json file for your project,
that you can download by navigating to your
Firebase console [https://console.firebase.google.com/](https://console.firebase.google.com/) and
going to Project Settings > General > SDK setup and configuration.

Some of the shared resources used in functions are defined in the shared.ts(server/src/controller/shared.ts)
file. The index.ts(server/src/index.ts) file exports these endpoints to deploy them on Firebase Cloud
Functions. The other files in the server directory contain configuration information for firebase, node packages
and other server related configuration details.

### Web App

The sample web app to run the Account Linking flow is present in the web directory(web). The code in the
index.html(web/index.html) and the scripts.js(web/scripts.js) performs authentication for a user using
default Firebase Auth methods. The oauth(web/oauth) directory contains the code for performing user authentication
using the firebase cloud functions defined in the server(server) module to trigger the OAuth Account Linking
flow. The redirect Url needs to be replaced from the current given value of "REPLACE_ME" in the
scripts.js(web/oauth/scripts.js) file in the oauth directory with the redirect Url being used by
your firebase project and authentication endpoints.

To deploy the web app, perform the following steps -
1. Make sure you have installed Node.js, npm, and Firebase CLI

    * [https://firebase.google.com/docs/cli/](https://firebase.google.com/docs/cli/)

1. Run `npm install` to install dependencies

1. Setup your firebase project to be used

    ```
       firebase use --add {your_firebase_project_id}
    ```

1. Install Node packages

    ```
        cd {project_folder}/firebase/web
        npm install
    ```

1. Run `firebase deploy` to deploy your web app to Firebase

Once the deployment completes, the Authentication web app will be hosted at the URL -

    ```
        https://{your_firebase_project_id}.web.app
    ```

And the web app page for the Account Linking flow will be hosted at the URL -

    ```
        https://{your_firebase_project_id}.web.app/oauth
    ```
/**
 * Copyright 2019 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

// Initialize the FirebaseUI Widget using Firebase.
const ui = new firebaseui.auth.AuthUI(firebase.auth());

console.log(new URL(window.location.href));

// Configure FirebaseUI Auth
const uiConfig = {
  callbacks: {
    signInSuccessWithAuthResult: function(authResult) {
      console.log(authResult);
      // Example redirectUrl = 'https://my-app.cloudfunctions.net/auth_callback'
      const redirectUrl = 'REPLACE_ME';
      const params = (new URL(window.location.href)).searchParams;
      window.location.href = redirectUrl + "?client_id=" + params.get("client_id") +
          "&state=" + params.get("state") + "&scope=" + params.get("scope") +
          "&uid=" + authResult.user.uid + "&redirect_uri=" + params.get("redirect_uri");
      return false;
    },
    signInFailure: function(error) {
      console.error("Authentication failed");
    }
  },
  // signInSuccessUrl: 'https://us-central1-atv-reference-app.cloudfunctions.net/auth_callback',
  signInOptions: [
    firebase.auth.GoogleAuthProvider.PROVIDER_ID,
    firebase.auth.EmailAuthProvider.PROVIDER_ID,
  ],
  tosUrl: "",
  privacyPolicyUrl: ""
};

// Show FirebaseUI login UI to user.
ui.start('#firebaseui-auth-container', uiConfig);

// Listen to authentication state changes and update UI accordingly
firebase.auth().onAuthStateChanged(user => {
  if (user) {
    console.log("Authenticated");
  } else {
    console.log("Unauthenticated");
  }
});
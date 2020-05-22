/**
 * Copyright 2018 Google LLC. All Rights Reserved.
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

const loginUi = document.querySelector('.login-ui');
const contentUi = document.querySelectorAll('.content-ui');

const hideElement = element => {
  element.classList.add('hidden');
}

const showElement = element => {
  element.classList.remove('hidden');
}

// Show login screen for user who hasn't logged in
const showLoginUI = () => {
  showElement(loginUi);
  
  // Configure FirebaseUI Auth
  const uiConfig = {
    signInSuccessUrl: '/',
    signInOptions: [
      firebase.auth.GoogleAuthProvider.PROVIDER_ID,
      firebase.auth.EmailAuthProvider.PROVIDER_ID,
    ],
  };

  // Show FirebaseUI login UI to user.
  ui.start('#firebaseui-auth-container', uiConfig);
}

// Show content screen for user who has logged in
const showContentUI = () => {
  // Hide Login UI and show Content UI
  contentUi.forEach(showElement);
  hideElement(loginUi);
}

// Sign the user out
const signOut = () => {
  firebase.auth().signOut();
}

// Listen to authentication state changes and update UI accordingly
firebase.auth().onAuthStateChanged(user => {
  if (user) {
    showContentUI();
  } else {
    showLoginUI();
  }
});

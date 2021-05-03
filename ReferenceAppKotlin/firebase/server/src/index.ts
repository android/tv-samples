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

import * as firebase from 'firebase-admin';
firebase.initializeApp();

import { auth_endpoint, user_info, token_exchange, auth_callback, token_revocation } from './controller/functions/authentication';

/*
 * This file is the main entrace for Cloud Functions for Firebase.
 * It exposes functions that will be deployed to the backend
 */

// This is a trick to improve performance when there are many functions, 
// by only exporting the function that is needed by the particular instance.
if (!process.env.FUNCTION_NAME || process.env.FUNCTION_NAME === 'auth_endpoint') {
  exports.auth_endpoint = auth_endpoint;
}

if (!process.env.FUNCTION_NAME || process.env.FUNCTION_NAME === 'user_info') {
  exports.user_info = user_info;
}

if (!process.env.FUNCTION_NAME || process.env.FUNCTION_NAME === 'token_exchange') {
  exports.token_exchange = token_exchange;
}

if (!process.env.FUNCTION_NAME || process.env.FUNCTION_NAME === 'auth_callback') {
  exports.auth_callback = auth_callback;
}

if (!process.env.FUNCTION_NAME || process.env.FUNCTION_NAME === 'token_revocation') {
  exports.token_revocation = token_revocation;
}
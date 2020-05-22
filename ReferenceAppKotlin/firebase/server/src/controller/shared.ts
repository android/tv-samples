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

import * as firebase from 'firebase-admin'
import * as functions from 'firebase-functions';

import * as cryptoRandomString from 'crypto-random-string';

/*
 * This file defines shared resources that are used in functions
 */

// Shared config
export const PACKAGE_NAME = functions.config().app.package_name;

// Generate a cryptographically secure token string
export async function generateToken(desiredLength: number, type: string) {
  let candidate: string;
  let isCandidateFound: boolean = false;

  // Loop until a unique candidate is found
  while(!isCandidateFound) {
    // Generate a secure string
    candidate = cryptoRandomString({length: desiredLength});
    if (type === 'code') {
      // Check the Authorization Codes database for duplicates
      const codesDbRef = firebase.app().firestore().collection('authcodes');
      // Check for duplicate authorization codes
      const snapshot = await codesDbRef
          .where('auth_code', '==', candidate)
          .get();
      if (snapshot.empty) {
        isCandidateFound = true;
      }
    } else if (type === 'token') {
      // Check the Tokens database for duplicates
      const tokensDbRef = firebase.app().firestore().collection('tokens');
      // Check for duplicate refresh tokens
      const refreshSnapshot = await tokensDbRef
          .where('refresh_token', '==', candidate)
          .get();
      if (refreshSnapshot.empty) {
        // Check for duplicate access tokens
        const accessSnapshot = await tokensDbRef
            .where('access_token', '==', candidate)
            .get();
        if (accessSnapshot.empty) {
          isCandidateFound = true;
        }
      }
    } else {
      // Throw an error if the requested string type is neither a token nor an
      //     auth code
      throw new functions.https.HttpsError('invalid-argument',
          'Invalid token type requested');
    }
  }
  return candidate;
}
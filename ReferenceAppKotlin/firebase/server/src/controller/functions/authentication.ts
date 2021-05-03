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

import * as firebase from 'firebase-admin';
import * as functions from 'firebase-functions';
import { OAuth2Client } from 'google-auth-library';
import { generateToken } from '../shared';
// Put config.json file into server/ root folder.
// It should hold firebase configuration info such as 
//     - the project id
//     - GIS OAuth client id
import * as CONFIG from '../../../config.json';

const AUTH_CODE_EXPIRY = 600; // seconds until expiry, 10 minutes
const ACCESS_TOKEN_EXPIRY = 3600; // seconds until expiry, 1 hour
const PROJECT_ID = CONFIG.project_id;
const CLIENT_ID = CONFIG.client_id;
// Audience ID for OAuth 2.0 Playground
const PLAYGROUND_ID = '407408718192.apps.googleusercontent.com';

/**
 * This endpoint starts the authorization flow.
 * @param client_id The ID given to the client sending the request
 * @param redirect_uri The URI supplied by the client where the authorization
 *     code will be sent
 * @param state A bookkeeping value that should be returned unchanged to the
 *     redirect URI
 * @param scope Optional, a space-delimited set of strings
 * @param response_type The string 'code' - this server only supports the
 *     authorization code flow
 * @return Redirects the user to a sign-in page
 */
export const auth_endpoint = functions.https.onRequest(async (req, res) => {
  // OAuth error response details: https://www.oauth.com/oauth2-servers/authorization/the-authorization-response/

  console.log(`method: ${req.method}
client_id: ${req.query.client_id}
redirect_uri: ${req.query.redirect_uri}
state: ${req.query.state}
scope: ${req.query.scope}
response_type: ${req.query.response_type}`);

  // Verify request method
  if (req.method !== 'GET') {
    // Only accept GET requests
    res.status(405).send({
      error: 'invalid_request',
      error_description: 'Method not allowed'
    });
    return;
  }

  // This is ugly fix with casting client id to string
  // TODO: https://github.com/firebase/firebase-functions/issues/587
  const client_id = <string>req.query.client_id;
  
  const redirect_uri = req.query.redirect_uri;
  const state = req.query.state;
  const scope = req.query.scope;
  const response_type = req.query.response_type;

  // Verify client_id
  const db = firebase.app().firestore();
  const snapshot = await db.collection('clients').doc(client_id).get();
  if (!snapshot.exists) {
    // Error if client ID is not found
    res.redirect(`${redirect_uri}?error=unauthorized_client&state=${state}`);
    return;
  }

  // Verify redirect_uri
  if (snapshot.get('redirect_uri') !== redirect_uri) {
    // Error if redirect_uri doesn't match
    res.redirect(`${redirect_uri}?error=invalid_request&error_description=
Incorrect+or+malformed+redirect+URI&state=${state}`);
    return;
  }

  // Verify response_type === 'code'
  if (response_type !== 'code') {
    // Error if response_type is not 'code'
    res.redirect(`${redirect_uri}?error=unsupported_response_type
&state=${state}`);
    return;
  }

  res.redirect(`https://${PROJECT_ID}.web.app/oauth?redirect_uri=
${redirect_uri}&client_id=${client_id}&state=${state}&scope=${scope}`);
});

/**
 * This endpoint completes the authorization flow, after a user has signed in.
 * @param client_id The ID given to the client sending the request, passed
 *     through from auth_endpoint
 * @param redirect_uri The URI supplied by the client where the authorization
 *     code will be sent, passed through from auth_endpoint
 * @param state A bookkeeping value that should be returned unchanged to the
 *     redirect URI, passed through from auth_endpoint
 * @param scope Optional, a space-delimited set of strings, passed through
 *     from auth_endpoint
 * @param uid The unique ID for the signed-in user
 * @return Redirects to the redirect_uri with the authorization code
 */
export const auth_callback = functions.https.onRequest(async (req, res) => {
  console.log(`method: ${req.method}
client_id: ${req.query.client_id}
redirect_uri: ${req.query.redirect_uri}
state: ${req.query.state}
scope: ${req.query.scope}
uid: ${req.query.uid}`);

  const client_id = req.query.client_id;
  const redirect_uri = req.query.redirect_uri;
  const state = req.query.state;
  const scope = req.query.scope;
  const uid = req.query.uid;

  // Generate new authentication code
  const newCode = await generateToken(32, 'code');
  // Store newly generated authentication code in database
  try {
    const db = firebase.app().firestore();
    await db.collection('authcodes').add({
      client_id: client_id,
      user_id: uid,
      scope: scope,
      auth_code: newCode,
      expires_in: AUTH_CODE_EXPIRY,
      created_on: Date.now()
    });

    // Return generated authentication code if database write is successful
    res.redirect(`${redirect_uri}?code=${newCode}&state=${state}`);
  } catch (err) {
    // Return error if database write is unsuccessful
    console.error(err);
    res.redirect(`${redirect_uri}?error=server_error&state=${state}`);
  }
});

/**
 * This endpoint returns information about a user given an access token
 * @param access_token Located in the Authorization header, preceded by 'Bearer'
 * @return UserInfo object with fields 'sub' and 'email'
 */
export const user_info = functions.https.onRequest(async (req, res) => {
  console.log(`method: ${req.method}
auth header: ${req.headers.authorization}`);

  // Verify request method
  if (req.method !== 'GET' && req.method !== 'POST') {
    // Only accept GET or POST requests
    res.status(405).send('Method not allowed');
    return;
  }

  const access_token = req.headers.authorization.replace('Bearer ', '');

  // Verify access token
  const tokensDbRef = firebase.app().firestore().collection('tokens');
  const snapshot = await tokensDbRef.orderBy('created_on', 'desc')
      .where('access_token', '==', access_token).get();
  if (snapshot.empty) {
    // Error if access token is not found
    res.set('WWW-Authenticate', 'error=\'invalid_token\',' +
        'error_description=\'Invalid access token\'').status(401).send();
    return;
  }

  // Pick the newest entry in the database, probably redundant (there should
  // only be one entry anyway)
  const token_info = snapshot.docs[0];
  const created_on = token_info.get('created_on');
  const expires_in = token_info.get('expires_in');
  // Check if access token is expired
  if (created_on + expires_in * 1000 > Date.now()) {
    const user_id = token_info.get('user_id');
    const user = await firebase.auth().getUser(user_id);

    // Construct response from user data
    const data = {
      sub: user.email,
      email: user.email
    };
    res.status(200).send(data);
  } else {
    // Error if access token is expired
    res.set('WWW-Authenticate', 'error=\'invalid_token\',' +
        'error_description=\'Expired access token\'').status(401).send();
  }
});

/**
 * This endpoint handles a few different use-cases:
 * 1. Exchanging an Authorization Code for Access + Refresh tokens
 * 2. Exchanging a Refresh token for a new Access token
 * 3. Streamlined Account Linking Check, Link, and Create requests
 * @param client_id The ID given to the client sending the request
 * @param client_secret The secret given to the client sending the request
 * @param grant_type The type of exchange that is being requested
 * @param [code] For use-case 1, the Authorization Code to exchange
 * @param [refresh_token] For use-case 2, the Refresh token to exchange
 * @param [intent] For use-case 3, the type of request
 * @param [assertion] For use-case 3, the Google ID Token
 */
export const token_exchange = functions.https.onRequest(async (req, res) => {
  // OAuth error response details: https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/

  // NOTE: This log statement is for demonstrative/testing purposes only. It is
  //     recommended that you do not display/store the client secret in
  //     plaintext anywhere.
  console.log(`method: ${req.method}
client_id: ${req.body.client_id}
client_secret: ${req.body.client_secret}
grant_type: ${req.body.grant_type}
code: ${req.body.code}
intent: ${req.body.intent}
assertion: ${req.body.assertion}`);

  // OAuth error response details:
  //     https://www.oauth.com/oauth2-servers/access-tokens/access-token-response/

  if (req.method !== 'POST') {
    // Only accept POST requests
    res.status(405).send('Method not allowed');
    return;
  }

  const client_id = req.body.client_id;
  const client_secret = req.body.client_secret;

  // Verify client
  if(!await verifyClient(client_id, client_secret)) {
    res.status(401).send({
      error: 'invalid_client',
      error_description: 'Invalid client ID or secret'
    });
    return;
  }

  // Split into different process based on grant type
  const grant_type = req.body.grant_type;
  if (grant_type === 'authorization_code') {
    console.log("Authorization code exchange");
    await handleAuthCodeExchange(req, res);
  } else if (grant_type === 'refresh_token') {
    console.log("Refresh token exchange");
    await handleRefreshTokenExchange(req, res);
  } else if (grant_type === 'urn:ietf:params:oauth:grant-type:jwt-bearer') {
    const intent = req.body.intent;
    const assertion = req.body.assertion;
    let uid = '';
    let email = '';
    let hd = '';
    let verified = false;
    try {
      // Using the Google ID Token: https://developers.google.com/identity/sign-in/web/backend-auth#verify-the-integrity-of-the-id-token
      const client = new OAuth2Client(PROJECT_ID);
      console.log(`ID Token: ${assertion}`);
      const ticket = await client.verifyIdToken({
        idToken: assertion,
        audience: [
            PROJECT_ID, CLIENT_ID, PLAYGROUND_ID
        ],
      });
      const payload = ticket.getPayload();
      uid = payload['sub'];
      email = payload['email'];
      hd = payload['hd'];
      verified = payload['email_verified'];

      const iss = payload['iss'];
      if (iss !== 'https://accounts.google.com') {
        console.error('Invalid iss');
        res.status(400).send({
          error: 'invalid_grant',
          error_description: 'Error with Google ID Token'
        });
        return;
      }
      const aud = payload['aud'];
      if (aud !== CLIENT_ID) {
        console.error('Invalid aud');
        res.status(400).send({
          error: 'invalid_grant',
          error_description: 'Error with Google ID Token'
        });
        return;
      }

    } catch (err) {
      console.error(err);
      res.status(400).send({
        error: 'invalid_grant',
        error_description: 'Error with Google ID Token'
      });
      return;
    }
    // Split into different Streamlined Account Linking requests
    if (intent === 'check') {
      console.log("Streamlined Account Linking - Check request");
      await handleStreamlinedCheck(uid, email, hd, verified, res);
    } else if (intent === 'get') {
      console.log("Streamlined Account Linking - Link request");
      try {
        const user = await firebase.auth().getUserByEmail(email);
        // handleStreamlinedLinkOrCreate catches all errors internally
        await handleStreamlinedLinkOrCreate(user.uid, client_id, res);
      } catch {
        // We should only reach here if the user could not be found
        res.status(401).send({
          error: 'user_not_found',
          error_description: 'User not found'
        });
      }
    } else if (intent === 'create') {
      console.log("Streamlined Account Linking - Create request");
      try {
        const user = await firebase.auth().createUser({email: email});
        // handleStreamlinedLinkOrCreate catches all errors internally
        await handleStreamlinedLinkOrCreate(user.uid, client_id, res);
      } catch {
        // We should only reach here if the user could not be created
        res.status(401).send({
          error: 'linking_error',
          error_description: 'Could not create user'
        });
      }
    } else {
      res.status(400).send({
        error: 'invalid_grant',
        error_description: 'Intent not supported'
      });
    }
  } else {
    res.status(400).send({
      error: 'unsupported_grant_type',
      error_description: 'Grant type not supported'
    });
  }
});

/**
 * This helper function handles use-case 1 for the Token Exchange endpoint.
 * A successful response will include the new access token (access_token) and
 * refresh token (refresh_token) issued to the client for the authenticated
 * user, the validity lifetime of the access token in seconds (expires_in), and
 * the string 'Bearer' (token_type)
 * @param client_id The ID given to the client sending the request, passed
 *     through from token_exchange
 * @param code The Authorization Code to exchange, passed through from
 *     token_exchange
 */
async function handleAuthCodeExchange(
    req: functions.Request, res: functions.Response) {
  const client_id = req.body.client_id;
  const code = req.body.code;
  const db = firebase.app().firestore();

  // Verify authorization code
  const codeSnapshot = await db.collection('authcodes')
      .orderBy('created_on', 'desc')
      .where('client_id', '==', client_id)
      .where('auth_code', '==', code)
      .get();
  if (codeSnapshot.empty) {
    res.status(400).send({
      error: 'invalid_grant',
      error_description: 'Invalid authorization code'
    });
    return;
  }

  // Pick the newest entry in the database, probably redundant (there should
  //     only be one entry anyway)
  const code_info = codeSnapshot.docs[0];
  const user_id = code_info.get('user_id');
  const created_on = code_info.get('created_on');
  const expires_in = code_info.get('expires_in');
  // Check if token is expired
  if (created_on + expires_in * 1000 < Date.now()) {
    res.status(400).send({
      error: 'invalid_grant',
      error_description: 'Authorization code expired'
    });
    return;
  }

  // Generate tokens
  const access_token = await generateToken(32, 'token');
  const refresh_token = await generateToken(64, 'token');
  try {
    // Store in database
    await db.collection('tokens').add({
      client_id: client_id,
      user_id: user_id,
      scope: [],
      refresh_token: refresh_token,
      access_token: access_token,
      expires_in: ACCESS_TOKEN_EXPIRY,
      created_on: Date.now()
    });

    // Return generated tokens if database write was successful
    res.status(200).send({
      access_token: access_token,
      expires_in: ACCESS_TOKEN_EXPIRY,
      refresh_token: refresh_token,
      token_type: 'Bearer'
    });
  } catch (err) {
    // Return error if database write was unsuccessful
    console.error(err);
    res.status(500).send(err);
  }
}

/**
 * This helper function handles use-case 2 for the Token Exchange endpoint.
 * A successful response will include the new access token (access_token) and
 * old refresh token (refresh_token) issued to the client for the authenticated
 * user, the validity lifetime of the access token in seconds (expires_in), and
 * the string 'Bearer' (token_type)
 * @param client_id The ID given to the client sending the request, passed
 *     through from token_exchange
 * @param refresh_token The Refresh token to exchange, passed through from
 *     token_exchange
 */
async function handleRefreshTokenExchange(
    req: functions.Request, res: functions.Response) {
  const client_id = req.body.client_id;
  const refresh_token = req.body.refresh_token;
  const db = firebase.app().firestore();

  // Verify refresh token
  const tokenSnapshot = await db.collection('tokens')
      .where('client_id', '==', client_id)
      .where('refresh_token', '==', refresh_token)
      .get();
  if (tokenSnapshot.empty) {
    res.status(400).send({
      error: 'invalid_grant',
      error_description: 'Invalid refresh token'
    });
    return;
  }

  // Pick the newest entry in the database, probably redundant (should only be
  //     one entry anyway)
  const token_info = tokenSnapshot.docs[0];
  const user_id = token_info.get('user_id');
  const scope = token_info.get('scope');
  // Generate token
  const access_token = await generateToken(32, 'token');
  try {
    // Store in database
    await db.collection('tokens').add({
      client_id: client_id,
      user_id: user_id,
      scope: scope,
      refresh_token: refresh_token,
      access_token: access_token,
      expires_in: ACCESS_TOKEN_EXPIRY,
      created_on: Date.now()
    });

    // Return generated token if database write was successful
    res.status(200).send({
      access_token: access_token,
      expires_in: ACCESS_TOKEN_EXPIRY,
      refresh_token: refresh_token,
      token_type: 'Bearer'
    });
  } catch (err) {
    // Return error if database write was unsuccessful
    console.error(err);
    res.status(500).send(err);
  }
}

/**
 * This helper function handles the 'Check' request for use-case 3 of the Token
 * Exchange endpoint.
 * @param uid The requested account's UID, extracted from the Google ID Token
 *     in the Token Exchange endpoint
 * @param email The requested account's email address, extracted from the Google
 *     ID Token in the Token Exchange endpoint
 * @param hd The requested account's host domain, extracted from the Google ID
 *     Token in the Token Exchange endpoint
 * @param verified The requested account's email verification status, extracted
 *     from the Google ID Token in the Token Exchange endpoint
 * @return account_found A boolean for whether the requested account exists
 */
async function handleStreamlinedCheck(
    uid: string,
    email: string,
    hd: string,
    verified: boolean,
    res: functions.Response) {
  try {
    // Attempt to find user via UID
    await firebase.auth().getUser(uid);
    // Return true if found
    res.status(200).send({account_found: true});
  } catch {
    try {
      // If not found, attempt to find user via email
      await firebase.auth().getUserByEmail(email);
      // Verify that Google is authoritative
      if (email.endsWith('@gmail.com')) {
        // If email has @gmail.com suffix, always authoritative
        res.status(200).send({account_found: true});
        return;
      }
      if (hd) {
        // If host domain is populated, always authoritative
        res.status(200).send({account_found: true});
        return;
      }
      if (verified) {
        // If email_verified is true, not authoritative but still trust
        console.warn(`Google is not authoritative for ${email}`);
        res.status(200).send({account_found: true});
        return;
      }
      // Google is not authoritative and email is not verified
      console.warn(`${email} is not verified`);
      res.status(404).send({account_found: false});
      return;
    } catch {
      // Return false if error (not found)
      res.status(404).send({account_found: false});
      return;
    }
  }
}

/**
 * This helper function handles the 'Link' and 'Create' requests for use-case 3
 * of the Token Exchange endpoint.
 * A successful response will include the new access token (access_token) and
 * old refresh token (refresh_token) issued to the client for the authenticated
 * user, the validity lifetime of the access token in seconds (expires_in), and
 * the string 'Bearer' (token_type)
 * @param user_id The user's UID to check for, passed through from
 *     token_exchange. If the UID is found, perform the Link the account.
 *     Otherwise, create an account.
 * @param client_id The ID given to the client sending the request, passed
 *     through from token_exchange
 */
async function handleStreamlinedLinkOrCreate(
    user_id: string, client_id: string, res: functions.Response) {
  const db = firebase.app().firestore();

  try {
    // Generate tokens
    const access_token = await generateToken(32, 'token');
    const refresh_token = await generateToken(64, 'token');
    // Check for existing tokens db entry
    const token_snapshot = await db.collection('tokens')
        .where('client_id', '==', client_id)
        .where('user_id', '==', user_id)
        .get();
    if (token_snapshot.empty) {
      // Add new db entry
      await db.collection('tokens').add({
        client_id: client_id,
        user_id: user_id,
        scope: [], // TODO (nevmital@): Introduce scoping?
        refresh_token: refresh_token,
        access_token: access_token,
        expires_in: ACCESS_TOKEN_EXPIRY,
        created_on: Date.now()
      });
    } else {
      // Update db entry
      await token_snapshot.docs[0].ref.set({
        client_id: client_id,
        user_id: user_id,
        scope: [], // TODO (nevmital@): Introduce scoping?
        refresh_token: refresh_token,
        access_token: access_token,
        expires_in: ACCESS_TOKEN_EXPIRY,
        created_on: Date.now()
      });
    }

    // Return tokens if database write is successful
    res.status(200).send({
      access_token: access_token,
      expires_in: ACCESS_TOKEN_EXPIRY,
      refresh_token: refresh_token,
      token_type: 'Bearer'
    });
  } catch (err) {
    // Return linking_error if database write unsuccessful
    console.log(err);
    res.status(401).send({error: 'linking_error', error_description: err});
  }
}

/**
 * This endpoint handles revoking a Access + Refresh token pair.
 * @param token The token to revoke
 * @param client_id The ID given to the client that was issued the token
 * @param client_secret The secret given to the client that was issued the token
 */
export const token_revocation = functions.https.onRequest(async (req, res) => {
  // NOTE: This log statement is for demonstrative/testing purposes only. It is
  //     recommended that you do not display/store the client secret in
  //     plaintext anywhere.
  console.log(`method: ${req.method}
client_id: ${req.body.client_id}
client_secret: ${req.body.client_secret}
token: ${req.body.token}`);

  if (req.method !== 'POST') {
    // Only accept POST requests
    res.status(405).send('Method not allowed');
    return;
  }

  const token = req.body.token;
  const client_id = req.body.client_id;
  const client_secret = req.body.client_secret;

  // Verify client
  if(!await verifyClient(client_id, client_secret)) {
    res.status(401).send({
      error: 'invalid_client',
      error_description: 'Invalid client ID or secret'
    });
    return;
  }

  // Verify token length
  let token_type;
  if (token.length === 32) {
    // Access token
    token_type = 'access';
  } else if (token.length === 64) {
    // Refresh token
    token_type = 'refresh';
  } else {
    // Invalid token
    res.status(200).send({
      error: 'invalid_grant',
      error_description: 'Invalid token'
    });
    return;
  }

  // Find token in database
  const db = firebase.app().firestore();
  const tokenSnapshot = await db.collection('tokens')
      .where(`${token_type}_token`, '==', token).get();
  if (tokenSnapshot.empty) {
    res.status(200).send({
      error: 'invalid_grant',
      error_description: `Invalid ${token_type} token`
    });
    return;
  }
  const doc = tokenSnapshot.docs[0];

  if (client_id !== doc.get('client_id')) {
    // Error if client secret does not match token
    res.status(401).send({
      error: 'invalid_client',
      error_description: 'Invalid client'
    });
    return;
  }

  try {
    await doc.ref.delete();
  } catch (err) {
    res.status(503).send({
      error: 'server_error',
      error_description: 'Error when deleting tokens'
    });
    return;
  }

  res.status(200).send('Token revoked!');
});

/**
 * This helper function verifies that the given client_id exists and that the
 * given client_secret matches the client_id.
 * Returns false if the client_id doesn't exist or if the client_secret doesn't
 * match, returns true otherwise.
 * @param client_id The ID given to the client
 * @param client_secret The secret given to the client
 */
async function verifyClient(
    client_id: string, client_secret: string): Promise<boolean> {
// Get client_id from client_secret
  const db = firebase.app().firestore();
  const snapshot = await db.collection('clients').doc(client_id).get();
  if (!snapshot.exists) {
    // Error if client ID is not found
    return false;
  }

  // Verify client_secret
  if (snapshot.get('client_secret') !== client_secret) {
    // Error if client secret does not match
    return false;
  }

  return true;
}
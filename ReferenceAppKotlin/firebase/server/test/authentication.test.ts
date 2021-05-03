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

import {assert} from "chai";
import * as firebaseTest from 'firebase-functions-test';

// Example for database URL - https://my-app.firebaseio.com
const dbURL = "REPLACE_ME"
// Example for projectId - my-app
const projectIdentifier = "REPLACE_ME"
// Example for storageBucket - my-app.appspot.com
const StorageBucketId = "REPLACE_ME"

const test = firebaseTest({
  databaseURL: dbURL,
  projectId: projectIdentifier,
  storageBucket: storageBucketId
}, './service-account-firebase.json');
test.mockConfig({
  app: {
    package_name: "com.android.tv.reference"
  }
});

import * as firebase from 'firebase-admin';
import firebaseServiceAccountJson = require('./service-account-firebase.json');

firebase.initializeApp({
  credential: firebase.credential.cert(firebaseServiceAccountJson),
  databaseURL: dbURL,
});

import {auth_endpoint, user_info, token_exchange} from "../src/controller/functions/authentication";

const AUTH_CODE_EXPIRY = 600; // seconds until expiry, 10 minutes
const ACCESS_TOKEN_EXPIRY = 3600; // seconds until expiry, 1 hour

const PROJECT_ID = projectIdentifier;
// ID Token for classy.taxi.exists@gmail.com, account exists
const id_token_valid = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImEwNjgyNGI3OWUzOTgyMzk0ZDVjZTdhYzc1YmY5MmNiYTMwYTJlMjUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE1NzMxNzQ2NDYsImF1ZCI6IjcwMTk2NjM5MTI4MS1tam8zaG85NXViajA0M2tkaWJkMmgwdW1zanBkcHRpci5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExMjE4MDY1MTM0OTc5NTM3ODA1NiIsImVtYWlsIjoiY2xhc3N5LnRheGkuZXhpc3RzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiQ2xhc3N5VGF4aSBFeGlzdHMiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1ocXhidmNBVWlLay9BQUFBQUFBQUFBSS9BQUFBQUFBQUFBQS9BQ0hpM3JlOV90Vm5CNjh4VTduZmlJQVhOMmx4U1ZnSEx3L3M5Ni1jL3Bob3RvLmpwZyIsImdpdmVuX25hbWUiOiJDbGFzc3lUYXhpIiwiZmFtaWx5X25hbWUiOiJFeGlzdHMiLCJsb2NhbGUiOiJlbi1VUyIsImlhdCI6MTU3MzE3NDk0NiwiZXhwIjoxNTczMTc4NTQ2LCJqdGkiOiIxZGExNTAxMGZlYWYzOTcyMThjY2JlYzI1ZWNlYjU3YjA4NjgyMDA4In0.D7BPcBbcBh0XeKM9UxEsV1JTpN-CMl3j80Lo9D4-UyN8gs_FvZDyeHPmPxo0LiCcG2dubyWs09PdNRUhsZeXEmIQeoKj4qXDMto-jOltljDcA7fjHC_EpgW6rbj-pQIsfe7MdEBx3PapPFF8dtNAXiBhpcg9KJloul_wrDYyWot2e13qaM9cAhOXA8N1k2BKDUtLVECW1OAoEVHniK9oNu5EqmQc0wl2-K95BoXpoaOx8fpX0g9zZW2Hz_8O6mdqA6bJO6rwhBkdvPw1OkduMOxzqO9Xg_ZdBEQW2ZOdqOVgSzn3rq3xxkU_kA9VGTqY44eMPCUvMAgelA4coavuhg";
// ID Token for classy.taxi.fake@gmail.com, account doesn't exist
const id_token_invalid = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImEwNjgyNGI3OWUzOTgyMzk0ZDVjZTdhYzc1YmY5MmNiYTMwYTJlMjUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJuYmYiOjE1NzMxNzQ5NDEsImF1ZCI6IjcwMTk2NjM5MTI4MS1tam8zaG85NXViajA0M2tkaWJkMmgwdW1zanBkcHRpci5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwMDQ0NTM2MDk3MDU5MTEyNDQxOSIsImVtYWlsIjoiY2xhc3N5LnRheGkuZmFrZUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IkNsYXNzeVRheGkgRmFrZSIsInBpY3R1cmUiOiJodHRwczovL2xoNi5nb29nbGV1c2VyY29udGVudC5jb20vLXNsZUg4NkgxMHFRL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FDSGkzcmY1ZXNnclJGT21HOWo1anpTMG9lZjBZRElFMFEvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IkNsYXNzeVRheGkiLCJmYW1pbHlfbmFtZSI6IkZha2UiLCJsb2NhbGUiOiJlbi1VUyIsImlhdCI6MTU3MzE3NTI0MSwiZXhwIjoxNTczMTc4ODQxLCJqdGkiOiIwMjYzMzU0N2Y1MWRlYjUyZDJkMTFjNDhjOWVjYmY4YzNjNWQzYTAwIn0.paf9pxYSYmOAeXU-5SKbVsB_vgX6Mvxnst6Q3-uww4xxPSTMnZcoO2NjDfzQDv2j-IZHwoF155zhvVY9HTfg13Nmhij9XBWlyj5_JFf2lML7Ju_07pkBjZtM2Z1H30PAfUqI4G86GWcEglUfRDbfguXJh0ZKMt20-o3KV1AdkxnT8dwfvY0MY291EENm1YcCD4s2JqvNvB06_V_-FSDbQyWFFfOqUMkj2ArFvY5kNEaZg6NgPbsOPUokV9ecQT7vVwkrf7n7c9waCYRNQ4hmJcLsZNSjgThiQrKpxxXP7lfJEVbSbUiKHlndBZQIDeMUgatV0GvQG9DbnYZVeNIH3A";

const db = firebase.app().firestore();

describe('OAuth: Authentication endpoint', () => {
  after(() => {
    test.cleanup();
  });

  it('Valid request', (done) => {
    const req = {
      method: "GET",
      query: {
        client_id: "test-client",
        redirect_uri: `https://oauth-redirect.googleusercontent.com/r/${PROJECT_ID}`,
        state: "state",
        scope: [],
        response_type: "code"
      }
    };

    const res = {
      redirect: (url) => {
        const expectedUrl = new RegExp("^https:\\/\\/oauth-redirect\\.googleusercontent\\.com\\/r\\/" + PROJECT_ID + "\\?code=(................................)&state=state$");
        assert.isTrue(expectedUrl.test(url));
        const codeToDelete = expectedUrl.exec(url)[1];
        db.collection('authcodes').where('auth_code', "==", codeToDelete).get()
            .then((snapshot) => {
              snapshot.forEach(async (doc) => await doc.ref.delete());
            })
            .catch((err) => {
              console.log(`Couldn't delete ${codeToDelete}: ${err}`);
            });
        done();
      }
    };

    auth_endpoint(req, res);
  });

  it('Using POST method', (done) => {
    const req = {
      method: "POST",
      query: {
        client_id: "test-client",
        redirect_uri: `https://oauth-redirect.googleusercontent.com/r/${PROJECT_ID}`,
        state: "state",
        scope: [],
        response_type: "code"
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 405);
        return res;
      },
      send: (message) => {
        assert.equal(message.error, "invalid_request");
        done();
      }
    };

    auth_endpoint(req, res);
  });

  it('Invalid client', (done) => {
    const req = {
      method: "GET",
      query: {
        client_id: "fake-client",
        redirect_uri: `https://oauth-redirect.googleusercontent.com/r/${PROJECT_ID}`,
        state: "state",
        scope: [],
        response_type: "code"
      }
    };

    const res = {
      redirect: (url) => {
        // TODO (nevmital@): Check error and state
        done();
      }
    };

    auth_endpoint(req, res);
  });

  it('Invalid URI', (done) => {
    const req = {
      method: "GET",
      query: {
        client_id: "test-client",
        redirect_uri: `https://fake.redirect.com/r/${PROJECT_ID}`,
        state: "state",
        scope: [],
        response_type: "code"
      }
    };

    const res = {
      redirect: (url) => {
        // TODO (nevmital@): Check error and state
        done();
      }
    };

    auth_endpoint(req, res);
  });

  it('Invalid response_type', (done) => {
    const req = {
      method: "GET",
      query: {
        client_id: "test-client",
        redirect_uri: `https://oauth-redirect.googleusercontent.com/r/${PROJECT_ID}`,
        state: "state",
        scope: [],
        response_type: "token"
      }
    };

    const res = {
      redirect: (url) => {
        // TODO (nevmital@): Check error and state
        done();
      }
    };

    auth_endpoint(req, res);
  });
});

describe('OAuth: UserInfo endpoint', () => {
  after(() => {
    test.cleanup();
  });

  it("Account exists", (done) => {
    const req = {
      method: "POST",
      headers: {
        authorization: "Bearer b447bb884080a467db3ca7552497875c"
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 200);
        return res;
      },
      send: (data) => {
        assert.equal(data.email, "classy.taxi.exists@gmail.com");
        done();
      }
    };

    user_info(req, res);
  });

  it("Account does not exist", (done) => {
    const req = {
      method: "POST",
      headers: {
        authorization: "Bearer fake-token"
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 401);
        return res;
      },
      send: () => {
        done();
      },
      set: (header, value) => {
        assert.equal(header, "WWW-Authenticate");
        assert.equal(value, "error='invalid_token', error_description='Invalid access token'");
        return res;
      }
    };

    user_info(req, res);
  });
});

describe("OAuth: Token exchange endpoint", () => {
  const authCode = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
  const accessToken = "tttttttttttttttttttttttttttttttt";
  const refreshToken = "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr";

  after(async () => {
    await db.collection('authcodes').doc('testdoc').delete();
    await db.collection('tokens').doc('testdoc').delete();
    test.cleanup();
  });

  it("Valid authentication code request", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "authorization_code",
        code: authCode
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 200);
        return res;
      },
      send: (data) => {
        assert.equal(data.token_type, "Bearer");
        assert.equal(data.expires_in, ACCESS_TOKEN_EXPIRY);
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("refresh_token", "==", data.refresh_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              snapshot.forEach(async (doc) => await doc.ref.delete());
            })
            .catch((err) => {
              console.log(`Couldn't delete ${data.access_token}: ${err}`)
            });
        done();
      }
    };

    db.collection('authcodes').doc('testdoc').set({
          client_id: "test-client",
          user_id: "DXZc65jRVadD0hALWsvGAh7kFbZ2",
          scope: [],
          auth_code: authCode,
          expires_in: AUTH_CODE_EXPIRY,
          created_on: Date.now()
        })
        .then(() => {
          token_exchange(req, res);
        })
        .catch((err) => {
          console.log(err);
          assert.fail();
        });
  });

  it("Valid refresh token request", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "refresh_token",
        refresh_token: refreshToken
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 200);
        return res;
      },
      send: (data) => {
        assert.equal(data.token_type, "Bearer");
        assert.equal(data.expires_in, ACCESS_TOKEN_EXPIRY);
        assert.notEqual(data.access_token, accessToken);
        assert.equal(data.refresh_token, refreshToken);
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("refresh_token", "==", data.refresh_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 2);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              snapshot.forEach(async (doc) => await doc.ref.delete());
            })
            .catch((err) => {
              console.log(`Couldn't delete ${data.access_token}: ${err}`)
            });
        done();
      }
    };

    db.collection('tokens').doc('testdoc').set({
          client_id: "test-client",
          user_id: "DXZc65jRVadD0hALWsvGAh7kFbZ2",
          scope: [],
          access_token: accessToken,
          refresh_token: refreshToken,
          expires_in: ACCESS_TOKEN_EXPIRY,
          created_on: Date.now()
        })
        .then(() => {
          token_exchange(req, res)
        })
        .catch((err) => {
          console.log(err);
          assert.fail();
        });
  })

});

describe('FSubs: Streamlined Account Linking, Check request', () => {
  after(() => {
    test.cleanup();
  });

  it("Account exists", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        intent: "check",
        assertion: id_token_valid
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 200);
        return res;
      },
      send: (data) => {
        assert.isTrue(data.account_found);
        done();
      }
    };

    token_exchange(req, res);
  });

  it("Account does not exist", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        intent: "check",
        assertion: id_token_invalid
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 404);
        return res;
      },
      send: (data) => {
        assert.isFalse(data.account_found);
        done();
      }
    };

    token_exchange(req, res);
  });
});

describe('FSubs: Streamlined Account Linking, Link request', () => {
  after(() => {
    test.cleanup();
  });

  it("Account exists", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        intent: "get",
        assertion: id_token_valid
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 200);
        return res;
      },
      send: (data) => {
        assert.equal(data.token_type, "Bearer");
        assert.equal(data.expires_in, ACCESS_TOKEN_EXPIRY);
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("refresh_token", "==", data.refresh_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        done();
      }
    };

    token_exchange(req, res);
  });

  it("Account does not exist", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        intent: "get",
        assertion: id_token_invalid
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 401);
        return res;
      },
      send: (data) => {
        assert.equal(data.error, 'linking_error');
        done();
      }
    };

    token_exchange(req, res);
  });
});

describe('FSubs: Streamlined Account Linking, Create request', () => {
  after(() => {
    test.cleanup();
  });

  it("Account already exists", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        intent: "create",
        assertion: id_token_valid
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 401);
        return res;
      },
      send: (data) => {
        assert.equal(data.error, 'linking_error');
        done();
      }
    };

    token_exchange(req, res);
  });

  it("Account does not yet exist", (done) => {
    const req = {
      method: "POST",
      body: {
        client_id: "test-client",
        client_secret: "shhh",
        grant_type: "urn:ietf:params:oauth:grant-type:jwt-bearer",
        intent: "create",
        assertion: id_token_invalid
      }
    };

    const res = {
      status: (code) => {
        assert.equal(code, 200);
        return res;
      },
      send: (data) => {
        assert.equal(data.token_type, "Bearer");
        assert.equal(data.expires_in, ACCESS_TOKEN_EXPIRY);
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("refresh_token", "==", data.refresh_token).get()
            .then((snapshot) => {
              assert.isTrue(snapshot.size === 1);
            })
            .catch((err) => {
              console.log(err);
              assert.fail();
            });
        db.collection('tokens').where("access_token", "==", data.access_token).get()
            .then((snapshot) => {
              snapshot.forEach(async (doc) => await doc.ref.delete());
            })
            .catch((err) => {
              console.log(`Couldn't delete ${data.access_token}: ${err}`)
            });
        firebase.auth().getUserByEmail("classy.taxi.fake@gmail.com")
            .then((user) => {
              firebase.auth().deleteUser(user.uid);
            })
            .catch((err) => {
              console.log(`Couldn't delete user classy.taxi.fake@gmail.com: ${err}`)
            });
        done();
      }
    };

    token_exchange(req, res);
  });
});
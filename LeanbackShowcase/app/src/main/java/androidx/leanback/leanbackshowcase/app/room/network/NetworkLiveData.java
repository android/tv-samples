/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.leanback.leanbackshowcase.app.room.network;

import androidx.lifecycle.LiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import androidx.annotation.MainThread;
import android.util.Log;

public class NetworkLiveData extends LiveData<Boolean> {

    // For debugging purpose
    private static final boolean DEBUG = false;
    private static final String TAG = "NetworkLiveData";

    // The network live data should existed as long as the application is still running
    // so we use a static variable to represent it.
    // To make sure there is no memory leakage, when there is no active observer, we will remove
    // the reference to the callback.
    private static NetworkLiveData sNetworLivekData;

    private ConnectivityManager connectivityManager;

    @MainThread
    public static NetworkLiveData sync(Context context) {
        if (sNetworLivekData == null) {
            sNetworLivekData = new NetworkLiveData(context.getApplicationContext());
        }
        return sNetworLivekData;
    }

    private NetworkLiveData(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerDefaultNetworkCallback(callback);
    }

    private ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {

        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            postValue(true);
        }

        @Override
        public void onUnavailable() {
            super.onUnavailable();
            postValue(false);
        }

        @Override
        public void onLost(Network network) {
            super.onLost(network);
            postValue(false);
        }

        @Override
        public void onLosing(Network network, int maxMsToLive) {
            super.onLosing(network, maxMsToLive);
        }

        @Override
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
        }

        @Override
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
        }
    };

    /**
     * When there is an active observer observing the network live data, our network live data
     * will register a callback to the ConnectivityManager to listen to the change of network
     *
     * Also at first time, it will fetch the connectivity information through network info
     */
    @Override
    protected void onActive() {
        super.onActive();
        if (DEBUG) {
            Log.e(TAG, "onActive: ", new Exception());
        }
        postConnectivityStatus.execute();
    }

    // The interaction with connectivityManager may block the UI, so it is delegated as a
    // background task
    private AsyncTask<Void, Void, Void>  postConnectivityStatus= new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            if (connectivityManager.getActiveNetworkInfo() != null) {
                postValue(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting());
            } else {
                postValue(false);
            }
            return null;
        }
    };

    /**
     * When there is no active observer observing our network live data. Our network live data
     * will unregister the callback to make sure there is no memory leakage.
     */
    @Override
    protected void onInactive() {
        if (DEBUG) {
            Log.e(TAG, "onActive: ", new Exception());
        }
        super.onInactive();
        connectivityManager.unregisterNetworkCallback(callback);
    }
}

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

package androidx.leanback.leanbackshowcase.app.room.controller.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.leanbackshowcase.R;
import androidx.leanback.app.SearchFragment;
import androidx.leanback.widget.SpeechRecognitionCallback;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * Extend from LifecycleActivity so this activity can be used as the owner of lifecycle event
 */
public class SearchActivity extends FragmentActivity implements HasSupportFragmentInjector{

    private static final String TAG = "SearchActivity";
    private static final boolean DEBUG = false;
    private static final boolean USE_INTERNAL_SPEECH_RECOGNIZER = false;
    private static final int REQUEST_SPEECH = 1;

    // The fragment has to be retrived from the id which is the runtime information
    public SearchFragment mFragment;

    public SpeechRecognitionCallback mSpeechRecognitionCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mFragment = (SearchFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
        if (USE_INTERNAL_SPEECH_RECOGNIZER) {
            mSpeechRecognitionCallback = new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    if (DEBUG) {
                        Log.v(TAG, "recognizeSpeech");
                    }
                    startActivityForResult(mFragment.getRecognizerIntent(), REQUEST_SPEECH);
                }
            };
            mFragment.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (DEBUG){
            Log.v(TAG, "onActivityResult requestCode="
                    + requestCode +
                    " resultCode="
                    + resultCode +
                    " data="
                    + data);
        }
        if (requestCode == REQUEST_SPEECH && resultCode == RESULT_OK) {
            mFragment.setSearchQuery(data, true);
        }
    }

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

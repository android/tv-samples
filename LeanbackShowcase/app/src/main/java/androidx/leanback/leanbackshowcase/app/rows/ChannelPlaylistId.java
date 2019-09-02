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

package androidx.leanback.leanbackshowcase.app.rows;

/**
 * It is special designed class to group the information of channel Contents ID (internal ID) and
 * published ID
 * The reason to have this class is to use channel contents ID (which is unique and won't be
 * changed for each channel) to locate the published ID (which will be changed along with
 * the transaction on content provider)
 *
 * I.e. For the same channel in different insert operation (add channel to home screen),
 * system will generate different published ID. It is necessary to use the immutable content ID
 * to locate the correct channel and toggle its publish status.
 */
public class ChannelPlaylistId {
    String mId;
    long mChannelId;

    ChannelPlaylistId(String id, long channelId) {
        mId = id;
        mChannelId = channelId;
    }
}

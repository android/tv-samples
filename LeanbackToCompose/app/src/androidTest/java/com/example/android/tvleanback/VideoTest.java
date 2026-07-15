/*
 * Copyright 2019 Google LLC
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
package com.example.android.tvleanback;

import android.media.MediaDescription;
import android.net.Uri;
import android.os.Parcel;

import com.example.android.tvleanback.model.Video;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class VideoTest {

    @Test
    public void VideoParceableTest() {
        List<Video> testVideoList = new ArrayList<>();
        testVideoList.add(new Video.VideoBuilder()
                .title("Dragon Movie")
                .description("A delightful kids movie")
                .studio("Dream Movies")
                .id((long) (Math.random()*100L))
                .build());
        testVideoList.add(new Video.VideoBuilder()
                .title("Grimm Fairy Tales")
                .description("A live action adaptation of a classic cartoon")
                .studio("Hollywood Studios")
                .cardImageUrl("http://example.com/grim_poster.png")
                .bgImageUrl("http://example.com/grim_bg.png")
                .build());
        testVideoList.add(new Video.VideoBuilder()
                .title("Kyle")
                .description("A live action adaptation of an old story")
                .studio("Wolf")
                .cardImageUrl("http://example.com/kyle_poster.png")
                .bgImageUrl("http://example.com/kyle_bg.png")
                .id(200)
                .build());
        testVideoList.add(new Video.VideoBuilder()
            .buildFromMediaDesc(new MediaDescription.Builder()
                    .setTitle("The Adventures of Albert the Raccoon")
                    .setDescription("Albert and friends travel around Arnoria completing quests")
                    .setMediaId("535")
                    .setSubtitle("Fantasy Productions")
                    .setIconUri(Uri.parse("http://www.example.co.uk/static/images/raccoon_colour.jpg"))
                    .build()));

        for (Video testVideo : testVideoList) {
            Parcel testVideoParcel = Parcel.obtain();
            testVideo.writeToParcel(testVideoParcel, 0);
            Video testVideoClone = Video.CREATOR.createFromParcel(testVideoParcel);

            assert testVideo.id == testVideoClone.id;
            assert testVideo.title.equals(testVideoClone.title);
            assert testVideo.description.equals(testVideoClone.description);
            assert testVideo.category.equals(testVideoClone.category);
            assert testVideo.studio.equals(testVideoClone.studio);
            assert testVideo.bgImageUrl.equals(testVideoClone.bgImageUrl);
            assert testVideo.cardImageUrl.equals(testVideoClone.cardImageUrl);
            assert testVideo.toString().equals(testVideoClone.toString());
        }
    }
}

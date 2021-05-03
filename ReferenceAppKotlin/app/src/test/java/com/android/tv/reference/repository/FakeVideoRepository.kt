package com.android.tv.reference.repository

import android.app.Application
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import java.time.Duration

class FakeVideoRepository(override val application: Application) : VideoRepository {
    override fun getAllVideos(): List<Video> {
        return episodes
    }

    override fun getVideoById(id: String): Video? {
        return episodes.firstOrNull { it.id == id }
    }

    override fun getVideoByVideoUri(uri: String): Video? {
        return episodes.firstOrNull { it.uri == uri }
    }

    override fun getAllVideosFromSeries(seriesUri: String): List<Video> {
        return episodes
    }

    companion object {

        // values taken from api.json from resources.
        const val TEST_VIDEO_ID =
            "https://atv-reference-app.firebaseapp.com/movies-tech/seomb-seo-mythbusting-101"
        const val TEST_VIDEO_NAME = "SEO Mythbusting 101"

        private const val TEST_EPISODE_PREFIX = "https://atv-reference-app.firebaseapp.com/tv/"
        private const val TEST_EPISODE_CATEGORY = "TV Category"

        /*  ktlint-disable max-line-length */
        private const val TEST_VIDEO_DESCRIPTION =
            "In this first - introductory - episode of SEO Mythbusting, Martin Splitt (WebMaster Trends Analyst, Google) and his guest Juan Herrera (Angular GDE, Wed Developer at Parkside) discuss the very basics of SEO."

        /*  ktlint-disable max-line-length */
        private const val TEST_VIDEO_URI =
            "https://storage.googleapis.com/atv-reference-app-videos/movies-tech/seomb-seo-mythbusting-101.mp4"

        /*  ktlint-disable max-line-length */
        private const val TEST_VIDEO_PLACEHOLDER_URI =
            "https://storage.googleapis.com/atv-reference-app-videos/movies-tech/seomb-seo-mythbusting-101.mp4"

        /*  ktlint-disable max-line-length */
        private const val TEST_VIDEO_THUMBNAIL_URI =
            "https://storage.googleapis.com/atv-reference-app-videos/movies-tech/seomb-seo-mythbusting-101-thumbnail.png"

        // Adjust the playback position and duration with the time you want to test
        // Add approximate duration and playback position.
        var TEST_VIDEO_PLAYBACK_POSITION_MILLIS = Duration.ofMinutes(17).toMillis().toInt()
        var TEST_VIDEO_PLAYBACK_CREDIT_SCENE_POSITION_MILLIS = Duration.ofMinutes(38).toMillis().toInt()
        private var TEST_VIDEO_DURATION = "PT00H40M"

        var video_movie = Video(
            id = TEST_VIDEO_ID,
            name = TEST_VIDEO_NAME,
            description = TEST_VIDEO_DESCRIPTION,
            uri = TEST_VIDEO_URI,
            videoUri = TEST_VIDEO_PLACEHOLDER_URI,
            thumbnailUri = TEST_VIDEO_THUMBNAIL_URI,
            backgroundImageUri = TEST_VIDEO_PLACEHOLDER_URI,
            category = "",
            videoType = VideoType.MOVIE,
            duration = TEST_VIDEO_DURATION
        )

        val episodes = listOf(
            Video(
                id = TEST_EPISODE_PREFIX + "season-1-episode-10",
                name = "Elly Becomes a Secretary",
                description = "Jed takes over Drysdale's job for a day.",
                uri = TEST_EPISODE_PREFIX + "season-1-episode-10",
                videoUri = TEST_EPISODE_PREFIX + "season-1-episode-10.mp4",
                thumbnailUri = TEST_EPISODE_PREFIX + "season-1-episode-10.png",
                backgroundImageUri = TEST_EPISODE_PREFIX + "season-1-episode-10.jpg",
                category = TEST_EPISODE_CATEGORY,
                videoType = VideoType.EPISODE,
                episodeNumber = "10",
                seasonNumber = "1",
                seriesUri = TEST_EPISODE_PREFIX + "tv_series",
                duration = TEST_VIDEO_DURATION
            ),
            Video(
                id = TEST_EPISODE_PREFIX + "season-1-episode-11",
                name = "Jethrod's Friend",
                description = "One of Jethro's school friends spends a day at the Clampett mansion",
                uri = TEST_EPISODE_PREFIX + "season-1-episode-11",
                videoUri = TEST_EPISODE_PREFIX + "season-1-episode-11.mp4",
                thumbnailUri = TEST_EPISODE_PREFIX + "season-1-episode-11.png",
                backgroundImageUri = TEST_EPISODE_PREFIX + "season-1-episode-11.jpg",
                category = TEST_EPISODE_CATEGORY,
                videoType = VideoType.EPISODE,
                episodeNumber = "11",
                seasonNumber = "1",
                seriesUri = TEST_EPISODE_PREFIX + "tv_series",
                duration = TEST_VIDEO_DURATION
            ),
            Video(
                id = TEST_EPISODE_PREFIX + "season-2-episode-1",
                name = "Jed Gets the Misery",
                description = "Jed fakes illness for doctor Granny.",
                uri = TEST_EPISODE_PREFIX + "season-2-episode-1",
                videoUri = TEST_EPISODE_PREFIX + "season-2-episode-1.mp4",
                thumbnailUri = TEST_EPISODE_PREFIX + "season-2-episode-1.png",
                backgroundImageUri = TEST_EPISODE_PREFIX + "season-2-episode-1.jpg",
                category = TEST_EPISODE_CATEGORY,
                videoType = VideoType.EPISODE,
                episodeNumber = "1",
                seasonNumber = "2",
                seriesUri = TEST_EPISODE_PREFIX + "tv_series",
                duration = TEST_VIDEO_DURATION
            )
        )
    }
}
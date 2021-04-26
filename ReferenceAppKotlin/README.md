# TV Reference App

This sample demonstrates how to create a video playback app that is optimized for Android TV and
Google TV. It demonstrates how to

* Use [Leanback templates][leanback-templates] for browse and playback
* Play remote video with [ExoPlayer][exoplayer]
* Support playback controls with [MediaSession][media-session] support via
[MediaSessionConnector][media-session-connector]
* Deep link directly into playback for Assistant, Cast, and home screen programs
* Create and populate a [home screen channel][home-screen-channel]
* Update the [Watch Next][watch-next] row to support play next/continue watching
* Enable the Android TV app to act as a Cast receiver with [Cast Connect][cast-connect]

The app also shows how to use Firebase Cloud Functions for handling Google Account linking. This
requires additional work detailed below.

## Getting Started

To open this sample in Android Studio, begin by cloning the repository with git:

```sh
git clone https://github.com/android/tv-samples.git
```

This repository contains multiple samples. To run this one, you need to:

- Open the ReferenceAppKotlin project in [Android Studio][studio].
- Compile and deploy to your Android TV emulator or device (such as an ADT-3).

See the [official getting started guide][getting-started] for more info.

## App Organization

The app is organized by features. For instance, instead of having all Fragments in one package,
there is a package for `browse`, which contains the Fragment, ViewModel, Adapter, etc. all in one
place. Functionality that is shared across features such as the data models are in the “shared”
package.

### Firebase Integration

If FIREBASE_ENABLED is set to false in the app's [build.gradle](app/build.gradle) file (not the
project's file) the app displays a warning when it starts up saying that Firebase is not configured.
In this case account-specific features are disabled. If you change the flag to true the message no
longer appears, but you will need to still update the project to use Firebase.

See the [firebase](firebase) directory and its [README file](firebase/README.md) for an example of
how to set up Firebase. You should modify
[RemoteAuthClient](app/src/main/java/com/android/tv/reference/auth/RemoteAuthClient.kt) to reflect
your configuration.

The example code shows how to include Google Account Linking. This requires registering your
endpoints with Google. See the [Google Account Linking][account-linking] documentation for details.

### Browse

The sample implements Browse with a [BrowseSupportFragment][browse-support-fragment]. To keep the
sample simple, the [JSON][api-json] containing the media is a raw resource.

### Playback

Playback is built on top of [VideoSupportFragment][video-support-fragment], which simplifies the
creation of a video playback UI. The actual playback itself is handled by [ExoPlayer][exoplayer].
This sample also supports playback via controls such as voice, a remote control, a Bluetooth
headset, etc. via the [MediaSession][media-session] integration. Watch progress is stored locally in
a SQL database using Room to support resuming playback from where the user was last watching.

Playback tends to be a very complicated feature in apps, so this sample has a basic state machine
that allows components to listen to the states that they care about (e.g., to know when playback is
paused) and respond accordingly. For more details about the state machine, see
[STATE_MACHINE.md](STATE_MACHINE.md).

### Home screen channel

A [home screen channel][home-screen-channel] is created when the HomeScreenChannelReceiver is
triggered. When a user installs an app from Google Play, a broadcast is automatically sent by the
system to allow the app to add its channel(s). You can simulate this same broadcast with the
following:

```sh
adb shell am broadcast -a android.media.tv.action.INITIALIZE_PROGRAMS -n com.android.tv.reference/.homescreenchannels.HomeScreenChannelReceiver
```

### Watch Next

Watch Next (sometimes called play next or continue watching) is a feature that allows users to
quickly resume content. For example, if a user pauses a movie halfway through, that movie can show
up on the device's home screen. Similarly, if a user finishes a TV episode, the next episode in the
series will show up. This sample follows the [Watch Next guidelines][watch-next-guidelines] to
ensure the best user experience.

On Android TV, all apps can add content to the Watch Next row. On Google TV, apps must be certified
before their content will show up on device (see the [documentation][watch-next-certify] for more
details). That means it's easiest to test this feature by running the sample on an Android TV,
watching a movie for a few minutes, and then returning to the Android TV home screen. The Watch Next
content will show just below the apps row.

### Cast Connect

Android TV and Google TV Receivers use the Cast Connect library to allow existing sender
applications to communicate with Android TV/Google TV applications via the Cast protocol.
Cast Connect builds on top of the Cast infrastructure, with your TV app acting as a receiver.

The Cast Connect library allows your TV app to receive messages and broadcast media
status, as if it were a Chromecast. Using Cast Connect, a cast sender application on a mobile
device can directly send cast signals to your TV App and open the TV App for casting, playback
and media controls. See the [documentation][cast-connect] for more details and the
[README file](CAST_CONNECT.md) for steps on how to configure a Cast Sender application to work with
this TV Reference App.

## Support

For general Android, Android TV, and Google TV related questions, please use the
[android-tv][stack-overflow] tag on Stack Overflow.

If you find a problem in this sample, you can
[file an issue](https://github.com/android/tv-samples/issues). Please specify that the problem is
with the ReferenceAppKotlin sample and include details that allow us to reproduce the problem. If
the issue is causing a crash, including the stack trace will help a lot.

Patches are encouraged and may be submitted by forking this project and submitting a pull request
through GitHub. Please see [CONTRIBUTING.md](../CONTRIBUTING.md) for more details.

## Media

The clips used in this sample are copyright Google. The TV episodes and movies are in the public
domain.

[leanback-templates]: https://developer.android.com/training/tv/playback
[browse-support-fragment]: https://developer.android.com/reference/androidx/leanback/app/BrowseSupportFragment
[media-session-connector]: https://exoplayer.dev/doc/reference/com/google/android/exoplayer2/ext/mediasession/MediaSessionConnector.html
[cast-connect]: https://developers.google.com/cast/docs/android_tv_receiver
[api-json]: app/src/main/res/raw/api.json
[exoplayer]: https://github.com/google/ExoPlayer
[video-support-fragment]: https://developer.android.com/reference/androidx/leanback/app/VideoSupportFragment
[media-session]: https://developer.android.com/guide/topics/media-apps/working-with-a-media-session
[home-screen-channel]: https://developer.android.com/training/tv/discovery/recommendations-channel
[watch-next]: https://developer.android.com/training/tv/discovery/watch-next-add-programs
[watch-next-guidelines]: https://developer.android.com/training/tv/discovery/guidelines-app-developers
[watch-next-certify]: https://developer.android.com/training/tv/discovery/watch-next-add-programs#steps
[studio]: https://developer.android.com/tools/studio/index.html
[getting-started]: https://developer.android.com/training/tv/start/start.html
[stack-overflow]: https://stackoverflow.com/questions/tagged/android-tv

# Leanback Support Library Showcase App for Android TV

This sample app showcases different components that come with Leanback library.

The home screen in this sample app is a BrowseFragment consisting of a set of different cards.
Each card represents a component in Leanback.

All the cards in this sample app work except the Settings.
Settings for now crashes due to some internal issue, and we are working on
resolving that in the next few days.

We will also add a new music example card soon.
This will be a new PlaybackOverlayFragment that comes with a set of new
features.

## Introduction

- [Read more about Android TV introduction](http://www.android.com/tv/)
- [Android TV Developer Documentation](http://developer.android.com/tv)
- [Android TV apps in Google Play Store][store-apps]

## Dependencies

If you use Android Studio as recommended, the following dependencies will **automatically** be installed by Gradle.

- Android SDK v7 appcompat library
- Android SDK v17 leanback support library
- Android SDK v7 recyclerview library

## Getting Started

- Clone this repo:

```sh
git clone https://github.com/android/tv-samples.git
```

- In the local.properties file add the location to your Android SDK (You need to install Android SDK 'N').
- Open the project in [Android Studio][studio].
- Compile and deploy to your Android TV emulator or device (such as an ADT-2 or ADT-3).


## Screenshots

![Screenshot](screenshots/Showcase-Snapshots.png)

Need more information about getting started with Android TV? Check the [official getting started guide][getting-started].

## Support

If you need additional help, our community might be able to help.

- Stack Overflow: http://stackoverflow.com/questions/tagged/android-tv

## Contributing

We love contributions! :smile: Please follow the steps in the [CONTRIBUTING guide][contributing] to get started. If you found a bug, please file it [here][bugs].

## License

See the [LICENSE file][license] for details.

[store-apps]: https://play.google.com/store/apps/collection/promotion_3000e26_androidtv_apps_all
[studio]: https://developer.android.com/tools/studio/index.html
[getting-started]: https://developer.android.com/training/tv/start/start.html
[bugs]: https://github.com/googlesamples/androidtv-Leanback/issues/new
[contributing]: ../CONTRIBUTING.md
[license]: ../LICENSE

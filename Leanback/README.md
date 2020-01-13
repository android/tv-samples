# Android TV Leanback Support Library sample - Videos by Google

This sample is a Videos By Google app, designed to run on an Android TV device, which demonstrates how to use the Leanback Support library which enables you to easily develop beautiful Android TV apps with a user-friendly UI that complies with the UX guidelines of Android TV.


## Getting Started

- Clone this repo:

```sh
git clone https://github.com/android/tv-samples.git
```

- Open the Leanback project within tv-samples in [Android Studio][studio].
- Compile and deploy to your Android TV emulator or device (such as an ADT-2 or ADT-3).

Need more information about getting started with Android TV? Check the [official getting started guide][getting-started].

## Explore the sample

- Choose a layout
  - Videos grouped by [category][mainfragment] (See BrowseFragment in [screenshots][screenshots])
  - Freeform [vertical grid][verticalgridfragment] of videos (See Vertical Grid Fragment in [screenshots][screenshots])
- Customize video cards with a [Card Presenter][cardpresenter] (See Card Views in [screenshots][screenshots])
- Display in-depth [details][detailsfragment] about your video
- Play a video
  - [Playback with ExoPlayer2][playbackfragment]
  - [Add extra buttons to control playback][videoplayerglue]
- [Display an error][errorfragment]
- Make your app globally searchable
  - Review searchable training [document][searchable]
     - Creating a [content provider][videoprovider]
     - Defining [searchable.xml][searchable.xml]
     - Receive search intent in [manifest][manifestsearch]
- [Search][searchfragment] within your app
- [Onboard][onboardingfragment] new users (explain new features)
- Customize [preference and settings][settingsfragment]
- Add a wizard with [guided steps][guidedstep]

[screenshots]: https://github.com/googlesamples/androidtv-Leanback#screenshots

[manifestsearch]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/AndroidManifest.xml#L79

[searchfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/SearchFragment.java

[cardpresenter]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/presenter/CardPresenter.java

[searchable.xml]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/res/xml/searchable.xml

[searchable]: https://developer.android.com/training/tv/discovery/searchable.html

[videoprovider]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/data/VideoProvider.java

[errorfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/BrowseErrorFragment.java

[mainfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/MainFragment.java

[detailsfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/VideoDetailsFragment.java

[verticalgridfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/VerticalGridFragment.java

[guidedstep]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/GuidedStepActivity.java

[onboardingfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/OnboardingFragment.java

[settingsfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/SettingsFragment.java

[videoplayerglue]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/player/VideoPlayerGlue.java

[playbackfragment]: https://github.com/googlesamples/androidtv-Leanback/blob/master/app/src/main/java/com/example/android/tvleanback/ui/PlaybackFragment.java

## Additonal Resouroces

- [Android TV Introduction](http://www.android.com/tv/)
- [Android TV Developer Documentation](http://developer.android.com/tv)
- [Android TV Apps in Google Play Store][store-apps]


## Screenshots

[![Screenshot](screenshots/atv-leanback-all.png)](https://raw.githubusercontent.com/googlesamples/androidtv-Leanback/master/screenshots/atv-leanback-all.png)

## Support

If you need additional help, our community might be able to help.

- Stack Overflow: [http://stackoverflow.com/questions/tagged/android-tv](http://stackoverflow.com/questions/tagged/android-tv)

## Dependencies

If you use Android Studio as recommended, the following dependencies will **automatically** be installed by Gradle.

- Android SDK v7 appcompat library
- Android SDK v17 leanback support library
- Android SDK v7 recyclerview library

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

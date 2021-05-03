# Cast Sender Application Setup

To make the sender Android application [CastVideos-Android](https://github.com/googlecast/CastVideos-android)
Cast to this sample instead of the default application(cast sample app) that it casts to, a few
small changes need to be made to the sender application.

The complete list of steps to be performed are listed as below -

1. Download the sample [Android sender application](https://github.com/googlecast/CastVideos-android) from Github.

1. Import the project into Android Studio or use gradle to build the project.

1. Compile and deploy to your Android mobile device.

After performing these steps, you will be able to Cast to the default receiver application that this sender uses.

### Casting to this sample (TV Reference App)

To Cast to this sample instead, perform the following steps -

1. Register an application on the [Cast SDK Developers Console](http://cast.google.com/publish) following
the instructions in the [Cast Registration Docs](https://developers.google.com/cast/docs/registration).
Select the Custom Receiver option and enter this sample's package name(com.android.tv.reference) in the
“Package Name” field. You will get an App ID when you finish registering your application. If you need
to do any console debugging, you would need to have your own App ID.

1. Add a device on the Cast SDK developers console by entering the serial number of the cast interface on
your android TV. In the console, click on “ADD NEW DEVICE” and enter the serial number. To find the serial
number, either go to settings on your Android TV and find it under Cast settings or if your Android TV is
on the same WiFi network as the device on which you are accessing the Cast SDK console, then you will have
an option to have the serial number read out aloud on your TV (ensure you have your volume turned up) when
you click on the “ADD NEW DEVICE” button.

1. Wait for your device to be registered for testing on the console. This will take around 15 minutes,
and you can see the status on the console. Once the status shows “Ready for Testing”, restart your
Android TV/Google TV. This device will now be able to pick up signals from an unpublished application
that you have created on the console.

### Changes in Cast Sender App

The changes to be made in the Cast Sender App is that in the [strings.xml file in the res folder](app/src/main/res/values/strings.xml),
update the “app_id” string to the APP_ID that was created for your application when you registered
a new application on the Cast SDK Console.

   ```
      <string name="app_id">Your_App_ID</string>
   ```

This change would now connect the sender to your TV application by linking the sender to the APP_ID
that was created on the Cast SDK Console, and this APP_ID is linked to the package name of your receiver
application as was entered in the Console.

### Changes to load application-specific content and data

To load content and media data that is specific to your application and not the one that is listed
in the sample sender app, and to use your deeplinks/content IDs to be sent to the receiver, follow
either of these two approaches:

1. In the class [VideoBrowserFragment](https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/browser/VideoBrowserFragment.java),
update the instance variable “CATALOG_URL” to point to a URL that returns a JSON
string in the same format as the one present at
[https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json](https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json)
so that it can be converted to the appropriate MediaInfo objects that are used to play the video in the player
locally and to send them to be casted to the receiver. This would require minimum change to the code of
the sender application, however, it must be ensured that the JSON returned by the URL is of the right
format and has the right fields so that it can be parsed correctly. If the format of the JSON returned
for your application at the URL is different, the parsing can be done accordingly by making changes
to the class VideoProvider in the “browser” folder, in the methods buildMedia() and buildMediaInfo().

1. Update the MediaInfo object that is sent to the Cast receiver so that it can be used by the receiver
TV App to identify the content and play it on the receiver. This change needs to be done in the
`loadRemoteMedia()` method in the class [LocalPlayerActivity](https://github.com/googlecast/CastVideos-android/blob/master/src/com/google/sample/cast/refplayer/mediaplayer/LocalPlayerActivity.java).
Instead of loading the MediaInfo object of the selected content item, it needs to be changed to
create a MediaInfo object with the fields corresponding to the content of your application using one
of the following options:

    * To load the content of your application using a deeplink, create a MediaInfo object and load it
    for the receiver using code from the [documentation](https://developers.google.com/cast/docs/android_tv_receiver/core_features#load_by_entity_on_sender).

    * To load the content of your application using a content ID, use the code from this [documentation]
    (https://developers.google.com/cast/docs/android_tv_receiver/core_features#loading_by_content_id_or_mediaqueuedata).







# Baseline Profiles

[Details on developer.android.com](https://developer.android.com/studio/profile/baselineprofiles)


## Generating baseline profiles

To generate the baseline profile for Jetstream app, run "Generate Baseline Profile" from "**Run**".

> Note: The baseline profile needs to be re-generated for release builds that touch code which changes app startup.

To learn more on generating baseline profiles, follow this [Codelab](http://goo.gle/baseline-profiles).


## Applying baseline profiles

Baseline profiles are automatically applied when an app is installed from the Play Store and also when running benchmark test cases.


To apply the baseline profile when running the app from Android Studio, follow these steps:


**Step-1**: Install and launch the app manually or follow this command:

```
adb shell am start -n com.google.jetstream/com.google.jetstream.MainActivity
```

> Note: When testing for performance, it is always best to install the release build of the app.


**Step-2**: Compile the app with baseline profile using the following command: 

```
adb shell cmd package compile -f -m speed-profile com.google.jetstream
```


**Step-3**: Determine the status of profile by running the following:

```
adb shell dumpsys package dexopt | grep -A 1 com.google.jetstream
```

If the status is `status=speed-profile`, it means that baseline profile rules have been applied to optimize the app. Therefore, you can skip directly to **Step-5**.

If the status is different, follow the next step.


**Step-4**: Execute the background optimizations by running the following command: 

```
adb shell cmd package bg-dexopt-job
```

It executes a background task that typically takes about ~40 seconds to complete. Once complete, please follow **Step-3** again to verify the status.


**Step-5**: Force close the app and launch it again manually or follow these commands:

```
// Force close the app
adb shell am force-stop com.google.jetstream

// Launch the app
adb shell am start -n com.google.jetstream/com.google.jetstream.MainActivity
```

Now, you can observe the improvement in the app's startup and overall performance as perceived by the end user.



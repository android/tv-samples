# Android TV Accessibility Demo

## The purpose of the demo

The main purpose of the demo is 1) to show developers how to best use the
Accessibility APIs: provide a sample source code with basic accessibility
functionality. 2) to gather more consistent and standardized feedback: for developers to build their issue into the demo to show the technical bugs
to Google.

## Overview
The demo consist of five parts:
1. An activity built with standard Android components.
2. An activity built with a custom view which supports accessibility well with
    ExploreByTouchHelper.
3. An activity built with a custom view which supports accessibility well with
    AccessibilityNodeProvider and a layout information JSON file.
4. A simple activity built with a custom view that contains four custom
    components. That is the source code of the [best practice example on Android developer site](https://developer.android.com/training/tv/accessibility/non-native-best-practices)
5. An activity built with a web view and JavaScript code.

The basic function of the demo is to control the accessibility focus, the green
rectangle, to navigate on different views, like TextView, ImageView, RecycleView,
Custom View and WebView.

Please note, in the custom view samples, scrolling hasn't been implemented to
keep the sample easier to read.

##  Code structure:

*  app/src/main/java/com/sample/atva11ydemo/:
   * “model/”: defining the Movie and MovieList classes providing the simplest
        media data input resource.
   * “standard/”: defining activity built with standard Android components.
      * “MovieAdapter” is the adapter for RecycleView of Movie list.
      * “StandardViewActivity” defines the movie list view.
   * “custom/”: defining activity built with a custom view.
      * “bestpractices/”: defining best practices with simple examples
         * “CustomViewBestPracticeActivity”: defines the activity of best
                practices.
         * “SimpleCustomView”: draws four rectangles on the custom views.
         * “SimpleExploreByTouchHelper”: defines a simple ExploreByTouchHelper
                object to expose AccessibilityNodeInfo objects of those 4
                rectangles to Accessibility services. As a result, it make
                custom view support accessibility well.
      * “CustomViewWithAccessibilityNodeProviderActivity”: defines the activity
            which use AccessibilityNodeProvider to implement accessibility
            support.
      * “CustomViewWithExploreByTouchHelperActivity”: defines the activity
            which uses ExploreByTouchHelper to implement accessibility support.
            The difference between those two "CustomViewWith.* Activity" is that
            the value of "use_explore_by_touch_helper" in their layout file are
            different.
      * “FocusController”: defines the focus navigation logic under the
            non-accessibility mode.
      * “SampleAccessibilityNodeProvider”: extends AccessibilityNodeProvider
            class to support accessibility on custom views. For more details see
            below section “Instruction of Accessibility development”, and
            source code.
      * “SampleCustomView”:
         1) Draws the title and movie image on the screen;
         2) Interact with FocusControl to control the orange focus under
            non-accessibility mode.
      * “SampleExploreByTouchHelper”: extends ExploreByTouchHelper class to
            support accessibility on custom views. For more details see below
            section “Instruction of Accessibility development”, and source code.
   * “web/”: defining activity built with WebView/HTML:
      * “WebViewActivity”: initiates and assigns URL to WebView.
   * "MainActivity" defines the home page of the app. It defines five buttons to
         open different samples.
* app/src/main/asset/: Defines the web content.
   * “index.html”: defines HTML DOM and accessibility related attributes.
   * “model.js”: provides the same simplest movie source data as Android source
        code.
   * "custom_view_virtual_nodes.json": defines the required information of
        elements in custom views to make those support accessibility.

## Instructions for Accessibility development
1. Standard Android Component

Our developer documentation recommends that you [use standard Android components](https://developer.android.com/guide/topics/ui/accessibility).
as frequently as possible, because Android provides native accessibility support
for these components.

2. Custom view support

If your app is using custom views, you should create a new class that inherits
from ExploreByTouchHelper and then override its 4 methods in the [ExploreByTouchHelper](https://developer.android.com/reference/android/support/v4/widget/ExploreByTouchHelper#onpopulateeventforhost)
here:

```
  // Return the virtual view ID whose view is covered by the input point (x, y).
    **protected int getVirtualViewAt(float x, float y)**
  // Fill the virtual view ID list into the input parameter virutalViewIds.
    **protected void getVisibleVirtualViews(List<Integer> virtualViewIds)**
  // For the view whose virtualViewId is the input virtualViewId, populate the
  // accessibility node information into the AccessibilityNodeInfoCompat
  // parameter.
    **protected void onPopulateNodeForVirtualView(int virtualViewId,  @NonNull AccessibilityNodeInfoCompat node)**
  // Set the accessibility handling when perform action.
    **protected boolean onPerformActionForVirtualView(int virtualViewId, int action, @Nullable Bundle arguments)**
```

* What information should be stored into AccessibilityNodeInfo?

There are some values you have to set if you want to support accessibility
services well.
   1. setVisibleToUser() as true
   2. setClassName()
   3. setBoundsInScreen()
   4. setContentDescription() to declare the content announced by Talkback
   5. addAction() if want to perform action on the virtual node.

Another method is to use AccessibilityNodeProvider. It's equivalent to
ExploreByTouchHelper. Override the following method in the
[AccessibilityNodeProvider](https://developer.android.com/reference/android/view/accessibility/AccessibilityNodeProvider)

```
  // For the view whose virtualViewId is the input virtualViewId, create and
  // return an AccessibilityNodeInfo object with necessary accessibility
  // information
    **public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId)**
```

* Why do we need JSON file to expose accessibility information? Is that
necessary?

No, that's not necessary. We just provide an alternative approach to you to
transmit the information between custom view definition and
AccessibilityDelegate object.

The JSON file is used in the SampleAccessibilityNodeProvider.
It does not mean JSON file and AccessibilityNodeProvider are a fixed combination.
You can also use JSON file with ExploreByTouchHelper. That's up to you.

For more details, you can refer to the sample code, [developer doc](https://developer.android.com/training/tv/accessibility/non-native-app)
and [tech talk video](https://www.youtube.com/watch?v=ld7kZRpMGb8&feature=youtu.be&t=1196).

3. Web view support

In the demo, the web based app is only based on a single web page
with JavaScript code. The HTML file will be transferred into the
AccessibilityNode Tree by ChromeVox. What you should do is develop a web
accessibility web app. For more information refer to [Web Fundamentals Accessibility Doc](https://developers.google.com/web/fundamentals/accessibility).

## Expected Feedback Template

The demo is used as a medium to present developer’s bugs. When you send out the
issue on GitHub, please also update the demo source code containing
the bug you are struggling with. And also fill out the following feedback
template.

```
Android Build Version:
(Check that at Settings -> Devices -> About -> Build)


Talkback Version:
(If the issue is related to Talkback, check that at Settings ->
Devices Preferences > Accessibility -> TalkBack -> Configuration ->
See action bar on the screen)


Device model:
(Settings ->  Device Preferences -> About -> Model)


VERBOSE TALKBACK LOG
(If the issue is related to Talkback, please provide Android log or talkback log
following below steps)
1: Enable logs at Settings -> Accessibility -> TalkBack -> TalkBack Settings ->
Developer -> Log -> Verbose
2: Reproduce the problem
3: Capture logs by running "adb logcat -d *:V > my.log" , or with bug-report
tools.)


SCREENRECORD
(If possible, screen record video or take video by phone with remote controller
operation would always be welcome and helpful.)


PRE-CONDITIONS
(For example, Connected to the Internet, Turn Talkback on, Google account signed
in, Specific App installed etc. )


STEPS TO REPRODUCE
(This should be understandable years later, by someone who will never talk to
the reporter or assignee.
1:
2:
3: …)

WHETHER reproducible on Mobile devices? (YES / NO, If YES, please also provide
the reproduction steps when that is different from what on Android TV).


OBSERVED RESULTS


EXPECTED RESULTS
```

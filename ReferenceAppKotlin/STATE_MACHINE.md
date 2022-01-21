# State Machine

This sample uses a state machine to simplify the playback code. A common problem in apps is the
need to keep track of different states and trigger code based on changes to those states. A state
machine allows distinct pieces of code to listen to the states that they care about without having
any knowledge of how states are triggered or managed.

For example, here are some features that care about specific states:

* Errors - When there is a problem with playback such as a connection failure, an error should be
shown.
* Watch Progress - When a video is paused, the locally stored progress should be updated. When a
video is first prepared, the watch progress needs to be loaded to resume from the last watched
position.
* Watch Next - When a video is paused or completed, the Watch Next tiles should be updated.
* Video completion - When the user finishes watching a full video, playback should stop and the app
should return to the previous screen.

## Key Principles

State machines can easily become very complicated, so this app has a few key principles for the
state machine:

* Statelessness - The state machine informs listeners of changes to the state. It doesn't keep track
of the current state nor does it care what the previous state was.
* Immutability - The same state is sent to every listener and listeners cannot manipulate the
states.
* Decoupling of dependencies - Since the state machine is stateless and the same state is sent to
all listeners, there is no dependency between listeners. For example, the watch progress and Play
Next features may listen to the same events but they don't know about each other.

## Implementation

This sample represents each state via a [`VideoPlaybackState`][state-machine-code]. This is a Kotlin
sealed class that allows each state to be represented discretely with each of its required parts
(for example, an error state would want to include an error reason but the pause state wouldn't).
The state machine is represented with the `PlaybackStateMachine` interface.

Within the app code, the state machine is implemented by the `PlaybackViewModel`. It is responsible
for keeping references to the listeners and telling them about state changes. The view model was
chosen as the best place to maintain the state machine. The states are tightly coupled to playback
and thus the player. By using the view model, the states are decoupled from the fragment and
player's lifecycle events, which enables the view model to transform highly specific
Android/ExoPlayer details into the app's domain business logic. For testing, the
`FakePlaybackStateMachine` implements the interface and provides methods to verify states.

[state-machine-code]: app/src/main/java/com/android/tv/reference/playback/PlaybackStateMachine.kt

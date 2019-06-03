package android.support.v17.leanback.supportleanbackshowcase.app.room.di.androidinject;


import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.FragmentManager.FragmentLifecycleCallbacks;
import android.os.Bundle;
import android.support.v17.leanback.supportleanbackshowcase.app.room.controller.app.SampleApplication;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import dagger.android.AndroidInjection;
import dagger.android.HasFragmentInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;


/**
 * The automated dependency injector for android related dependency injection discussed in the
 * comments of {@link ActivityBuildersModule}.
 *
 * The injector should be initialized in the application class firstly (which means the application
 * will be injected by the App component and can return the DispatchAndroidInjector for further
 * processing.). Then it will set up a callback based on activity's lifecycle stage to process the
 * activity accordingly.
 *
 * When activity is created, (before the call of super.onCreate()) the dependency injection will
 * be performed immediately.
 */
public class AppInjector {

    public static void init(SampleApplication sampleApplication) {
        DaggerAppComponent.builder().application(sampleApplication).build()
                .inject(sampleApplication);

        sampleApplication.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                handleActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * The strategy for automated dependency injection is:
     *
     *   If this activity has implemented HasSupportFragmentInjector interfacea which means it has
     *   to return the DispatchInjector through overriding public AndroidInjector<Fragment>
     *   fragmentInjector() method to return the @Inject DispatchInjector. Then this activity must
     *   be processed with the AndroidInjection by calling the AndroidInjection.inject method.
     *
     *   If the activity is extended from the FragmentActivity, then we will set up the dependency
     *   injection for all the fragments by instrumenting the AndroidInjection.inject method at
     *   the beginning of fragment's creation. Since the fragment takes the main responsibility on
     *   UI rendering.
     *
     * @param activity activity to be processed.
     */
    private static void handleActivity(Activity activity) {

        // TODO: an injectable for activity separately
        if (activity instanceof HasSupportFragmentInjector) {

            // When the activity has implemented the HasSupportFragmentInjector interface, android
            // injector will inject into it, so the activity can be injected with the dispatch
            // injector specific for fragment. Then this dispatch injector will be returned through
            // the override method and contribute to android injector factory for fragment
            AndroidInjection.inject(activity);
        }
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            new FragmentManager.FragmentLifecycleCallbacks() {
                                @Override
                                public void onFragmentCreated(FragmentManager fm, Fragment f,
                                        Bundle savedInstanceState) {
                                    if (f instanceof Injectable) {

                                        // The injector only contains the provision method for the
                                        // field in the fragment.
                                        // But for activity, there will be a module providing the
                                        // binding method which will be bound to the fragment's
                                        // subcomponent's builder (Android injector builder) and
                                        // then be cast to AndroidInjector.Factory
                                        AndroidSupportInjection.inject(f);
                                    }
                                }
                            }, true);
        }
    }
}

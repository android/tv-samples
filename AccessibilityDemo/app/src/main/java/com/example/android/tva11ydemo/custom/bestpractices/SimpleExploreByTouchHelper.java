package com.example.android.tva11ydemo.custom.bestpractices;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.example.android.tva11ydemo.BuildConfig;
import com.example.android.tva11ydemo.custom.bestpractices.SimpleCustomView.VirtualRect;
import java.util.List;
import java.util.Objects;

/**
 * SimpleExploreByTouchHelper defines a simple ExploreByTouchHelper object to expose
 * AccessibilityNodeInfo objects of those 4 rectangles to Accessibility services. As a result, it
 * makes custom view support accessibility well.
 */
class SimpleExploreByTouchHelper extends ExploreByTouchHelper {

  private final String TAG = SimpleExploreByTouchHelper.class.getName();

  private SimpleCustomView parentView;

  SimpleExploreByTouchHelper(SimpleCustomView parentView) {
    super(parentView);
    this.parentView = parentView;
  }

  /**
   * Define AccessibilityNodeInfo objects with necessary attributes.
   * Set required attributes into the AccessibilityNodeInfo for the UI component whose virtualID is
   * the virtualViewId.
   * @param virtualViewId virtual ID for the component.
   * @param node The AccessibilityNodeInfoCompat used to set attribute here.
   */
  @Override
  protected void onPopulateNodeForVirtualView(
      int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {
    VirtualRect rectEle = parentView.virtualIdRectMap.get(virtualViewId);

    assert rectEle != null;
    node.setContentDescription("Selected rect " + rectEle.id);
    node.setClassName(ImageView.class.getName());
    node.setPackageName(BuildConfig.APPLICATION_ID);
    node.setVisibleToUser(true);
    node.addAction(AccessibilityActionCompat.ACTION_CLICK);
    // *****************************************************************
    // It's very important to set rectangle area for each node. As a
    // result the accessibility service knows where to set accessibility
    // focus on.
    //
    // setBoundsInParent() is a deprecated method, however ExploreByTouchHelper.createNodeForChild()
    // replies on the bounds in parent being set.
    // *****************************************************************
    node.setBoundsInParent(
        Objects.requireNonNull(parentView.virtualIdRectMap.get(
            virtualViewId)).rect);
  }

  @Override
  protected int getVirtualViewAt(float x, float y) {
    for (VirtualRect virtualRect : parentView.virtualIdRectMap.values()) {
      Rect rect = virtualRect.rect;
      if (rect.left <= x && x <= rect.right
          && rect.top <= y && y <= rect.bottom) {
        return virtualRect.id;
      }
    }
    return ExploreByTouchHelper.INVALID_ID;
  }

  @Override
  protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
    for (Integer virtualId : parentView.virtualIdRectMap.keySet()) {
      virtualViewIds.add(virtualId);
    }
  }

  @Override
  protected boolean onPerformActionForVirtualView(
      int virtualViewId, int action, @Nullable Bundle arguments) {
    switch (action) {
      // After turned Talkback on, if you want to handle
      // DPAD_CENTER key event with your own logic,
      // implement under ACTION_CLICK action.
      case AccessibilityNodeInfoCompat.ACTION_CLICK:
        Log.e(TAG, "ACTION_CLICK action handled here.");
        return true;
      // All the DPAD directional key will be handled by
      // Accessibility based on the AccessibilityNodeInfo Tree
      // you defined above.
      default:
        // fall through
    }

    return false;
  }
}
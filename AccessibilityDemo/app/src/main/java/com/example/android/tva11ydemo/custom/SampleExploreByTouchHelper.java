package com.example.android.tva11ydemo.custom;

import static java.util.Objects.requireNonNull;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.example.android.tva11ydemo.BuildConfig;
import com.example.android.tva11ydemo.custom.SampleCustomView.VirtualRect;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SampleExploreByTouchHelper is a sample class inherit from ExploreByTouchHelper.
 * The method will be invoked in the corresponding CustomView, in this case, SampleCustomView.
 *
 * Both of ExploreByTouchHelper and AccessibilityNodeProvider could be set in
 * View.setAccessibilityDeletgate() and take the same effect.
 */
class SampleExploreByTouchHelper extends ExploreByTouchHelper {

  private final SampleCustomView parentView;

  SampleExploreByTouchHelper(SampleCustomView parentView) {
    super(parentView);
    this.parentView = parentView;
  }

  /**
   * Define AccessibilityNodeInfo objects with necessary attributes.
   * Set required attributes into the AccessibilityNodeInfo for the UI component whose virtualID is
   * the virtualViewId.
   * There are some values you have to set if you want to support accessibility services well.
   *  1. setVisibleToUser() as true
   *  2. setClassName()
   *  3. setBoundsInScreen()
   *  4. setContentDescription() to declare the content announced by Talkback
   *  5. addAction() if want to perform action on the virtual node.
   * @param virtualViewId virtual ID for the virtual node.
   * @param node The AccessibilityNodeInfoCompat used to set attribute here.
   */
  @Override
  protected void onPopulateNodeForVirtualView(
      int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {
    VirtualRect rectEle;
    rectEle = parentView.virtualIdRectMap.get(virtualViewId);
    requireNonNull(rectEle).setAccessibilityNodeInfo(node);
    node.setPackageName(BuildConfig.APPLICATION_ID);
    node.setVisibleToUser(true);
    node.setScreenReaderFocusable(true);
    node.setEnabled(true);
    node.setFocusable(true);
    node.setImportantForAccessibility(true);
    node.addAction(AccessibilityActionCompat.ACTION_CLICK);
    // *****************************************************************
    // It's very important to set rectangle area for each node. As a
    // result the accessibility service know where to set accessibility
    // focus on.
    //
    // setBoundsInParent() is a deprecated method, however ExploreByTouchHelper.createNodeForChild()
    // reply on the bounds in parent being set.
    // *****************************************************************
    node.setBoundsInParent(
        Objects.requireNonNull(parentView.virtualIdRectMap.get(virtualViewId)).rect);
  }

  @Override
  protected int getVirtualViewAt(float x, float y) {
    final int index = getRectIndexAt(x, y);
    if (index > 0) {
      return index;
    }
    return ExploreByTouchHelper.INVALID_ID;
  }

  @Override
  protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
    for (Map.Entry<Integer, VirtualRect> mapElement :
        parentView.virtualIdRectMap.entrySet()) {
      virtualViewIds.add(mapElement.getKey());
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
        parentView.onRectClicked(virtualViewId);
        return true;
      // All the DPAD directional key will be handled by
      // Accessibility based on the AccessibilityNodeInfo Tree
      // you defined above.
      default:
    }

    return false;
  }

  /**
   * [Key method] To get the index of rectangle located at (x, y).
   * @param x The horizontal plx on screen
   * @param y The vertical plx on screen
   * @return The virtual ID of the UI component which cover the point (x, y).
   */
  private int getRectIndexAt(float x, float y) {
    for (Map.Entry<Integer, VirtualRect> mapElement :
        parentView.virtualIdRectMap.entrySet()) {
      Rect rect = mapElement.getValue().rect;
      if (rect.left <= x && x <= rect.right
          && rect.top <= y && y <= rect.bottom) {
        return mapElement.getValue().id;
      }
    }
    return -1;
  }
}
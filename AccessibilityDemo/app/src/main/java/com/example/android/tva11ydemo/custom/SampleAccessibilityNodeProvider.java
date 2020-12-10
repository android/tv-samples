package com.example.android.tva11ydemo.custom;

import static android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK;
import static java.util.Objects.*;

import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SampleAccessibilityNodeProvider is a sample class inherit from AccessibilityNodeProvider.
 * The method will be invoked in the corresponding CustomView, in this case, SampleCustomView.
 *
 * Both of ExploreByTouchHelper and AccessibilityNodeProvider could be set in
 * View.setAccessibilityDelegate() and take the same effect.
 *
 * In this class, it gets virtual node information, like class name, bounds, content
 * description from a JSON file, instead of from virtualIdRectMap field in parent class as we do
 * in SampleExploreByTouchHelper. We intend to do so and provide you alternative way to initiate
 * your custom view.
 *
 * During cross platforms development, we usually define the layout in another language and tune
 * source codes separately. Using JSON file would be a good intermediary for adapter.
 */
class SampleAccessibilityNodeProvider extends AccessibilityNodeProvider {
  private static final String TAG = SampleAccessibilityNodeProvider.class.getName();

  private final HashMap<Integer, JSONObject> jsonObjects;
  private final View host;
  private final JSONObject hostJsonObject;
  private final String JSON_CHILDREN_FIELDNAME = "children";
  private final String JSON_VIRTUALID_FIELDNAME = "virtual_id";
  private final String JSON_CLASSNAME_FIELDNAME = "class_name";
  private final String JSON_RECTANGLE_FIELDNAME = "rectangle";
  private final String JSON_CONTENTDESCRIPTION_FIELDNAME = "contentDescription";
  private final String JSON_LEFTTOPX_FIELDNAME = "left_top_x";
  private final String JSON_LEFTTOPY_FIELDNAME = "left_top_y";
  private final String JSON_RIGHTBOTTOMX_FIELDNAME = "right_bottom_x";
  private final String JSON_RIGHTBOTTOMY_FIELDNAME = "right_bottom_y";

  public SampleAccessibilityNodeProvider(View host, String json) {
    jsonObjects = new HashMap<>();
    this.host = host;
    try {
      hostJsonObject = new JSONObject(json);
      processJSONTree(hostJsonObject);
    } catch (JSONException e) {
      Log.e(TAG, "Failed to parse JSONObject from json string. ", e);
      throw new IllegalArgumentException("Failed to parse JSON string into Object. ", e);
    }
  }

  private void processJSONTree(JSONObject tree) throws JSONException {
    JSONArray jsonChildren = tree.getJSONArray(JSON_CHILDREN_FIELDNAME);
    jsonObjects.put(tree.getInt(JSON_VIRTUALID_FIELDNAME), tree);
    for (int i = 0; i < jsonChildren.length(); i++) {
      JSONObject jsonChild = jsonChildren.getJSONObject(i);
      processJSONTree(jsonChild);
    }
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
   * @param virtualViewId The virtual ID of the virtual node.
   * @return
   */
  @Override
  public AccessibilityNodeInfo createAccessibilityNodeInfo(
      int virtualViewId) {
    if (virtualViewId == HOST_VIEW_ID) {
      AccessibilityNodeInfo hostNode =
          AccessibilityNodeInfo.obtain(host);
      setAccessibilityNodeInfo(hostNode, hostJsonObject);
      // setVisibleToUser is IMPORTANT to make virtual node visible.
      hostNode.setVisibleToUser(true);
      return hostNode;
    } else {
      AccessibilityNodeInfo childNode = AccessibilityNodeInfo
          .obtain(host, virtualViewId);
      JSONObject childRoot = jsonObjects.get(virtualViewId);
      if (childRoot != null) {
        setAccessibilityNodeInfo(childNode, childRoot);
        childNode.setVisibleToUser(true);
        childNode.setEnabled(true);
        childNode.setImportantForAccessibility(true);
        childNode.addAction(ACTION_CLICK);
      }
      return childNode;
    }
  }

  private void setAccessibilityNodeInfo(AccessibilityNodeInfo node, JSONObject json) {
    try {
      JSONArray jsonChildren =
          json.getJSONArray(JSON_CHILDREN_FIELDNAME);
      for (int i = 0; i < jsonChildren.length(); i++) {
        JSONObject child = jsonChildren.getJSONObject(i);
        node.addChild(host,
            child.getInt(JSON_VIRTUALID_FIELDNAME));
      }
      JSONObject jsonFrame =
          json.getJSONObject(JSON_RECTANGLE_FIELDNAME);
      int rectX = jsonFrame.getInt(JSON_LEFTTOPX_FIELDNAME);
      int rectY = jsonFrame.getInt(JSON_LEFTTOPY_FIELDNAME);
      int rectX2 = jsonFrame.getInt(JSON_RIGHTBOTTOMX_FIELDNAME);
      int rectY2 = jsonFrame.getInt(JSON_RIGHTBOTTOMY_FIELDNAME);
      Rect rect = new Rect(rectX, rectY, rectX2, rectY2);
      node.setBoundsInScreen(rect);
      node.setClassName(
          json.getString(JSON_CLASSNAME_FIELDNAME));
      node.setContentDescription(
          json.getString(JSON_CONTENTDESCRIPTION_FIELDNAME));
    } catch (JSONException e) {
      Log.e(TAG, "Failed to read field from JSONObject. ", e);
      throw new IllegalArgumentException(
          "Cannot get JSON fields successfully with exception.", e);
    }
  }


  /**
   * Set action handler here to handle all kinds of ACTION events.
   * Please also set addAction() in the AccessibilityNodeInfo of the corresponding virtual node.
   * @param virtualViewId The virtual ID of the virtual node.
   * @param action The action to perform.
   * @param arguments Optional action arguments.
   * @return
   */
  @Override
  public boolean performAction(int virtualViewId, int action,
      Bundle arguments) {
    if (virtualViewId == HOST_VIEW_ID) {
      return host.performAccessibilityAction(action, arguments);
    }

    switch (action) {
      case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
        sendAccessibilityEvent(
            AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED,
            virtualViewId);
        Log.d(TAG,
            "Add logic to handle accessibility focus action here");
        return true;
      case AccessibilityNodeInfo.ACTION_CLICK:
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED,
            virtualViewId);
        Log.d(TAG, "Add logic to handle click action here");
        return true;
      default:
        return false;
    }
  }

  private void sendAccessibilityEvent(int eventType, int virtualViewId) {
    AccessibilityEvent event;
    event = AccessibilityEvent.obtain(eventType);
    event.setSource(host, virtualViewId);

    try {
      event.setClassName(
          requireNonNull(jsonObjects.get(virtualViewId)).getString(JSON_CLASSNAME_FIELDNAME));
    } catch (JSONException e) {
      Log.e(TAG,
          "Failed to read class_name field from JsonObject for the index "
              + virtualViewId
              + ", and get exception.", e);
    }
    event.setPackageName(host.getContext().getPackageName());

    host.getParent().requestSendAccessibilityEvent(host, event);
    host.invalidate();
  }
}

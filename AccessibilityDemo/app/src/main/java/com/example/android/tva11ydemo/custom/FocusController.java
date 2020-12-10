package com.example.android.tva11ydemo.custom;

import android.graphics.Point;

/**
 * FocusController defines the focus navigation logic under the non-accessibility mode.
 */
public class FocusController {
  private final Point prevRect = new Point();
  private final Point curRect = new Point();
  private final Point maxRect = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

  public boolean setMaximum(int maxX, int maxY) {
    if (maxX > 0 && maxY > 0) {
      maxRect.x = maxX;
      maxRect.y = maxY;
      return true;
    }
    return false;
  }

  public void storePreviousLocation() {
    prevRect.x = curRect.x;
    prevRect.y = curRect.y;
  }

  public void moveUp() {
    storePreviousLocation();
    if (curRect.x > 0) {
      curRect.x -= 1;
    }
  }

  public void moveDown() {
    storePreviousLocation();
    if (curRect.x < maxRect.x - 1) {
      curRect.x += 1;
    }
  }

  public void moveLeft() {
    storePreviousLocation();
    if (curRect.y > 0) {
      curRect.y -= 1;
    }
  }

  public void moveRight() {
    storePreviousLocation();
    if (curRect.y < maxRect.y - 1) {
      curRect.y += 1;
    }
  }

  public void updateFocus(int x, int y) {
    if (x < 0 || x > maxRect.x || y < 0 || y >= maxRect.y) {
      return;
    }
    storePreviousLocation();
    curRect.x = x;
    curRect.y = y;
  }

  public Point getCurrentPosition() {
    return curRect;
  }

  public Point getPreviousPosition() {
    return prevRect;
  }
}

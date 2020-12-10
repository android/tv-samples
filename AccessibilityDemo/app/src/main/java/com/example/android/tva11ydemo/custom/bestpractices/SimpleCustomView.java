package com.example.android.tva11ydemo.custom.bestpractices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.example.android.tva11ydemo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * SimpleCustomView draws four rectangle on the custom view.
 */
public class SimpleCustomView extends View {
  final Map<Integer, VirtualRect> virtualIdRectMap = new HashMap<>();

  private final int RECTANGLE_START_POINT_LEFT =
      getResources().getDimensionPixelSize(R.dimen.best_practice_rectangle_start_point_left);
  private final int RECTANGLE_START_POINT_TOP =
      getResources().getDimensionPixelSize(R.dimen.best_practice_rectangle_start_point_top);
  private final int RECTANGLE_WIDTH =
      getResources().getDimensionPixelSize(R.dimen.best_practice_rectangle_width);
  private final int RECTANGLE_HEIGHT =
      getResources().getDimensionPixelSize(R.dimen.best_practice_rectangle_height);
  private final int HORIZONTAL_SPACING =
      getResources().getDimensionPixelSize(R.dimen.best_practice_horizontal_spacing);
  private final int VERTICAL_SPACING =
      getResources().getDimensionPixelSize(R.dimen.best_practice_vertical_spacing);
  private final int RECTANGLE_COLOR = Color.argb(100, 0, 163, 245);

  private int rectangleId = 1;

  static class VirtualRect {
    final Rect rect;
    final Paint paint;
    final int id;

    VirtualRect(Rect rect, Paint paint, int id) {
      this.rect = rect;
      this.paint = paint;
      this.id = id;
    }
  }

  public SimpleCustomView(Context context) {
    super(context);
    init();
  }

  public SimpleCustomView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public SimpleCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initializeVirtualRectangles();

    ViewCompat.setAccessibilityDelegate(this,
        new SimpleExploreByTouchHelper(this));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    for (VirtualRect virtualRect : virtualIdRectMap.values()) {
      canvas.drawRect(virtualRect.rect, virtualRect.paint);
    }
  }

  private void initializeVirtualRectangles() {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(RECTANGLE_COLOR);
    Rect leftTopRectangle = new Rect();
    leftTopRectangle.left = RECTANGLE_START_POINT_LEFT;
    leftTopRectangle.right = leftTopRectangle.left + RECTANGLE_WIDTH;
    leftTopRectangle.top = RECTANGLE_START_POINT_TOP;
    leftTopRectangle.bottom = leftTopRectangle.top + RECTANGLE_HEIGHT;
    virtualIdRectMap.put(rectangleId, new VirtualRect(leftTopRectangle, paint, rectangleId++));

    Rect rightTopRectangle = new Rect();
    rightTopRectangle.left = leftTopRectangle.right + HORIZONTAL_SPACING;
    rightTopRectangle.right = rightTopRectangle.left + RECTANGLE_WIDTH;
    rightTopRectangle.top = leftTopRectangle.top;
    rightTopRectangle.bottom = rightTopRectangle.top + RECTANGLE_HEIGHT;
    virtualIdRectMap.put(rectangleId, new VirtualRect(rightTopRectangle, paint, rectangleId++));

    Rect leftBottomRectangle = new Rect();
    leftBottomRectangle.left = leftTopRectangle.left;
    leftBottomRectangle.right = leftBottomRectangle.left + RECTANGLE_WIDTH;
    leftBottomRectangle.top = leftTopRectangle.bottom + VERTICAL_SPACING;
    leftBottomRectangle.bottom = leftBottomRectangle.top + RECTANGLE_HEIGHT;
    virtualIdRectMap.put(rectangleId, new VirtualRect(leftBottomRectangle, paint, rectangleId++));

    Rect rightBottomRectangle = new Rect();
    rightBottomRectangle.left = leftBottomRectangle.right + HORIZONTAL_SPACING;
    rightBottomRectangle.right = rightBottomRectangle.left + RECTANGLE_WIDTH;
    rightBottomRectangle.top = leftBottomRectangle.top;
    rightBottomRectangle.bottom = rightBottomRectangle.top + RECTANGLE_HEIGHT;
    virtualIdRectMap.put(rectangleId, new VirtualRect(rightBottomRectangle, paint, rectangleId++));
  }
}
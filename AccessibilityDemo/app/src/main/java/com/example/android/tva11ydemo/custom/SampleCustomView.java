package com.example.android.tva11ydemo.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.example.android.tva11ydemo.BuildConfig;
import com.example.android.tva11ydemo.R;
import com.example.android.tva11ydemo.model.Movie;
import com.example.android.tva11ydemo.model.MovieList;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SampleCustomView is the sample view build with custom defined view. The "custom defined view" is
 * implement with Canvas and drawing APIs.
 *
 * With Talkback off, onKeyDown() method will handle the DPAD_KEY events.
 *
 * With Talkback on, there is no implementation to handle the DPAD_KEY events. But the
 * AccessibilityNodeInfo tree exposed by CustomAccessHelper, and then Accessibility Service will
 * handle the DPAD_KEY events.
 */
public class SampleCustomView extends View {
    private final float TEXT_SIZE = getResources().getDimensionPixelSize(R.dimen.text_size_sp);
    private final List<String> categories = MovieList.getCategories();

    private final List<Movie> movieList = MovieList.getMovieList();

    private static final String JSON_FILE_NAME = "custom_view_virtual_nodes.json";

    private final int gridHeight = getResources().getDimensionPixelSize(R.dimen.grid_item_height);
    private final int gridWidth = getResources().getDimensionPixelSize(R.dimen.grid_item_width);
    private final int gridHeightMargin =
        (int) getResources().getDimensionPixelSize(R.dimen.grid_height_margin);
    private final int gridWidthMargin =
        (int) getResources().getDimensionPixelSize(R.dimen.grid_width_margin);
    private final int titleHeight = getResources().getDimensionPixelSize(R.dimen.title_height);
    private final int titleMargin = getResources().getDimensionPixelSize(R.dimen.title_margin);
    private final int textBoundsVerticalOffset =
        getResources().getDimensionPixelSize(R.dimen.text_bounds_vertical_offset);
    private final int textBoundsHorizontalOffset=
        getResources().getDimensionPixelSize(R.dimen.text_bounds_horizontal_offset);
    private final int rectBoundsHorizontalOffset =
        getResources().getDimensionPixelSize(R.dimen.rect_bounds_horizontal_offset);
    private int rectColor;
    private int rectSelectedColor;
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SampleExploreByTouchHelper customAccessHelper;

    // The 2-D array rectList stores all focusable elements in the view.
    final Map<Integer, VirtualRect> virtualIdRectMap = new HashMap<>();
    private ArrayList<ArrayList<VirtualRect>> rectList;
    private final FocusController focusController = new FocusController();
    private int nextItemId = 0;

    public SampleCustomView(Context context) {
        super(context);
        init(null);
    }

    public SampleCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SampleCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        rectList = new ArrayList<>();
        focusController.setMaximum(categories.size(), movieList.size());
        textPaint.setColor(Color.TRANSPARENT);
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(TEXT_SIZE);

        customAccessHelper = new SampleExploreByTouchHelper(this);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SampleCustomView);

        // setAccessibilityDelegate() is the important method to implement. We can use two methods
        // to set the proper AccessibilityDelegate.
        if (ta.getBoolean(R.styleable.SampleCustomView_use_explore_by_touch_helper, true)) {
            // Option 1: Use ExploreByTouchHelper
            ViewCompat.setAccessibilityDelegate(this, customAccessHelper);
        } else {
            // Option 2: Use AccessibilityNodeProvider.
            setAccessibilityDelegate(new AccessibilityDelegate() {
                @Override
                public AccessibilityNodeProvider getAccessibilityNodeProvider(View host) {
                    return new SampleAccessibilityNodeProvider(host, loadJSONFromAsset());
                }
            });
        }
        ta.recycle();

        parseAttrs(attrs);
        populateMovieData();
        focusController.updateFocus(0,0);
        updateHighlight();
    }

    private void parseAttrs(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.SampleCustomView);
        rectColor = ta.getColor(R.styleable.SampleCustomView_rect_color, Color.GREEN);
        rectSelectedColor =
            ta.getColor(R.styleable.SampleCustomView_rect_selected_color, Color.RED);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Map.Entry<Integer, VirtualRect> mapElement :
            virtualIdRectMap.entrySet()) {
            VirtualRect virtualRect = mapElement.getValue();
            virtualRect.draw(canvas);
        }
    }

    /**
     * onKeyDown works only for normal mode. When turned Talkback on, this method cannot be invoked,
     * because Talkback take over the key event handling.
     * @param keyCode Key code received.
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = false;

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                focusController.moveDown();
                handled = true;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                focusController.moveUp();
                handled = true;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                focusController.moveLeft();
                handled = true;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                focusController.moveRight();
                handled = true;
                break;
            default:
                // fall through
                break;
        }

        if (handled) {
            updateHighlight();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Set the AccessibilityNodeInfo for the host view, SampleCustomView.
     * @param info The AccessibilityNodeInfo object set for the view.
     */
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setPackageName(BuildConfig.APPLICATION_ID);
        info.setVisibleToUser(true);
        info.setClassName(this.getClass().getName());
        info.setEnabled(true);
        info.setFocusable(false);
        info.setFocused(false);
        info.setImportantForAccessibility(false);
    }

    private void populateMovieData() {
        for (int i = 0; i < categories.size(); ++i) {
            for (int j = 0; j < movieList.size(); ++j) {
                recordMovieData(i, j);
            }
        }
    }

    private void recordMovieData(int indexX, int indexY) {
        Paint paint;
        int posX = indexY * gridWidth + indexY * gridWidthMargin;
        int posY = rectBoundsHorizontalOffset +
            indexX * (gridHeight + gridHeightMargin + titleHeight + titleMargin);

        // Draw title
        if (indexY == 0) {
            String category = categories.get(indexX);

            // Get text bounds rectangle.
            Rect textRect = new Rect();
            Rect textBoundsRect = new Rect();
            textPaint.getTextBounds(
                category, 0, category.length(), textRect);
            textBoundsRect.left = posX;
            textBoundsRect.right =
                textBoundsRect.left
                    + textRect.width()
                    + textBoundsVerticalOffset;
            textBoundsRect.bottom = posY + textBoundsHorizontalOffset;
            textBoundsRect.top = textBoundsRect.bottom - textRect.height();
            recordTitleRect(textBoundsRect, textPaint, category);
        }
        // Draw Rect
        Rect rect;
        if (rectList.size() > indexX && rectList.get(indexX).size() > indexY) {
            paint = rectList.get(indexX).get(indexY).paint;
            rect = rectList.get(indexX).get(indexY).rect;
        } else {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(rectColor);
            rect = new Rect();
            rect.left = posX;
            rect.right = rect.left + gridWidth;
            rect.top = posY + titleHeight + titleMargin;
            rect.bottom = rect.top + gridHeight;
        }
        recordDrawedRect(rect, paint, indexX, indexY);
    }

    // Helper method to record the Title rect into map.
    private void recordTitleRect(Rect rect, Paint paint, String text) {
        TextVirtualRect curRect =
            new TextVirtualRect(rect, paint, nextItemId, -1, -1, text);
        virtualIdRectMap.put(nextItemId, curRect);
        nextItemId++;
    }

    // Helper method to record the Drawed rect into map.
    private void recordDrawedRect(Rect rect, Paint paint,
        int indexX, int indexY) {
        if (indexX >= rectList.size()) {
            rectList.add(new ArrayList<VirtualRect>());
        }
        ImageVirtualRect curRect =
            new ImageVirtualRect(rect, paint, nextItemId, indexX, indexY);
        rectList.get(indexX).add(curRect);
        virtualIdRectMap.put(nextItemId, curRect);
        nextItemId++;
    }

    // Helper method to update the focus selected rectangle on draw Rect
    private void updateHighlight() {
        int curX = focusController.getCurrentPosition().x;
        int curY = focusController.getCurrentPosition().y;
        int preX = focusController.getPreviousPosition().x;
        int preY = focusController.getPreviousPosition().y;

        // Update rectangle focus previously on.
        rectList.get(preX).get(preY).paint.setColor(rectColor);

        // Update rectangle focus currently on.
        rectList.get(curX).get(curY).paint.setColor(rectSelectedColor);
        invalidate();
    }

    // Click handling to highlight the focused element.
    void onRectClicked(int virtualId) {
        VirtualRect rectEle = virtualIdRectMap.get(virtualId);
        if (rectEle == null || (rectEle.x < 0 && rectEle.y < 0)) {
            return;
        }
        focusController.updateFocus(rectEle.x, rectEle.y);
        updateHighlight();
        if (customAccessHelper != null) {
            customAccessHelper.sendEventForVirtualView(
                virtualId, AccessibilityEvent.TYPE_VIEW_CLICKED);
        }
    }

    // VirtualRect is a structure to store element info for creating
    // AccessibilityNodeInfo.
    static abstract class VirtualRect {
        Rect rect;
        Paint paint;
        final int id;
        final int x;
        final int y;

        VirtualRect(Rect rect, Paint paint, int id, int x, int y) {
            this.rect = rect;
            this.paint = paint;
            this.id = id;
            this.x = x;
            this.y = y;
        }

        abstract void draw(Canvas canvas);

        abstract void setAccessibilityNodeInfo(@NonNull AccessibilityNodeInfoCompat node);
    }

    static class ImageVirtualRect extends VirtualRect {
        ImageVirtualRect(Rect rect, Paint paint, int id, int x, int y) {
            super(rect, paint, id, x, y);
        }

        @Override
        void draw(Canvas canvas) {
            canvas.drawRect(rect, paint);
        }

        @Override
        void setAccessibilityNodeInfo(@NonNull AccessibilityNodeInfoCompat node) {
                node.setContentDescription(
                    "Selected rect "
                        + id
                        + " at ("
                        + x
                        + ", "
                        + y
                        + ")");
                node.setClassName(ImageView.class.getName());
        }
    }

    static class TextVirtualRect extends VirtualRect {
        final String text;

        TextVirtualRect(Rect rect, Paint paint, int id,
            int x, int y, String text) {
            super(rect, paint, id, x, y);
            this.text = text;
        }

        @Override
        void draw(Canvas canvas) {
            canvas.drawText(text, rect.left, rect.bottom, paint);
        }

        @Override
        void setAccessibilityNodeInfo(@NonNull AccessibilityNodeInfoCompat node) {
            node.setContentDescription(text);
            node.setClassName(TextView.class.getName());
        }
    }

    // Load JSON string from file
    private String loadJSONFromAsset() {
        String json = null;
        try (InputStream is = getContext().getAssets().open(SampleCustomView.JSON_FILE_NAME)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            if (is.read(buffer) >= 0) {
                json = new String(buffer, StandardCharsets.UTF_8);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // no-op
        return json;
    }
}

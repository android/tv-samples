/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.leanback.leanbackshowcase.app.room.adapter;

import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListAdapter<T> extends ObjectAdapter {

    // For debugging purpose
    private final static String TAG =  "ListAdapter";
    private final static Boolean DEBUG = false;

    private List<T> mItems = new ArrayList<>();

    // Un modifiable version of mItems
    private List<T> mUnmodifiableItems;

    /**
     * Constructs an adapter with the given {@link PresenterSelector}.
     */
    public ListAdapter(PresenterSelector presenterSelector) {
        super(presenterSelector);
    }

    /**
     * Constructs an adapter that uses the given {@link Presenter} for all items.
     */
    public ListAdapter(Presenter presenter) {
        super(presenter);
    }

    /**
     * Constructs an adapter.
     */
    public ListAdapter() {
        super();
    }


    public int size() {
        return mItems.size();
    }

    @Override
    public T get(int position) {
        return mItems.get(position);
    }

    /**
     * Returns the index for the first occurrence of item in the adapter, or -1 if
     * not found.
     *
     * @param item The item to find in the list.
     * @return Index of the first occurrence of the item in the adapter, or -1
     * if not found.
     */
    public int indexOf(T item) {
        return mItems.indexOf(item);
    }

    /**
     * Adds an item to the end of the adapter.
     *
     * @param item The item to add to the end of the adapter.
     */
    public void add(T item) {
        add(mItems.size(), item);
    }

    /**
     * Inserts an item into this adapter at the specified index.
     * If the index is > {@link #size} an exception will be thrown.
     *
     * @param index The index at which the item should be inserted.
     * @param item  The item to insert into the adapter.
     */
    public void add(int index, T item) {
        mItems.add(index, item);
        notifyItemRangeInserted(index, 1);
    }

    /**
     * Adds the objects in the given collection to the adapter, starting at the
     * given index.  If the index is >= {@link #size} an exception will be thrown.
     *
     * @param index The index at which the items should be inserted.
     * @param items A {@link Collection} of items to insert.
     */
    public void addAll(int index, Collection items) {
        int itemsCount = items.size();
        if (itemsCount == 0) {
            return;
        }
        mItems.addAll(index, items);
        notifyItemRangeInserted(index, itemsCount);
    }

    /**
     * Removes the first occurrence of the given item from the adapter.
     *
     * @param item The item to remove from the adapter.
     * @return True if the item was found and thus removed from the adapter.
     */
    public boolean remove(T item) {
        int index = mItems.indexOf(item);
        if (index >= 0) {
            mItems.remove(index);
            notifyItemRangeRemoved(index, 1);
        }
        return index >= 0;
    }


    /**
     * Replaces item at position with a new item and calls notifyItemRangeChanged()
     * at the given position.  Note that this method does not compare new item to
     * existing item.
     *
     * @param position The index of item to replace.
     * @param item     The new item to be placed at given position.
     */
    public void replace(int position, T item) {
        mItems.set(position, item);
        notifyItemRangeChanged(position, 1);
    }

    /**
     * Removes a range of items from the adapter. The range is specified by giving
     * the starting position and the number of elements to remove.
     *
     * @param position The index of the first item to remove.
     * @param count    The number of items to remove.
     * @return The number of items removed.
     */
    public int removeItems(int position, int count) {
        int itemsToRemove = Math.min(count, mItems.size() - position);
        if (itemsToRemove <= 0) {
            return 0;
        }

        for (int i = 0; i < itemsToRemove; i++) {
            mItems.remove(position);
        }
        notifyItemRangeRemoved(position, itemsToRemove);
        return itemsToRemove;
    }

    /**
     * Removes all items from this adapter, leaving it empty.
     */
    public void clear() {
        int itemCount = mItems.size();
        if (itemCount == 0) {
            return;
        }
        mItems.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    /**
     * Gets a read-only view of the list of object of this ArrayObjectAdapter.
     */
    public <E extends T> List<T> unmodifiableList() {
        if (mUnmodifiableItems == null) {
            mUnmodifiableItems = Collections.unmodifiableList(mItems);
        }
        return mUnmodifiableItems;
    }

    @Override
    public boolean isImmediateNotifySupported() {
        return true;
    }

    /**
     * Set a new item list to adapter. The Diffutil will compute the difference and dispatch it to
     * according position
     *
     * @param itemList              List of new Items
     * @param sameItemComparator    The comparator to determine if two item are same or not
     * @param sameContentComparator The comparator to determin if two item's content are same or not
     */
    public void setItems(final List<T> itemList, final Comparator<T> sameItemComparator,
                         final Comparator<T> sameContentComparator) {
        if (DEBUG) {
            Log.e(TAG, "new items: " + itemList);
            Log.e(TAG, "old items: " + mItems);
        }

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mItems.size();
            }

            @Override
            public int getNewListSize() {
                return itemList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return sameItemComparator.compare(mItems.get(oldItemPosition),
                        itemList.get(newItemPosition)) == 0;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return sameContentComparator.compare(mItems.get(oldItemPosition),
                        itemList.get(newItemPosition)) == 0;
            }
        });

        mItems.clear();
        mItems.addAll(itemList);

        result.dispatchUpdatesTo(new ListUpdateCallback() {

            @Override
            public void onInserted(int position, int count) {
                if (DEBUG) {
                    Log.e(TAG, "onInserted: ");
                }
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                if (DEBUG) {
                    Log.e(TAG, "onRemoed: ");
                }
                notifyItemRangeRemoved(position, count);
            }

            /**
             * Currently Leanback support library doesn't support move operation, use item range
             * changed method to replace it as a temporal solution.
             * @param fromPosition from position.
             * @param toPosition to position.
             */
            @Override
            public void onMoved(int fromPosition, int toPosition) {
                if (DEBUG){
                    Log.e(TAG, "onMoved: ");
                }
                notifyItemRangeChanged(fromPosition, toPosition - fromPosition + 1);
            }

            @Override
            public void onChanged(int position, int count, Object payload) {
                if (DEBUG) {
                    Log.e(TAG, "onChanged: ");
                }

                // the support for payload has not been added to leanback support library, just
                // ignore it currently.
                notifyItemRangeChanged(position, count);
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }

        ListAdapter<?> that = (ListAdapter<?>) o;

        return mItems != null ? mItems.equals(that.mItems) : that.mItems == null;
    }


    @Override
    public int hashCode() {
        return mItems != null ? mItems.hashCode() : 0;
    }
}

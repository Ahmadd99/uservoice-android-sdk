package com.uservoice.uservoicesdk.compatibility;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.uservoice.uservoicesdk.R;
import com.uservoice.uservoicesdk.activity.BaseActivity;

/**
 * <em>Copy from Android source to enable {@link Fragment} support.</em>
 *
 * @see ListActivity
 */
public abstract class FragmentListActivity extends BaseActivity {

    // changed to private as original suggested
    private ListAdapter mAdapter;
    // changed to private as original suggested
    private ListView mList;

    private Handler mHandler = new Handler();
    private boolean mFinishedStart = false;

    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };

    /**
     * This method will be called when an item in the list is selected.
     * Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the data
     * associated with the selected item.
     *
     * @param l        The ListView where the click happened
     * @param v        The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }

    /**
     * Ensures the list view has been created before Activity restores all of
     * the view states.
     *
     * @see Activity#onRestoreInstanceState(Bundle)
     */
    @Override
    protected void onRestoreInstanceState(Bundle state) {
        ensureList();
        super.onRestoreInstanceState(state);
    }


    /**
     * Provide the cursor for the list view.
     */
    public void setListAdapter(ListAdapter adapter) {
        synchronized (this) {
            ensureList();
            mAdapter = adapter;
            mList.setAdapter(adapter);
        }
    }

    /**
     * Set the currently selected list item to the specified position with the
     * adapter's data
     *
     * @param position
     */
    public void setSelection(int position) {
        mList.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        return mList.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        return mList.getSelectedItemId();
    }

    /**
     * Get the activity's list view widget.
     */
    public ListView getListView() {
        ensureList();
        return mList;
    }

    /**
     * Get the ListAdapter associated with this activity's ListView.
     */
    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    private synchronized void ensureList() {
        if (mList != null)
            return;

        mList = new ListView(this);
        mList.setId(android.R.id.list);
        ViewFlipper viewFlipper = new ViewFlipper(this);
        viewFlipper.setId(R.id.uv_view_flipper);
        viewFlipper.addView(mList);
        setContentView(viewFlipper);
        mList.setOnItemClickListener(mOnClickListener);
        if (mFinishedStart) {
            setListAdapter(mAdapter);
        }
        mHandler.post(mRequestFocus);
        mFinishedStart = true;
    }

    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((ListView) parent, v, position, id);
        }
    };
}

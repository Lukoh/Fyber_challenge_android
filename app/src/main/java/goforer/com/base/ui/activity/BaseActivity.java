/*
 * Copyright (C) 2015-2016 Lukoh Nam, goForer
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

package goforer.com.base.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import goforer.com.fyber_challenge_android.R;
import goforer.com.fyber_challenge_android.model.action.FinishAction;
import goforer.com.fyber_challenge_android.model.event.ActivityStackClearEvent;
import goforer.com.fyber_challenge_android.utility.ConnectionUtils;

/**
 * Base class for activities that want to use the support-based {@link AppCompatActivity}
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private boolean mIsResumed = false;

    public static Activity mCurrentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (savedInstanceState == null) {
            setEffectIn();
        }

        setActionBar();
        setContentView();
        bindViews();

        if (ConnectionUtils.INSTANCE.isNetworkAvailable(this)) {
            setViews();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mIsResumed = true;
        mCurrentActivity = this;

    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsResumed = false;

    }

    /**
     * Return true if this activity is resumed
     *
     * @return true if this activity is resumed
     */
    public boolean resumed() {
        return mIsResumed;
    }

    /**
     * Initialize the ActionBar and set options into it.
     *
     * @see ActionBar
     */
    protected void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        }
    }

    /**
     * Set the activity content from a layout resource.  The resource will be
     * inflated, adding all top-level views to the activity.
     * <p>
     * All activity must implement this method to get the resource inflated like below example:
     *
     * Example :
     * @@Override
     * public void setContentView() {
     * setContentView(R.layout.activity_gallery);
     * }
     * </p>
     *
     * @see #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    protected abstract void setContentView();

    /**
     * Inject annotated fields and methods in the specified target {@link Activity} for field
     * injection. The current content view is used as the view root.
     *
     * @see ButterKnife#bind(Activity target)
     */
    protected void bindViews() {
        ButterKnife.bind(this);
    }

    /**
     * Initialize all views to set into the activity.
     * <p>
     * The activity which has no Fragment must override this method to set all views
     * into the activity.
     * </p>
     *
     */
    protected void setViews() {
    }

    /**
     * Set the effect when the activity is starting.
     *
     * See {@link Activity#overridePendingTransition(int enterAnim, int exitAnim)}.
     */
    protected void setEffectIn() {
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.scale_down_exit);
    }

    /**
     * Set the effect when the activity is closing.
     *
     * See {@link Activity#overridePendingTransition(int enterAnim, int exitAnim)}.
     */
    protected void setEffectOut() {
        overridePendingTransition(R.anim.scale_up_enter, R.anim.slide_out_to_right);
    }

    @Override
    public void finish() {
        super.finish();
        setEffectOut();
    }

    /**
     * Transact an existing fragment that was added to a container.
     *
     * @param cls the component class that is to be used for BaseActivity
     * @param containerViewId Identifier of the container whose fragment(s) are to be replaced.
     * @param args Bundle of arguments to supply to the fragment
     */
    protected void transactFragment(Class<?> cls, @IdRes int containerViewId, Bundle args) {
        transactFragment(cls.getName(), containerViewId, args);
    }

    /**
     * Transact an existing fragment that was added to a container.
     *
     * @param tag Optional tag name for the fragment
     * @param containerViewId Identifier of the container whose fragment(s) are to be replaced.
     * @param args Bundle of arguments to supply to the fragment
     */
    protected void transactFragment(String tag, @IdRes int containerViewId, Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = Fragment.instantiate(this, tag, args);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(containerViewId, fragment, tag);
        ft.commit();
    }

    /**
     * Return previously set Fragment with given the component class.
     *
     * @param cls The previously set the component class that is to be used for BaseActivity.
     *
     * @return The previously set Fragment
     */
    protected Fragment getFragment(Class<?> cls) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(cls.getName());
    }

    /**
     * Return previously set Fragment with given the tag.
     *
     * @param tag The previously set the component class tag that is to be used for BaseActivity.
     *
     * @return The previously set Fragment
     */
    protected Fragment getFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(tag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Show a loading progress with given string
     *
     * @param stringResId the string resource ID
     */
    public void showProgress(int stringResId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (stringResId <= 0) {
            stringResId = R.string.loading;
        }
        mProgressDialog.setMessage(getString(stringResId));
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Dismiss a loading progress, removing it from the screen. This method can be invoked safely
     * from any thread.
     */
    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivityStackClearEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(FinishAction action){
        finish();
    }
}

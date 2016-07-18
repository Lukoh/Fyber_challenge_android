/*
 * Copyright (C) 2016 Lukoh Nam, goForer
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

package com.goforer.fyber_challenge_android.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.goforer.base.ui.activity.BaseActivity;
import com.goforer.fyber_challenge_android.R;
import com.goforer.fyber_challenge_android.model.action.FinishAction;
import com.goforer.fyber_challenge_android.model.action.MoveItemAction;
import com.goforer.fyber_challenge_android.model.action.SelectAction;
import com.goforer.fyber_challenge_android.model.data.Profile;
import com.goforer.fyber_challenge_android.ui.fragment.OfferGridFragment;
import com.goforer.fyber_challenge_android.ui.fragment.OfferListFragment;
import com.goforer.fyber_challenge_android.utility.ActivityCaller;
import com.goforer.fyber_challenge_android.utility.CommonUtils;
import com.goforer.fyber_challenge_android.utility.ConnectionUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;

public class OffersActivity extends BaseActivity {
    private Profile mProfile;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_notice)
    TextView mNoticeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new GetGAIDTask(this).execute();

        mProfile = getIntent().getExtras().getParcelable(ActivityCaller.EXTRA_PROFILE);

        super.onCreate(savedInstanceState);

        if (!ConnectionUtils.INSTANCE.isNetworkAvailable(getApplicationContext())) {
            mNoticeText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void setViews(Bundle savedInstanceState) {
        transactFragment(OfferListFragment.class, R.id.content_holder, null);
    }

    @Override
    protected void setActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            actionBar.setTitle(getResources().getString(R.string.app_name));
            actionBar.setElevation(0);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragment_offer_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.view_list:
                transactFragment(OfferListFragment.class, R.id.content_holder, null);
                return true;
            case R.id.view_grid:
                transactFragment(OfferGridFragment.class, R.id.content_holder, null);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    protected void setEffectIn() {
        overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.scale_down_exit);
    }

    @Override
    protected void setEffectOut() {
        overridePendingTransition(R.anim.scale_up_enter, R.anim.slide_out_to_bottom);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_base);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ActivityCaller.SELECTED_ITEM_POSITION:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra(
                            ActivityCaller.EXTRA_SELECTED_ITEM_POSITION, -1);
                    if (position != -1) {
                        MoveItemAction action = new MoveItemAction();
                        action.setPosition(position);
                        EventBus.getDefault().post(action);
                    }
                }
                break;
            default:
        }
    }

    public Profile getProfile() {
        return mProfile;
    }

    public void showDialog(FinishAction action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(action.getCode());
        builder.setMessage(action.getMessage() + "\n"
                + getResources().getString(R.string.restart_phase));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
                finish();
            }
        });

        builder.show();
    }

    private static class GetGAIDTask extends AsyncTask<String, Integer, String> {
        private OffersActivity mActivity;
        private WeakReference<OffersActivity> mFragmentWeakRef;

        public GetGAIDTask(OffersActivity activity) {
            mActivity = activity;
            mFragmentWeakRef = new WeakReference<>(activity);
        }

        @Override
        protected String doInBackground(String... strings) {
            AdvertisingIdClient.Info adInfo;
            adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                        mActivity.getApplicationContext());
                if (adInfo.isLimitAdTrackingEnabled()) {
                    CommonUtils.setLimitAdTrackingEnabled(true);
                    return "Not found GAID";
                }

                CommonUtils.setLimitAdTrackingEnabled(false);
            } catch (IOException | GooglePlayServicesNotAvailableException
                    | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }

            return adInfo != null ? adInfo.getId() : null;
        }

        @Override
        protected void onPostExecute(String id) {
            OffersActivity activity = mFragmentWeakRef.get();
            if (activity != null) {
                CommonUtils.setGoogleAID(id.trim().replaceAll("-", ""));
            }
        }
    }

    @SuppressWarnings("")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAction(SelectAction action) {
        ActivityCaller.INSTANCE.callInfo(this, action.getOffersList(), action.getPosition(),
                ActivityCaller.FROM_OFFERS_LIST, ActivityCaller.SELECTED_ITEM_POSITION);
    }
}

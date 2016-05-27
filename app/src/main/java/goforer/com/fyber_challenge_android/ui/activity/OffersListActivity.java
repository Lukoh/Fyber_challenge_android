package goforer.com.fyber_challenge_android.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import goforer.com.base.ui.activity.BaseActivity;
import goforer.com.fyber_challenge_android.R;
import goforer.com.fyber_challenge_android.ui.fragment.OfferListFragment;
import goforer.com.fyber_challenge_android.utility.CommonUtils;
import goforer.com.fyber_challenge_android.utility.ConnectionUtils;

/**
 * Created by lukohnam on 16. 5. 24..
 */
public class OffersListActivity extends BaseActivity {
    private static final String TAG = "OffersListActivity";

    @BindView(R.id.fam_menu)
    FloatingActionMenu mMenu;
    @BindView(R.id.tv_notice)
    TextView mNoticeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetGAIDTask().execute();

        if (!ConnectionUtils.INSTANCE.isNetworkAvailable(this)) {
            mNoticeText.setVisibility(View.VISIBLE);
        }

        mMenu.showMenuButton(true);
        mMenu.setClosedOnTouchOutside(true);
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
            actionBar.setTitle(getResources().getString(R.string.app_name));
            actionBar.setElevation(0);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("")
    @OnClick(R.id.fam_menu)
    void onMenuToggle() {
        mMenu.toggle(true);
    }

    @SuppressWarnings("")
    @OnClick(R.id.fab_offer)
    void onViewOffers() {
        transactFragment(OfferListFragment.class, R.id.content_holder, null);
    }

    private class GetGAIDTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            AdvertisingIdClient.Info adInfo;
            adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                        OffersListActivity.this.getApplicationContext());
                if (adInfo.isLimitAdTrackingEnabled()) {
                    CommonUtils.setLimitAdTrackingEnabled(true);
                    return "Not found GAID";
                }

                CommonUtils.setLimitAdTrackingEnabled(false);
            } catch (IOException | GooglePlayServicesNotAvailableException
                    | GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }

            return adInfo.getId();
        }

        @Override
        protected void onPostExecute(String id) {
            CommonUtils.setGoogleAID(id.trim().replaceAll("-", ""));
        }
    }
}

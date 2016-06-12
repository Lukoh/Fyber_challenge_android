package com.goforer.fyber_challenge_android.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.goforer.fyber_challenge_android.model.data.Offers;
import com.goforer.fyber_challenge_android.ui.fragment.OffersInfoFragment;

import java.util.List;

public class OffersInfoAdapter extends FragmentStatePagerAdapter {
    private List<Offers> mOffersItems;

    public OffersInfoAdapter(FragmentManager fm, List<Offers> offersItems) {
        super(fm);

        mOffersItems = offersItems;
    }

    @Override
    public Fragment getItem(int position) {
        return OffersInfoFragment.newInstance(mOffersItems, position );
    }

    @Override
    public int getCount() {
        return mOffersItems.size();
    }
}
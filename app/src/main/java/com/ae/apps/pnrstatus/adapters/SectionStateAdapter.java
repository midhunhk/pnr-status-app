package com.ae.apps.pnrstatus.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ae.apps.pnrstatus.fragments.AboutFragment;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;

public class SectionStateAdapter extends FragmentStateAdapter {

    private final Fragment pnrStatusFragment;
    private final Fragment aboutFragment;

    public SectionStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        pnrStatusFragment = new PnrStatusFragment();
        aboutFragment = new AboutFragment();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = pnrStatusFragment;
        } else if (position == 1) {
            fragment = aboutFragment;
        } else {
            fragment = aboutFragment;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

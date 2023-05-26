package com.threestepdare.assessment.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.threestepdare.assessment.Helpers.PreferenceHelper;
import com.threestepdare.assessment.R;
import com.threestepdare.assessment.Adapters.TabPagerAdapter;

public class DisplayDetailFragment extends Fragment {

    PreferenceHelper preferenceHelper;
    TabPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_detail, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getContext() != null){

            TabLayout tabLayout = view.findViewById(R.id.tab_layout);
            ViewPager viewPager = view.findViewById(R.id.view_pager);

            preferenceHelper = new PreferenceHelper(getContext());

            pagerAdapter = new TabPagerAdapter(getChildFragmentManager());

            pagerAdapter.addFragment(new FirstTabFragment(), "Item one");
            pagerAdapter.addFragment(new DetailsFragment(), "Item two");
            pagerAdapter.addFragment(new ThirdTabFragment(), "Item three");

            viewPager.setAdapter(pagerAdapter);

            viewPager.setCurrentItem(1);

            tabLayout.setupWithViewPager(viewPager);

            // Set the tab view
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(R.layout.tab_item);
                    View tabView = tab.getCustomView();
                    TextView tabTextView = null;
                    if (tabView != null) {
                        tabTextView = tabView.findViewById(R.id.tab_text);
                    }

                    if (tabTextView != null) {
                        tabTextView.setText(pagerAdapter.getPageTitle(i));
                    }
                }
            }

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View tabView = tab.getCustomView();
                    if (tabView != null) {
                        TextView tabTextView = tabView.findViewById(R.id.tab_text);
                        tabTextView.setTextColor(getResources().getColor(R.color.black));
                        tabView.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.bg_selected_tab));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View tabView = tab.getCustomView();
                    if (tabView != null) {
                        TextView tabTextView = tabView.findViewById(R.id.tab_text);
                        tabTextView.setTextColor(getResources().getColor(R.color.black));
                        tabView.setBackground(null);
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    // Do nothing
                }
            });




        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
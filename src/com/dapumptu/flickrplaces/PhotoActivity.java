/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dapumptu.flickrplaces;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.dapumptu.flickrplaces.ui.PhotoPageFragment;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see ScreenSlidePageFragment
 */
public class PhotoActivity extends FragmentActivity {

    public static final String NUM_PAGES_KEY = "TotalPages";
    public static final String CURRENT_PAGE_KEY = "CurrentPage";
    
    //private static final int NUM_PAGES = 15;

    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), bundle.getInt(NUM_PAGES_KEY));

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(mPagerAdapter);

            mPager.setCurrentItem(bundle.getInt(CURRENT_PAGE_KEY));

            mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When changing pages, reset the action bar actions since
                    // they are dependent
                    // on which page is currently active. An alternative
                    // approach is to have each
                    // fragment expose actions itself (rather than the activity
                    // exposing actions),
                    // but for simplicity, the activity provides the actions in
                    // this sample.
                    invalidateOptionsMenu();
                }
            });
        }
    }


    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        
        private int mPages;
        
        public ScreenSlidePagerAdapter(FragmentManager fm, int pages) {
            super(fm);
            
            mPages = pages;
        }

        @Override
        public Fragment getItem(int position) {
            return PhotoPageFragment.create(position);
        }

        @Override
        public int getCount() {
            return mPages;
        }
    }
}

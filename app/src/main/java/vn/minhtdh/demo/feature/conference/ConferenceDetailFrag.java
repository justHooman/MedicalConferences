package vn.minhtdh.demo.feature.conference;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.feature.topic.TopicFrag;
import vn.minhtdh.demo.feature.user.ParticipantsFrag;
import vn.minhtdh.demo.frag.BaseToolBarFrag;

/**
 * Created by exoplatform on 8/24/15.
 */
public class ConferenceDetailFrag extends BaseToolBarFrag {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_ADMIN = 1;

    public int mode = MODE_NORMAL;

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.conference_detail_frag, container, false);
        ViewPager vp = (ViewPager) v.findViewById(R.id.vpContent);
        vp.setAdapter(new PagerAdapter(getChildFragmentManager()));
        TabLayout tl = (TabLayout) v.findViewById(R.id.tabs);
        tl.setupWithViewPager(vp);
        return v;
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        int[] pages;

        static final int PAGE_TOPIC = 0;
        static final int PAGE_PARTICIPANT = 1;
        static final int PAGE_TOPIC_REQUEST = 2;

        public void changeMode() {
            boolean changed = false;
            int[] newPages = mode == MODE_ADMIN ? new int[]{PAGE_TOPIC_REQUEST, PAGE_TOPIC, PAGE_PARTICIPANT} : new int[]{PAGE_TOPIC, PAGE_PARTICIPANT};
            if (pages == null) {
                changed = true;
            } else if (pages.length != newPages.length) {
                changed = true;
            }
            if (changed) {
                pages = newPages;
                notifyDataSetChanged();
            }
        }

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            changeMode();
        }

        @Override
        public Fragment getItem(int position) {
            int page = pages[position];
            Fragment frag = null;
            // TODO
            switch (page) {
                case PAGE_TOPIC:
                    frag = new TopicFrag();
                    break;
                case PAGE_TOPIC_REQUEST:
                    frag = new TopicFrag();
                    break;
                case PAGE_PARTICIPANT:
                    frag = new ParticipantsFrag();
                    break;
            }
            return frag;
        }

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            final int page = pages[position];
            int strId;
            switch (page) {
                default:
                case PAGE_TOPIC:
                    strId = R.string.tab_topics;
                    break;
                case PAGE_TOPIC_REQUEST:
                    strId = R.string.tab_topics_request;
                    break;
                case PAGE_PARTICIPANT:
                    strId = R.string.tab_participants;
                    break;
            }
            return getString(strId);
        }
    }
}

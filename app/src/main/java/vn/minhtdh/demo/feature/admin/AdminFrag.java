package vn.minhtdh.demo.feature.admin;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.feature.conference.ConferenceDetailFrag;
import vn.minhtdh.demo.frag.BaseToolBarFrag;
import vn.minhtdh.demo.model.Conference;

/**
 * Created by exoplatform on 8/24/15.
 */
public class AdminFrag extends BaseToolBarFrag.BaseListFrag<List<Conference>> {

    @Override
    public CharSequence getTitle() {
        return getString(R.string.admin_page);
    }

    @Override
    public Loader<List<Conference>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Conference>> loader, List<Conference> data) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.fabAdd) {
            ConferenceDetailFrag frag = new ConferenceDetailFrag();
            frag.mode = ConferenceDetailFrag.MODE_ADMIN;
            move(getFragmentManager(), frag);
        }
    }
}

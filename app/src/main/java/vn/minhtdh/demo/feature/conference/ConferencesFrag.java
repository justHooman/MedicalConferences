package vn.minhtdh.demo.feature.conference;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.frag.BaseToolBarFrag;
import vn.minhtdh.demo.model.Conference;
import vn.minhtdh.demo.utils.Utils;

/**
 * Created by exoplatform on 8/24/15.
 */
public class ConferencesFrag extends BaseToolBarFrag.BaseListFrag<List<Conference>> {
    @Override
    public CharSequence getTitle() {
        return getString(R.string.conferences);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.setVisibility(Utils.getVal(mFab), View.GONE);
    }

    @Override
    public Loader<List<Conference>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Conference>> loader, List<Conference> data) {

    }
}

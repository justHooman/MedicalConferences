package vn.minhtdh.demo.feature.user;

import android.os.Bundle;
import android.support.v4.content.Loader;

import java.util.List;

import vn.minhtdh.demo.frag.BaseToolBarFrag;
import vn.minhtdh.demo.model.Participant;

/**
 * Created by exoplatform on 8/24/15.
 */
public class ParticipantsFrag extends BaseToolBarFrag.BaseListFrag<List<Participant>> {
    @Override
    public Loader<List<Participant>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Participant>> loader, List<Participant> data) {

    }
}

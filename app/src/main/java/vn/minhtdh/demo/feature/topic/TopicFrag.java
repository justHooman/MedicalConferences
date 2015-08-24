package vn.minhtdh.demo.feature.topic;

import android.os.Bundle;
import android.support.v4.content.Loader;

import java.util.List;

import vn.minhtdh.demo.frag.BaseToolBarFrag;
import vn.minhtdh.demo.model.Topic;

/**
 * Created by exoplatform on 8/24/15.
 */
public class TopicFrag extends BaseToolBarFrag.BaseListFrag<List<Topic>> {
    @Override
    public Loader<List<Topic>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Topic>> loader, List<Topic> data) {

    }
}

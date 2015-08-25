package vn.minhtdh.demo.feature.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.minhtdh.demo.db.DbHelper;
import vn.minhtdh.demo.db.DbUtils;
import vn.minhtdh.demo.frag.BaseToolBarFrag;
import vn.minhtdh.demo.model.Topic;
import vn.minhtdh.demo.utils.Contanst;
import vn.minhtdh.demo.utils.Utils;
import vn.minhtdh.demo.widget.CmnAdt;
import vn.minhtdh.demo.widget.CmnLoader;
import vn.minhtdh.demo.widget.SimpleHolder;

/**
 * Created by exoplatform on 8/24/15.
 */
public class TopicFrag extends BaseToolBarFrag.BaseListFrag<List<Topic>> {

    public TopicFrag() {
        super();
    }
    static final String KEY_CONFERENCE_ID = "conference_id";
    static final String KEY_TOPIC_STATUS = "topic_status";
    static final String KEY_MODE = "topic_mode";
    int mConferenceId = Contanst.UNKNOW_ID, mTopicStatus;

    public void setData(int confId, int status) {
        Bundle extras = getArguments();
        if (extras == null) {
            extras = new Bundle();
        }
        extras.putInt(KEY_CONFERENCE_ID, confId);
        extras.putInt(KEY_TOPIC_STATUS, status);
        mConferenceId = confId;
        mTopicStatus = status;
        if (confId == Contanst.UNKNOW_ID) {
            mAdt.setItems(new ArrayList<Topic>());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mConferenceId = args.getInt(KEY_CONFERENCE_ID, Contanst.UNKNOW_ID);
            mTopicStatus = args.getInt(KEY_TOPIC_STATUS);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    TopicAdapter mAdt = new TopicAdapter();
    @Override
    protected void configureAdapter(RecyclerView rv) {
        super.configureAdapter(rv);
        rv.setAdapter(mAdt);
    }

    @Override
    public Loader<List<Topic>> onCreateLoader(int id, Bundle args) {
        return new TopicLoader(getActivity(), mConferenceId, mTopicStatus);
    }

    @Override
    public void onLoadFinished(Loader<List<Topic>> loader, List<Topic> data) {
        if (mConferenceId != Contanst.UNKNOW_ID) {
            mAdt.addAll(data);
        }
    }

    @Override
    protected void onFabClick(View v) {
        super.onFabClick(v);
        TopicDetailFrag frag = new TopicDetailFrag();
        frag.editable = true;
        frag.topic = new Topic();
        frag.topic.conferenceId = mConferenceId;
        frag.show(getChildFragmentManager(), "add topic");
    }

    static class TopicAdapter extends CmnAdt.SimpleAdt<Topic> {

        @Override
        public void onBindViewHolder(SimpleHolder holder, int position) {
            Topic item = getItem(position);
            holder.mTv.setText(item == null ? null : item.title);
        }
    }

    public static class TopicLoader extends CmnLoader<List<Topic>> {

        List<Topic> mItems;
        boolean loaded = false;

        final int topicStatus;
        final int conferenceId;

        public TopicLoader(Context context, int confId, int status) {
            super(context);
            topicStatus = status;
            this.conferenceId = confId;
        }

        @Override
        public void deliverResult(List<Topic> data) {
            super.deliverResult(data);
            loaded = true;
            mItems = data;
        }

        @Override
        public List<Topic> loadInBackground() {
            if (conferenceId == Contanst.UNKNOW_ID) {
                return null;
            }
            return new DbUtils().getTopics(DbHelper.getIns().getReadableDatabase(), conferenceId, topicStatus);
        }

        @Override
        protected void onStartLoading() {
            // only load when not start yet and mItems is not null
            if (loaded && mItems != null) {
                return;
            }
            super.onStartLoading();
        }
    }
}

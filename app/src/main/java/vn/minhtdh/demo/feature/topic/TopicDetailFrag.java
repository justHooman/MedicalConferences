package vn.minhtdh.demo.feature.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.frag.BaseDialogFrag;
import vn.minhtdh.demo.frag.DateTimeFrag;
import vn.minhtdh.demo.frag.EventFrag;
import vn.minhtdh.demo.model.Topic;
import vn.minhtdh.demo.utils.UIUtils;
import vn.minhtdh.demo.utils.Utils;

/**
 * Created by exoplatform on 8/25/15.
 */
public class TopicDetailFrag extends BaseDialogFrag {

    public Topic topic;
    public boolean editable = false;
    WeakReference<TextView> mTvTitle, mTvContent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.topic_detail_frag, container, false);
        ViewStub stub;
        stub = (ViewStub) v.findViewById(R.id.stub_title);
        UIUtils.inflateText(stub, editable);

        stub = (ViewStub) v.findViewById(R.id.stub_content);
        UIUtils.inflateText(stub, editable);

        TextView tv;
        tv = (TextView) v.findViewById(R.id.tvTitle);
        mTvTitle = new WeakReference<TextView>(tv);

        tv = (TextView) v.findViewById(R.id.tvTopicContent);
        mTvContent = new WeakReference<TextView>(tv);


        // TODO add save action
        stub = (ViewStub) v.findViewById(R.id.stub_btn);
        stub.setLayoutResource(R.layout.btn_group);
        stub.inflate();
        v.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventFrag frag = new EventFrag();
        frag.editable = editable;
        frag.event = topic;
        getChildFragmentManager().beginTransaction().add(R.id. event_content, frag).commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        fillData();
    }

    public void fillData() {
        Utils.setText(Utils.getVal(mTvTitle), topic == null ? null : topic.title);
        Utils.setText(Utils.getVal(mTvContent), topic == null ? null : topic.content);
    }

    public void saveData() {
        EventFrag frag = (EventFrag) getChildFragmentManager().findFragmentById(R.id.event_content);
        if (frag != null) {
            frag.saveData();
        }
        if (topic == null)
            return;
        TextView tv;
        tv = Utils.getVal(mTvTitle);
        if (tv != null) {
            topic.title = tv.getText().toString();
        }
        tv = Utils.getVal(mTvContent);
        if (tv != null) {
            topic.content = tv.getText().toString();
        }
    }
}

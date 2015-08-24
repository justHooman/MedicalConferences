package vn.minhtdh.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDateTime;

import java.lang.ref.WeakReference;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.model.Event;
import vn.minhtdh.demo.utils.Utils;

/**
 * Created by exoplatform on 8/24/15.
 */
public class EventFrag extends BaseFrag {

    public boolean editable = false;

    Event event;

    WeakReference<TextView> mTv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int layoutId = editable ? R.layout.event_frag : R.layout.event_view_only;
        View v = inflater.inflate(layoutId, container, false);
        TextView tv = (TextView) v.findViewById(R.id.tvLocation);
        Utils.setText(tv, event == null ? "null" : event.location);
        mTv = new WeakReference<TextView>(tv);

        DateTimeFrag frag = (DateTimeFrag) getChildFragmentManager().findFragmentById(R.id.timeStart);
        frag.setClickable(editable);
        frag.setDateTime(event == null ? LocalDateTime.now() : new LocalDateTime(event.timeStart));

        frag = (DateTimeFrag) getChildFragmentManager().findFragmentById(R.id.timeEnd);
        frag.setClickable(editable);
        frag.setDateTime(event == null ? LocalDateTime.now() : new LocalDateTime(event.timeEnd));

        return v;
    }

    public void saveData() {
        if (event != null) {
            TextView tv = Utils.getVal(mTv);
            if (tv != null) {
                event.location = tv.getText().toString();
            }
            DateTimeFrag frag = (DateTimeFrag) getChildFragmentManager().findFragmentById(R.id.timeStart);
            if (frag != null && frag.mDateTime != null) {
                event.timeStart = frag.mDateTime.toDate().getTime();
            }
            frag = (DateTimeFrag) getChildFragmentManager().findFragmentById(R.id.timeEnd);
            if (frag != null && frag.mDateTime != null) {
                event.timeEnd = frag.mDateTime.toDate().getTime();
            }
        }
    }
}

package vn.minhtdh.demo.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by exoplatform on 8/25/15.
 */
public class SimpleHolder extends RecyclerView.ViewHolder {

    public TextView mTv;
    public SimpleHolder(View itemView) {
        super(itemView);
        mTv = (TextView) itemView.findViewById(android.R.id.text1);
    }
}

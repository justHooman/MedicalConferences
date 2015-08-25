package vn.minhtdh.demo.widget;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by exoplatform on 8/25/15.
 */
public abstract class CmnLoader<D> extends AsyncTaskLoader<D> {
    public CmnLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}

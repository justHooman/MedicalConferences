package vn.minhtdh.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.utils.Utils;

/**
 * Created by exoplatform on 8/24/15.
 */
public abstract class BaseToolBarFrag extends BaseFrag {

    protected WeakReference<Toolbar> mToolBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.toolbar_frag, container, false);
        Toolbar tb = (Toolbar) vg.findViewById(R.id.toolbar);
        if (tb != null) {
            mToolBar = new WeakReference<Toolbar>(tb);
            getAct().setSupportActionBar(tb);
        }
        vg.addView(onCreateContentView(inflater, vg, savedInstanceState));
        return vg;
    }
    public abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public void setTitle(CharSequence cs) {
        Toolbar tb = Utils.getVal(mToolBar);
        if (tb != null) {
            tb.setTitle(cs);
        }
    }

    public static abstract class BaseListFrag<E> extends BaseToolBarFrag implements LoaderManager.LoaderCallbacks<E> {

        public int getLayoutId() {
            return R.layout.rv_with_fab;
        }

        public CharSequence getTitle() {
            return null;
        }

        protected WeakReference<RecyclerView> mRv;
        protected WeakReference<FloatingActionButton> mFab;

        @Override
        public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(getLayoutId(), container, false);
            setTitle(getTitle());
            RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
            rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            configureAdapter(rv);
            mRv = new WeakReference<RecyclerView>(rv);

            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabAdd);
            fab.setOnClickListener(mFabClickListener);
            mFab = new WeakReference<FloatingActionButton>(fab);
            return v;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            initData(savedInstanceState);
        }

        protected void configureAdapter(RecyclerView rv) {

        }

        private View.OnClickListener mFabClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick(v);
            }
        };

        protected void onFabClick(View v) {

        }

        // Loader

        protected void initData(@Nullable Bundle savedInstanceState) {
            getLoaderManager().initLoader(DATA_LOADER_ID, savedInstanceState, this);
        }

        protected void resetData() {
            getLoaderManager().restartLoader(DATA_LOADER_ID, null, this);
        }

        public static final int DATA_LOADER_ID = 1;

        @Override
        public void onLoaderReset(Loader<E> loader) {

        }
    }
}

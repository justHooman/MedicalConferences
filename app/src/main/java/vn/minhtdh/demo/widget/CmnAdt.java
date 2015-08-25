package vn.minhtdh.demo.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import vn.minhtdh.demo.utils.Utils;

/**
 * Created by exoplatform on 8/25/15.
 */
public abstract class CmnAdt<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected List<E> mItems;

    public void setItems(List<E> items) {
        mItems = items;
    }

    public E getItem(int position) {
        return Utils.getItem(mItems, position);
    }

    @Override
    public int getItemCount() {
        return Utils.getSize(mItems);
    }
}

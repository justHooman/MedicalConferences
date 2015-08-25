package vn.minhtdh.demo.feature.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import vn.minhtdh.demo.model.User;
import vn.minhtdh.demo.widget.CmnAdt;
import vn.minhtdh.demo.widget.SimpleHolder;

/**
 * Created by exoplatform on 8/25/15.
 */
public class UserAdapter extends CmnAdt<User, SimpleHolder> {

    @Override
    public SimpleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        SimpleHolder ret = new SimpleHolder(v);
        return ret;
    }

    @Override
    public void onBindViewHolder(SimpleHolder holder, int position) {
        User user = getItem(position);
        holder.mTv.setText(user == null? null : user.userMail);
    }
}

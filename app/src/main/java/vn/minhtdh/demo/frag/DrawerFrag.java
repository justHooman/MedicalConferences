package vn.minhtdh.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.minhtdh.demo.MainActivity;
import vn.minhtdh.demo.R;
import vn.minhtdh.demo.model.User;

/**
 * Created by minhtdh on 8/23/15.
 */
public class DrawerFrag extends BaseFrag {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_frag, container, false);
        v.findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ((MainActivity) getActivity()).onSignInClicked(v);
            }
        });
        return v;
    }

    public void setUser(User user) {
        View v = getView();
        if (v != null) {
            ((TextView) v.findViewById(R.id.name)).setText(user == null ? null : user.displayName);
            ((TextView) v.findViewById(R.id.mail)).setText(user == null ? null : user.userMail);
            ((TextView) v.findViewById(R.id.roles)).setText(user == null ? null : user.roles);
        }
    }


}

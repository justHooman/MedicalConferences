package vn.minhtdh.demo.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import vn.minhtdh.demo.MainActivity;
import vn.minhtdh.demo.R;
import vn.minhtdh.demo.model.User;
import vn.minhtdh.demo.utils.Contanst;
import vn.minhtdh.demo.utils.Utils;

/**
 * Created by minhtdh on 8/23/15.
 */
public class DrawerFrag extends BaseFrag implements View.OnClickListener {
    WeakReference<TextView> mTvName, mTvMail, mTvRole, mBtnAdmin, mBtnConference;
    WeakReference<ImageView> mImgAvatar;
    WeakReference<View> mBtnSignIn, mBtnSignOut;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final
    Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drawer_frag, container, false);


        ImageView img = (ImageView) v.findViewById(R.id.imgAvatar);
        mImgAvatar = new WeakReference<ImageView>(img);

        TextView tmp = null;
        tmp = (TextView) v.findViewById(R.id.name);
        mTvName = new WeakReference<TextView>(tmp);

        tmp = (TextView) v.findViewById(R.id.mail);
        mTvMail = new WeakReference<TextView>(tmp);

        tmp = (TextView) v.findViewById(R.id.roles);
        mTvRole = new WeakReference<TextView>(tmp);

        tmp = (TextView) v.findViewById(R.id.btn_admin);
        tmp.setOnClickListener(this);
        mBtnAdmin = new WeakReference<TextView>(tmp);

        tmp = (TextView) v.findViewById(R.id.btn_conference);
        tmp.setOnClickListener(this);
        mBtnConference = new WeakReference<TextView>(tmp);

        View tmpV;
        tmpV = v.findViewById(R.id.sign_in_button);
        tmpV.setOnClickListener(this);
        mBtnSignIn = new WeakReference<View>(tmpV);

        tmpV = v.findViewById(R.id.sign_out_button);
        mBtnSignOut = new WeakReference<View>(tmpV);

        fillData(mUser, mPerson);
        return v;
    }

    private User mUser;
    private Person mPerson;

    public void setUser(User user, Person person) {
        mUser = user;
        mPerson = person;
        fillData(mUser, mPerson);
    }

    private void fillData(User user, Person person) {
        if (user == null) {
            Utils.setVisibility(Utils.getVal(mBtnSignIn), View.VISIBLE);
            Utils.setVisibility(Utils.getVal(mBtnSignOut), View.INVISIBLE);
            Utils.setVisibility(Utils.getVal(mBtnAdmin), View.GONE);
            Utils.setVisibility(Utils.getVal(mBtnConference), View.GONE);
        } else {
            Utils.setVisibility(Utils.getVal(mBtnSignIn), View.GONE);
            Utils.setVisibility(Utils.getVal(mBtnSignOut), View.VISIBLE);
            Utils.setVisibility(Utils.getVal(mBtnAdmin), Contanst.UserRole.isUserAdmin(user) ? View.VISIBLE : View.GONE);
            Utils.setVisibility(Utils.getVal(mBtnConference), View.VISIBLE);
        }
        Utils.setText(Utils.getVal(mTvName), user == null ? null : user.displayName);
        Utils.setText(Utils.getVal(mTvMail), user == null ? null : user.userMail);
        Utils.setText(Utils.getVal(mTvRole), user == null ? null : user.roles);
        ImageView imgAvatar = Utils.getVal(mImgAvatar);
        if (imgAvatar != null) {
            Picasso.with(getActivity()).cancelRequest(imgAvatar);
            if (person != null && person.hasImage()) {
                String url = person.getImage().getUrl();
                if (url != null) {
                    Picasso.with(getActivity()).load(url).into(imgAvatar);
                }
            } else {
                imgAvatar.setImageDrawable(null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
            case R.id.sign_out_button:
            case R.id.btn_admin:
            case R.id.btn_conference:
                ((MainActivity) getActivity()).onClick(v);
                break;
            default:
                break;
        }
    }
}

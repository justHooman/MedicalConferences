package vn.minhtdh.demo.feature.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import vn.minhtdh.demo.R;
import vn.minhtdh.demo.db.DbHelper;
import vn.minhtdh.demo.db.DbUtils;
import vn.minhtdh.demo.frag.BaseDialogFrag;
import vn.minhtdh.demo.frag.BaseToolBarFrag;
import vn.minhtdh.demo.model.User;
import vn.minhtdh.demo.utils.Utils;
import vn.minhtdh.demo.widget.CmnLoader;

/**
 * Created by exoplatform on 8/25/15.
 */
public class UsersFrag extends BaseToolBarFrag.BaseListFrag<List<User>> {
    @Override
    public CharSequence getTitle() {
        return getString(R.string.users);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int id, Bundle args) {
        return new UserLoader(getActivity());
    }

    UserAdapter mAdt = new UserAdapter();

    @Override
    protected void configureAdapter(RecyclerView rv) {
        super.configureAdapter(rv);
        rv.setAdapter(mAdt);
    }

    @Override
    public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
        RecyclerView rv = Utils.getVal(mRv);
        mAdt.setItems(data);
        if (rv != null) {
            rv.setAdapter(mAdt);
        }
    }

    @Override
    protected void onFabClick(View v) {
        super.onFabClick(v);
        final InputAlertDlg dlg = new InputAlertDlg();
        dlg.cs = getString(R.string.input_user_title);
        dlg.mListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CharSequence mail = dlg.getText();
                if (Utils.notEmpty(mail)) {
                    User user = new User();
                    user.userMail = mail.toString();
                    new DbUtils().insertUser(DbHelper.getIns().getWritableDatabase(), user);
                    UsersFrag.this.resetData();
                }
            }
        };
        dlg.show(getChildFragmentManager(), "add users");
    }

    public static class InputAlertDlg extends BaseDialogFrag {

        public DialogInterface.OnClickListener mListener;
        public CharSequence cs;
        WeakReference<TextView> mTv;
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
            EditText edt = new EditText(getActivity());
            mTv = new WeakReference<TextView>(edt);
            edt.setId(android.R.id.text1);
            bld.setView(edt);
            bld.setTitle(cs);
            bld.setPositiveButton(android.R.string.ok, mListener);
            return bld.create();
        }

        public CharSequence getText() {
            TextView tv = Utils.getVal(mTv);
            return tv == null ? null : tv.getText();
        }
    }

    public static class UserLoader extends CmnLoader<List<User>> {

        public UserLoader(Context context) {
            super(context);
        }

        @Override
        public List<User> loadInBackground() {
            return new DbUtils().queryUsers(DbHelper.getIns().getReadableDatabase());
        }
    }
}

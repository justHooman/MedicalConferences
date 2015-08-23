package vn.minhtdh.demo.frag;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Created by minhtdh on 8/24/15.
 */
public class ProgressFrag extends BaseDialogFrag {

    public CharSequence title;

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        ProgressDialog dlg = new ProgressDialog(getActivity(), STYLE_NO_TITLE);
        dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlg.setCancelable(false);
        dlg.setTitle(title);
        return dlg;
    }
}

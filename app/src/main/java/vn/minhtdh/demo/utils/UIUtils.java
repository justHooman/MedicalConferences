package vn.minhtdh.demo.utils;

import android.view.ViewStub;

import vn.minhtdh.demo.R;

/**
 * Created by exoplatform on 8/25/15.
 */
public class UIUtils {
    public static void inflateText(ViewStub stub, boolean editable) {
        stub.setLayoutResource(editable ? R.layout.edittext : R.layout.textview);
        stub.inflate();
    }
}

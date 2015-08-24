package vn.minhtdh.demo.frag;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import vn.minhtdh.demo.MainActivity;

/**
 * Created by minhtdh on 8/23/15.
 */
public class BaseFrag extends Fragment {
    public final String TAG = getClass().getSimpleName();

    public Option generateDefaultOption() {
        Option opt = new Option();
        opt.tag = getClass().getSimpleName();
        opt.backStackName = getClass().getName();
        return opt;
    }

    public MainActivity getAct() {
        return getActivity() instanceof MainActivity ? (MainActivity) getActivity() : null;
    }

    public static class Option {
        public static final int ACTION_REPLACE = 0;
        public static final int ACTION_ADD = 1;
        public int action = ACTION_REPLACE;
        public int placeHolder;
        public boolean addBackStack = true;

        public Option() {
            super();
        }

        public Option(int pPlaceHolder) {
            this();
            placeHolder = pPlaceHolder;
        }

        public boolean finishCurrent = false;
        public boolean useAnimation = true;
        public String backStackName;
        public boolean useSenderPlaceHolder = true;
        public String tag;

        public void setPlaceHolder(int holderId) {
            useSenderPlaceHolder = false;
            placeHolder = holderId;
        }
    }

    private Option mOption;

    public void move(FragmentManager fm, BaseFrag newFrag) {
        Option opt = newFrag.generateDefaultOption();
        if (mOption != null && opt.useSenderPlaceHolder) {
            opt.placeHolder = mOption.placeHolder;
        }
        move(fm, newFrag, opt);
    }

    public void move(FragmentManager fm, BaseFrag frag, Option opt) {
        if (opt== null || frag == null) {
            return;
        }
        frag.mOption = opt;
        FragmentTransaction ft = fm.beginTransaction();

        ft.setCustomAnimations(android.support.v7.appcompat.R.anim.abc_slide_in_top, android.support.v7.appcompat.R.anim.abc_slide_out_bottom,
                android.support.v7.appcompat.R.anim.abc_slide_out_top, android.support.v7.appcompat.R.anim.abc_slide_in_bottom);
        switch (opt.action) {
            case Option.ACTION_ADD :

                if (opt.placeHolder == 0) {
                    ft.add(frag, opt.tag);
                } else {
                    ft.add(opt.placeHolder, frag, opt.tag);
                }
                break;
            case Option.ACTION_REPLACE :
                if (opt.placeHolder != 0) {
                    ft.replace(opt.placeHolder, frag, opt.tag);
                }
                break;
            default :
                break;
        }
        if (opt.addBackStack) {
            ft.addToBackStack(opt.backStackName);
        }
        ft.commit();
    }
}

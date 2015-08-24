package vn.minhtdh.demo.frag;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.LocalDateTime;

import java.lang.ref.WeakReference;

import vn.minhtdh.demo.utils.Utils;

/**
 * Created by exoplatform on 8/24/15.
 */
public class DateTimeFrag extends BaseFrag implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    WeakReference<TextView> mTv;
    LocalDateTime mDateTime;

    boolean clickable = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button btn = new Button(inflater.getContext());
        mTv = new WeakReference<TextView>(btn);
        if (mDateTime == null)
            mDateTime = LocalDateTime.now();
        btn.setOnClickListener(this);
        btn.setClickable(clickable);
        updateText();
        return btn;
    }

    public LocalDateTime getDateTime() {
        return mDateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        mDateTime = dateTime;
        updateText();
    }

    public void setClickable(boolean isClickable) {
        clickable = isClickable;
        View v = Utils.getVal(mTv);
        if (v != null) {
            v.setClickable(clickable);
        }
    }

    private void updateText() {
        Utils.setText(Utils.getVal(mTv), String.valueOf(mDateTime));
    }

    @Override
    public void onClick(View v) {
        DatePickerFragment fragment = new DatePickerFragment();
        if (mDateTime == null) {
            mDateTime = LocalDateTime.now();
        }
        fragment.mDateTime = mDateTime;
        fragment.mListener = this;
        fragment.show(getChildFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TimePickerFragment newFragment = new TimePickerFragment();
        mDateTime = mDateTime.withYear(year).withMonthOfYear(monthOfYear).withDayOfMonth(dayOfMonth);
        updateText();
        newFragment.mDateTime = mDateTime;
        newFragment.mListener = this;
        newFragment.show(getChildFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mDateTime = mDateTime.withHourOfDay(hourOfDay).withMinuteOfHour(minute);
        updateText();
    }

    public static class DatePickerFragment extends BaseDialogFrag {
        DatePickerDialog.OnDateSetListener mListener;
        LocalDateTime mDateTime;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            if (mDateTime == null) {
                mDateTime = LocalDateTime.now();
            }
            int year = mDateTime.getYear();
            int month = mDateTime.getMonthOfYear();
            int day = mDateTime.getDayOfMonth();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), mListener, year, month, day);
        }
    }

    public static class TimePickerFragment extends BaseDialogFrag {
        TimePickerDialog.OnTimeSetListener mListener;
        LocalDateTime mDateTime;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            int hour = mDateTime.getHourOfDay();
            int minute = mDateTime.getMinuteOfHour();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), mListener, hour, minute, true);
        }
    }
}

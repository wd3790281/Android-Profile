package com.example.dingwang.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


public class DatePickDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public interface OnDatePickListener {
        void onDatePickButtonClick(DialogFragment dialog, int year, int month, int day);
    }

    OnDatePickListener mListener;

    public static DatePickDialogFragment newInstance(Fragment fragment, int title, Date date, int positiveButton, int negativeButton) {

        DatePickDialogFragment dialogFragment = new DatePickDialogFragment();

        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putSerializable("date", date);
        args.putInt("positive", positiveButton);
        args.putInt("negative", negativeButton);

        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(fragment, 0);
        return dialogFragment;
    }

    Date mDate = new Date();
    int mYear;
    int mMonth;
    int mDay;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();


        final Calendar calendar = Calendar.getInstance();

        int year = 0;
        int month = 0;
        int day = 0;

        if (bundle.getSerializable("date") == null) {

            mDate = new Date(System.currentTimeMillis());

        }else{

            mDate = (Date) bundle.getSerializable("date");
            mDate.getTime();

        }
        calendar.setTime(mDate);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


         final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mListener = (OnDatePickListener) getTargetFragment();
                mListener.onDatePickButtonClick(DatePickDialogFragment.this, year, monthOfYear, dayOfMonth);

            }
        },year, month, day);
        return datePickerDialog;

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        OnDatePickListener listener;
        try {
            listener = (OnDatePickListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException(dialog.toString() + "must implement DialogListener");
        }

        int year = mYear;
        int month = mMonth;
        int day = mDay;

        listener.onDatePickButtonClick(this, year, month, day);

    }


}

package com.example.dingwang.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class ListDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    int mSelectedItem;

    public interface OnListDialogListener {
        void onListDialogButtonClick(DialogFragment dialog, int which, int data, int layoutID);
    }
    int mLayoutID;

    public static ListDialogFragment newInstance(Fragment fragment, int layoutID, int title, int list, int positiveButton, int negativeButton) {

        ListDialogFragment dialogFragment = new ListDialogFragment();


        Bundle args = new Bundle();

        args.putInt("title", title);
        args.putInt("list", list);
        args.putInt("negative", negativeButton);
        args.putInt("layoutID", layoutID);

        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(fragment, 0);
        return dialogFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        int title = bundle.getInt("title");
        int list = bundle.getInt("list");
        int negativeButton = bundle.getInt("negative");
        mLayoutID = bundle.getInt("layoutID");

        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedItem = which;
                OnListDialogListener listener;
                try {
                    listener = (OnListDialogListener) getTargetFragment();
                }catch (ClassCastException e) {
                    throw new ClassCastException(dialog.toString() + "must implement DialogListener");
                }

                int data = mSelectedItem;
                int layoutID = mLayoutID;
                listener.onListDialogButtonClick(null, which, data, layoutID);
            }
        });

        if(title != 0)
            builder.setTitle(title);
        if(negativeButton !=0)
            builder.setNegativeButton(negativeButton, this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        OnListDialogListener listener;
        try {
            listener = (OnListDialogListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException(dialog.toString() + "must implement DialogListener");
        }

        int data = mSelectedItem;
        int layoutID = mLayoutID;

        listener.onListDialogButtonClick(this, which, data, layoutID);

    }

}

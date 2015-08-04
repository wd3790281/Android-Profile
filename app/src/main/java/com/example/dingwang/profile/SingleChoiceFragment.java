package com.example.dingwang.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;


public class SingleChoiceFragment extends DialogFragment implements DialogInterface.OnClickListener{

    int mSelectedItem;
    int mLayoutID;

    public interface OnSingleChoiceListener {
        void onSingleChoiceButtonClick(DialogFragment dialog, int layoutID, int which, int data);
    }

    public static SingleChoiceFragment newInstance(Fragment fragment, int layoutID, int title, int list, int positiveButton, int negativeButton) {

        SingleChoiceFragment dialogFragment = new SingleChoiceFragment();

        Bundle args = new Bundle();

        args.putInt("title", title);
        args.putInt("list", list);
        args.putInt("positive", positiveButton);
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
        int positiveButton = bundle.getInt("positive");
        int negativeButton = bundle.getInt("negative");
        mLayoutID = bundle.getInt("layoutID");


        builder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelectedItem = which;
            }
        });

        if(title != 0)
            builder.setTitle(title);
        if(positiveButton != 0)
            builder.setPositiveButton(positiveButton, this);
        if(negativeButton !=0)
            builder.setNegativeButton(negativeButton, this);
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        OnSingleChoiceListener listener;
        try {
            listener = (OnSingleChoiceListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException(dialog.toString() + "must implement DialogListener");
        }

        int data = mSelectedItem;
        int layoutID = mLayoutID;

        listener.onSingleChoiceButtonClick(this, layoutID, which, data);

    }
}

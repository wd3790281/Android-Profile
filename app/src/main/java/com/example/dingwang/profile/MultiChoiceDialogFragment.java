package com.example.dingwang.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;


public class MultiChoiceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    ArrayList<Boolean> mSelectedItems = new ArrayList(30);
    ArrayList<String> mSelcetedItemString = new ArrayList();

    public interface OnMultiChoiceListener {
        void onMultiButtonClick(DialogFragment dialog, int which, ArrayList data, ArrayList<String> dataString);
    }

    public static MultiChoiceDialogFragment newInstance(Fragment fragment,int arrayID, boolean[] defaultItem, ArrayList<String> itemsName, int title, int positiveButton, int negativeButton) {

        MultiChoiceDialogFragment dialogFragment = new MultiChoiceDialogFragment();

        Bundle args = new Bundle();

        args.putInt("title", title);
        args.putInt("positive", positiveButton);
        args.putInt("negative", negativeButton);
        args.putInt("arrayID", arrayID);
        args.putBooleanArray("defaultItem", defaultItem);

        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(fragment, 0);
        return dialogFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        int title = bundle.getInt("title");
        int positiveButton = bundle.getInt("positive");
        int negativeButton = bundle.getInt("negative");
        final int arrayID = bundle.getInt("arrayID");
        boolean[] defaultItems = null;
        if (bundle.getBooleanArray("defaultItem") != null) {
            defaultItems = bundle.getBooleanArray("defaultItem");
        }
        for(int i = 0; i <= 30; i++){
            if (defaultItems != null) {
                mSelectedItems.add(defaultItems[i]);
                String[] topics = getResources().getStringArray(arrayID);
                if (defaultItems[i]) {
                    mSelcetedItemString.add(topics[i]);
                }
            }else {
                mSelectedItems.add(false);
            }
        }

        builder.setMultiChoiceItems(arrayID, defaultItems , new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] topics = getResources().getStringArray(arrayID);
                if (isChecked) {
                    mSelectedItems.set(which, true);
                    mSelcetedItemString.add(topics[which]);
                } else if (mSelectedItems.get(which)) {
                    mSelectedItems.set(which, false);
                    mSelcetedItemString.remove(topics[which]);
                }
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
        OnMultiChoiceListener listener;
        try {
            listener = (OnMultiChoiceListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException(dialog.toString() + "must implement DialogListener");
        }

        ArrayList data = mSelectedItems;
        ArrayList<String> dataString = mSelcetedItemString;

        listener.onMultiButtonClick(this, which, data, dataString);

    }
}

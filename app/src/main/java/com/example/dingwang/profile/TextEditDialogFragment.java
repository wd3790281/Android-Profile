package com.example.dingwang.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class TextEditDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public interface OnTextDialogListener {
        void onTextEditDialogButtonClick(DialogFragment dialog, int layoutID, int which, String data);
    }


    int mLayoutID;

    @Override
    public void onClick(DialogInterface dialog, int which) {

        OnTextDialogListener listener;
        try {
            listener = (OnTextDialogListener) getTargetFragment();
        }catch (ClassCastException e) {
            throw new ClassCastException(dialog.toString() + "must implement DialogListener");
        }

        String data = mEditText.getText().toString();
        int layoutID = mLayoutID;

        listener.onTextEditDialogButtonClick(this, layoutID,  which, data);

    }


    public static TextEditDialogFragment newInstance(Fragment fragment, int layoutID, int title,  int positiveButton, int negativeButton) {

        TextEditDialogFragment dialogFragment = new TextEditDialogFragment();

        Bundle args = new Bundle();

        args.putInt("title", title);
        args.putInt("positive", positiveButton);
        args.putInt("negative", negativeButton);
        args.putInt("layoutID", layoutID);

        dialogFragment.setArguments(args);
        dialogFragment.setTargetFragment(fragment, 0);
        return dialogFragment;
    }

    private EditText mEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView=inflater.inflate(R.layout.edit_text, null);

        builder.setView(rootView);

        mEditText= (EditText) rootView.findViewById(R.id.edit_context);

        int title = bundle.getInt("title");
        int positiveButton = bundle.getInt("positive");
        int negativeButton = bundle.getInt("negative");
        mLayoutID = bundle.getInt("layoutID");

        if(title != 0)
            builder.setTitle(title);
        if(positiveButton != 0)
            builder.setPositiveButton(positiveButton, this);
        if(negativeButton !=0)
            builder.setNegativeButton(negativeButton, this);
        return builder.create();

    }



}

package com.example.dingwang.profile;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.camera.CropImageIntentBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TextEditDialogFragment.OnTextDialogListener,
        MultiChoiceFragment.OnMultiChoiceListener, SingleChoiceFragment.OnSingleChoiceListener, ListDialogFragment.OnListDialogListener,
DatePickFragment.OnDatePickListener{

    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private LinearLayout mLinearLayoutPhoto;
    private LinearLayout mLinearLayoutFirstName;
    private LinearLayout mLinearLayoutLastName;
    private LinearLayout mLinearLayoutGender;
    private LinearLayout mLinearLayoutEdition;
    private LinearLayout mLinearLayoutIntersetingTopic;
    private LinearLayout mLinearLayoutBirth;

    private TextView mTextView;
    private String[] mSingleChoice;
    private ImageView mImageView;
    private TextView mFirstNameText;
    private TextView mLastNameText;
    private TextView mEditionText;
    private TextView mGenderText;
    private TextView mBirthText;
    private TextView mTopicText;
    private Date mDate;
    private SharedPreferences mPreference;
    private SharedPreferences.Editor mPreferenceEd;
    private Bitmap mPhoto;
    private ArrayList<String> mItemsName = new ArrayList<String>();


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mPreferenceEd = mPreference.edit();

        mLinearLayoutPhoto = (LinearLayout) rootView.findViewById(R.id.profile_setting_linear_layout_photo);
        mLinearLayoutFirstName = (LinearLayout) rootView.findViewById(R.id.profile_setting_linear_layout_firstname);
        mLinearLayoutLastName = (LinearLayout) rootView.findViewById(R.id.profile_setting_linear_layout_lastname);
        mLinearLayoutGender = (LinearLayout) rootView.findViewById(R.id.profile_setting_linear_layout_gender);
        mLinearLayoutBirth = (LinearLayout) rootView.findViewById(R.id.profile_setting_linear_layout_birth);
        mLinearLayoutEdition = (LinearLayout) rootView.findViewById(R.id.profile_setting_linear_layout_edition);
        mLinearLayoutIntersetingTopic = (LinearLayout) rootView.findViewById((R.id.profile_setting_linear_layout_interesting_topic));


        mFirstNameText = (TextView) rootView.findViewById(R.id.profile_text_view_first_name);
        mLastNameText = (TextView) rootView.findViewById(R.id.profile_text_view_last_name);
        mEditionText = (TextView) rootView.findViewById(R.id.profile_text_view_edition);
        mGenderText = (TextView) rootView.findViewById(R.id.profile_text_view_gender);
        mBirthText = (TextView) rootView.findViewById(R.id.profile_text_view_birth);
        mTopicText = (TextView) rootView.findViewById(R.id.profile_text_view_topic);

        String imageCode = mPreference.getString("encoded image", null);
        mImageView = (ImageView) rootView.findViewById(R.id.profile_setting_profile_image);
        if (imageCode == null){
            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_picture));
        }else {

            byte[] decodedByte = Base64.decode(imageCode,Base64.DEFAULT);
            mPhoto = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            mImageView.setImageBitmap(mPhoto);

        }
        String firstName = mPreference.getString("first name", "First Name");
        mFirstNameText.setText(firstName);

        String lastName = mPreference.getString("last name", "Last Name");
        mLastNameText.setText(lastName);

        String edition = mPreference.getString("edition", "AU");
        mEditionText.setText(edition);

        String gender = mPreference.getString("gender", "Male");
        mGenderText.setText(gender);

        String birth = mPreference.getString("date time", "Birthday");
        mBirthText.setText(birth);

        String topic = mPreference.getString("topic name", "Please Select");
        if (topic.equals("Please Select")){
            mItemsName.add("");
        }
        mTopicText.setText(topic);
//        mTopicText.setMovementMethod(new ScrollingMovementMethod());



        mLinearLayoutPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromList(mLinearLayoutPhoto,R.array.photo);
            }
        });
        mLinearLayoutFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("fragment name", 0);
                editName(mLinearLayoutFirstName, R.id.profile_setting_linear_layout_firstname);
            }
        });

        mLinearLayoutLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName(mLinearLayoutLastName, R.id.profile_setting_linear_layout_lastname);
            }
        });

        mLinearLayoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromList(mLinearLayoutGender, R.array.gender);
            }
        });

        mLinearLayoutBirth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                DialogFragment dialog = DatePickFragment.newInstance(MainActivityFragment.this, R.string.birthday, date, R.string.ok, R.string.cancel);
                dialog.show(getFragmentManager(), "Date Picker");
                mTextView = (TextView) rootView.findViewById(R.id.profile_text_view_birth);
            }
        });

        mLinearLayoutEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromList(mLinearLayoutEdition, R.array.pref_editions);
            }
        });

        mLinearLayoutIntersetingTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topickOnClick();

            }
        });
        mTopicText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topickOnClick();
            }
        });

        return rootView;
    }

    public void topickOnClick() {
        String defaultItems = mPreference.getString("topic", null);
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(defaultItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean[] items = null;
        if(jsonArray != null) {
            items = new boolean[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    items[i] = jsonArray.getBoolean(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        ArrayList<String>  itemsName = mItemsName;

        DialogFragment dialog = MultiChoiceFragment.newInstance(MainActivityFragment.this,R.array.pref_topics, items, itemsName, R.string.interesting_topic, R.string.ok, R.string.cancel);
        dialog.show(getFragmentManager(), "Pick Interesting Topic");
    }
    @Override
    public void onTextEditDialogButtonClick(DialogFragment dialog, int layoutID, int which, String data) {

        if(which == DialogInterface.BUTTON_POSITIVE) {
            mTextView.setText(data);
            if (layoutID == R.id.profile_setting_linear_layout_firstname) {
                mPreferenceEd.putString("first name", data);
                mPreferenceEd.commit();
            } else if (layoutID == R.id.profile_setting_linear_layout_lastname) {
                mPreferenceEd.putString("last name", data);
                mPreferenceEd.commit();
            }
        }
        dialog.dismiss();

    }

    public void editName(LinearLayout layout, final int resourceID){
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(resourceID == R.id.profile_setting_linear_layout_firstname) {

            mTextView = (TextView) layout.findViewById(R.id.profile_text_view_first_name);
            DialogFragment dialog = TextEditDialogFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_firstname, R.string.first_name, R.string.ok, R.string.cancel);
            dialog.show(getFragmentManager(), "Edit First Name");

        } else if(resourceID == R.id.profile_setting_linear_layout_lastname) {
            mTextView = (TextView) layout.findViewById(R.id.profile_text_view_last_name);
            DialogFragment dialog = TextEditDialogFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_lastname, R.string.last_name, R.string.ok, R.string.cancel);
            dialog.show(getFragmentManager(), "Edit Last Name");
        }
    }

    @Override
    public void onMultiButtonClick(DialogFragment dialog, int which, ArrayList data, ArrayList<String> dataString) {
        if(which == DialogInterface.BUTTON_POSITIVE) {

            JSONArray jsonArray = new JSONArray(data);
            String itemsName = dataString.toString().replaceAll("\\[", "").replaceAll("\\]", "");
            mItemsName = dataString;
            mTopicText.setText(itemsName);
            mPreferenceEd.putString("topic name", itemsName);
            mPreferenceEd.putString("topic", jsonArray.toString());
            mPreferenceEd.commit();

        }
        dialog.dismiss();
    }

    public void singleChoice(LinearLayout layout, int array) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(array == R.array.gender) {

            mSingleChoice = getResources().getStringArray(R.array.gender);
            mTextView = (TextView) layout.findViewById(R.id.profile_text_view_gender);
            DialogFragment dialog = SingleChoiceFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_gender, R.string.gender, R.array.gender, R.string.ok, R.string.cancel);
            dialog.show(getFragmentManager(), "Choose Gender");

        }else if(array == R.array.pref_editions) {

            mSingleChoice = getResources().getStringArray(R.array.pref_editions);
            mTextView = (TextView) layout.findViewById(R.id.profile_text_view_edition);
            DialogFragment dialog = SingleChoiceFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_edition, R.string.edition, R.array.pref_editions, R.string.ok,R.string.cancel);
            dialog.show(getFragmentManager(), "Choose Edition");

        }
    }


    @Override
    public void onSingleChoiceButtonClick(DialogFragment dialog,int layoutID, int which, int data) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            String text = mSingleChoice[data];
            mTextView.setText(text);
            if (layoutID == R.id.profile_setting_linear_layout_gender) {
                mPreferenceEd.putString("gender", text);
                mPreferenceEd.commit();
            } else if (layoutID == R.id.profile_setting_linear_layout_edition) {
                mPreferenceEd.putString("edition", text);
                mPreferenceEd.commit();
            }

        }
        dialog.dismiss();
    }

    public void pickFromList(LinearLayout layout, int array){
        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(array == R.array.photo){

            mSingleChoice = getResources().getStringArray(R.array.photo);
            DialogFragment dialog = ListDialogFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_photo, R.string.profile_photo, R.array.photo, R.string.ok, R.string.cancel);
            dialog.show(getFragmentManager(), "Choose photo");

        } else if(array == R.array.gender) {

            mSingleChoice = getResources().getStringArray(R.array.gender);
            mTextView = (TextView) layout.findViewById(R.id.profile_text_view_gender);
            DialogFragment dialog = ListDialogFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_gender, R.string.gender, R.array.gender, R.string.ok, R.string.cancel);
            dialog.show(getFragmentManager(), "Choose Gender");

        }else if(array == R.array.pref_editions) {

            mSingleChoice = getResources().getStringArray(R.array.pref_editions);
            mTextView = (TextView) layout.findViewById(R.id.profile_text_view_edition);
            DialogFragment dialog = ListDialogFragment.newInstance(MainActivityFragment.this, R.id.profile_setting_linear_layout_edition, R.string.edition, R.array.pref_editions, R.string.ok,R.string.cancel);
            dialog.show(getFragmentManager(), "Choose Edition");

        }
    }

    protected static final int REQUEST_CAMERA = 0;
    protected static final int REQUEST_GALLARY = 1;
    protected static final int REQUEST_CROP_PICTURE = 2;
    private Uri mImageCaptureUri;
    private File mFile;


    @Override
    public void onListDialogButtonClick(DialogFragment dialog, int which, int data, int layoutID) {
        if(layoutID == R.id.profile_setting_linear_layout_photo) {
            Intent imageActionIntent = null;
            if (which == 0){

                imageActionIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFile = new File(Environment.getExternalStorageDirectory(), "tmp_contact_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                mImageCaptureUri = Uri.fromFile(mFile);

                imageActionIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                imageActionIntent.putExtra("return-data", true);
                startActivityForResult(imageActionIntent, REQUEST_CAMERA);


            } else if ( which == 1){

                imageActionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imageActionIntent.setType("image/*");
                startActivityForResult(imageActionIntent, REQUEST_GALLARY);
            }else{
                dialog.dismiss();
            }

        }else {

            if ( which != DialogInterface.BUTTON_NEGATIVE) {
                String text = mSingleChoice[data];
                mTextView.setText(text);
                if (layoutID == R.id.profile_setting_linear_layout_gender) {
                    mPreferenceEd.putString("gender", text);
                    mPreferenceEd.commit();
                } else if (layoutID == R.id.profile_setting_linear_layout_edition) {
                    mPreferenceEd.putString("edition", text);
                    mPreferenceEd.commit();
                }
            } else {
                dialog.dismiss();
            }
        }
    }


    @Override
    public void onDatePickButtonClick(DialogFragment dialog, int year, int month, int day ) {

        String date = "";
        date = "" + day + "/" + month + "/" + year;
        mTextView.setText(date);
        mPreferenceEd.putString("date time", date);
        mPreferenceEd.commit();
        dialog.dismiss();
    }

    private Boolean mPhotoChanged = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        File croppedImageFile = new File(getActivity().getFilesDir(), "test.jpg");

        if (resultCode == Activity.RESULT_OK && requestCode ==REQUEST_CAMERA) {

            Uri croppedImage = Uri.fromFile(croppedImageFile);
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(150, 150, croppedImage);
            cropImage.setOutlineColor(0xFF03A9F4);
            cropImage.setSourceImage(mImageCaptureUri);
            Context c = getActivity();
            startActivityForResult(cropImage.getIntent(getActivity()), REQUEST_CROP_PICTURE);

        } else if (requestCode == REQUEST_GALLARY){

            Uri croppedImage = Uri.fromFile(croppedImageFile);
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(150, 150, croppedImage);
            cropImage.setOutlineColor(0xFF03A9F4);
            cropImage.setSourceImage(data.getData());
            Context c = getActivity();
            startActivityForResult(cropImage.getIntent(getActivity()), REQUEST_CROP_PICTURE);

        } else if (requestCode == REQUEST_CROP_PICTURE && resultCode == Activity.RESULT_OK) {

            Bitmap image = BitmapFactory.decodeFile(croppedImageFile.getAbsolutePath());

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
            byte[] b = byteArray.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

            mPreferenceEd.putString("encoded image", imageEncoded);
            mPreferenceEd.commit();
            mImageView.setImageBitmap(image);

        }


    }

}


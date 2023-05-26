package com.threestepdare.assessment.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.threestepdare.assessment.Helpers.PreferenceHelper;
import com.threestepdare.assessment.R;


public class DetailsFragment extends Fragment {

    private TextView name, occupation, gender, business, location, txtBirthPlace, txtDateOfBirth, txtHouseNumber, txtCommunity, txtDistrict, txtRegion, txtMaritalStatus, txtBio;
    private Button btnEditProfile, btnDelete;
    private Animation scaleUp, scaleDown;
    private CircleImageView profile_image;

    PreferenceHelper preferenceHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getContext() != null){

            preferenceHelper = new PreferenceHelper(getContext());

            // initialize views
            initializeViews(view);

            // set user info to views
            setInfo();

            // edit profile
            btnEditProfile.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnEditProfile.startAnimation(scaleUp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnEditProfile.startAnimation(scaleDown);

                    if (Build.VERSION.SDK_INT >= 26) {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddOrEditDetailFragment()).commit();
                    } else {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddOrEditDetailFragment()).commit();
                    }

                }
                return true;
            });

            // delete profile info
            btnDelete.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnDelete.startAnimation(scaleUp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnDelete.startAnimation(scaleDown);

                    // open edit section
                    preferenceHelper.putIsDataSaved(false);
                    preferenceHelper.clearPrefs();

                    deleteImagePathFromSharedPreference();

                    Toast.makeText(getContext(), "Data deleted...", Toast.LENGTH_SHORT).show();

                    if (Build.VERSION.SDK_INT >= 26) {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddOrEditDetailFragment()).commit();
                    } else {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddOrEditDetailFragment()).commit();
                    }

                }
                return true;
            });


            if(getSavedImagePathFromSharedPreference() != null){
                Bitmap imageBitmap = BitmapFactory.decodeFile(getSavedImagePathFromSharedPreference());
                profile_image.setImageBitmap(imageBitmap);
            }

        }
    }

    private void initializeViews(View view){

        txtBirthPlace = view.findViewById(R.id.txtBirthPlace);
        txtDateOfBirth = view.findViewById(R.id.txtDateOfBirth);
        txtHouseNumber = view.findViewById(R.id.txtHouseNumber);
        txtCommunity = view.findViewById(R.id.txtCommunity);
        txtDistrict = view.findViewById(R.id.txtDistrict);
        occupation = view.findViewById(R.id.occupation);
        location = view.findViewById(R.id.location);
        business = view.findViewById(R.id.business);
        gender = view.findViewById(R.id.gender);
        name = view.findViewById(R.id.name);

        profile_image = view.findViewById(R.id.profile_image);

        txtBio = view.findViewById(R.id.txtBio);
        txtRegion = view.findViewById(R.id.txtRegion);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);

        scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down);

    }

    private void setInfo(){

        name.setText(String.format("%s %s", preferenceHelper.getFirstName(), preferenceHelper.getLastName()));
        business.setText(getResources().getText(R.string.placeholder_business_type)); // no field for business was found in mock-up
        txtDateOfBirth.setText(preferenceHelper.getDateOfBirth());
        txtHouseNumber.setText(preferenceHelper.getHouseNumber());
        txtBirthPlace.setText(preferenceHelper.getBirthPlace());
        txtCommunity.setText(preferenceHelper.getCommunity());
        occupation.setText(preferenceHelper.getOccupation());
        txtDistrict.setText(preferenceHelper.getDistrict());
        txtRegion.setText(preferenceHelper.getRegion());
        location.setText(preferenceHelper.getRegion());
        gender.setText(preferenceHelper.getGender());
        txtBio.setText(preferenceHelper.getBio());

    }

    private String getSavedImagePathFromSharedPreference() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_app_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("image_path", null);
    }

    private void deleteImagePathFromSharedPreference() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("image_path");
        editor.apply();
    }


}
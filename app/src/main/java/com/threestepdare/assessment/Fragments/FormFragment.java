package com.threestepdare.assessment.Fragments;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.threestepdare.assessment.Helpers.PreferenceHelper;
import com.threestepdare.assessment.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormFragment extends Fragment {

    private Animation scaleUp, scaleDown;
    private ImageButton btnTakePhoto, btnUploadPhoto;
    private RadioGroup gender;
    private RadioButton radioBtnMale, radioBtnFemale;
    private Button btnReset, btnSave;
    private EditText firstName, lastName, birthPlace, dateOfBirth, houseNumber, community, occupation, district, region, maritalStatus, bio;
    private CircleImageView profileImage;
    PreferenceHelper preferenceHelper;

    Pattern specialCharacter;
    Pattern upperCase;
    Pattern numbers;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 100;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getContext() != null) {

            specialCharacter = Pattern.compile("[!@#$%&*()_+=|<>?{}/-]");
            upperCase = Pattern.compile("[A-Z]");
            numbers = Pattern.compile("[0123456789]");

            preferenceHelper = new PreferenceHelper(getContext());

            initializeViews(view);

            btnTakePhoto.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnTakePhoto.startAnimation(scaleUp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnTakePhoto.startAnimation(scaleDown);
                    checkCameraPermission(getContext());
                }
                return true;
            });

            btnUploadPhoto.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnUploadPhoto.startAnimation(scaleUp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnUploadPhoto.startAnimation(scaleDown);
                    openFileChooser();
                }
                return true;
            });

            btnReset.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnReset.startAnimation(scaleUp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnReset.startAnimation(scaleDown);
                    // clear all fields
                    clearAllFields();
                }
                return true;
            });

            btnSave.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnSave.startAnimation(scaleUp);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnSave.startAnimation(scaleDown);

                    if(firstName.length() == 0 || specialCharacter.matcher(Objects.requireNonNull(firstName.getText()).toString()).find()
                            || numbers.matcher(Objects.requireNonNull(firstName.getText()).toString()).find()){

                        displayError(firstName, "Invalid characters");

                    }
                    else{
                        removeErrorMessage(firstName, view);
                    }

                    if(lastName.length() == 0 || specialCharacter.matcher(Objects.requireNonNull(lastName.getText()).toString()).find()
                            || numbers.matcher(Objects.requireNonNull(lastName.getText()).toString()).find()){

                        displayError(lastName, "Invalid characters");

                    }
                    else{
                        removeErrorMessage(lastName, view);
                    }

                    if(!(lastName.length() == 0 || specialCharacter.matcher(Objects.requireNonNull(lastName.getText()).toString()).find()
                            || numbers.matcher(Objects.requireNonNull(lastName.getText()).toString()).find()) && !(firstName.length() == 0 || specialCharacter.matcher(Objects.requireNonNull(firstName.getText()).toString()).find()
                            || numbers.matcher(Objects.requireNonNull(firstName.getText()).toString()).find())){

                        preferenceHelper.putFirstName(firstName.getText().toString());
                        preferenceHelper.putLastName(lastName.getText().toString());
                        preferenceHelper.putBirthPlace(birthPlace.getText().toString());
                        preferenceHelper.putDateOfBirth(dateOfBirth.getText().toString());
                        preferenceHelper.putHouseNumber(houseNumber.getText().toString());
                        preferenceHelper.putCommunity(community.getText().toString());
                        preferenceHelper.putOccupation(occupation.getText().toString());
                        preferenceHelper.putDistrict(district.getText().toString());
                        preferenceHelper.putRegion(region.getText().toString());
                        preferenceHelper.putMaritalStatus(maritalStatus.getText().toString());
                        preferenceHelper.putBio(bio.getText().toString());

                        if(gender.getCheckedRadioButtonId() == radioBtnMale.getId()){
                            preferenceHelper.putGender("Male");
                        }
                        else if(gender.getCheckedRadioButtonId() == radioBtnFemale.getId()){
                            preferenceHelper.putGender("Female");
                        }

                        preferenceHelper.putIsDataSaved(true);

                        Toast.makeText(getContext(), "successful", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= 26) {
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DisplayDetailFragment()).commit();
                        } else {
                            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new DisplayDetailFragment()).commit();
                        }


                    }

                }
                return true;
            });

            displayEntryInfo(0, 30);

        }


    }


    private void initializeViews(View view) {

        profileImage = view.findViewById(R.id.profile_image);
        btnTakePhoto = view.findViewById(R.id.btn_take_photo);
        btnUploadPhoto = view.findViewById(R.id.btn_upload_photo);
        gender = view.findViewById(R.id.genderRadioGroup);
        radioBtnMale = view.findViewById(R.id.maleRadioButton);
        radioBtnFemale = view.findViewById(R.id.femaleRadioButton);
        btnReset = view.findViewById(R.id.btnReset);
        btnSave = view.findViewById(R.id.btnSave);

        firstName = view.findViewById(R.id.editTextFirstName);
        lastName = view.findViewById(R.id.editTextLastName);
        birthPlace = view.findViewById(R.id.editTextBirthPlace);
        dateOfBirth = view.findViewById(R.id.editTextDateOfBirth);
        houseNumber = view.findViewById(R.id.editTextHouseNumber);
        community = view.findViewById(R.id.editTextCommunity);
        occupation = view.findViewById(R.id.editTextOccupation);
        district = view.findViewById(R.id.editTextDistrict);
        region = view.findViewById(R.id.editTextRegion);
        maritalStatus = view.findViewById(R.id.editTextMaritalStatus);
        bio = view.findViewById(R.id.editTextBiography);
        gender = view.findViewById(R.id.genderRadioGroup);
        radioBtnFemale = view.findViewById(R.id.femaleRadioButton);
        radioBtnMale = view.findViewById(R.id.maleRadioButton);

        scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down);

        firstName.setText(preferenceHelper.getFirstName());
        lastName.setText(preferenceHelper.getLastName());
        birthPlace.setText(preferenceHelper.getBirthPlace());
        dateOfBirth.setText(preferenceHelper.getDateOfBirth());
        houseNumber.setText(preferenceHelper.getHouseNumber());
        community.setText(preferenceHelper.getCommunity());
        occupation.setText(preferenceHelper.getOccupation());
        district.setText(preferenceHelper.getDistrict());
        region.setText(preferenceHelper.getDistrict());
        maritalStatus.setText(preferenceHelper.getMaritalStatus());
        bio.setText(preferenceHelper.getBio());

    }

    private void clearAllFields() {

        firstName.setText("");
        lastName.setText("");
        birthPlace.setText("");
        dateOfBirth.setText("");
        houseNumber.setText("");
        community.setText("");
        occupation.setText("");
        district.setText("");
        region.setText("");
        maritalStatus.setText("");
        bio.setText("");

    }

    private void displayError(EditText editText, String errorMessage){
        editText.setError(errorMessage);
        editText.setBackgroundResource(R.drawable.error_background);
        View message = LayoutInflater.from(getContext()).inflate(R.layout.error_message, null);
        TextView errorText = message.findViewById(R.id.error_text);
        errorText.setText(errorMessage);
    }

    private void removeErrorMessage(EditText editText, View view){
        editText.setError(null);
        // Clear background color
        editText.setBackgroundResource(R.drawable.edittext_bg_less_rounded);
        // Remove the error message view if it exists
        View errorMessage = view.findViewById(R.id.error_text);
        if (errorMessage != null) {
            ViewGroup parentView = (ViewGroup) errorMessage.getParent();
            parentView.removeView(errorMessage);
        }
    }

    private void displayEntryInfo(int xOffset, int yOffset){
        firstName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && firstName.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter First Name", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        lastName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && lastName.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Last Name", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        birthPlace.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && birthPlace.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Place Of Birth", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        dateOfBirth.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && dateOfBirth.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Date Of Birth", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        houseNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && houseNumber.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Your House Number", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        community.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && community.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Your Community Name", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        occupation.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && occupation.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Your Occupation", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        district.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && district.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Your District", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        region.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && region.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Your Region", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        maritalStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && maritalStatus.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter Your Marital Status", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
        bio.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && bio.length() == 0) {
                Toast toast = Toast.makeText(getContext(), "Enter A Brief Description About Yourself", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, xOffset, yOffset);
                toast.show();
            }
        });
    }


    private void checkCameraPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,  android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            capturePhoto();
        }
    }

    private void capturePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(requireContext(), "Camera app not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profileImage.setImageBitmap(imageBitmap);

                // Save the image file path
                String imagePath = saveImageToInternalStorage(imageBitmap);
                saveImagePathToSharedPreference(imagePath);
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);

                // Save the image file path to shared preferences
                String imagePath = saveImageToInternalStorage(bitmap);
                saveImagePathToSharedPreference(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String saveImageToInternalStorage(Bitmap imageBitmap) {
        String fileName = "my_image.jpg";
        File storageDir = requireContext().getFilesDir();
        File imageFile = new File(storageDir, fileName);
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile.getAbsolutePath();
    }

    private void saveImagePathToSharedPreference(String imagePath) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image_path", imagePath);
        editor.apply();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto();
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



}
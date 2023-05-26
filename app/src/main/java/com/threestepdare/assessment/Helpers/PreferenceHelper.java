package com.threestepdare.assessment.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private final String firstName = "firstName";
    private final String lastName = "lastName";
    private final String birthPlace = "birthPlace";
    private final String dateOfBirth = "dateOfBirth";
    private final String houseNumber = "houseNumber";
    private final String community = "community";
    private final String occupation = "occupation";
    private final String gender = "gender";
    private final String district = "district";
    private final String region = "region";
    private final String maritalStatus = "maritalStatus";
    private final String bio = "bio";
    private final String dataSaved = "dataSaved";
    private final SharedPreferences app_prefs;


    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
    }

    public void putIsDataSaved(boolean isDataSaved) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(dataSaved, isDataSaved);
        edit.apply();
    }

    public void putFirstName(String _firstName) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(firstName, _firstName);
        edit.apply();
    }

    public void putLastName(String _lastName) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(lastName, _lastName);
        edit.apply();
    }

    public void putBirthPlace(String _birthPlace) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(birthPlace, _birthPlace);
        edit.apply();
    }

    public void putDateOfBirth(String _dateOfBirth) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(dateOfBirth, _dateOfBirth);
        edit.apply();
    }

    public void putHouseNumber(String _houseNumber) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(houseNumber, _houseNumber);
        edit.apply();
    }

    public void putCommunity(String _community) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(community, _community);
        edit.apply();
    }

    public void putOccupation(String _occupation) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(occupation, _occupation);
        edit.apply();
    }

    public void putGender(String _gender) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(gender, _gender);
        edit.apply();
    }

    public void putDistrict(String _district) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(district, _district);
        edit.apply();
    }

    public void putRegion(String _region) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(region, _region);
        edit.apply();
    }

    public void putMaritalStatus(String _maritalStatus) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(maritalStatus, _maritalStatus);
        edit.apply();
    }

    public void putBio(String _bio) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(bio, _bio);
        edit.apply();
    }

    public void clearPrefs() {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.clear().apply();
    }


    public String getFirstName() {
        return app_prefs.getString(firstName, "");
    }

    public String getLastName() {
        return app_prefs.getString(lastName, "");
    }

    public String getBirthPlace() {
        return app_prefs.getString(birthPlace, "");
    }

    public String getDateOfBirth() {
        return app_prefs.getString(dateOfBirth, "");
    }

    public String getHouseNumber() {
        return app_prefs.getString(houseNumber, "");
    }

    public String getCommunity() {
        return app_prefs.getString(community, "");
    }

    public String getOccupation() {
        return app_prefs.getString(occupation, "");
    }

    public String getGender(){return app_prefs.getString(gender, "");}

    public String getDistrict() {
        return app_prefs.getString(district, "");
    }

    public String getRegion() {
        return app_prefs.getString(region, "");
    }

    public String getMaritalStatus() {
        return app_prefs.getString(maritalStatus, "");
    }

    public String getBio() {
        return app_prefs.getString(bio, "");
    }

    public boolean getIsDataSaved(){
        return app_prefs.getBoolean(dataSaved, false);
    }

}

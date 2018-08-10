package com.example.marin.mobileradninalog.constants;

import android.content.Context;

public interface CheckForPermission {
    boolean checkIfAlreadyhavePermission(Context context);
    void requestForSpecificPermission();
    //onRequestPermissionsResult has to be overriden in implementation class
}

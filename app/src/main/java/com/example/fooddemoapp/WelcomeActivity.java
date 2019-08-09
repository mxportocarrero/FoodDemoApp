package com.example.fooddemoapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.fooddemoapp.utils.Constants.ERROR_DIALOG_REQUEST;
import static com.example.fooddemoapp.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.fooddemoapp.utils.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    // Verifying permission for GoogleMaps permission
    private boolean mLocationPermissionGranted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


    } // End of onCreate

    @Override
    protected void onResume() {
        super.onResume();
        // Hacemos la revision de los permisos para usar GPS y GoogleMaps
        if (checkMapServices()){
            if (mLocationPermissionGranted){
                launchThread();
            }else{
                getLocationPermission();
            }
        }

        // Lanzamos el thread que automaticamente nos enviara al Main Activity o al LoginActivity

    }

    // Launch Thread
    // -------------
    private void launchThread(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                }catch (Exception e){
                    Toast.makeText(WelcomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }finally {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null){
                        startActivity(new Intent( WelcomeActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent( WelcomeActivity.this, LoginActivity.class));
                    }

                    // Finalizamos esta activitidad
                    finish();
                }
            }
        };

        thread.start();
    } // End of Launch Thread

    // Permission Checking
    // -------------------
    // Services hace referencia a Google Services de Android
    private boolean checkMapServices(){
        if (isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    // Google Play Services
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if(available == ConnectionResult.SUCCESS){
            // everything is fine the user can make map request
            Log.d(TAG,"isServicesOK: Google Play Services is Working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            // Ocurrio un error pero puede resolverse
            Log.d(TAG, "isServicesOK: Error ocurred but we can fix it");

            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    } // End of isServicesOK

    // Revisar si el GPS esta habilitado
    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
            buildAlertNoGps();
            return false;
        }
        return true;
    } // End of isMapsEnabled

    public void buildAlertNoGps(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This app requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent enableGpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    } // End of buildAlertNoGps

    // Como llamamos a otra activity es necesario llamar al metodo onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: called");

        // Como pueden existir varios onActivity results, ellos estan codificados por las constantes y pueden
        // usarse con solo un switch
        switch (requestCode){
            case PERMISSIONS_REQUEST_ENABLE_GPS:{
                if (mLocationPermissionGranted){
                    // Lanzamos el thread que automaticamente nos enviara al Main Activity o al LoginActivity
                    launchThread();
                }else {
                    getLocationPermission();
                }
            }

        }

    } // End of onActivityResult

    private void getLocationPermission(){
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback, onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mLocationPermissionGranted = true;

            // Lanzamos el thread que automaticamente nos enviara al Main Activity o al LoginActivity
            launchThread();
            // getChatRooms();

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    } // Fin del Callback onRequestPermissionsResults

} // End of Welcome Activity

package com.example.mat.gps_orientation_wizzard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Looper;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import android.location.Location;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity {
    //VECTORS
    private Button computeButton;
    private EditText wpLatitudeText, wpLongitudeText;
    private TextView resultView;
    //GPS
    private String LastUpdateTime;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location MyCurrentLocation;
    private Button BtnStart=null;
    private Button BtnStop=null;
    private TextView TextView1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.computeButton = (Button) findViewById(R.id.computeButton);
        this.wpLatitudeText = (EditText) findViewById(R.id.wpLatitudeText);
        this.wpLongitudeText = (EditText) findViewById(R.id.wpLongitudeText);
        this.resultView = (TextView) findViewById(R.id.resultView);
        this.BtnStart=(Button)findViewById(R.id.BtnStart);
        this.BtnStart.setOnClickListener(ClickListenerBtnStart);
        this.BtnStop=(Button)findViewById(R.id.BtnStop);
        this.BtnStop.setOnClickListener(ClickListenerBtnStop);
        this.TextView1=(TextView)findViewById(R.id.TextView1);
        initLocationService();
    }

    public void computeVector(View view) {
        // TODO REMPLACE FIRST TWO VALUE BY ACTUAL GPS VALUES
        double resultVector[] = VectorGenerator.computeVector(
                2.1547299000000066,
                49.022697,
                Double.valueOf(this.wpLongitudeText.getText().toString()),
                Double.valueOf(this.wpLatitudeText.getText().toString())
        );
        String displayedString = "Longitude : " + resultVector[0] + "\nLatitude :" + resultVector[1];
        this.resultView.setText(displayedString);
    }
    private void initLocationService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                MyCurrentLocation = locationResult.getLastLocation();
                LastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                Maj_UI();
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void Maj_UI() {
        if (MyCurrentLocation != null) {
            TextView1.setText(" Lat  : " + MyCurrentLocation.getLatitude() + "\n" + " Long : " + MyCurrentLocation.getLongitude());
            TextView1.append("\n\n" + " MAJ : " + LastUpdateTime);
        }
        else{
            TextView1.setText("NO DATA !");
        }
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        TextView1.setText("LOCATION UPDATE ON");
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        Maj_UI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Toast.makeText(getApplicationContext(), "Autoriser GPS et Google Localisation !", Toast.LENGTH_SHORT).show();
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Toast.makeText(getApplicationContext(), "Autoriser GPS et Google Localisation !", Toast.LENGTH_SHORT).show();
                        }
                        Maj_UI();
                    }
                });
    }

    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }


    private View.OnClickListener ClickListenerBtnStart= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView1.setText("");
            startLocationUpdates();
        }
    };

    private View.OnClickListener ClickListenerBtnStop= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stopLocationUpdates();
            TextView1.append("\n\n" + "LOCATION UPDATE OFF");
        }
    };
}

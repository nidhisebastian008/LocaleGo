package com.example.nidhi.hikerwatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
     LocationManager locationManager;
     LocationListener locationListener;
     TextView tvlat;
     TextView tvlon;
     TextView tvacc;
     TextView tvalt;
     TextView tvadd;
     TextView tvtitle;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if(requestCode ==1)
         {

             if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)

             {
                 if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                 {


                     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);


                 }
             }


         }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);





        locationManager =(LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT<23)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location!=null)
                {
                    updateLocationInfo(location);
                }

            }
        }



    }

    private void updateLocationInfo(Location location) {
        tvlat=findViewById(R.id.tvlat);
        tvlon=findViewById(R.id.tvlon);
        tvacc=findViewById(R.id.tvacc);
        tvalt=findViewById(R.id.tvalt);


        tvlat.setText("Latitude:"+location.getLatitude());
        tvlon.setText("Longitude:"+location.getLongitude());
        tvacc.setText("Accuracy:"+location.getAccuracy());
        tvalt.setText("Altitude:"+location.getAltitude());


        Geocoder geocoder =new Geocoder(getApplicationContext(),Locale.getDefault());


        try
        {

            String address = "Could not find address";

            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0 ) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

                address = "Address: \n";

                if (listAddresses.get(0).getSubThoroughfare() != null) {

                    address += listAddresses.get(0).getSubThoroughfare() + " ";

                }

                if (listAddresses.get(0).getThoroughfare() != null) {

                    address += listAddresses.get(0).getThoroughfare() + "\n";

                }

                if (listAddresses.get(0).getLocality() != null) {

                    address += listAddresses.get(0).getLocality() + "\n";

                }

                if (listAddresses.get(0).getPostalCode() != null) {

                    address += listAddresses.get(0).getPostalCode() + "\n";

                }

                if (listAddresses.get(0).getCountryName() != null) {

                    address += listAddresses.get(0).getCountryName() + "\n";

                }

            }

            TextView addressTextView = (TextView) findViewById(R.id.tvadd);

            addressTextView.setText(address);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

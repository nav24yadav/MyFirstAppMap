package com.example.gur02790.myfirstmapappv2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        {


private GoogleMap mMap;
//private GoogleApiClient mGoogleApiClient;
private Location mLastLocation;

        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;

        LatLng latLng;
        GoogleMap mGoogleMap;
        SupportMapFragment mFragment;
        Marker currLocationMarker;

@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        }


/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */
    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/

        @Override
        public void onMapReady(GoogleMap googleMap){
                mGoogleMap=googleMap;
                mGoogleMap.setMyLocationEnabled(true);

                buildGoogeleApiClient();

                mGoogleApiClient.connect();
                }

        protected synchronized void buildGoogeleApiClient(){
            Toast.makeText(this,"Naveen build Google client",Toast.LENGTH_SHORT).show();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        @Override
        public void onConnected(Bundle bundle){
            Toast.makeText(this,"Naveen OnConnected",Toast.LENGTH_SHORT).show();
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(mLastLocation != null)
            {
                latLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Pranav Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                currLocationMarker = mGoogleMap.addMarker(markerOptions);

            }

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(3000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

         }

            @Override
            public void onConnectionSuspended(int i) {
                Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationChanged(Location location) {

                //place marker at current position
                //mGoogleMap.clear();
                if (currLocationMarker != null) {
                    currLocationMarker.remove();
                }
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                currLocationMarker = mGoogleMap.addMarker(markerOptions);

                Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

                //zoom to current position:
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();

                if(cameraPosition != null) {
                    mGoogleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
                else
                {
                    Toast.makeText(this,"Null pointer for cameraPosition object",Toast.LENGTH_SHORT).show();
                }

                //If you only need one location, unregister the listener
                //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            }
}



package com.example.denny_pc.photgraph;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewRequestsActivity extends AppCompatActivity {

    ListView requestListView;
    ArrayList<String> request = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<Double>requestLatitudes=new ArrayList<Double>();
    ArrayList<Double>requestLongitudes=new ArrayList<Double>();
    ArrayList<String> username=new ArrayList<String>();

    public void updateListView(Location location){
        if (location != null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());



            query.whereNear("location",geoPointLocation);
            query.whereDoesNotExist("driverUsername");
            query.setLimit(10);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e== null){

                        request.clear();
                        requestLongitudes.clear();
                        requestLatitudes.clear();

                        if (objects.size()>0){
                            for (ParseObject object:objects){
                                ParseGeoPoint requestLocation=(ParseGeoPoint)object.get("location");
                                if (requestLocation != null) {

                                    Double distanceMiles = geoPointLocation.distanceInMilesTo(requestLocation);
                                    Double distanceOneDp = (double) Math.round(distanceMiles * 10) / 10;

                                    request.add(distanceOneDp.toString() + "Miles");
                                    requestLatitudes.add(requestLocation.getLatitude());
                                    requestLongitudes.add(requestLocation.getLongitude());
                                    username.add(object.getString("username"));

                                }
                            }



                        }else {
                            request.add("No Active Requests Nearby");
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(ViewRequestsActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    updateListView(lastKnownLocation);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);


        setTitle("Nearby Request");

        requestListView = findViewById(R.id.requestListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, request);

        request.clear();
        request.add("Getting Nearby Requests");
        requestListView.setAdapter((arrayAdapter));


        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                if(ContextCompat.checkSelfPermission(ViewRequestsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){

                Location lastKnownLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);



                if (requestLatitudes.size()>i && requestLongitudes.size()>i && username.size()>i && lastKnownLocation!=null) {


                    Intent intent= new Intent(getApplicationContext(),PhotographerLocationActivity.class);
                    intent.putExtra("requestLatitude",requestLatitudes.get(i));
                    intent.putExtra("requestLongitude",requestLongitudes.get(i));
                    intent.putExtra("driverLatitude",lastKnownLocation.getLatitude());
                    intent.putExtra("driverLongitude",lastKnownLocation.getLongitude());
                    intent.putExtra("username",username.get(i));




                    startActivity(intent);

                }

                }
            }
        });







        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateListView(location);

                ParseUser.getCurrentUser().put("Location",new ParseGeoPoint(location.getLatitude(),location.getLongitude()));
                ParseUser.getCurrentUser().saveInBackground();



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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            Location lastKnownLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null){

                updateListView(lastKnownLocation);
            }
        }

    }
}

package com.example.denny_pc.photgraph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    public void redirectActivity(){
        if (ParseUser.getCurrentUser().get("riderOrDriver").equals("rider") ){
            Intent intent= new Intent(getApplicationContext(),UserActivity.class);
            startActivity(intent);
        }else {
            Intent intent= new Intent(getApplicationContext(), ViewRequestsActivity.class);
            startActivity(intent);
        }
    }


    public void About(View view){
        Intent i = new Intent(this, AboutActivity.class);
        startActivity(i);

    }

    public  void Credits(View view){
        Intent i = new Intent(this, CreditsActivity.class);
        startActivity(i);
    }


    public void  getStarted(View view){
        Switch userTypeSwitch= findViewById(R.id.userTypeSwitch);

        Log.i("Switch value",String.valueOf(userTypeSwitch.isChecked()) );
        String userType="rider";

        if (userTypeSwitch.isChecked()){

            userType="driver";
        }

        ParseUser.getCurrentUser().put("riderOrDriver",userType);
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                redirectActivity();

            }
        });




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        if (ParseUser.getCurrentUser() == null){
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null){
                        Log.i("Info","Anonymous login successful");
                    }else {
                        Log.i("Info","Anonymous login failed"+e.toString());
                    }
                }
            });
        }

     //   pengecekan jika di database sudah ada lgsung di di direct ke rideractivity
        else {
            if (ParseUser.getCurrentUser().get("riderOrDriver")!= null){
                Log.i("Info","Rederecting "+ParseUser.getCurrentUser().get("riderOrDriver"));
                redirectActivity();
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}

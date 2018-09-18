package com.example.denny_pc.photgraph;


import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
//    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
//            .applicationId("1eb6384f71319553737f51b19ac429ee71f019b3")
//            .clientKey("c4cc7456a7e3a1021f63f247ed75de8ad6eb3b3f")
//            .server("http://18.218.13.67:80/parse/")
//            .build()
//    );
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("e1700814ba4da400d5c0c6e360f4efb196a34e0b")
                .clientKey("aa326ae0a31aed831844e98342e7695898bcfdb6")
                .server("http://18.219.2.52:80/parse/")
                .build()
        );

       // ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
//
//        ParseObject object = new ParseObject("ExampleObject");
//        object.put("myNumber", "123");
//        object.put("myString", "bekk");
//
//        object.saveInBackground(new SaveCallback () {
//            @Override
//            public void done(ParseException ex) {
//                if (ex == null) {
//                    Log.i("Parse Result", "Successful!");
//                } else {
//                    Log.i("Parse Result", "Failed" + ex.toString());
//                }
//            }
//        });




    }
}

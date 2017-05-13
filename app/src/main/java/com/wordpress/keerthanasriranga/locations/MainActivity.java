package com.wordpress.keerthanasriranga.locations;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private TextView get_place;
    int PLACE_PICKER_REQUEST=1;
    Button rateButton;
    float rating;
    RatingBar ratingBar;
    HashMap<LatLng,ArrayList<Float>> RateMap;
    String queriedLocation;
    Button searchButton;
    FirebaseDatabase fb;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_place=(TextView)findViewById(R.id.textview);
        rateButton = (Button)findViewById(R.id.rateButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        RateMap = new HashMap<>();
        searchButton=(Button)findViewById(R.id.search_button);
        final MainActivity myActivity = this;

        fb = FirebaseDatabase.getInstance();

        get_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(myActivity);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = String.format("Place: %s", place.getAddress());
                queriedLocation = place.getId();
                Log.v("Latlong is", "" + queriedLocation);
//                Log.v("Latitude is", "" + queriedLocation.latitude);
//                Log.v("Longitude is", "" + queriedLocation.longitude);


                get_place.setText(address);
            }
        }
    }


   public void doneRating(View view){
       rating=ratingBar.getRating();



       Log.i("Longitude is", "" + queriedLocation);
       DatabaseReference myref = fb.getReference(queriedLocation);
       myref = myref.push();
       //myref.chi
       myref.child("Rating").setValue(rating);
       Toast.makeText(this, "Thanks for Rating "+rating, Toast.LENGTH_LONG).show();
//       rateList=null;
       ArrayList<Float> rateList=new ArrayList<>();


   }

}

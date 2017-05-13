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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    DatabaseReference dref;
    ArrayList<String> rateList = new ArrayList<String>();
    int flag =0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        get_place=(TextView)findViewById(R.id.textview);
        rateButton = (Button)findViewById(R.id.rateButton);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

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
        if (requestCode == PLACE_PICKER_REQUEST && flag == 0) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String address = String.format("Place: %s", place.getAddress());
                queriedLocation = place.getId();
                Log.v("PlaceId is", "" + queriedLocation);
//                Log.v("Latitude is", "" + queriedLocation.latitude);
//                Log.v("Longitude is", "" + queriedLocation.longitude);


                get_place.setText(address);
            }
        }
            if (requestCode == PLACE_PICKER_REQUEST && flag == 1) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String address = String.format("Place: %s", place.getAddress());
                    queriedLocation = place.getId();
                    Log.v("Place Id is", "" + queriedLocation);
//                Log.v("Latitude is", "" + queriedLocation.latitude);
//                Log.v("Longitude is", "" + queriedLocation.longitude);


                    fetchrate();
                }

        }
    }

    public void fetchrate(){

        //dref = FirebaseDatabase.getInstance().getReference().child(queriedLocation);
        //Get datasnapshot at your "users" root node
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(queriedLocation);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of
                        // in datasnapshot
                        Double sum = 0.0;
                        Double avg;
                        //collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            Log.d("Snapshot:", item.getValue().toString());
                            rateList.add(item.getValue().toString());
                        }
                        Log.i("Ratings are : ", rateList.toString());
                       // Log.i("Values are " , rateList.toString());
                        //avg = sum/rateList.size();
                        //Log.i("Avg rating is : " , avg.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }

    private void collectPhoneNumbers(Map<String,Object> users) {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((Long) singleUser.get(queriedLocation));
        }

        System.out.println(phoneNumbers.toString());
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

   public void getRating(View view){
       PlacePicker.IntentBuilder buildit = new PlacePicker.IntentBuilder();
       Intent intent;
       flag = 1;
       final MainActivity myActivity = this;
       try {
           intent = buildit.build(myActivity);
           startActivityForResult(intent, PLACE_PICKER_REQUEST);
       } catch (GooglePlayServicesRepairableException e) {
           e.printStackTrace();
       } catch (GooglePlayServicesNotAvailableException e) {
           e.printStackTrace();
       }
   }

}

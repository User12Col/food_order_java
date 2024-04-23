package com.example.foodorder.Screens;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.contract.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorder.R;
import com.example.foodorder.adapter.PlacesAutoCompleteAdapter;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

//public class AddressActivity extends AppCompatActivity{
//    private EditText edtAddress;
//    private ActivityResultLauncher<Intent> addressActivityResultLauncher;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_address);
//
////        reference();
//
//        Places.initialize(AddressActivity.this, "AIzaSyA-7j-MqqDwYh80uVKoXkSOD1CpKynJlYE");
//        PlacesClient placesClient = Places.createClient(AddressActivity.this);
//
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
//
//        //Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//        autocompleteFragment.setPlaceFields(fields);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//
//            @Override
//            public void onError(@NonNull Status status) {
//                Toast.makeText(AddressActivity.this, "Lỗi: "+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                Toast.makeText(AddressActivity.this, "Place: " + place.getName() + ", " + place.getId(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
////        edtAddress.setFocusable(false);
////        edtAddress.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                List<Place.Field> fieldList = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
////                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fieldList).build(AddressActivity.this);
////                startActivityForResult(intent, 100);
////            }
////        });
//    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
////        if(requestCode == 100 && resultCode == RESULT_OK){
////            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
////        } else{
////            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
////        }
////    }
//
////    private void reference(){
////        edtAddress = findViewById(R.id.edtAddress);
////    }
//
//}

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

public class AddressActivity extends AppCompatActivity implements PlacesAutoCompleteAdapter.ClickListener{

    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Places.initialize(this, "AIzaSyA-7j-MqqDwYh80uVKoXkSOD1CpKynJlYE");

        recyclerView = (RecyclerView) findViewById(R.id.places_recycler_view);
        ((EditText) findViewById(R.id.place_search)).addTextChangedListener(filterTextWatcher);

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {recyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {recyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    @Override
    public void click(Place place) {
        Toast.makeText(this, place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
    }
}

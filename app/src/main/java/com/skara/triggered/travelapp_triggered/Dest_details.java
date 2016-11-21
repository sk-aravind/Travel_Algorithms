package com.skara.triggered.travelapp_triggered;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Dest_details extends AppCompatActivity {

    TextView title;
    TextView weblinktext;
    TextView descriptiontext;
    TextView operttext;
    ImageView imgview;

    String weblink ;
    String descript ;
    String opert ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dest_details);

        title = (TextView) this.findViewById(R.id.detail_titlename);
        weblinktext = (TextView) this.findViewById(R.id.weblink);
        descriptiontext = (TextView) this.findViewById(R.id.description);
        operttext = (TextView) this.findViewById(R.id.opert);
        imgview = (ImageView) this.findViewById(R.id.dest_photo);


        Intent i = getIntent();
        String name = i.getStringExtra("name");
        int img = i.getIntExtra("img", 0 );

        System.out.println("this is it"+HomeScreen.dest_list);

        for(HomeScreen.Destination d : HomeScreen.dest_list){
            if(d.name.equals(name)){
            weblink = d.weblink;
            descript = d.description;
            opert = d.operatingHours;
            }
        }


        System.out.println("weblink   "+weblink);
        title.setText(name);
        imgview.setImageResource(img);
        weblinktext.setText(weblink);
        descriptiontext.setText(descript);
        operttext.setText(opert);


        final Button button = (Button) findViewById(R.id.button_link);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

    }
//
//    private void setupMap() {
//        GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//
//        double lat = getIntent().getDoubleExtra("lat", 37.6329946);
//        double lng = getIntent().getDoubleExtra("lng", -122.4938344);
//        float zoom = getIntent().getFloatExtra("zoom", 15.0f);
//
//        LatLng position = new LatLng(lat, lng);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
//        map.addMarker(new MarkerOptions().position(position));
//    }



}

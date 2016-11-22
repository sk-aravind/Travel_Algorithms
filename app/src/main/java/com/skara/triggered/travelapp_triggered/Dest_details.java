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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Dest_details extends AppCompatActivity {

    TextView title;
    TextView descriptiontext;
    ImageView imgview;

    String weblink ;
    String descript ;
    String opert ;
    String trip ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dest_details);

        title = (TextView) this.findViewById(R.id.detail_titlename);
        descriptiontext = (TextView) this.findViewById(R.id.description);

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
            trip = d.tripadvisor;

            }
        }

        title.setText(name);
        imgview.setImageResource(img);
        descriptiontext.setText(descript);



        final ImageButton button = (ImageButton) findViewById(R.id.button_link);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse(weblink); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        final ImageButton button2 = (ImageButton) findViewById(R.id.button_link2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse(trip); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

    }

}


package com.skara.triggered.travelapp_triggered;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity implements DetailsInterface{

    private DrawerLayout mDrawerLayout;
    public static List<Destination> dest_list;
    public static ArrayList<Destination> iti_list;
    public static ArrayList<Destination> data_list;
    public static String algo_selected;
    public static ArrayList<TransportData.locations> locationsToGo;
    public static double budget;
    public static final TransportData.locations startingLocation = TransportData.getLocationEnum("Marina Bay Sands");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        // creating navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_reorder_black_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_itinerary:
                                goToItinerary(menuItem.getActionView());
                                break;
                            case R.id.maps:
                                goToMaps(menuItem.getActionView());
                                break;
                            default:
                                return true;
                        }
                        return true;
                    }
                });
    }

    public void showDestDetails(View view,String destName, Integer destPhoto){

        Intent intent = new Intent(this, Dest_details.class);
        intent.putExtra("name", destName);
        intent.putExtra("img", destPhoto);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this, view.findViewById(R.id.dest_photo), "img_transition");
        startActivity(intent, options.toBundle());

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        else if (id == R.id.action_search) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openRouteOptions(View view) {
        Intent intent = new Intent(this, Route_options.class);
        startActivity(intent);
    }

    // Firebase methods & creating view
    @Override
    public void onStart() {
        super.onStart();

        //creating recycling view
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        initializeData();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(dest_list,this);
        rv.setAdapter(adapter);

    }



    // Navigation Drawer ===================================================================
    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void goToItinerary(View v){
        Intent i = new Intent(HomeScreen.this,ItineraryMain.class);
        startActivity(i);
    }

    public void goToMaps(View v){
        Intent i = new Intent(HomeScreen.this,MapsActivity.class);
        startActivity(i);
    }

    //===============================================================================


    // Data ===============================================================================
    public static class Destination {
        String name;
        String id;
        String weblink;
        String tripadvisor;
        String description;
        String operatingHours;


        int photoId;

        Destination(String name, int photoId,String weblink, String description , String operatingHours ,String tripadvisor) {
            this.name = name;
            this.photoId = photoId;
            this.id = name.substring(0,1);
            this.weblink = weblink ;
            this.tripadvisor = tripadvisor ;
            this.description = description;
            this.operatingHours = operatingHours;
        }

        Destination(String name, int photoId) {
            this.name = name;
            this.photoId = photoId;
            this.id = name.substring(0,1);
        }

    }


    private void initializeData() {
        // Initialize only once
        if (iti_list == null) {
            dest_list = new ArrayList<>();
            iti_list = new ArrayList<>();
            locationsToGo = new ArrayList<>();
            dest_list.add(new Destination("Singapore Flyer", R.drawable.c, getString(R.string.SF_weblink), getString(R.string.SF_des), getString(R.string.SF_opert), getString(R.string.SF_trip)));
            dest_list.add(new Destination("Buddha Tooth Relic Temple", R.drawable.bt, getString(R.string.BTRT_weblink), getString(R.string.BTRT_des), getString(R.string.BTRT_opert), getString(R.string.BTRT_trip)));
            dest_list.add(new Destination("Vivo City", R.drawable.a, getString(R.string.VC_weblink), getString(R.string.VC_des), getString(R.string.VC_opert), getString(R.string.VC_trip)));
            dest_list.add(new Destination("Resort World Sentosa", R.drawable.rws, getString(R.string.RWS_weblink), getString(R.string.RWS_des), getString(R.string.RWS_opert), getString(R.string.RWS_trip)));
            dest_list.add(new Destination("Zoo", R.drawable.zoo, getString(R.string.Z_weblink), getString(R.string.Z_des), getString(R.string.Z_opert), getString(R.string.Z_trip)));
            data_list = new ArrayList<>(dest_list);
        }




    }

}
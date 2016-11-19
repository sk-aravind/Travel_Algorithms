
package com.skara.triggered.travelapp_triggered;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static com.skara.triggered.travelapp_triggered.R.layout.destination_card;

public class HomeScreen extends AppCompatActivity implements DetailsInterface{

    private DrawerLayout mDrawerLayout;
    public static ArrayList<Destination> iti_list;
    public static ArrayList<TransportData.locations> locationsToGo;
    public static final double budget = 15;
    public static final TransportData.locations startingLocation = TransportData.getLocationEnum("Marina Bay Sands");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //creating recycling view
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        initializeData();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(dest_list,this);
        rv.setAdapter(adapter);

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
                            default:
                                return true;
                        }
                        return true;
                    }
                });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void showDestDetails(View view,String destName, Integer destPhoto){

        Intent intent = new Intent(this, Dest_details.class);
        intent.putExtra("name", destName);
        intent.putExtra("img", destPhoto);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                // the context of the activity
                this,
                // For each shared element, add to this method a new Pair item,
                // which contains the reference of the view we are transitioning *from*,
                // and the value of the transitionName attribute
                // new Pair<>(view.findViewById(R.id.dest_name), "text_transition"),
                new Pair<>(view.findViewById(R.id.dest_photo), "img_transition"));
        ActivityCompat.startActivity(this, intent, options.toBundle());
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
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == android.R.id.home) {
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

    //===============================================================================


    // Data ===============================================================================
    public static class Destination {
        String name;

        int photoId;

        Destination(String name, int photoId) {
            this.name = name;
            this.photoId = photoId;
        }
    }

    public List<Destination> dest_list;
    // dest_list = destination list


    private void initializeData() {
        dest_list = new ArrayList<>();
        dest_list.add(new Destination("Marina Bay Sands", R.drawable.a));
        dest_list.add(new Destination("Singapore Flyer", R.drawable.b));
        dest_list.add(new Destination("Vivo City", R.drawable.c));
        dest_list.add(new Destination("Resort World Sentosa", R.drawable.a));
        dest_list.add(new Destination("Buddha Tooth Relic Temple", R.drawable.b));
        dest_list.add(new Destination("Zoo", R.drawable.c));

    }
    //===============================================================================
}
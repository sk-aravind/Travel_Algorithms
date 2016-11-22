
package com.skara.triggered.travelapp_triggered;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
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
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static android.R.attr.data;
import static android.R.id.list;
import static com.skara.triggered.travelapp_triggered.R.layout.destination_card;

public class HomeScreen extends AppCompatActivity implements DetailsInterface{

    private DrawerLayout mDrawerLayout;
    public static List<Destination> dest_list;
    public static ArrayList<Destination> iti_list;
    public static String algo_selected;
    public static ArrayList<TransportData.locations> locationsToGo;
    public static double budget;
    public static final TransportData.locations startingLocation = TransportData.getLocationEnum("Marina Bay Sands");

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Type of Auth
    private static final String TAG = "AnonymousAuth";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
//        getWindow().setExitTransition(fade);
//        getWindow().setEnterTransition(fade);



        //Sign into Firebase
        signInAnonymously();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // creating navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        postponeEnterTransition();

        final View decor = getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });

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

    //Firebase methods & creating view
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

        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                            Toast.makeText(HomeScreen.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        dest_list = new ArrayList<>();
        dest_list.add(new Destination("Singapore Flyer", R.drawable.b,getString(R.string.SF_weblink),getString(R.string.SF_des),getString(R.string.SF_opert),getString(R.string.SF_trip)));
        dest_list.add(new Destination("Vivo City", R.drawable.c,getString(R.string.VC_weblink),getString(R.string.VC_des),getString(R.string.VC_opert),getString(R.string.VC_trip)));
        dest_list.add(new Destination("Resort World Sentosa", R.drawable.a,getString(R.string.RWS_weblink),getString(R.string.RWS_des),getString(R.string.RWS_opert),getString(R.string.RWS_trip)));
        dest_list.add(new Destination("Buddha Tooth Relic Temple", R.drawable.b,getString(R.string.BTRT_weblink),getString(R.string.BTRT_des),getString(R.string.BTRT_opert),getString(R.string.BTRT_trip)));
        dest_list.add(new Destination("Zoo", R.drawable.c,getString(R.string.Z_weblink),getString(R.string.Z_des),getString(R.string.Z_opert),getString(R.string.Z_trip)));



    }
    //===============================================================================
}
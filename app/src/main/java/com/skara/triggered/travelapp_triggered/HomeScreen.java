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

public class HomeScreen extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;


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

        rv.addOnItemTouchListener(new RecyclerClickListener(this, new RecyclerClickListener.OnItemClickListener()
        {
            @Override
            public void onItemClick (View view, int position)
            {
                showDestDetails(view,dest_list.get(position));
            }
        }));


        //telling the recycler where to read data from
        RVAdapter adapter = new RVAdapter(dest_list);
        rv.setAdapter(adapter);


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
                        return true;
                    }
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    public void showDestDetails(View view,Destination destination){

        Intent intent = new Intent(this, Dest_details.class);
        intent.putExtra("name", destination.name);
        intent.putExtra("img", destination.photoId);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                // the context of the activity
                this,
                // For each shared element, add to this method a new Pair item,
                // which contains the reference of the view we are transitioning *from*,
                // and the value of the transitionName attribute
//                new Pair<>(view.findViewById(R.id.dest_name), "text_transition"),
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

            return super.onOptionsItemSelected(item);
        }

    public void openRouteOptions(View view) {
        Intent intent = new Intent(this, Route_options.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }




// Data ===============================================================================
        class Destination
            implements Serializable {
            String name;

            int photoId;

            Destination(String name, int photoId) {
                this.name = name;
                this.photoId = photoId;
            }
        }

        private List<Destination> dest_list;
        // dest_list = destination list


    private void initializeData() {
        dest_list = new ArrayList<>();
        dest_list.add(new Destination("THE ZOO", R.drawable.a));
        dest_list.add(new Destination("MARINA BAY", R.drawable.b));
        dest_list.add(new Destination("SUTD", R.drawable.c));
        dest_list.add(new Destination("THE ZOO", R.drawable.a));
        dest_list.add(new Destination("MARINA BAY", R.drawable.b));
        dest_list.add(new Destination("SUTD", R.drawable.c));

    }
    //===============================================================================


}



package com.skara.triggered.travelapp_triggered;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Route_options extends AppCompatActivity {
    public static final String ARG_PAGE = "ARG_PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void storeValue(View view){
        HomeScreen.algo_selected = "";

        EditText edit = (EditText)findViewById(R.id.budget_input);
        String budget = edit.getText().toString();

        RadioGroup rg = (RadioGroup)findViewById(R.id.radioButtonGrp);
        //get the algorithm selected here
        HomeScreen.algo_selected = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
        //get value of budget from here
        HomeScreen.budget = Double.valueOf(budget).doubleValue();

        Intent i = new Intent(this, ItineraryMain.class);
        i.putExtra(ARG_PAGE,2);
        startActivity(i);
}


}

package com.skara.triggered.travelapp_triggered;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Route_options extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        EditText edit = (EditText)findViewById(R.id.budget_input);
        String budget = edit.getText().toString();

        RadioGroup rg = (RadioGroup)findViewById(R.id.radioButtonGrp);
        String selectedRadio = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();

        String toDisplay = "$" + budget + " " + selectedRadio;
        Toast.makeText(view.getContext(), toDisplay, Toast.LENGTH_LONG).show();
        //get value of budget from here

//        HomeScreen.budget = Double.valueOf(budget).doubleValue();
    }


}

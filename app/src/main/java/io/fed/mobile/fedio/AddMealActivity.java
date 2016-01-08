package io.fed.mobile.fedio;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.app.ActionBar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.UUID;

public class AddMealActivity extends Activity {

    SearchView search;
    Entry meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        search = (SearchView) findViewById(R.id.searchView1);

        search.setQueryHint("What did you eat?");

//        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//            // TODO --- nothing at this time
////             Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
////             Toast.LENGTH_SHORT).show();
//            }
//        });

        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO-DONE --- update results list with Entry ArrayList -> Food.getResults(query)
                ListView lv = (ListView) findViewById(R.id.listView);
                ArrayList<Entry> results = Food.getResults(query);
                ArrayAdapter adapter = new ArrayAdapter<String>(AddMealActivity.this,R.id.listView, results);
                lv.setAdapter(adapter);

                // TODO-DONE --- get selected value from results
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        String selectedFromList =(String) (lv.getItemAtPosition(myItemInt));
                    }
                });

                //TODO --- check format of items in ListView and create Entry meal object
                //meal = new Entry(UUID.randomUUID(),selectedFromList.name, dose, calories per dose);
                //TextView textView = (TextView)findViewById(R.id.textViewDose);
                //textView.setText(meal.getDose);

//                Toast.makeText(getBaseContext(), query,
//                        Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO --- nothing at this time
//                Toast.makeText(getBaseContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }



    public void buttonOnClick(View v){

        Spinner timeOfDay=(Spinner) findViewById(R.id.spinnerTimeOfDay);
        String timeOfDayTxt = timeOfDay.getSelectedItem().toString();
        //meal.setTimeOfDay = timeOfDayTxt;

        // TODO - DONE --- itemId cannot be class field, it needs to be unique across the COMPLETE data set: ex. md5 function on ( itemName + new Date() )

        // adding data to custom parse table
        // ParseObject userData = new ParseObject("UserData");
        // userData.put("itemId", meal.getItemId);
        // userData.put("itemName", meal.getItemName);
       //  userData.put("timeOfDay", meal.getTimeOfDay);
       //  userData.put("dose", meal.getDose);
       //  userData.put("caloriesPerDose", meal.getCaloriesPerDose);

       //  userData.put("createdBy", ParseUser.getCurrentUser().getUsername());
       //  userData.saveInBackground();     // asynchronous adding of data to Parse UserData table

    }



}

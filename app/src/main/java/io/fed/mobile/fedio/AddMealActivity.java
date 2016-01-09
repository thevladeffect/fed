package io.fed.mobile.fedio;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

public class AddMealActivity extends Activity {

    private Entry meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SearchView search;

        class EntryAdapter extends ArrayAdapter<Entry> {
            public EntryAdapter(Context context, ArrayList<Entry> users) {
                super(context, 0, users);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the data item for this position
                Entry entry = getItem(position);
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_row, parent, false);
                }
                // Lookup view for data population
                TextView name = (TextView) convertView.findViewById(R.id.itemName);
                TextView calories = (TextView) convertView.findViewById(R.id.caloriesPerDose);
                // Populate the data into the template view using the data object
                name.setText(entry.getItemName());
                calories.setText(entry.getCaloriesPerDose() + "");
                // Return the completed view to render on screen
                return convertView;
            }
        }

        final Context context = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_meal);

        search = (SearchView) findViewById(R.id.searchView1);

        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                final ListView lv = (ListView) findViewById(R.id.listView);
                ArrayList<Entry> results = Food.getResults(context, query);
                EntryAdapter adapter = new EntryAdapter(context, results);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                        meal = (Entry) (lv.getItemAtPosition(myItemInt));
                        // TODO --- color the selected item in the list so that it looks "selected"
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private int generateUniqueId(String seed){
        return seed.hashCode();
    };

    public void acceptButtonClick(View v){

        Spinner timeOfDay = (Spinner) findViewById(R.id.spinnerTimeOfDay);
        String timeOfDayTxt = timeOfDay.getSelectedItem().toString().toLowerCase();
        EditText dose = (EditText) findViewById(R.id.editText);
        double doseValue = Double.parseDouble(dose.getText().toString());

        meal.setTimeOfDay(timeOfDayTxt);
        meal.setDose(doseValue);
        meal.setItemId(generateUniqueId(meal.getItemName() + new Date()));

        ParseObject userData = new ParseObject("UserData");

        userData.put("itemId", meal.getItemId());
        userData.put("itemName", meal.getItemName());
        userData.put("timeOfDay", meal.getTimeOfDay());
        userData.put("dose", meal.getDose());
        userData.put("caloriesPerDose", meal.getCaloriesPerDose());
        userData.put("createdBy", ParseUser.getCurrentUser().getUsername());

        try {
            userData.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent output = new Intent();
        output.putExtra("ACCEPT", 1);
        setResult(RESULT_OK, output);
        finish();

    }



}

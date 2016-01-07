package io.fed.mobile.fedio;


import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.app.ActionBar;
import android.widget.AdapterView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class AddMealActivity extends Activity {

    SearchView search;

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
                // TODO --- update results list with Entry ArrayList -> Food.getResults(query)



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

        // TODO --- itemId cannot be class field, it needs to be unique across the COMPLETE data set: ex. md5 function on ( itemName + new Date() )

        // adding data to custom parse table
        // ParseObject userData = new ParseObject("UserData");
        // userData.put("itemId", );
        // userData.put("itemName", );
        // userData.put("createdBy", ParseUser.getCurrentUser().getUsername());
        // userData.saveInBackground();     // asynchronous adding of data to Parse UserData table

    }



}

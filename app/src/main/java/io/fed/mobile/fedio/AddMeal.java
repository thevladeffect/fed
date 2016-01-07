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

public class AddMeal extends Activity {

    private int id=0;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        search=(SearchView) findViewById(R.id.searchView1);
        search.setQueryHint("SearchView");

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Toast.makeText(getBaseContext(), String.valueOf(hasFocus),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                Toast.makeText(getBaseContext(), query,
                        Toast.LENGTH_SHORT).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                Toast.makeText(getBaseContext(), newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }



    public void buttonOnClick(View v){
        // adding data to custom parse table
        //ParseObject userData = new ParseObject("UserData");
       // userData.put("itemId",id);
       // userData.put("itemName", );
       // userData.put("createdBy", ParseUser.getCurrentUser().getUsername());
        //id++;
       // userData.saveInBackground();     // adding data to custom parse table

    }



}

package io.fed.mobile.fedio;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.List;

public class MainActivity extends Activity {

    private final int RES = 1;
    private ParseObject userData;
    private List<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        if(ParseUser.getCurrentUser() == null){
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            Intent parseLogin = builder.build();
            startActivityForResult(parseLogin, RES);
        }

//        ArrayAdapter<Entry> adapter = new MainAdapter(this,R.layout.main_row_layout,entries);
//        setListAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RES){
            if(resultCode == RESULT_OK){
                userData = new ParseObject("UserData");
//                userData.put("itemId",1);
//                userData.put("itemName","Banana");
//                userData.put("createdBy", ParseUser.getCurrentUser().getUsername());
//                userData.saveInBackground();
            }
        }
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//    }
}

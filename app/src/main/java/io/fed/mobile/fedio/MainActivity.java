package io.fed.mobile.fedio;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private final int LIST_ITEM_TYPE_HEADER = 0;
    private final int LIST_ITEM_TYPE_CONTENT = 1;
    private final int LIST_ITEM_TYPE_COUNT = 2;

    // TODO remove
    private final int LIST_ITEM_TYPE_1_COUNT = 5;

    private final int RES = 1;
    private ParseObject userData;
    private List<Entry> entries;

    private CustomListAdapter adapter;

    private class CustomListAdapter extends BaseAdapter {

        private ArrayList<String> mData = new ArrayList<String>();
        private LayoutInflater mInflater;

        public CustomListAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final String item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            String content = getItem(position);
            if(!content.contains("***"))
                return LIST_ITEM_TYPE_HEADER;
            else
                return LIST_ITEM_TYPE_CONTENT;
        }

        @Override
        public int getViewTypeCount() {
            return LIST_ITEM_TYPE_COUNT;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                switch(type) {
                    case LIST_ITEM_TYPE_HEADER:
                        convertView = mInflater.inflate(R.layout.list_item_type_header, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.list_item_type1_text_view);
                        break;
                    case LIST_ITEM_TYPE_CONTENT:
                        convertView = mInflater.inflate(R.layout.list_item_type_content, null);
                        holder.textView = (TextView)convertView.findViewById(R.id.list_item_type2_button);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(mData.get(position));
            return convertView;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Parse initialization
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, getString(R.string.parse_application_id), getString(R.string.parse_client_key));
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        // login if not logged in
        if(ParseUser.getCurrentUser() == null){
            ParseLoginBuilder builder = new ParseLoginBuilder(MainActivity.this);
            Intent parseLogin = builder.build();
            startActivityForResult(parseLogin, RES);
        }

        // create and populate adapter
        adapter = new CustomListAdapter();
        for(Entry entry : getEntries(ParseUser.getCurrentUser().getUsername())){
            String item = entry.getItemId() == -1 ? entry.getTimeOfDay() : entry.getItemName() + "***" + entry.getDose()*getDoseEnergy(entry.getItemName()) + "***" +entry.getTimeOfDay();
            adapter.addItem(item);
        }
        setListAdapter(adapter);

    }

    private ArrayList<Entry> getEntries(String user){
        ArrayList<Entry> list = new ArrayList<Entry>();

        // TODO get actual data from Parse for @user

        list.add(new Entry(-1,"","breakfast",0));
        list.add(new Entry(1,"Banana","breakfast", 1.0));
        list.add(new Entry(2,"Coffee","breakfast",1.0));
        list.add(new Entry(-1,"","lunch",0));
        list.add(new Entry(3,"BLT","lunch",0.75));
        list.add(new Entry(4,"Coke","lunch",0.33));
        list.add(new Entry(-1,"","dinner",0));
        list.add(new Entry(5,"French toast","dinner",2.0));

        return list;
    }

    private double getDoseEnergy(String item){

        // TODO return calories per dose for @item

        return 400.00;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RES){
            if(resultCode == RESULT_OK){
                // populate adapter
                for(Entry entry : getEntries(ParseUser.getCurrentUser().getUsername())){
                    String item = entry.getItemId() == -1 ? entry.getTimeOfDay() : entry.getItemName() + "***" + entry.getDose()*getDoseEnergy(entry.getItemName()) + "***" +entry.getTimeOfDay();
                    adapter.addItem(item);
                }
                adapter.notifyDataSetChanged();

                // adding data to custom parse table

//                userData = new ParseObject("UserData");
//                userData.put("itemId",1);
//                userData.put("itemName","Banana");
//                userData.put("createdBy", ParseUser.getCurrentUser().getUsername());
//                userData.saveInBackground();

            }
        }
    }

    public static class ViewHolder {
        public TextView textView;
    }
}

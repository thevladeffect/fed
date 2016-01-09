package io.fed.mobile.fedio;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends ListActivity {

    private final int LIST_ITEM_TYPE_HEADER = 0;
    private final int LIST_ITEM_TYPE_CONTENT = 1;

    public static final int RES = 1;

    private CustomListAdapter adapter;

    private Date date;

    private class CustomListAdapter extends BaseAdapter {

        private ArrayList<String> mData = new ArrayList<>();
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
            if(!content.contains(" - "))
                return LIST_ITEM_TYPE_HEADER;
            else
                return LIST_ITEM_TYPE_CONTENT;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
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
            ViewHolder holder;
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
                assert convertView != null;
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
        if(date == null) date = new Date();
        if(ParseUser.getCurrentUser() != null)
        for(Entry entry : getEntries(ParseUser.getCurrentUser().getUsername(), date)){
            String item = entry.getItemId() == -1 ? entry.getTimeOfDay() : entry.getItemName() + " - " + entry.getDose()*entry.getCaloriesPerDose();
            adapter.addItem(item);
        }
        setListAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent myIntent = new Intent(this,AddMealActivity.class);
                myIntent.putExtra("date",date.getTime());
                startActivityForResult(myIntent, RES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // returns the diary entries for the @user on the @date
    private ArrayList<Entry> getEntries(String user, Date date){

        class CustomEntrySorter implements Comparator<Entry> {

            @Override
            public int compare(Entry lhs, Entry rhs) {
                switch (lhs.getTimeOfDay()) {
                    case "breakfast":
                        if (!rhs.getTimeOfDay().equals("breakfast")) return -1;
                        break;
                    case "lunch":
                        if (rhs.getTimeOfDay().equals("breakfast")) return 1;
                        else if (rhs.getTimeOfDay().equals("dinner")) return -1;
                        break;
                    case "dinner":
                        if (!rhs.getTimeOfDay().equals("dinner")) return 1;
                        break;
                }
                return 0;
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Date begin = calendar.getTime();

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1);

        Date end = calendar.getTime();

        final ArrayList<Entry> list = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserData");
        query.whereEqualTo("createdBy", user);
        query.whereGreaterThanOrEqualTo("createdFor", begin);
        query.whereLessThan("createdFor", end);
        query.addAscendingOrder("createdFor");
        try {
            for (ParseObject entry : query.find()) {
                list.add(new Entry(entry.getInt("itemId"), entry.getString("itemName"),
                        entry.getString("timeOfDay"), entry.getDouble("dose"), entry.getDouble("caloriesPerDose")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Collections.sort(list, new CustomEntrySorter());

        if(getIndexOf("breakfast",list) != -1)
            list.add(getIndexOf("breakfast", list), new Entry(-1, "", "Breakfast", 0, 0));

        if(getIndexOf("lunch",list) != -1)
            list.add(getIndexOf("lunch",list),new Entry(-1,"","Lunch",0, 0));

        if(getIndexOf("dinner",list) != -1)
            list.add(getIndexOf("dinner",list),new Entry(-1,"","Dinner",0, 0));

        double totalCalories = 0.00;
        for(Entry entry : list) totalCalories += entry.getDose() * entry.getCaloriesPerDose();
        list.add(new Entry(-1,"","Total calories: " + totalCalories,0, 0));

        return list;
    }

    private void setDateText(){
        TextView dateText = (TextView) findViewById(R.id.date);
        String formattedDate = "";
        DateFormat df = new SimpleDateFormat("E, dd MMMM yyyy");

        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

        Calendar c2 = Calendar.getInstance(); // today

        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_YEAR, 1); // tomorrow

        Calendar c4 = Calendar.getInstance();
        c4.setTime(date);

        if (c1.get(Calendar.YEAR) == c4.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c4.get(Calendar.DAY_OF_YEAR)) {
            formattedDate = "Yesterday";
        }
        else if (c2.get(Calendar.YEAR) == c4.get(Calendar.YEAR)
                && c2.get(Calendar.DAY_OF_YEAR) == c4.get(Calendar.DAY_OF_YEAR)){
            formattedDate = "Today";
        }
        else if (c3.get(Calendar.YEAR) == c4.get(Calendar.YEAR)
                && c3.get(Calendar.DAY_OF_YEAR) == c4.get(Calendar.DAY_OF_YEAR)){
            formattedDate = "Tomorrow";
        }
        else{
            formattedDate = df.format(date);
        }

        dateText.setText(formattedDate);
    }

    private void increaseDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        date = calendar.getTime();
    }

    private void decreaseDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
    }

    // returns the index of the entry with @timeOfDay in @list
    private int getIndexOf(String timeOfDay, ArrayList<Entry> list){
        for(Entry entry : list){
            if(entry.getTimeOfDay().equals(timeOfDay)) return list.indexOf(entry);
        }
        return -1;
    }

    public void leftButtonClick(View v){
        adapter = new CustomListAdapter();
        decreaseDate();
        setDateText();
        for(Entry entry : getEntries(ParseUser.getCurrentUser().getUsername(), date)){
            String item = entry.getItemId() == -1 ? entry.getTimeOfDay() : entry.getItemName() + " - " + entry.getDose()*entry.getCaloriesPerDose();
            adapter.addItem(item);
        }
        setListAdapter(adapter);
    }

    public void rightButtonClick(View v){
        adapter = new CustomListAdapter();
        increaseDate();
        setDateText();
        for(Entry entry : getEntries(ParseUser.getCurrentUser().getUsername(), date)){
            String item = entry.getItemId() == -1 ? entry.getTimeOfDay() : entry.getItemName() + " - " + entry.getDose()*entry.getCaloriesPerDose();
            adapter.addItem(item);
        }
        setListAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RES){
            if(resultCode == RESULT_OK){
                // populate adapter
                adapter = new CustomListAdapter();
                if(date == null) date = new Date();
                for(Entry entry : getEntries(ParseUser.getCurrentUser().getUsername(), date)){
                    String item = entry.getItemId() == -1 ? entry.getTimeOfDay() : entry.getItemName() + " - " + entry.getDose()*entry.getCaloriesPerDose();
                    adapter.addItem(item);
                }
                setListAdapter(adapter);

            }
        }
    }

    public static class ViewHolder {
        public TextView textView;
    }
}

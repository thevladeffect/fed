package io.fed.mobile.fedio;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Vlad on 05-Jan-16.
 */
public class Food {

    // returns a list of foods matching the @keyword parameter
    public static ArrayList<Entry> getResults(Context context, String keyword){

        ArrayList<Entry> list = new ArrayList<Entry>();
        Entry entry;

        InputStream is = context.getResources().openRawResource(R.raw.food);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        String data;
        try {
            while ((data = reader.readLine()) != null)
            {
                String pattern = "~[0-9]{5}~\\^~([A-Z,\\- ]*)~\\^[\\d\\.]+\\^([\\d\\.]+)\\^";
                Pattern pat = Pattern.compile(pattern);
                Matcher match = pat.matcher(data);

                if(match.find()){

                    String description = match.group(1);

                    if(description.contains(keyword.toUpperCase())){
                        entry = new Entry();

                        entry.setCaloriesPerDose(Double.parseDouble(match.group(2)));
                        entry.setItemName(description);

                        list.add(entry);
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

}

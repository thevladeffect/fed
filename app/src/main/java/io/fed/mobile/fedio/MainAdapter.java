package io.fed.mobile.fedio;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by alecu_000 on 13-Nov-15.
 */
public class MainAdapter extends ArrayAdapter<Entry>{
    public MainAdapter(Context context, int resource, List<Entry> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        return null;
    }
}

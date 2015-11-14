package edu.osu.restchooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by paranjay on 11/14/15.
 */

public class MySimpleArrayAdapter extends ArrayAdapter<Restaurant> {
    private final Context context;
    private final List<Restaurant> values;

    public MySimpleArrayAdapter(Context context, List<Restaurant> restaurants) {
        super(context, -1, restaurants);
        this.context = context;
        this.values = restaurants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.restaurant_row_layout, parent, false);
        TextView restName = (TextView)rowView.findViewById(R.id.nameText);
        TextView restRating = (TextView)rowView.findViewById(R.id.reviewRatingText);

        restName.setText(values.get(position).businessId);
        restRating.setText(values.get(position).reviewRating);

        return rowView;
    }
}
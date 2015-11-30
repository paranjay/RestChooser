package edu.osu.restchooser;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantsFragment extends FragmentActivity implements
        AdapterView.OnItemClickListener {

    public RestaurantsFragment() {
    }

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_restaurants);

        listView = (ListView)findViewById(R.id.restaurantList);
        List<Restaurant> restaurants = new DatabaseHelper(this).GetAllRestaurants();

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, restaurants);
        ArrayAdapter adapter1 = new MySimpleArrayAdapter(this, restaurants);
        listView.setAdapter(adapter1);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Toast.makeText(getApplicationContext(),
                "Click ListItem Number " + position, Toast.LENGTH_LONG)
                .show();
    }
}

package se.parkourspots.view;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.controller.MarkerHandler;
import se.parkourspots.model.Spot;

/**
 * Created by Gabriel on 12/08/2015.
 */

public class SearchActivity extends ListActivity {

    private SimpleCursorAdapter mAdapter;
    private String[] spotNames;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (ListView) findViewById(R.id.listView);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SPOT", "Searched  = " + query);
            search(query);
        }
    }

    private void search(String query) {
        ArrayList<Spot> spots = MarkerHandler.getInstance().getSpots();

        spotNames = new String[spots.size()];
        int i = 0;
        for (Spot spot : spots) {
            if (spot.getName().contains(query)) {
                spotNames[i] = spot.getName();
                i++;
            }
        }
        ArrayAdapter<Spot> adapter = new ArrayAdapter<Spot>(this, android.R.layout.simple_list_item_1, spots);
        setListAdapter(adapter);

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO : Do something when a list item is clicked
    }
}

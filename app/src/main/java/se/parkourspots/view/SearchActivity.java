package se.parkourspots.view;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.controller.SpotHandler;
import se.parkourspots.model.Spot;

/**
 * The activity stared when a user searches for a spot.
 */
public class SearchActivity extends ListActivity {

    public static final String EXTRA_MESSAGE_SPOT_NAME = "se.parkourspots.view.SearchActivity";
    private TextView queryText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        queryText = (TextView) findViewById(R.id.queryText);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Handles the intent and calls the search method.
     *
     * @param intent The intent to handle
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).toLowerCase();
            queryText.setText(query);
            search(query);
        }
    }

    /**
     * Called when a user should search for a spot with the given string query.
     *
     * @param query The string to search spot name for.
     */
    private void search(String query) {
        ArrayList<Spot> spots = SpotHandler.getInstance().getSpots();
        ArrayList<String> spotNames = new ArrayList<>();
        for (Spot spot : spots) {
            if (spot.getName().toLowerCase().contains(query)) {
                spotNames.add(spot.getName());
            }
        }
        if (!spotNames.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, spotNames);
            setListAdapter(adapter);
        }
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Intent intent = new Intent(this, SpotInfoActivity.class);
        String spotName = (String) listView.getItemAtPosition(position);
        intent.putExtra(EXTRA_MESSAGE_SPOT_NAME, spotName);

        startActivity(intent);
    }
}

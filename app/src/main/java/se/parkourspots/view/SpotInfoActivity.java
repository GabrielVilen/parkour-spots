package se.parkourspots.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.connector.SpotHandler;
import se.parkourspots.connector.SpotInfoWindowAdapter;
import se.parkourspots.model.Spot;
import se.parkourspots.util.Keyboard;

/**
 * This class represent the info activity which is displayed when a user clicks a marker.
 * The class contains information about the spot.
 */
public class SpotInfoActivity extends AppCompatActivity {

    private EditText spotTitle, description, difficulty, groundMaterial, goodFor, size;
    private final ArrayList<EditText> textViews = new ArrayList<>();
    private boolean inEditMode, isEdited;
    private Spot spot;
    private SpotHandler spotHandler;
    private SpotInfoWindowAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_info);
        isEdited = false;

        textViews.add(groundMaterial = (EditText) findViewById(R.id.groundMaterialInfoActivity));
        textViews.add(description = (EditText) findViewById(R.id.descriptionInfoActivity));
        textViews.add(difficulty = (EditText) findViewById(R.id.difficultyInfoActivity));
        textViews.add(spotTitle = (EditText) findViewById(R.id.spotTitleInfoActivity));
        textViews.add(goodFor = (EditText) findViewById(R.id.goodForInfoActivity));
        textViews.add(size = (EditText) findViewById(R.id.sizeInfoActivity));
        ImageView photoView1 = (ImageView) findViewById(R.id.photo1InfoActivity);

        spotHandler = SpotHandler.getInstance();
        adapter = new SpotInfoWindowAdapter(this);

        Intent intent = getIntent();
        if (intent.hasExtra(SpotInfoWindowAdapter.EXTRA_MESSAGE_SPOT_LATLNG)) {
            spot = spotHandler.getSpot((LatLng) intent.getParcelableExtra(SpotInfoWindowAdapter.EXTRA_MESSAGE_SPOT_LATLNG));
        } else if (intent.hasExtra(SearchActivity.EXTRA_MESSAGE_SPOT_NAME)) {
            spot = spotHandler.getSpot(intent.getStringExtra(SearchActivity.EXTRA_MESSAGE_SPOT_NAME));
        }

        if (spot != null) {
            spotTitle.setText(spot.getName());
            description.setText(spot.getDescription());
            difficulty.setText(spot.getDifficulty());
            goodFor.setText(spot.getGoodFor());
            size.setText(spot.getSize());
            groundMaterial.setText(spot.getMaterial());
            if (spot.getBitmap() != null) {
                photoView1.setImageBitmap(spot.getBitmap());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spot_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit:
                editSpotInfo();
                break;
            case R.id.delete:
                deleteSpotInfo();
                break;
        }

        return true;
    }

    /**
     * Called when the user clicks the edit button in the actionbar in this activity.
     * Makes the fields editable in this activity.
     */
    private void editSpotInfo() {
        boolean isFocusable;
        int color, inputType;

        inEditMode = !inEditMode;
        if (inEditMode) {
            isEdited = true;
            isFocusable = true;
            color = Color.LTGRAY;
            inputType = InputType.TYPE_CLASS_TEXT;
        } else {
            isFocusable = false;
            color = Color.TRANSPARENT;
            inputType = InputType.TYPE_NULL;
            Keyboard.hideKeyboard(this);
        }
        for (int i = 0; i < textViews.size(); i++) {
            EditText text = textViews.get(i);
            text.setBackgroundColor(color);
            text.setInputType(inputType);
            text.setFocusable(isFocusable);
            text.setFocusableInTouchMode(isFocusable);
        }
    }

    /**
     * Called when the user clicks the delete button in the actionbar in this activity.
     * Alerts the user if the spot should be deleted, and if so deletes it.
     */
    private void deleteSpotInfo() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Are you sure you want to delete this spot?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(SpotInfoActivity.this, "Spot deleted", Toast.LENGTH_SHORT).show();
                        spotHandler.deleteSpot(spot);
                        onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alert.create().show();
    }

    @Override
    public void onBackPressed() {
        if (isEdited) {
            spot.setName(spotTitle.getText().toString());
            spot.setDescription(description.getText().toString());
            spot.setDifficulty(difficulty.getText().toString());
            spot.setGoodFor(goodFor.getText().toString());
            spot.setMaterial(groundMaterial.getText().toString());
            if (spotHandler == null) {
                spotHandler = SpotHandler.getInstance();
            }
            adapter.updateContent(spotHandler.getMarker(spot));
        }
        NavUtils.navigateUpFromSameTask(this);
    }


}

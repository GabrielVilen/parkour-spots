package se.parkourspots.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.controller.MarkerHandler;
import se.parkourspots.controller.SpotInfoWindowAdapter;
import se.parkourspots.model.Spot;

public class SpotInfoActivity extends AppCompatActivity {

    private EditText spotTitle, description, difficulty, size, groundMaterial, goodFor;
    private ArrayList<EditText> textViews = new ArrayList<>();
    private ImageView photoView1;
    private boolean inEditMode;
    private Spot spot;
    private boolean isEdited;


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
        photoView1 = (ImageView) findViewById(R.id.photo1InfoActivity);

        spot = MarkerHandler.getInstance().getSpot((LatLng) getIntent().getParcelableExtra(SpotInfoWindowAdapter.EXTRA_MESSAGE_SPOT));

        if (spot != null) {
            spotTitle.setText(spot.getName());
            description.setText(spot.getDescription());
            difficulty.setText(spot.getDifficulty());
            goodFor.setText(spot.getGoodFor());
            size.setText(spot.getSize());
            groundMaterial.setText(spot.getMaterial());
            photoView1.setImageBitmap(spot.getPhoto());
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

    @Override
    public void onBackPressed() {
        if (isEdited) {
            Log.d("SPOT", "saving state.");
            spot.setName(spotTitle.getText().toString());
            spot.setDescription(description.getText().toString());
            spot.setDifficulty(difficulty.getText().toString());
            spot.setGoodFor(goodFor.getText().toString());
            spot.setMaterial(groundMaterial.getText().toString());
            SpotInfoWindowAdapter.getStaticAdapter().updateContent(spot.getMarker()); // TODO: se över
        }
        finish();
    }


    public void editSpotInfo() {
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

    public void deleteSpotInfo() {
        // TODO
    }


}

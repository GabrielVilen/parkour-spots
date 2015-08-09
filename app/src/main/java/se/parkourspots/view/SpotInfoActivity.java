package se.parkourspots.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import se.parkourspots.R;
import se.parkourspots.controller.SpotInfoWindowAdapter;
import se.parkourspots.model.Spot;

public class SpotInfoActivity extends AppCompatActivity {

    private TextView twSpotTitle, twDescription, twDifficulty, twSize, twGroundMaterial, twGoodFor;
    private ImageView photoView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_info);

        twSpotTitle = (TextView) findViewById(R.id.spotTitleInfoActivity);
        twDescription = (TextView) findViewById(R.id.descriptionInfoActivity);
        twGroundMaterial = (TextView) findViewById(R.id.groundMaterialInfoActivity);
        twDifficulty = (TextView) findViewById(R.id.difficultyInfoActivity);
        twSize = (TextView) findViewById(R.id.sizeInfoActivity);
        twGoodFor = (TextView) findViewById(R.id.goodForInfoActivity);
        photoView1 = (ImageView) findViewById(R.id.photo1InfoActivity);

        Spot spot = getIntent().getParcelableExtra(SpotInfoWindowAdapter.EXTRA_MESSAGE_SPOT);

        if (spot != null) {
            twSpotTitle.setText(spot.getName());
            twDescription.setText(spot.getDescription());
            twDifficulty.setText(spot.getDifficulty());
            twGoodFor.setText(spot.getGoodFor());
            twSize.setText(spot.getSize());
            twGroundMaterial.setText(spot.getMaterial());
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
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

}

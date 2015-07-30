package se.parkourspots.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import se.parkourspots.R;
import se.parkourspots.model.Spot;

public class SpotInfoActivity extends AppCompatActivity {

    TextView twSpotTitle, twDescription;
    ImageView photoView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_info);

        twSpotTitle = (TextView) findViewById(R.id.spotTitleInfoActivity);
        photoView1 = (ImageView) findViewById(R.id.photo1InfoActivity);

        Spot spot = (Spot) getIntent().getSerializableExtra(SpotInfoWindowAdapter.EXTRA_MESSAGE_SPOT);

        twSpotTitle.setText(spot.getName());
        photoView1.setImageBitmap(spot.getPhoto());

        // MarkerHandler.getInstance().getSpot()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        CustomActionBarInflater.getInstance().inflateCustomActionBar(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

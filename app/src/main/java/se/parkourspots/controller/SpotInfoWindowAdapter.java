package se.parkourspots.controller;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import se.parkourspots.R;
import se.parkourspots.model.Spot;
import se.parkourspots.view.SpotInfoActivity;


/**
 * Created by Gabriel on 28/07/2015.
 */
public class SpotInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private SpotInfoWindowAdapter adapter;
    private final Activity activity;
    private Spot spot;
    public final static String EXTRA_MESSAGE_SPOT_LATLNG = "se.parkourspots.view.SpotInfoWindowAdapter";

    public SpotInfoWindowAdapter(Activity activity) {
        this.activity = activity;
        adapter = this;
    }

    /**
     * Called before <code>getInfoContents</code> on the marker. Customizes the entire info window.
     *
     * @param marker The clicked marker
     * @return The view to inflate or null
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     * Called if null is returned from <code>getInfoWindow</code>. Customizes only the content of the info window.
     *
     * @param marker The clicked marker
     * @return The view to inflate or null
     */
    @Override
    public View getInfoContents(Marker marker) {
        View view = activity.getLayoutInflater().inflate(R.layout.info_window, null);

        ImageView icon = (ImageView) view.findViewById(R.id.spotIconInfoWindow);
        TextView title = (TextView) view.findViewById(R.id.spotTitleInfoWindow);

        SpotHandler handler = SpotHandler.getInstance();
        spot = handler.getSpot(marker);
        if (spot != null) {
            if (spot.getName() != null) {
                title.setText(spot.getName());
            }
            if (spot.getBitmap() != null) {
                icon.setBackground(null);
                icon.setImageBitmap(spot.getBitmap());
            }
        }

        return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(activity, SpotInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE_SPOT_LATLNG, marker.getPosition());
        activity.startActivity(intent);
    }

    public void updateContent(Marker marker) {
        adapter.getInfoContents(marker);
        marker.showInfoWindow();
    }

}

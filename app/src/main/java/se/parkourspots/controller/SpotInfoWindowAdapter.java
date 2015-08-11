package se.parkourspots.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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

    private static SpotInfoWindowAdapter adapter;
    private final Activity activity;
    private Spot spot;
    public final static String EXTRA_MESSAGE_SPOT = "se.parkourspots.view.SPOT";

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
        Log.d("SPOT", "getInfoContents"); // TODO: KALLA PÅ METODEN DÅ SPOT ÄNDRATS
        View view = activity.getLayoutInflater().inflate(R.layout.info_window, null);

        ImageView icon = (ImageView) view.findViewById(R.id.spotIconInfoWindow);
        TextView title = (TextView) view.findViewById(R.id.spotTitleInfoWindow);

        MarkerHandler handler = MarkerHandler.getInstance();
        spot = handler.getSpot(marker);
        if (spot != null) {
            if (spot.getName() != null) {
                title.setText(spot.getName());
            }
            if (spot.getPhoto() != null) {
                icon.setBackground(null);
                icon.setImageBitmap(spot.getPhoto());
            }
        }

        return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(activity, SpotInfoActivity.class);
        intent.putExtra(EXTRA_MESSAGE_SPOT, marker.getPosition());
        activity.startActivity(intent);
    }

    public static SpotInfoWindowAdapter getStaticAdapter() {
        return adapter;
    }

    public void updateContent(Marker marker) {
        getInfoContents(marker);
    }
}

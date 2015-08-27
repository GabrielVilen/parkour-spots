package se.parkourspots.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.Map;

import se.parkourspots.connector.SpotHandler;
import se.parkourspots.model.Spot;

/**
 * Class which contains methods for saving and retrieving key-value data pairs by using <Code>SharedPreferences</Code>.
 */
public class SharedPreferencesSaver {

    /**
     * Saves the data in the application to a <Code>SharedPreferences</Code>.
     *
     * @param activity The activity to get the preferences from.
     */
    public static void saveSharedPreferences(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Map map = SpotHandler.getInstance().getMap();
        Iterator it = map.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Spot spot = (Spot) entry.getValue();
            Marker marker = (Marker) entry.getKey();

            spot.saveSharedPreferences(editor, i);

            editor.putFloat("LAT" + i, (float) marker.getPosition().latitude);
            editor.putFloat("LONG" + i, (float) marker.getPosition().longitude);
            i++;
        }
        editor.putInt("I", i);

        editor.apply();

    }

    /**
     * Restores the data in the application using a <Code>SharedPreferences</Code>.
     *
     * @param activity The activity to get the preferences from.
     * @param mMap     The map to restore the data to.
     */
    public static void restoreSharedPreferences(Activity activity, GoogleMap mMap) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);

        SpotHandler spotHandler = SpotHandler.getInstance();
        int i = preferences.getInt("I", 0);
        double longitude, latitude;
        LatLng latLng;
        Marker marker;
        Spot spot;
        for (int j = 0; j < i; j++) {
            longitude = preferences.getFloat("LONG" + j, -1);
            latitude = preferences.getFloat("LAT" + j, -1);
            if (longitude != -1) {
                latLng = new LatLng(latitude, longitude);
                marker = mMap.addMarker(new MarkerOptions().position(latLng));

                spot = new Spot().restoreSavedPreferences(preferences, j);
                spot.restoreBitmap(preferences, j);

                spotHandler.addEntry(marker, spot);
            }
        }
    }
}

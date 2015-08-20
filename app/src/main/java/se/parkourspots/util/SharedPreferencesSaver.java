package se.parkourspots.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.Marker;

import java.util.Map;
import java.util.Set;

import se.parkourspots.controller.SpotHandler;
import se.parkourspots.model.Spot;

/**
 * Created by Gabriel on 13/08/2015.
 */

// TODO: använd sharedpf eller filer för spara spots.
public class SharedPreferencesSaver {

    public final static String PREF_NAME = "PREF_NAME";
    private static final String SET_NAME = "SET_NAME";

    public static void saveToSharedPreferences(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //     ArrayList<Spot> spots = SpotHandler.getInstance().getSpots();
        //   for (int i = 0; i < spots.size(); i++) {
        //     editor.putString(PREF_NAME + i, spots.get(i).getName());
        //}

        Map.Entry<Marker, Spot> entry = SpotHandler.getInstance().getEntry();
        Set set = SpotHandler.getInstance().getSet();
        editor.putStringSet(SET_NAME, set);

        //   editor.putInt(getString(R.string.saved_high_score), newHighScore);
        editor.commit();
    }

    public static void readFromSharedPreferences(Activity activity) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        Set set = sharedPref.getStringSet(SET_NAME, null);
        if (set != null) {
            SpotHandler.getInstance().putSet(set);
        }

        /*SpotHandler spotHandler = SpotHandler.getInstance();
        for (int i = 0; i < sharedPref.getAll().size(); i++) {
            String spotName = sharedPref.getString(PREF_NAME + i, null);
            if (spotName != null) {
                spotHandler.putSpot(spotName);
            }
        }*/

        //      int defaultValue = activity.getResources().getInteger(R.string.saved_high_score_default);
        //    long highScore = sharedPref.getInt(getString(R.string.saved_high_score), defaultValue);
    }

}

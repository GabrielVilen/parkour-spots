package se.parkourspots.controller;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import se.parkourspots.model.Spot;

/**
 * Created by Gabriel on 27/07/2015.
 */
public class MarkerHandler {

    private static MarkerHandler instance;
    private Map<Marker, Spot> markerMap = new HashMap();

    private MarkerHandler() {
    }

    public static MarkerHandler getInstance() {
        if (instance == null)
            instance = new MarkerHandler();
        return instance;
    }

    public void addMarker(Marker marker, Spot spot) {
        markerMap.put(marker, spot);
    }

    public void removeMarker(Marker marker) {
        markerMap.remove(marker);
    }

    public Spot getSpot(Marker marker) {
        return markerMap.get(marker);
    }

}

package se.parkourspots.controller;

import com.google.android.gms.maps.model.Marker;

import java.util.Map;
import java.util.WeakHashMap;

import se.parkourspots.model.Spot;

/**
 * Created by Gabriel on 27/07/2015.
 */
public class MarkerHandler {

    private Map<Marker, Spot> markerMap = new WeakHashMap();

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

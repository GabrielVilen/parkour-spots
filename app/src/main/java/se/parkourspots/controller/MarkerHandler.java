package se.parkourspots.controller;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    public Spot getSpot(LatLng latLng) throws NullPointerException {
        Iterator it = markerMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            Marker m = (Marker) e.getKey();
            if (m.getPosition().equals(latLng)) {
                return getSpot(m);
            }
        }
        return null;
    }

    public Spot getSpot(Marker marker) {
        return markerMap.get(marker);
    }

    public void deleteSpot(Spot spot) {
        if (markerMap.containsValue(spot)) {
            markerMap.remove(spot.getMarker());
            spot.remove();
        }
    }

    public ArrayList<Spot> getSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        Iterator it = markerMap.entrySet().iterator();
        while (it.hasNext()) {
            spots.add((Spot) ((Map.Entry) it.next()).getValue());
        }
        return spots;
    }
}

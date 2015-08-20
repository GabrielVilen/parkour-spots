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
public class SpotHandler {

    private static SpotHandler instance;
    private Map<Marker, Spot> map = new HashMap();

    private SpotHandler() {
    }

    public static SpotHandler getInstance() {
        if (instance == null)
            instance = new SpotHandler();
        return instance;
    }

    public void addMarker(Marker marker, Spot spot) {
        map.put(marker, spot);
    }

    public Spot getSpot(LatLng latLng) throws NullPointerException {
        Iterator it = map.entrySet().iterator();
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
        return map.get(marker);
    }

    public void deleteSpot(Spot spot) {
        if (map.containsValue(spot)) {
            map.remove(spot.getMarker());
            spot.remove();
        }
    }

    public ArrayList<Spot> getSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            spots.add((Spot) ((Map.Entry) it.next()).getValue());
        }
        return spots;
    }


    public Spot getSpot(String spotName) {
        Iterator it = map.entrySet().iterator();
        Spot spot = null;
        while (it.hasNext()) {
            spot = (Spot) ((Map.Entry) it.next()).getValue();
            if (spot.getName().equals(spotName)) {
                return spot;
            }
        }
        return spot;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map<Marker, Spot> getMap() {
        return map;
    }
}

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
    private Map<Marker, Spot> map = new HashMap<>();

    private SpotHandler() {
    }

    public static SpotHandler getInstance() {
        if (instance == null)
            instance = new SpotHandler();
        return instance;
    }

    public void addEntry(Marker marker, Spot spot) {
        map.put(marker, spot);
    }

    public Spot getSpot(LatLng latLng) throws NullPointerException {
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
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
            Marker marker = getMarker(spot);
            Spot tmpSpot = map.get(marker);
            tmpSpot = null;
            map.remove(marker);
            marker.remove();
        }
    }

    public ArrayList<Spot> getSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        for (Object o : map.entrySet()) {
            spots.add((Spot) ((Map.Entry) o).getValue());
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

    public Marker getMarker(Spot spot) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (entry.getValue().equals(spot)) {
                return (Marker) entry.getKey();
            }
        }
        return null;
    }

    public ArrayList<Marker> getMarkers() {
        ArrayList<Marker> markers = new ArrayList<>();
        for (Object o : map.entrySet()) {
            markers.add((Marker) ((Map.Entry) o).getKey());
        }
        return markers;
    }
}

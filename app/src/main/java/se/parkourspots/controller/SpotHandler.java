package se.parkourspots.controller;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import se.parkourspots.model.Spot;

/**
 * Singleton class. Handles the mapping between the spots and markers.
 */
public class SpotHandler {

    private static SpotHandler instance;
    private Map<Marker, Spot> map = new HashMap<>();

    private SpotHandler() {
    }

    /**
     * Gets the instance of the singleton. If the instance is null a new one is instantiated.       *
     *
     * @return The static instance of this class.
     */
    public static SpotHandler getInstance() {
        if (instance == null)
            instance = new SpotHandler();
        return instance;
    }

    /**
     * Adds a new entry, a mapping between a marker and a spot, to the map.
     *
     * @param marker The marker to add.
     * @param spot   The spot to add.
     */
    public void addEntry(Marker marker, Spot spot) {
        map.put(marker, spot);
    }

    /**
     * Gets the spot mapped by the given latLng.
     *
     * @param latLng The latlng to match with a spot.
     * @return The spot, or null if no mapping exist.
     * @throws NullPointerException if the latlng has no mapping.
     */
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

    /**
     * Gets the spot mapped to the given marker.
     *
     * @param marker The marker to get the spot from.
     * @return The mapped spot.
     */
    public Spot getSpot(Marker marker) {
        return map.get(marker);
    }

    public void deleteSpot(Spot spot) {
        if (map.containsValue(spot)) {
            Marker marker = getMarker(spot);
            Spot tmpSpot = map.get(marker);
            tmpSpot = null;     // used for java garbage collector
            map.remove(marker);
            marker.remove();
        }
    }

    /**
     * Gets a list of all the spots in the map.
     *
     * @return An <Code>ArrayList</Code> containg all the spots.
     */
    public ArrayList<Spot> getSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        for (Object o : map.entrySet()) {
            spots.add((Spot) ((Map.Entry) o).getValue());
        }
        return spots;
    }


    /**
     * Gets the spot with the given title name.
     *
     * @param spotName The name to get the spot from.
     * @return The matched spot, or null if no spot with the name exists.
     */
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

    /**
     * Get the map which contains all mappings between markers and spots.
     *
     * @return The maps
     */
    public Map<Marker, Spot> getMap() {
        return map;
    }

    /**
     * Get the marker mapped to the given spot.
     *
     * @param spot The spot to get the marker from.
     * @return The mapped marker, or null if no mapping exists.
     */
    public Marker getMarker(Spot spot) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (entry.getValue().equals(spot)) {
                return (Marker) entry.getKey();
            }
        }
        return null;
    }
}

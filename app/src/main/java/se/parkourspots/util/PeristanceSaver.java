package se.parkourspots.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import se.parkourspots.controller.SpotHandler;
import se.parkourspots.model.Spot;

/**
 * Writes/reads an object to/from a private local file
 */
public class PeristanceSaver {
    /**
     * @param context
     * @param filename
     */
    public static void writeObjectToFile(Context context, String filename) {
        Log.d("SPOT", "writeObjectToFile()");
        ObjectOutputStream objectOut = null;
        try {
            FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
            objectOut = new ObjectOutputStream(fileOut);

            ArrayList<Spot> spots = SpotHandler.getInstance().getSpots();
            for (int i = 0; i < spots.size(); i++) {
                Spot spot = spots.get(i);
                LatLng latLng = SpotHandler.getInstance().getMarker(spot).getPosition();
                objectOut.writeObject(spot); // TODO might be impossible due to marker not being serializable
                objectOut.writeUTF(latLng.latitude + "," + latLng.latitude);
            }

            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * @param context
     * @param filename
     * @return
     */
    public static Object readObjectFromFile(Context context, String filename) {
        Log.d("SPOT", "readingObjectFromFile()");
        ObjectInputStream objectIn = null;
        Object object = null;
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject(); // TODO: java.io.EOFException

        } catch (IOException | ClassNotFoundException e) {
            Log.e("SPOT", e.getMessage());
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    Log.e("SPOT", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return object;
    }
}


package se.parkourspots.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
        ObjectOutputStream out = null;
        try {
            FileOutputStream fileOut = context.openFileOutput(filename, Activity.MODE_PRIVATE);
            out = new ObjectOutputStream(fileOut);

            ArrayList<Spot> spots = SpotHandler.getInstance().getSpots();
            for (int i = 0; i < spots.size(); i++) {
                if (spots.get(i) != null) {
                    spots.get(i).writeToFile(out);// TODO: nullpointerexception
                }
            }
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
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
        SpotHandler spotHandler = SpotHandler.getInstance();
        ObjectInputStream ins = null;
        Object object = null;
        try {
            FileInputStream fileIn = context.getApplicationContext().openFileInput(filename);
            ins = new ObjectInputStream(fileIn);
            ArrayList<Spot> spots = SpotHandler.getInstance().getSpots();
            for (int i = 0; i < spots.size(); i++) {
                spots.get(i).restoreFromFile(ins); // TODO throws nullpointer? dvs försvinner instansen onPause?
                spotHandler.addSpot(spots.get(i));
                Log.d("SPOT", "bitmap: " + spots.get(i).getBitmap());
            }
        } catch (IOException e) {
            Log.e("SPOT", "" + e.getMessage());
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    Log.e("SPOT", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return object;
    }
}


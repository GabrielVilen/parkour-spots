package se.parkourspots.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by Gabriel on 20/08/2015.
 */
public class FileSaver {

    private static String filename = "map";

    public static void saveMapToFile(Map map, Context context) throws IOException {
        Log.d("SPOT", "Saved file");

        File file = new File(context.getFilesDir(), filename);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));

        outputStream.writeObject(map);
        outputStream.flush();
        outputStream.close();
    }

    public static Map getMapFromFile() throws Exception {
        Log.d("SPOT", "Reading file");

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));

        Map map = (Map) inputStream.readObject();
        return map;
    }
}

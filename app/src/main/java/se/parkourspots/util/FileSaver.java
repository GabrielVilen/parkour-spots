package se.parkourspots.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import se.parkourspots.controller.SpotHandler;

/**
 * Created by Gabriel on 20/08/2015.
 */
public class FileSaver {

    private static String filename = "map";

    public static void saveMapToFile(Map map, Context context) throws IOException {
        File file = new File(context.getDir("data", Context.MODE_PRIVATE), filename);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(map);
        outputStream.flush();
        outputStream.close();
    }

    public static void setMapFromFile() throws IOException, ClassNotFoundException {
        // create an ObjectInputStream for the file we created before
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));

        // read and print an object and cast it as string
        System.out.println("" + (String) inputStream.readObject());

        // read and print an object and cast it as string
        Map map = (Map) inputStream.readObject();
        SpotHandler.getInstance().setMap(map);
    }
}

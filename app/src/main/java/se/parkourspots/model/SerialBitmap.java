package se.parkourspots.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class SerialBitmap implements Serializable {

    public Bitmap bitmap;

    // TODO: Finish this constructor
    public SerialBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        // Take your existing call to BitmapFactory and put it here
        // bitmap = BitmapFactory.decodeSomething(name);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    // Converts the Bitmap into a byte array for serialization
    public void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (bitmap != null) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteStream); // TODO: NULLPOINTER
            byte bitmapBytes[] = byteStream.toByteArray();
            out.write(bitmapBytes);
        }
    }

    // Deserializes a byte array representing the Bitmap and decodes it
    public void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }
}
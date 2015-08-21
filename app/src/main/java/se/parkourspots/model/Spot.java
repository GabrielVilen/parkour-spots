package se.parkourspots.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Gabriel on 24/07/2015.
 */
public class Spot implements Parcelable, Serializable {

    private String name, description, goodFor, material, size, difficulty;
    private SerialBitmap serialBitmap = new SerialBitmap(null);

    public Spot() {
    }

    public Spot(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel source) {
        name = source.readString();
        description = source.readString();
        goodFor = source.readString();
        difficulty = source.readString();
        size = source.readString();
        material = source.readString();

        try {
            serialBitmap.bitmap = Bitmap.CREATOR.createFromParcel(source);
        } catch (Exception e) {
            // Ignore. Exception thrown if no bitmap exist.
        }
    }

    public void writeToFile(ObjectOutputStream outputStream) {
        try {
            serialBitmap.writeObject(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreFromFile(ObjectInputStream ins) {
        try {
            serialBitmap.readObject(ins);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * We just need to write each field into the
     * parcel. When we read from parcel, they
     * will come back in the same order
     *
     * @param
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(goodFor);
        dest.writeString(difficulty);
        dest.writeString(size);
        dest.writeString(material);

        if (serialBitmap.bitmap != null) {
            serialBitmap.bitmap.writeToParcel(dest, 0);
        }
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     * <p/>
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Spot createFromParcel(Parcel in) {
                    return new Spot(in);
                }

                public Spot[] newArray(int size) {
                    return new Spot[size];
                }
            };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoodFor() {
        return goodFor;
    }

    public void setGoodFor(String goodFor) {
        this.goodFor = goodFor;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setBitmap(Bitmap bitmap) {
        serialBitmap.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return serialBitmap.bitmap;
    }
}

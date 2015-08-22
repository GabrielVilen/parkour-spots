package se.parkourspots.model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by Gabriel on 24/07/2015.
 */
public class Spot implements Parcelable, Serializable {

    private String name, description, goodFor, material, size, difficulty;
    private Bitmap bitmap;

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
            bitmap = Bitmap.CREATOR.createFromParcel(source);
        } catch (Exception e) {
            // Ignore. Exception thrown if no bitmap exist.
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

        if (bitmap != null) {
            bitmap.writeToParcel(dest, 0);
        }
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     * <p>
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
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void saveSharedPreferences(SharedPreferences.Editor editor, int i) {
        editor.putString("name" + i, name);
        editor.putString("description" + i, description);
        editor.putString("goodFor" + i, goodFor);
        editor.putString("difficulty" + i, difficulty);
        editor.putString("size" + i, size);
        editor.putString("material" + i, material);

        if (bitmap != null) {
            saveBitmap(editor, i);
        }

        Log.d("SPOT", "i: " + i + " saving name: " + name);
    }


    public Spot restoreSavedPreferences(SharedPreferences preferences, int i) {
        name = preferences.getString("name" + i, "");
        description = preferences.getString("description" + i, "");
        goodFor = preferences.getString("goodFor" + i, "");
        difficulty = preferences.getString("difficulty" + i, "");
        size = preferences.getString("size" + i, "");
        material = preferences.getString("material" + i, "");

        Log.d("SPOT", "i: " + i + " restoring name: " + name);

        return this;
    }

    public void saveBitmap(SharedPreferences.Editor editor, int i) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        editor.putString("bitmap" + i, encodedImage);
    }

    public void restoreBitmap(SharedPreferences preferences, int i) {
        String encodedBitmap = preferences.getString("bitmap" + i, "");

        if (!encodedBitmap.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(encodedBitmap, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            this.bitmap = bitmap;
            Log.d("SPOT", "bitmap : " + bitmap);
        }
    }
}

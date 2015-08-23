package se.parkourspots.model;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Class representing a spot in the application.
 * Contains information about the spot, such as its name, photo or difficulty.
 */
public class Spot implements Parcelable, Serializable {

    private String name, description, goodFor, material, size, difficulty;
    private Bitmap bitmap;

    /**
     * Empty constructor required by the <Code>Parcelable.CREATOR</Code>.
     */
    public Spot() {
    }

    private Spot(Parcel in) {
        readFromParcel(in);
    }


    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
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


    /**
     * Saves the data in this spot to the given editor.
     *
     * @param editor The editor to the data save to.
     * @param i      An unique integer to identify the spot number.
     */
    public void saveSharedPreferences(SharedPreferences.Editor editor, int i) {
        editor.putString("name" + i, name);
        editor.putString("description" + i, description);
        editor.putString("goodFor" + i, goodFor);
        editor.putString("difficulty" + i, difficulty);
        editor.putString("size" + i, size);
        editor.putString("material" + i, material);

        saveBitmap(editor, i);
    }


    /**
     * Retrives the saved data in the given <Code>SharedPreferences</Code>.
     *
     * @param preferences The preferences with the containing the saved data.
     * @param i           An unique integer specifying which data to retrieve.
     * @return This spot.
     */
    public Spot restoreSavedPreferences(SharedPreferences preferences, int i) {
        name = preferences.getString("name" + i, "");
        description = preferences.getString("description" + i, "");
        goodFor = preferences.getString("goodFor" + i, "");
        difficulty = preferences.getString("difficulty" + i, "");
        size = preferences.getString("size" + i, "");
        material = preferences.getString("material" + i, "");

        return this;
    }

    /**
     * Encodes the bitmap to a byte array and stores it in the given editor.
     *
     * @param editor The editor to store the bitmap to.
     * @param i      A unique integer to identify the correct spot when retrieving the data.
     */
    private void saveBitmap(SharedPreferences.Editor editor, int i) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String encodedBitmap = Base64.encodeToString(b, Base64.DEFAULT);
            editor.putString("bitmap" + i, encodedBitmap);
        } else {
            editor.putString("bitmap" + i, "");
        }
    }

    /**
     * Restores the previously saved bitmap by decoding a given decoded bitmap string.
     *
     * @param preferences The <Code>SharedPreferences</Code> which contains the encoded bitmap.
     * @param i           An unique integer specifying which bitmap to get.
     */
    public void restoreBitmap(SharedPreferences preferences, int i) {
        String encodedBitmap = preferences.getString("bitmap" + i, "");

        if (!encodedBitmap.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(encodedBitmap, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Called when the previously stored data should be read from the parcel.
     *
     * @param source The parcel to read from.
     */
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

}

package se.parkourspots.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Gabriel on 24/07/2015.
 */
public class Spot implements Parcelable {

    private String strValue;
    private Integer intValue;

    private String name;
    private String description;
    private Bitmap photo;
    private Marker marker;

    public Spot() {
    }

    public Spot(Parcel in) {
        readFromParcel(in);
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

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private void readFromParcel(Parcel in) {
        strValue = in.readString();
        intValue = in.readInt();
    }

    /**
     * We just need to write each field into the
     * parcel. When we read from parcel, they
     * will come back in the same order
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strValue);
        dest.writeInt(intValue);
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
}

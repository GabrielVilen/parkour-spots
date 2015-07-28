package se.parkourspots.view;

import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import se.parkourspots.R;


/**
 * Created by Gabriel on 28/07/2015.
 */
public class SpotInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater inflater;
    private View view;
    private Bitmap photo;

    public SpotInfoWindowAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    /**
     * Called before <code>getInfoContents</code> on the marker
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        view = inflater.inflate(R.layout.info_window, null);

        ImageView spotIcon = (ImageView) view.findViewById(R.id.spotIcon);
        if (photo != null)
            spotIcon.setImageBitmap(photo);

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}

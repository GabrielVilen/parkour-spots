package se.parkourspots;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, SpotFragment.OnFragmentInteractionListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SpotFragment fragment;
    private FragmentManager fragmentManager;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        fragmentManager = getFragmentManager();
        final GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 13.5f));
                mMap.setOnMyLocationChangeListener(null);
            }
        };
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (marker != null && marker.isDraggable()) {
            marker.setPosition(latLng);
        } else {
            marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            marker.setDraggable(true);

            fragment = SpotFragment.newInstance(latLng);
            fragmentManager.beginTransaction().add(R.id.mapLayout, fragment).commit();

            setMapWeight(2);
        }
        fragment.setMarker(marker);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null && marker.isDraggable()) {
            marker.setPosition(latLng);
            fragment.setMarker(marker);
        }
    }

    private void removeFragment() {
        if (fragment != null && fragment.isAdded()) {
            setMapWeight(0);
            fragmentManager.beginTransaction().remove(fragment).commit();
            hideKeyboard();
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("SPOT", "onFragmentInteraction(" + uri + ")");

    }

    private void setMapWeight(int weight) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.mapLayout);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight));
    }

    // TODO: FLYTTA FRAGMENT FUNK. TILL SPOTFRAGMENT OCH TA BORT ONCLICK FR�N XML KNAPPARNA CANCEL OCH ADD SPOT
    public void addNewSpot(View view) {
        fragment.addNewSpot();
        marker.setDraggable(false);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        removeFragment();
    }

    public void cancelNewSpot(View view) {
        marker.remove();
        marker = null;
        removeFragment();
    }
}
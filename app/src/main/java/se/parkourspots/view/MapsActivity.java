package se.parkourspots.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

import se.parkourspots.R;
import se.parkourspots.controller.MarkerHandler;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener, SpotFragment.OnFragmentInteractionListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private FragmentManager fragmentManager;
    private Marker currentMarker;
    private MarkerHandler markerHandler;
    private SpotInfoWindowAdapter windowAdapter;
    private SpotFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (markerHandler == null) {
            markerHandler = MarkerHandler.getInstance();
        }
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
    }

    private void setUpCustomActionBar() {
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        View customView = getLayoutInflater().inflate(R.layout.action_bar_custom, null);
        mActionBar.setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
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
    private void setUpMapIfNeeded() {   // TODO: Varna om GPS avstängd!
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        windowAdapter = new SpotInfoWindowAdapter(getLayoutInflater());
        mMap.setInfoWindowAdapter(windowAdapter);

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

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled\n Please enable it").create().show();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (currentMarker != null && currentMarker.isDraggable()) {
            currentMarker.setPosition(latLng);
        } else {
            currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            currentMarker.setDraggable(true);

            if (fragment == null) {
                fragment = SpotFragment.newInstance(currentMarker);
                fragmentManager.beginTransaction().add(R.id.mapLayout, fragment).commit();
            }

            setMapWeight(2);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null && currentMarker.isDraggable()) {
            currentMarker.setPosition(latLng);
        }
    }

    public MarkerHandler getMarkerHandler() {
        return markerHandler;
    }

    @Override
    public void cancelFragment(Fragment fragment) {
        currentMarker.remove();
        currentMarker = null;

        setMapWeight(0);
        hideKeyboard();
    }

    @Override
    public Marker getCurrentMarker() {
        return currentMarker;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setUpCustomActionBar();
        return true;
    }

    @Override
    public void hideFragment(Fragment fragment) {
        setMapWeight(0);
        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setMapWeight(int weight) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.mapLayout);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight));
    }
}
package se.parkourspots.view;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import se.parkourspots.R;
import se.parkourspots.controller.SpotHandler;
import se.parkourspots.controller.SpotInfoWindowAdapter;
import se.parkourspots.util.Keyboard;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener, CreateSpotFragment.OnFragmentInteractionListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private FragmentManager fragmentManager;
    private Marker currentMarker;
    private SpotHandler spotHandler;
    private SpotInfoWindowAdapter windowAdapter;
    private CreateSpotFragment fragment;
    private LatLng currentLoc;
    private boolean isVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (spotHandler == null) {
            spotHandler = SpotHandler.getInstance();
        }
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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
        mMap.setOnMapClickListener(this);

        windowAdapter = new SpotInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(windowAdapter);
        mMap.setOnInfoWindowClickListener(windowAdapter);

        fragmentManager = getFragmentManager();
        final GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 13.5f));
                mMap.setOnMyLocationChangeListener(null);
            }
        };
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

        checkGPS();
    }

    private void checkGPS() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Your GPS is disabled\n Do you want to enable it?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            alert.create().show();
        }
    }

    public void toggleNewSpot(View view) {
        if (isVisible) {
            currentMarker.remove();
            currentMarker = null;
            detachFragment();
        } else {
            attachFragment();
        }
    }

    private void attachFragment() {
        isVisible = true;
        if (currentLoc != null) {
            currentMarker = mMap.addMarker(new MarkerOptions().position(currentLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLoc));
            currentMarker.setDraggable(true);
        }
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.mapLayout, CreateSpotFragment.newInstance(currentMarker)).commit();
            fragment = (CreateSpotFragment) fragmentManager.findFragmentById(R.id.createSpotFragment);
        } else {
            fragmentManager.beginTransaction().attach(fragment).commit();
        }

        setMapWeight(2);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void detachFragment() {
        Keyboard.hideKeyboard(this);
        isVisible = false;
        fragment.clearFields();
        fragmentManager.beginTransaction().detach(fragment).commit();

        setMapWeight(0);
    }


    private void setMapWeight(int weight) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.mapLayout);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, weight));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null && currentMarker.isDraggable()) {
            currentMarker.setPosition(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public SpotHandler getSpotHandler() {
        return spotHandler;
    }

    @Override
    public Marker getCurrentMarker() {
        return currentMarker;
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_search:
//                Intent intent = new Intent();
//               // intent.putExtra()
//                break;
//        }
//
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName())); // TODO: Sökaktiviteten startas ej

        return true;
    }

}
package se.parkourspots;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotFragment extends Fragment {

    private static final String LAT_LNG = "latLng";
    private OnFragmentInteractionListener mListener;
    private LatLng latLng;
    private Spot spot;
    private Marker marker;
    private EditText etSpotName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param latLng Parameter 1.
     * @return A new instance of fragment SpotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotFragment newInstance(LatLng latLng) {
        SpotFragment fragment = new SpotFragment();
        Bundle args = new Bundle();
        args.putParcelable(LAT_LNG, latLng);
        fragment.setArguments(args);
        return fragment;
    }

    public SpotFragment() {
        //required empty constructor
    }

    public void addNewSpot() {
        Log.d("SPOT", "marker = " + marker); // TODO fix marker == null

        if (marker == null) {
            throw new NullPointerException("Shit, marker is null!");
        }
        String spotName = etSpotName.getText().toString();

        marker.setTitle(spotName);
        marker.setDraggable(false);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        spot = new Spot(marker);
        spot.setMarker(marker);
        spot.setName(spotName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latLng = getArguments().getParcelable(LAT_LNG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spot, container, false);

        etSpotName = (EditText) view.findViewById(R.id.spotName);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case (R.id.addNewSpot):
                        addNewSpot();
                        break;
                    case (R.id.cancelNewSpot):
                        cancelNewSpot();
                        break;
                }
            }
        };
        view.findViewById(R.id.addNewSpot).setOnClickListener(listener);
        view.findViewById(R.id.cancelNewSpot).setOnClickListener(listener);

        return view;
    }

    private void cancelNewSpot() {
        marker.remove();
        marker = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setMarker(Marker marker) {
        Log.d("SPOT", "Added marker = " + marker);
        this.marker = marker;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

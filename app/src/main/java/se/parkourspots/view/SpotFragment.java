package se.parkourspots.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import se.parkourspots.R;
import se.parkourspots.controller.MarkerHandler;
import se.parkourspots.model.Spot;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100, RESULT_CANCELED = 0, RESULT_OK = -1;
    private static final String LAT_LNG = "latLng";

    private MarkerHandler markerHandler;
    private OnFragmentInteractionListener mListener;
    private LatLng latLng;
    private Spot spot;

    private EditText etSpotName, etDescription;
    private ImageView photoView;
    private ImageButton cameraButton;
    private Marker marker;
    private Bitmap photo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param marker Parameter 1.
     * @return A new instance of fragment SpotFragment.
     */
    public static SpotFragment newInstance(Marker marker) {
        SpotFragment fragment = new SpotFragment();

        Bundle args = new Bundle();
        args.putParcelable(LAT_LNG, marker.getPosition());
        fragment.setArguments(args);

        return fragment;
    }

    private void setUpHandler() {
        try {
            MapsActivity activity = (MapsActivity) getActivity();
            markerHandler = activity.getMarkerHandler();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public SpotFragment() {
    }

    public void addNewSpot() {
        String name = etSpotName.getText().toString();
        String description = etDescription.getText().toString();

        marker = mListener.getCurrentMarker();
        marker.setTitle(name);
        marker.setDraggable(false);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        spot = new Spot(marker);
        spot.setName(name);
        spot.setDescription(description);
        spot.setPhoto(photo);

        markerHandler.addMarker(marker, spot);

        hideFragment();
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
        etDescription = (EditText) view.findViewById(R.id.description);
        photoView = (ImageView) view.findViewById(R.id.photoView);
        cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);

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
                    case (R.id.cameraButton):
                        takePhoto();
                        break;
                }
            }
        };
        view.findViewById(R.id.addNewSpot).setOnClickListener(listener);
        view.findViewById(R.id.cancelNewSpot).setOnClickListener(listener);
        cameraButton.setOnClickListener(listener);

        return view;
    }

    private void takePhoto() {
        Toast.makeText(getActivity(), "Starting camera", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            photoView.setImageBitmap(photo);
            cameraButton.setVisibility(View.INVISIBLE);
        }
    }

    private void clearFields() {
        etSpotName.setText("");
        etDescription.setText("");
        photoView.setImageResource(android.R.color.white);
        cameraButton.setVisibility(View.VISIBLE);
    }

    private void cancelNewSpot() {
        mListener.cancelFragment(this);
        clearFields();
    }

    private void hideFragment() {
        mListener.hideFragment(this);
        clearFields();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("SPOT", "onAttach() " + this.toString());
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        setUpHandler();
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
        void cancelFragment(Fragment fragment);

        void hideFragment(Fragment fragment);

        Marker getCurrentMarker();
    }

}

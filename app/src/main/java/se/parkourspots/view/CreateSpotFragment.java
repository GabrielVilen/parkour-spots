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

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.controller.MarkerHandler;
import se.parkourspots.model.Spot;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateSpotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateSpotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateSpotFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100, RESULT_CANCELED = 0, RESULT_OK = -1;
    private static final String LAT_LNG = "latLng";

    private MarkerHandler markerHandler;
    private OnFragmentInteractionListener mListener;
    private LatLng latLng;
    private Spot spot;

    private EditText etSpotName, etDescription, etDifficulty, etGoodFor, etGroundMaterial, etSize;
    private ImageView photoView;
    private ImageButton cameraButton;
    private Marker marker;
    private Bitmap photo;
    private ArrayList<EditText> etList = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param marker Parameter 1.
     * @return A new instance of fragment .
     */
    public static CreateSpotFragment newInstance(Marker marker) {
        CreateSpotFragment fragment = new CreateSpotFragment();

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

    public CreateSpotFragment() {
    }

    public void addNewSpot() {
        String name = etSpotName.getText().toString();
        String description = etDescription.getText().toString();
        String size = etSize.getText().toString();
        String difficulty = etDifficulty.getText().toString();
        String material = etGroundMaterial.getText().toString();
        String goodFor = etGoodFor.getText().toString();

        marker = mListener.getCurrentMarker();
        marker.setTitle(name);
        marker.setDraggable(false);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        spot = new Spot();
        spot.setSize(size);
        spot.setDifficulty(difficulty);
        spot.setGoodFor(goodFor);
        spot.setMaterial(material);
        spot.setMarker(marker);
        spot.setName(name);
        spot.setDescription(description);
        if (photo != null)
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
        View view = inflater.inflate(R.layout.fragment_create_spot, container, false);

        etSpotName = (EditText) view.findViewById(R.id.spotName);
        etDescription = (EditText) view.findViewById(R.id.description);
        etDifficulty = (EditText) view.findViewById(R.id.difficulty);
        etGroundMaterial = (EditText) view.findViewById(R.id.ground);
        etGoodFor = (EditText) view.findViewById(R.id.goodFor);
        etSize = (EditText) view.findViewById(R.id.size);
        photoView = (ImageView) view.findViewById(R.id.photoView);
        cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);

        etList.add(etDescription);
        etList.add(etSpotName);
        etList.add(etDifficulty);
        etList.add(etGoodFor);
        etList.add(etSize);
        etList.add(etGroundMaterial);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case (R.id.saveNewSpot):
                        addNewSpot();
                        break;
                    case (R.id.cameraButton):
                        takePhoto();
                        break;
                }
            }
        };
        view.findViewById(R.id.saveNewSpot).setOnClickListener(listener);
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

    void clearFields() {
        Log.d("SPOT", "clearFields CALLED!");
        for (int i = 0; i < etList.size(); i++) {
            etList.get(i).setText("");
            etList.get(i).clearFocus();
        }
        photoView.setImageResource(android.R.color.white);
        cameraButton.setVisibility(View.VISIBLE);
        getView().scrollTo(0, 0);

    }

    @Override
    public void onDestroyView() {
        clearFields(); // TODO: fixa så den kallasa

        super.onDestroyView();

    }

    private void hideFragment() {
        mListener.hideFragment(this);
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
        void hideFragment(Fragment fragment);

        Marker getCurrentMarker();
    }

}
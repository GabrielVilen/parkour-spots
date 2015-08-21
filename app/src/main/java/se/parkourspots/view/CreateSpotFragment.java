package se.parkourspots.view;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.controller.SpotHandler;
import se.parkourspots.model.Spot;
import se.parkourspots.util.Keyboard;


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

    private SpotHandler spotHandler;
    private OnFragmentInteractionListener mListener;
    private LatLng latLng;
    private Spot spot;

    private EditText spotName, description, difficulty, goodFor, groundMaterial, size;
    private ImageView photoView;
    private ImageButton cameraButton;
    private Marker marker;
    private Bitmap photo;
    private ArrayList<EditText> textViews = new ArrayList<>();
    private ScrollView scrollView;

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
            spotHandler = activity.getSpotHandler();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public CreateSpotFragment() {
    }

    public void addNewSpot() {
        String name = spotName.getText().toString();
        String description = this.description.getText().toString();
        String size = this.size.getText().toString();
        String difficulty = this.difficulty.getText().toString();
        String material = groundMaterial.getText().toString();
        String goodFor = this.goodFor.getText().toString();

        marker = mListener.getCurrentMarker();
        marker.setTitle(name);
        marker.setDraggable(false);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        spot = new Spot();
        spot.setSize(size);
        spot.setDifficulty(difficulty);
        spot.setGoodFor(goodFor);
        spot.setMaterial(material);
        //spot.setMarker(marker);
        spot.setName(name);
        spot.setDescription(description);
        if (photo != null) {
            spot.setBitmap(photo);
            photo = null;
        }

        spotHandler.addMarker(marker, spot);

        mListener.detachFragment();
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

        textViews.add(groundMaterial = (EditText) view.findViewById(R.id.ground));
        textViews.add(description = (EditText) view.findViewById(R.id.description));
        textViews.add(difficulty = (EditText) view.findViewById(R.id.difficulty));
        textViews.add(spotName = (EditText) view.findViewById(R.id.spotName));
        textViews.add(goodFor = (EditText) view.findViewById(R.id.goodFor));
        textViews.add(size = (EditText) view.findViewById(R.id.size));
        cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        photoView = (ImageView) view.findViewById(R.id.photoView);

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
        Keyboard.hideKeyboard(getActivity());

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
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setText("");
            textViews.get(i).clearFocus();
        }
        photoView.setImageResource(android.R.color.white);
        cameraButton.setVisibility(View.VISIBLE);
        scrollView.scrollTo(0, 0);

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.mapLayout);
        layout.requestFocus();
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
        void detachFragment();

        Marker getCurrentMarker();
    }

}

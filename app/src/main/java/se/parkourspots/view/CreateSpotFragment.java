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
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import se.parkourspots.R;
import se.parkourspots.connector.SpotHandler;
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

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100, RESULT_OK = -1;

    private SpotHandler spotHandler;
    private OnFragmentInteractionListener mListener;

    private EditText spotName, description, difficulty, goodFor, groundMaterial, size;
    private ImageView photoView;
    private ImageButton cameraButton;
    private Bitmap photo;
    private final ArrayList<EditText> textViews = new ArrayList<>();
    private ScrollView scrollView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment.
     */
    public static CreateSpotFragment newInstance() {
        CreateSpotFragment fragment = new CreateSpotFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Sets up the handler, which contains the mapping between the markers and spots.
     */
    private void setUpHandler() {
        try {
            MapsActivity activity = (MapsActivity) getActivity();
            spotHandler = activity.getHandler();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public CreateSpotFragment() {
    }

    /**
     * Called when a new spot should be added.
     * Creates a new spot instance with the data entered in the fields.
     */
    private void addNewSpot() {
        String name = spotName.getText().toString();
        String description = this.description.getText().toString();
        String size = this.size.getText().toString();
        String difficulty = this.difficulty.getText().toString();
        String material = groundMaterial.getText().toString();
        String goodFor = this.goodFor.getText().toString();

        Marker marker = mListener.getCurrentMarker();
        marker.setTitle(name);
        marker.setDraggable(false);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        Spot spot = new Spot();
        spot.setSize(size);
        spot.setDifficulty(difficulty);
        spot.setGoodFor(goodFor);
        spot.setMaterial(material);
        spot.setName(name);
        spot.setDescription(description);
        if (photo != null) {
            spot.setBitmap(photo);
            photo = null;
        }

        spotHandler.addEntry(marker, spot);

        mListener.detachFragment();
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

    /**
     * Starts the camera activity. Called when the user presses the camera button.
     */
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

    /**
     * Clear (resets) the fields and focus in this spot fragment.
     */
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
     */
    public interface OnFragmentInteractionListener {
        void detachFragment();

        Marker getCurrentMarker();
    }

}

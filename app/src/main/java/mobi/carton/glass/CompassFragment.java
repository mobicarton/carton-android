package mobi.carton.glass;

import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mobi.carton.CartonFragment;
import mobi.carton.R;
import mobi.carton.glass.model.Landmarks;
import mobi.carton.glass.model.Place;

public class CompassFragment extends CartonFragment
        implements
        OrientationManager.OnChangedListener {


    private OrientationManager mOrientationManager;
    private CompassView mCompassView;

    private Landmarks mLandmarks;


    private final OrientationManager.OnChangedListener mCompassListener =
            new OrientationManager.OnChangedListener() {

                @Override
                public void onOrientationChanged(OrientationManager orientationManager) {
                    mCompassView.setHeading(orientationManager.getHeading());
                }

                @Override
                public void onLocationChanged(OrientationManager orientationManager) {
                    Location location = orientationManager.getLocation();
                    List<Place> places = mLandmarks.getNearbyLandmarks(
                            location.getLatitude(), location.getLongitude());
                    mCompassView.setNearbyPlaces(places);
                }

                @Override
                public void onAccuracyChanged(OrientationManager orientationManager) {
                }
            };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_compass, container, false);

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        mOrientationManager  = new OrientationManager(sensorManager, locationManager);

        mLandmarks = new Landmarks(getContext());
        mCompassView = (CompassView) rootView.findViewById(R.id.compassView);
        mCompassView.setOrientationManager(mOrientationManager);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mOrientationManager.addOnChangedListener(mCompassListener);
        mOrientationManager.start();

        if (mOrientationManager.hasLocation()) {
            Location location = mOrientationManager.getLocation();
            List<Place> nearbyPlaces = mLandmarks.getNearbyLandmarks(
                    location.getLatitude(), location.getLongitude());
            mCompassView.setNearbyPlaces(nearbyPlaces);
        }
    }


    @Override
    public void onPause() {
        super.onPause();

        mOrientationManager.removeOnChangedListener(mCompassListener);
        mOrientationManager.stop();
    }


    @Override
    public void onOrientationChanged(OrientationManager orientationManager) {
        mCompassView.setHeading(orientationManager.getHeading());
    }


    @Override
    public void onLocationChanged(OrientationManager orientationManager) {
        Location location = orientationManager.getLocation();
        List<Place> places = mLandmarks.getNearbyLandmarks(
                location.getLatitude(), location.getLongitude());
        mCompassView.setNearbyPlaces(places);
    }


    @Override
    public void onAccuracyChanged(OrientationManager orientationManager) {

    }
}
package com.onelightstudio.velibnroses;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onelightstudio.velibnroses.model.Station;
import com.onelightstudio.velibnroses.ws.WSDefaultHandler;
import com.onelightstudio.velibnroses.ws.WSRequest;
import com.onelightstudio.velibnroses.ws.WSSilentHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private final static String FORCE_CAMERA_POSITION = "ForceCameraPosition";

    private GoogleMap mMap;
    private boolean mForceCameraPosition;
    private LocationClient mLocationClient;
    private ArrayList<Station> stations;
    private boolean mStationsRequestSended;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setUpMapIfNeeded();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mForceCameraPosition == true) {
            Location userLocation = mLocationClient.getLastLocation();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), Constants.MAP_DEFAULT_USER_ZOOM), Constants.MAP_ANIMATE_TIME, null);
        }
    }

    @Override
    public void onDisconnected() {
        //TODO
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //TODO
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        mStationsRequestSended = false;

        if (pSavedInstanceState != null) {
            mForceCameraPosition = pSavedInstanceState.getBoolean(FORCE_CAMERA_POSITION);
        } else {
            mForceCameraPosition = true;
        }

        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLocationClient.connect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(FORCE_CAMERA_POSITION, false);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        restrictZoom(cameraPosition);
                        setMapStations();
                    }
                });

                if (mForceCameraPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(Constants.MAP_DEFAULT_LAT), Constants.MAP_DEFAULT_LNG), Constants.MAP_DEFAULT_ZOOM));
                }
            } else {
                //Tell the user to check its google play services
                Toast.makeText(this, R.string.error_google_play_service, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void restrictZoom(CameraPosition cameraPosition) {
        if (cameraPosition.zoom > Constants.MAP_LIMIT_MAX_ZOOM) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, Constants.MAP_LIMIT_MAX_ZOOM));
        }

        if (cameraPosition.zoom < Constants.MAP_LIMIT_MIN_ZOOM) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, Constants.MAP_LIMIT_MIN_ZOOM));
        }
    }

    private void setMapStations() {
        if (stations == null) {
            if (mStationsRequestSended == false) {
                mStationsRequestSended = true;
                WSRequest request = new WSRequest(this, Constants.JCD_URL);
                request.withParam(Constants.JCD_API_KEY, ((App) getApplication()).getApiKey(Constants.JCD_APP_API_KEY));
                request.handleWith(new WSDefaultHandler() {
                    @Override
                    public void onResult(Context context, JSONObject result) {
                        JSONArray stationsJSON = (JSONArray) result.opt("list");

                        stations = new ArrayList<Station>();
                        for(int i = 0; i < stationsJSON.length(); i++) {
                            stations.add(new Station(stationsJSON.optJSONObject(i)));
                        }

                        setMapStations();
                    }
                });

                request.call();
            }
        } else {
            LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

            for(Station station : stations) {
                if(bounds.contains(station.latLng)) {
                    station.addMarker(mMap);
                } else {
                    station.removeMarker();
                }
            }
        }
    }
}

package com.onelightstudio.velibnroses;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onelightstudio.velibnroses.model.Station;
import com.onelightstudio.velibnroses.ws.WSDefaultHandler;
import com.onelightstudio.velibnroses.ws.WSRequest;
import com.onelightstudio.velibnroses.ws.WSSilentHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.SupportMapFragment;


public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, View.OnClickListener {

    class GetAddressTask extends AsyncTask<Location, Void, String> {

        private EditText field;
        private ImageButton locationButton;
        private ProgressBar locationProgress;
        private Location location;

        public GetAddressTask(int fieldId) {
            switch (fieldId) {
                case FIELD_DEPARTURE:
                    field = departureField;
                    locationButton = departureLocationButton;
                    locationProgress = departureLocationProgress;
                    break;
                case FIELD_ARRIVAL:
                    field = arrivalField;
                    locationButton = arrivalLocationButton;
                    locationProgress = arrivalLocationProgress;
                    break;
            }
        }

        @Override
        protected String doInBackground(Location... params) {
            location = params[0];

            WSRequest request = new WSRequest(MainActivity.this, Constants.GOOGLE_API_GEOCODE_URL);
            request.withParam(Constants.GOOGLE_API_LATLNG, location.getLatitude()+","+location.getLongitude());
            request.withParam(Constants.GOOGLE_API_SENSOR, "true");
            request.handleWith(new WSSilentHandler() {
                @Override
                public void onResult(Context context, JSONObject result) {
                    locationButton.setVisibility(View.VISIBLE);
                    locationProgress.setVisibility(View.GONE);

                    JSONArray addresses = (JSONArray) result.opt("results");
                    if (addresses.length() > 0) {
                        JSONObject address = (JSONObject) addresses.opt(0);
                        field.setText(address.opt("formatted_address").toString());
                    } else {
                        Toast.makeText(MainActivity.this, R.string.address_not_found, Toast.LENGTH_LONG).show();
                    }
                }
            });
            request.call();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                locationButton.setVisibility(View.VISIBLE);
                locationProgress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, R.string.address_not_found, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            locationButton.setVisibility(View.GONE);
            locationProgress.setVisibility(View.VISIBLE);
        }
    }

    private static final int FIELD_DEPARTURE = 0;
    private static final int FIELD_ARRIVAL = 1;
    private final static String FORCE_CAMERA_POSITION = "ForceCameraPosition";

    private GoogleMap mMap;
    private boolean mForceCameraPosition;
    private LocationClient mLocationClient;
    private ArrayList<Station> stations;
    private boolean mStationsRequestSent;
    private Handler timer;
    private Runnable timeRunnable;
    private Long pausedTime;

    private View searchView;
    private View mapView;
    private boolean searchViewVisible;
    private EditText departureField;
    private ImageButton departureLocationButton;
    private ProgressBar departureLocationProgress;
    private EditText arrivalField;
    private ImageButton arrivalLocationButton;
    private ProgressBar arrivalLocationProgress;
    private Location departureLocation;
    private Location arrivalLocation;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        setUpMapIfNeeded();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mForceCameraPosition == true) {
            Location userLocation = mLocationClient.getLastLocation();
            if (userLocation != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), Constants.MAP_DEFAULT_USER_ZOOM), Constants.MAP_ANIMATE_TIME, null);
            }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.departure_mylocation_button:
                fillAddressFieldWithCurrentLocation(FIELD_DEPARTURE);
                break;
            case R.id.arrival_mylocation_button:
                fillAddressFieldWithCurrentLocation(FIELD_ARRIVAL);
                break;
            case R.id.search_button:
                searchBikesStandsPath();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                toggleSearchViewVisible();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayShowTitleEnabled(false);

        //Start timer
        timeRunnable = new Runnable() {

            @Override
            public void run() {
                Log.d("Refresh Map Tick");
                if (pausedTime == null) {
                    setMapStationsOnTick();

                    timer.postDelayed(this, Constants.MAP_TIMER_REFRESH_IN_MILLISECONDES);
                }
            }
        };
        timer = new Handler();
        timer.post(timeRunnable);

        //Init view and elements
        searchView = findViewById(R.id.search_view);
        mapView = findViewById(R.id.map_view);
        departureField = (EditText) findViewById(R.id.departure_field);
        departureLocationButton = (ImageButton) findViewById(R.id.departure_mylocation_button);
        departureLocationProgress = (ProgressBar) findViewById(R.id.departure_mylocation_progress);
        arrivalField = (EditText) findViewById(R.id.arrival_field);
        arrivalLocationButton = (ImageButton) findViewById(R.id.arrival_mylocation_button);
        arrivalLocationProgress = (ProgressBar) findViewById(R.id.arrival_mylocation_progress);

        //Station request
        mStationsRequestSent = false;

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

        if (!mLocationClient.isConnected()) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        pausedTime = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pausedTime != null) {
            if ((System.currentTimeMillis() - pausedTime) > Constants.MAP_TIMER_REFRESH_IN_MILLISECONDES) {
                //Too much time has passed, a refresh is needed
                setMapStationsOnTick();
                timer.postDelayed(timeRunnable, Constants.MAP_TIMER_REFRESH_IN_MILLISECONDES);
            } else {
                timer.postDelayed(timeRunnable, Constants.MAP_TIMER_REFRESH_IN_MILLISECONDES - (System.currentTimeMillis() - pausedTime));
            }

            pausedTime = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle pOutState) {
        super.onSaveInstanceState(pOutState);

        pOutState.putBoolean(FORCE_CAMERA_POSITION, false);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getExtendedMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                if (mForceCameraPosition) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(Constants.MAP_DEFAULT_LAT), Constants.MAP_DEFAULT_LNG), Constants.MAP_DEFAULT_ZOOM));
                }

                // clustering
                ClusteringSettings clusteringSettings = new ClusteringSettings();
                ClusteringSettings.IconDataProvider iconDataProvider = new ClusteringSettings.IconDataProvider() {
                    @Override
                    public MarkerOptions getIconData(int markersCount) {
                        return new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_station_cluster));
                    }
                };
                clusteringSettings.iconDataProvider(iconDataProvider);
                clusteringSettings.clusterSize(80);
                clusteringSettings.addMarkersDynamically(true);
                mMap.setClustering(clusteringSettings);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if (marker.isCluster()) {
                            float zoomLevel = Float.MAX_VALUE;
                            for (Marker innerMarker : marker.getMarkers()) {
                                float minZoomLevel = mMap.getMinZoomLevelNotClustered(innerMarker);
                                if (minZoomLevel < zoomLevel) {
                                    zoomLevel = minZoomLevel;
                                }
                            }
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoomLevel));
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                //Tell the user to check its google play services
                Toast.makeText(this, R.string.error_google_play_service, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setMapStationsOnTick() {
        if (!mStationsRequestSent) {
            mStationsRequestSent = true;
            Log.d("Call stations WS");
            boolean inBackground = stations != null;
            stations = null;
            setMapStations(inBackground);
        }
    }

    private void setMapStationsRequest(final boolean pDoInBackground) {
        WSRequest request = new WSRequest(this, Constants.JCD_URL);
        request.withParam(Constants.JCD_API_KEY, ((App) getApplication()).getApiKey(Constants.JCD_APP_API_KEY));
        if (pDoInBackground) {
            request.handleWith(new WSSilentHandler() {
                @Override
                public void onResult(Context context, JSONObject result) {
                    setMapStationsResult(result, pDoInBackground);
                }
            });
        } else {
            request.handleWith(new WSDefaultHandler() {
                @Override
                public void onResult(Context context, JSONObject result) {
                    setMapStationsResult(result, pDoInBackground);
                }
            });
        }

        request.call();
    }

    private void setMapStationsResult(final JSONObject result, final boolean inBackground) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(Void... voids) {
                JSONArray stationsJSON = (JSONArray) result.opt("list");

                Log.i("Stations received : " + stationsJSON.length() + " stations");

                stations = new ArrayList<Station>();
                LatLng userLocation = new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude());
                if (userLocation == null) {
                    userLocation = new LatLng(Constants.MAP_DEFAULT_LAT, Constants.MAP_DEFAULT_LNG);
                }
                for (int i = 0; i < stationsJSON.length(); i++) {
                    Station station = new Station(stationsJSON.optJSONObject(i));
                    station.distanceFromUser = getDistance(userLocation, station.latLng);
                    int idx = 0;
                    for (Station otherStation : stations) {
                        if (station.distanceFromUser > otherStation.distanceFromUser) {
                            idx++;
                        }
                    }
                    stations.add(idx, station);
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                if (!inBackground) {
                    setProgressBarIndeterminateVisibility(true);
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!inBackground) {
                    setProgressBarIndeterminateVisibility(false);
                }
                mMap.clear();
                setMapStations();
                mStationsRequestSent = false;
            }
        }.execute();
    }

    private void setMapStations() {
        setMapStations(true);
    }

    private void setMapStations(boolean pDoInBackground) {
        if (stations == null) {
            setMapStationsRequest(pDoInBackground);
        } else {
            Log.d("Set up position");

            final ArrayList<Station> stationsToDisplay = new ArrayList<Station>();
            for (int i = 0; i < Constants.MAP_MAX_STATION_MARKERS; i++) {
                stationsToDisplay.add(stations.get(i));
            }

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    for (Station station : stationsToDisplay) {
                        station.prepareMarker(MainActivity.this);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    for (Station station : stationsToDisplay) {
                        station.showOnMap(mMap);
                    }
                }
            }.execute();
        }
    }

    private long getDistance(LatLng point1, LatLng point2) {
        double lat1 = point1.latitude;
        double lng1 = point1.longitude;
        double lat2 = point2.latitude;
        double lng2 = point2.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(radLat1) * Math.cos(radLat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (long) (Constants.EARTH_RADIUS * c);
    }

    private void toggleSearchViewVisible() {
        if (!searchViewVisible) {
            searchViewVisible = true;
            searchView.setVisibility(View.VISIBLE);
            mapView.animate().translationY(searchView.getHeight());
        } else {
            searchViewVisible = false;
            mapView.animate().translationY(0);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void fillAddressFieldWithCurrentLocation(int field) {
        Location userLocation = mLocationClient.getLastLocation();

        if (userLocation == null) {
            Toast.makeText(this, R.string.location_unavailable, Toast.LENGTH_LONG).show();
        } else {
            new GetAddressTask(field).execute(userLocation);
        }
    }

    private void searchBikesStandsPath() {
        if (departureField.getText().length() == 0) {
            Toast.makeText(this, R.string.departure_unavailable, Toast.LENGTH_LONG).show();
        } else if(arrivalField.getText().length() == 0) {
            Toast.makeText(this, R.string.arrival_unavailable, Toast.LENGTH_LONG).show();
        } else {
            departureLocation = null;
            arrivalLocation = null;
            findStationsFromAddress(departureField.getText().toString(), FIELD_DEPARTURE);
            findStationsFromAddress(arrivalField.getText().toString(), FIELD_ARRIVAL);
        }
    }

    private void findStationsFromAddress(String address, int fieldId) {
        WSRequest request = new WSRequest(MainActivity.this, Constants.GOOGLE_API_DIRECTIONS_URL);
        request.withParam(Constants.GOOGLE_API_ADDRESS, address);
        request.withParam(Constants.GOOGLE_API_SENSOR, "true");
        request.handleWith(new WSDefaultHandler() {
            @Override
            public void onResult(Context context, JSONObject result) {
                JSONArray addressLatLng = (JSONArray) result.opt("results");
                if (addressLatLng.length() > 0) {
                    /*TODO
                    Remplir les 2 localisations
                    Créer la liste des stations (max 3) les plus proches
                    Pour la première station de chaque
                    Requête "directions" http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&sensor=true&mode=walking
                    Polyline à partir de routes[0]["overview_polyline"]["points"]
                    Dessiner le polyline
                    Afficher les 3 stations conservées
                    */
                }
            }
        });
        request.call();
    }
}

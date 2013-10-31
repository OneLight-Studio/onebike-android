package com.onelightstudio.onebike.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onelightstudio.onebike.Constants;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Station {

    public final String number;
    public final String name;
    public final String address;
    public final LatLng latLng;
    public final double lat;
    public final double lng;
    public final boolean banking;
    public final boolean bonus;
    public String status;
    public final String contractName;
    public final int bikeStands;
    public int availableBikeStands;
    public int availableBikes;
    public long lastUpdate;

    public final String displayName;
    public MarkerOptions markerOptions;
    public Marker searchMarker;

    public Station(JSONObject pStationJSON, Contract.Provider provider) {
        if (provider == null) {
            throw  new IllegalArgumentException("Cannot parse station from JSON: no provider");
        }
       // System.out.println("## STATION "+provider+" "+pStationJSON.toString());
        switch (provider) {
            case JCDECAUX:
                number = pStationJSON.optString(Constants.JCD_NUMBER_KEY);
                name = pStationJSON.optString(Constants.JCD_NAME_KEY);
                address = pStationJSON.optString(Constants.JCD_ADDRESS_KEY);
                banking = pStationJSON.optBoolean(Constants.JCD_BANKING_KEY);
                bonus = pStationJSON.optBoolean(Constants.JCD_BONUS_KEY);
                status = pStationJSON.optString(Constants.JCD_STATUS_KEY);
                contractName = pStationJSON.optString(Constants.JCD_CONTRACT_NAME_KEY);
                bikeStands = pStationJSON.optInt(Constants.JCD_BIKE_STANDS_KEY);
                availableBikeStands = pStationJSON.optInt(Constants.JCD_AVAILABLE_BIKE_STANDS_KEY);
                availableBikes = pStationJSON.optInt(Constants.JCD_AVAILABLE_BIKE_KEY);
                lastUpdate = pStationJSON.optLong(Constants.JCD_LAST_UPDATE_KEY);
                JSONObject pos = pStationJSON.optJSONObject(Constants.JCD_POSITION_KEY);
                lat = pos.optDouble(Constants.JCD_LAT_KEY);
                lng = pos.optDouble(Constants.JCD_LNG_KEY);
                latLng = new LatLng(lat, lng);
                displayName = buildDisplayName(name);
                break;

            case CITYBIKES:
                number = pStationJSON.optString("id");
                name = pStationJSON.optString("name");
                address = pStationJSON.optString("description");
                banking = false;
                bonus = false;
                status = pStationJSON.optString("status");
                contractName = null;
                bikeStands = -1;
                availableBikeStands = pStationJSON.optInt("free");
                availableBikes = pStationJSON.optInt("bikes");
                // "timestamp":"2013-10-31T11:55:52.016834"
                //lastUpdate = pStationJSON.optLong(Constants.JCD_LAST_UPDATE_KEY);
                lat = pStationJSON.optDouble("lat") / 1e6;
                lng = pStationJSON.optDouble("lng") / 1e6;
                latLng = new LatLng(lat, lng);
                displayName = buildDisplayName(name);
                break;

            default:
                throw  new IllegalArgumentException("Cannot parse station from JSON: unknown provider " + provider.getName());
        }
    }

    private String buildDisplayName(String name) {
        String regexp = "[\\d\\s]*([a-zA-Z](.*)$)";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(name);
        if (m.find()) {
            return m.group();
        }
        return name;
    }

    public void updateDynamicDataWithStation(Station station) {
        status = station.status;
        availableBikeStands = station.availableBikeStands;
        availableBikes = station.availableBikes;
        lastUpdate = station.lastUpdate;
    }
}

package com.onelightstudio.onebike.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by eguilbaud on 30/10/13.
 */
public class Contract {

    public enum Provider {
        JCDECAUX("JCDecaux"),
        CITYBIKES("CityBikes");

        private String name;

        Provider(String providerName) {
            this.name = providerName;
        }

        public String getName() {
            return this.name;
        }

        public static Provider getFromName(String name) {
            for (Provider provider : Provider.values()) {
                if (provider.getName().equals(name)) {
                    return provider;
                }
            }
            return null;
        }
    }

    private String name;
    private String url;
    private int radius;
    private LatLng center;
    private Provider provider;

    private Contract(String name, String url, int radius, LatLng center, Provider provider) {
        this.name = name;
        this.url = url;
        this.radius = radius;
        this.center = center;
        this.provider = provider;
    }

    public static Contract getFromJSon(JSONObject jsonContract) {
        if (jsonContract == null) {
            return null;
        }
        String contractName =  jsonContract.optString("name");
        String url = jsonContract.optString("url");
        int radius = jsonContract.optInt("radius");
        double lat = jsonContract.optDouble("lat");
        double lng = jsonContract.optDouble("lng");
        LatLng center = new LatLng(lat, lng);
        Provider provider = Provider.getFromName(jsonContract.optString("provider"));

        return new Contract(contractName, url, radius, center, provider);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getRadius() {
        return radius;
    }

    public LatLng getCenter() {

        return center;
    }

    public Provider getProvider() {
        return provider;
    }
}

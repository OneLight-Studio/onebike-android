package com.onelightstudio.velibnroses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by agalinier on 29/08/13.
 */
public class Util {

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     */
    public static <K, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> map) {
        List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();

        for (Map.Entry<K, V> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    /*
      * Paramterized method to sort Map e.g. HashMap or Hashtable in Java
      * throw NullPointerException if Map contains null key
      */
    public static <K extends Comparable, V> Map<K, V> sortMapByKeys(Map<K, V> map) {
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);

        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        Map<K, V> sortedMap = new LinkedHashMap<K, V>();
        for (K key : keys) {
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }


    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * Retrieve the JC Decaux contract name covering a given city
     * @param city
     * @param context
     * @return the contract name for this city, null if not covered
     */
    public static String getContractNameForCity(String city, Context context) {
        InputStream is = context.getResources().openRawResource(R.raw.contracts);
        try {
            Reader reader = new InputStreamReader(is, "UTF_8");
            int read;
            final char[] buffer = new char[0x10000];
            StringBuilder out = new StringBuilder();
            do {
                read = reader.read(buffer, 0, buffer.length);
                if (read > 0) {
                    out.append(buffer, 0, read);
                }
            } while (read >= 0);


            String fileContent = out.toString();
            JSONArray array = new JSONArray(fileContent);
            for (int i = 0; i < array.length(); i++) {
                JSONObject contract = array.getJSONObject(i);
                JSONArray cities = contract.getJSONArray("cities");
                for (int cityIndex = 0; cityIndex < cities.length(); cityIndex++) {
                    String cityName = cities.getString(cityIndex);
                    if (cityName.equalsIgnoreCase(city)) {
                        return contract.getString("name");
                    }
                }
            }

        } catch (IOException e) {
            Log.e("Could not read JCD Contract file", e);
        } catch (JSONException e) {
            Log.e("Could not parse JCD Contract file", e);
        }

        return null;
    }

}

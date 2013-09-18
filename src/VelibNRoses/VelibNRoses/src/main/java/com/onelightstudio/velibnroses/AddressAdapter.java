package com.onelightstudio.velibnroses;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.onelightstudio.velibnroses.model.Station;
import com.onelightstudio.velibnroses.ws.WSDefaultHandler;
import com.onelightstudio.velibnroses.ws.WSRequest;
import com.onelightstudio.velibnroses.ws.WSSilentHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

class AddressAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> resultList;

    public AddressAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        if(resultList != null){
            return resultList.size();
        }
        return 0;
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    if (constraint.toString().trim().length() >= 3) {
                        resultList = autocomplete(constraint.toString());
                    } else {
                        resultList = new ArrayList<String>();

                    }

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> autoCompleteResult = new ArrayList<String>();

        Log.d("Start autocomplete");
        WSRequest request = new WSRequest(this.getContext(), Constants.GOOGLE_API_AUTOCOMPLETE_URL);
        request.withParam(Constants.GOOGLE_API_KEY, (((App)((MainActivity)this.getContext()).getApplication()).getApiKey(Constants.GOOGLE_APP_API_KEY)));
        request.withParam(Constants.GOOGLE_API_INPUT, input);

        LocationClient locationClient = ((MainActivity) this.getContext()).getLocationClient();

        try{
            if (locationClient.getLastLocation().getLatitude() != 0 && locationClient.getLastLocation().getLongitude() != 0) {
                request.withParam(Constants.GOOGLE_API_SENSOR, "true");
                request.withParam(Constants.GOOGLE_API_LOCATION, locationClient.getLastLocation().getLatitude() + "," + locationClient.getLastLocation().getLongitude());
                request.withParam(Constants.GOOGLE_API_RADIUS, "500");
            } else {
                request.withParam(Constants.GOOGLE_API_SENSOR, "false");
            }
        } catch (Exception e) {
            request.withParam(Constants.GOOGLE_API_SENSOR, "false");
        }

        JSONObject result = request.wsRequestGetSimple();

        JSONArray predictions = (JSONArray) result.opt("predictions");
        if (predictions != null && predictions.length() > 0) {

            for (int i = 0; i < predictions.length(); i++) {
                JSONObject place = predictions.optJSONObject(i);
                String description = place.optString("description");
                if (description != null) {
                    autoCompleteResult.add(description);
                }
            }
        }

        Log.d("End autocomplete");

        return autoCompleteResult;
    }

}

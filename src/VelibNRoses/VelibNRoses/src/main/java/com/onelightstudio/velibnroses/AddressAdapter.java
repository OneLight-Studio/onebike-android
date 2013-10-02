package com.onelightstudio.velibnroses;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.android.gms.location.LocationClient;
import com.onelightstudio.velibnroses.ws.WSRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

        JSONObject result = request.wsRequestGet();

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

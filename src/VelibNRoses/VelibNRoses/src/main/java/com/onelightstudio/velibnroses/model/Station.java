package com.onelightstudio.velibnroses.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onelightstudio.velibnroses.Constants;
import com.onelightstudio.velibnroses.Display;
import com.onelightstudio.velibnroses.R;

import org.json.JSONObject;

import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.Marker;

public class Station {

    private static final int MARKER_WIDTH = 50;
    private static final int MARKER_HEIGHT = 40;
    private static final int MARKER_TEXT_SIZE = 18;
    private static final int MARKER_BIKES_X_MIN = 9;
    private static final int MARKER_BIKES_X_MAX = 13;
    private static final int MARKER_BIKES_Y = 16;
    private static final int MARKER_STANDS_X_MIN = 9;
    private static final int MARKER_STANDS_X_MAX = 13;
    private static final int MARKER_STANDS_Y = 33;
    private static final float MARKER_ANCHOR_U = 0.7f;
    private static final float MARKER_ANCHOR_V = 0.9f;

    private static Bitmap markerBitmap;
    private static Bitmap markerBitmapNoBike;
    private static Bitmap markerBitmapNoStand;
    private static Paint paint;

    public String number;
    public String name;
    public String address;
    public LatLng latLng;
    public double lat;
    public double lng;
    public boolean banking;
    public boolean bonus;
    public String status;
    public String contractName;
    public int bikeStands;
    public int availableBikeStands;
    public int availableBikes;
    public long lastUpdate;

    public MarkerOptions markerOptions;
    public Marker marker;
    public long distanceFromUser;

    public Station(JSONObject pStationJSON) {
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

        distanceFromUser = Long.MAX_VALUE;
    }

    public void showOnMap(GoogleMap pMap) {
        if (marker == null && markerOptions != null) {
            marker = pMap.addMarker(markerOptions);
            //pMap.addMarker(new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.defaultMarker()));
        }
    }

    public void prepareMarker(Context ctx) {
        if (markerOptions == null) {
            Bitmap bmp = createBitmap(ctx);
            markerOptions = new MarkerOptions().position(latLng).title(name).icon(BitmapDescriptorFactory.fromBitmap(bmp)).anchor(MARKER_ANCHOR_U, MARKER_ANCHOR_V);
            bmp.recycle();
        }
    }

    private Bitmap createBitmap(Context ctx) {
        // load any static object if needed
        if (markerBitmap == null) {
            markerBitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_station);
        }
        if (markerBitmapNoBike == null) {
            markerBitmapNoBike = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_station);
        }
        if (markerBitmapNoStand == null) {
            markerBitmapNoStand = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_station);
        }
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(MARKER_TEXT_SIZE);
            paint.setFakeBoldText(true);
            paint.setColor(Color.WHITE);
        }

        // get the bitmap size in pixels
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        int bmpWidth = Display.dpToPx(dm, MARKER_WIDTH);
        int bmpHeight = Display.dpToPx(dm, MARKER_HEIGHT);
        int xMinBikes = Display.dpToPx(dm, MARKER_BIKES_X_MIN);
        int xMaxBikes = Display.dpToPx(dm, MARKER_BIKES_X_MAX);
        int yBikes = Display.dpToPx(dm, MARKER_BIKES_Y);
        int xMinStands = Display.dpToPx(dm, MARKER_STANDS_X_MIN);
        int xMaxStands = Display.dpToPx(dm, MARKER_STANDS_X_MAX);
        int yStands = Display.dpToPx(dm, MARKER_STANDS_Y);

        Bitmap bmp = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(availableBikes > 0 ? (availableBikeStands > 0 ? markerBitmap : markerBitmapNoStand) : markerBitmapNoBike, 0, 0, paint);
        canvas.drawText(String.valueOf(availableBikes), availableBikes < 10 ? xMaxBikes : xMinBikes, yBikes, paint);
        canvas.drawText(String.valueOf(availableBikeStands), availableBikeStands < 10 ? xMaxStands : xMinStands, yStands, paint);

        return bmp;
    }
}

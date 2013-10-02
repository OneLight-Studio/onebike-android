package com.onelightstudio.velibnroses;

public interface Constants {
    public static final Integer EARTH_RADIUS = 6371000;

    public static final int MAP_DEFAULT_NO_LOCATION_ZOOM = 12;
    public static final int MAP_DEFAULT_USER_ZOOM = 15;
    public static final int MAP_ANIMATE_TIME = 500;
    //public static final int MAP_TIMER_REFRESH_IN_MILLISECONDES = 300000;
    public static final int MAP_TIMER_REFRESH_IN_MILLISECONDES = 15000;

    public static final int SEARCH_RESULT_MAX_STATIONS_NUMBER = 3;
    public static final int STATION_SEARCH_MAX_RADIUS_IN_METERS = 5000;
    public static final int STATION_SEARCH_MIN_RADIUS_IN_METERS = 500;

    public static final double TLS_LAT = 43.610477;
    public static final double TLS_LNG = 1.443615;

    public static final String GOOGLE_API_GEOCODE_URL = "http://maps.google.com/maps/api/geocode/json";
    public static final String GOOGLE_API_DIRECTIONS_URL = "http://maps.google.com/maps/api/directions/json";
    public static final String GOOGLE_API_AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    public static final String GOOGLE_API_LATLNG = "latlng";
    public static final String GOOGLE_API_ADDRESS = "address";
    public static final String GOOGLE_API_SENSOR = "sensor";
    public static final String GOOGLE_API_ORIGIN = "origin";
    public static final String GOOGLE_API_DESTINATION = "destination";
    public static final String GOOGLE_LAT_KEY = "lat";
    public static final String GOOGLE_LNG_KEY = "lng";
    public static final String GOOGLE_API_MODE_KEY = "mode";
    public static final String GOOGLE_API_MODE_VALUE = "walking";
    public static final String GOOGLE_API_INPUT = "input";
    public static final String GOOGLE_API_LOCATION = "location";
    public static final String GOOGLE_API_KEY = "key";
    public static final String GOOGLE_APP_API_KEY = "GoogleMapPlace_API";
    public static final String GOOGLE_API_RADIUS = "radius";


    public static final String JCD_URL = "https://api.jcdecaux.com/vls/v1/stations";
    public static final String JCD_API_KEY = "apiKey";
    public static final String JCD_CONTRACT = "contract";
    public static final String JCD_APP_API_KEY = "JCDecaux_API";
    public static final String JCD_NUMBER_KEY = "number";
    public static final String JCD_NAME_KEY = "name";
    public static final String JCD_ADDRESS_KEY = "address";
    public static final String JCD_POSITION_KEY = "position";
    public static final String JCD_LAT_KEY = "lat";
    public static final String JCD_LNG_KEY = "lng";
    public static final String JCD_BANKING_KEY = "banking";
    public static final String JCD_BONUS_KEY = "bonus";
    public static final String JCD_STATUS_KEY = "status";
    public static final String JCD_CONTRACT_NAME_KEY = "contract_name";
    public static final String JCD_BIKE_STANDS_KEY = "bike_stands";
    public static final String JCD_AVAILABLE_BIKE_STANDS_KEY = "available_bike_stands";
    public static final String JCD_AVAILABLE_BIKE_KEY = "available_bikes";
    public static final String JCD_LAST_UPDATE_KEY = "last_update";
    public static final String JCD_DEFAULT_CONTRACT_KEY = "Toulouse";
}

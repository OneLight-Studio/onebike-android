package com.onelightstudio.onebike;

public interface Constants {

    static final Integer EARTH_RADIUS = 6371000;

    static final int MAP_DEFAULT_NO_LOCATION_ZOOM = 12;
    static final int MAP_DEFAULT_USER_ZOOM = 15;
    static final int MAP_ANIMATE_TIME = 500;
    static final int MAP_TIMER_REFRESH_IN_MILLISECONDES = 300000;

    static final int SEARCH_RESULT_MAX_STATIONS_NUMBER = 3;
    static final int STATION_SEARCH_MAX_RADIUS_IN_METERS = 5000;
    static final int STATION_SEARCH_MIN_RADIUS_IN_METERS = 500;

    static final double TLS_LAT = 43.610477;
    static final double TLS_LNG = 1.443615;

    static final String GOOGLE_API_GEOCODE_URL = "http://maps.google.com/maps/api/geocode/json";
    static final String GOOGLE_API_DIRECTIONS_URL = "http://maps.google.com/maps/api/directions/json";
    static final String GOOGLE_API_AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    static final String GOOGLE_API_LATLNG = "latlng";
    static final String GOOGLE_API_ADDRESS = "address";
    static final String GOOGLE_API_SENSOR = "sensor";
    static final String GOOGLE_API_ORIGIN = "origin";
    static final String GOOGLE_API_DESTINATION = "destination";
    static final String GOOGLE_LAT_KEY = "lat";
    static final String GOOGLE_LNG_KEY = "lng";
    static final String GOOGLE_API_MODE_KEY = "mode";
    static final String GOOGLE_API_MODE_VALUE = "walking";
    static final String GOOGLE_API_INPUT = "input";
    static final String GOOGLE_API_LOCATION = "location";
    static final String GOOGLE_API_KEY = "key";
    static final String GOOGLE_APP_API_KEY = "GoogleMapPlace_API";
    static final String GOOGLE_API_RADIUS = "radius";


    static final String JCD_URL = "https://api.jcdecaux.com/vls/v1/stations";
    static final String JCD_API_KEY = "apiKey";
    static final String JCD_CONTRACT = "contract";
    static final String JCD_APP_API_KEY = "JCDecaux_API";
    static final String JCD_NUMBER_KEY = "number";
    static final String JCD_NAME_KEY = "name";
    static final String JCD_ADDRESS_KEY = "address";
    static final String JCD_POSITION_KEY = "position";
    static final String JCD_LAT_KEY = "lat";
    static final String JCD_LNG_KEY = "lng";
    static final String JCD_BANKING_KEY = "banking";
    static final String JCD_BONUS_KEY = "bonus";
    static final String JCD_STATUS_KEY = "status";
    static final String JCD_CONTRACT_NAME_KEY = "contract_name";
    static final String JCD_BIKE_STANDS_KEY = "bike_stands";
    static final String JCD_AVAILABLE_BIKE_STANDS_KEY = "available_bike_stands";
    static final String JCD_AVAILABLE_BIKE_KEY = "available_bikes";
    static final String JCD_LAST_UPDATE_KEY = "last_update";
    static final String JCD_DEFAULT_CONTRACT_KEY = "Toulouse";

    static final String EMAIL_SMTP_HOST = "mail.prometil.com";
    static final String EMAIL_SENDER = "no-reply@onelight-studio.com";
    static final String EMAIL_RECIPIENT = "support@onelight-studio.com";
    static final String EMAIL_SUBJECT = "OneBike Android Feedback";
}

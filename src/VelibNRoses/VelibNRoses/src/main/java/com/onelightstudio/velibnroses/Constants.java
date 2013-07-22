package com.onelightstudio.velibnroses;

public interface Constants {
    public static final Integer EARTH_RADIUS = 6371000;

    public static final Double MAP_DEFAULT_LAT = 43.610477;
    public static final Double MAP_DEFAULT_LNG = 1.443615;
    public static final Integer MAP_DEFAULT_ZOOM = 12;
    public static final Integer MAP_DEFAULT_USER_ZOOM = 15;
    public static final Integer MAP_LIMIT_MIN_ZOOM = 14;
    public static final Integer MAP_LIMIT_MAX_ZOOM = 20;
    public static final Integer MAP_ANIMATE_TIME = 500;

    public static final String JCD_URL = "https://api.jcdecaux.com/vls/v1/stations";
    public static final String JCD_API_KEY = "apiKey";
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
}

package com.onelightstudio.velibnroses;

import android.app.Application;
import android.widget.Toast;

import java.io.IOException;
import java.util.Properties;

public class App extends Application {

    private Properties props;

    @Override
    public void onCreate() {
        // load the properties
        props = new Properties();
        try {
            props.load(getResources().openRawResource(R.raw.api_keys));
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_init, Toast.LENGTH_LONG).show();
        }
        // log level
        Log.setLevel(Log.ALL);
    }

    public String getApiKey(String key) {
        return props.getProperty(key);
    }
}
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
            props.load(getResources().openRawResource(R.raw.app));
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_init, Toast.LENGTH_LONG).show();
        }
    }

    public Properties getProperties() {
        return props;
    }

    public String getProp(String key) {
        return props.getProperty(key);
    }

    public Double getPropDouble(String key) {
        return Double.valueOf(props.getProperty(key));
    }

    public Integer getPropInt(String key) {
        return Integer.valueOf(props.getProperty(key));
    }
}
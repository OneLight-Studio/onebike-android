package com.onelightstudio.velibnroses;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class App extends Application {

    private Properties props;

    @Override
    public void onCreate() {
        // load the properties
        props = new Properties();
        try {
            props.load(getResources().openRawResource(R.raw.props));
        } catch (IOException e) {
            Toast.makeText(this, R.string.error_init, Toast.LENGTH_LONG).show();
        }
    }

    public Properties getProperties() {
        return props;
    }
}

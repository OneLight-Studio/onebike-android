package com.onelightstudio.onebike;

import android.os.Bundle;
import android.app.Activity;

public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getActionBar().setDisplayShowTitleEnabled(false);
    }
}

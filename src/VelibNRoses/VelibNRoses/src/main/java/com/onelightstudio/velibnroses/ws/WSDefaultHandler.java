package com.onelightstudio.velibnroses.ws;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.onelightstudio.velibnroses.R;

public abstract class WSDefaultHandler implements WSHandler {
    private boolean executeInBackground;

    public WSDefaultHandler(boolean executeInBackground) {
        this.executeInBackground = executeInBackground;
    }

    public void doAfter(Context context) {
        if (context instanceof Activity && executeInBackground == false) {
            ((Activity) context).setProgressBarIndeterminateVisibility(false);
        }
    }

    public void doBefore(Context context) {
        if (context instanceof Activity && executeInBackground == false) {
            ((Activity) context).setProgressBarIndeterminateVisibility(true);
        }
    }

    public void onCancelled(Context context) {
        if (context instanceof Activity && executeInBackground == false) {
            ((Activity) context).setProgressBarIndeterminateVisibility(true);
        }
    }

    public abstract void onError(Context context, int errorCode);

    public abstract void onException(Context context, Exception e);
}

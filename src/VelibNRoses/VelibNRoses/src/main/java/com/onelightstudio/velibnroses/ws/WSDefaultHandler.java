package com.onelightstudio.velibnroses.ws;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.onelightstudio.velibnroses.R;

public abstract class WSDefaultHandler implements WSHandler {

    public void doAfter(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).setProgressBarIndeterminateVisibility(false);
        }
    }

    public void doBefore(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).setProgressBarIndeterminateVisibility(true);
        }
    }

    public void onCancelled(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).setProgressBarIndeterminateVisibility(true);
        }
    }

    public void onError(Context context, int errorCode) {
        //Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
    }

    public void onException(Context context, Exception e) {
        //Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
    }
}

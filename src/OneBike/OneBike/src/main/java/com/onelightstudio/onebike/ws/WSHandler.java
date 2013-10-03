package com.onelightstudio.onebike.ws;

import android.content.Context;

import org.json.JSONObject;

public interface WSHandler {

    public void doAfter(Context context);

    public void doBefore(Context context);

    public void onCancelled(Context context);

    public void onError(Context context, int errorCode);

    public void onException(Context context, Exception e);

    public void onResult(Context context, JSONObject result);
}

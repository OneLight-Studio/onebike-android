package com.onelightstudio.velibnroses.ws;

import android.content.Context;

public abstract class WSSilentHandler implements WSHandler {

    public void doAfter(Context context) {
    }

    public void doBefore(Context context) {
    }

    public void onCancelled(Context context) {
    }

    public void onError(Context context, int errorCode) {
    }

    public void onException(Context context, Exception e) {
    }
}

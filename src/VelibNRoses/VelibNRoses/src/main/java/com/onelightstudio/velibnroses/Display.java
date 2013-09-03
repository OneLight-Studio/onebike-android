package com.onelightstudio.velibnroses;

import android.content.Context;
import android.util.DisplayMetrics;

public class Display {

    public static int dpToPx(DisplayMetrics dm, int dp) {
        return (int)((dp * dm.density) + 0.5);
    }
}

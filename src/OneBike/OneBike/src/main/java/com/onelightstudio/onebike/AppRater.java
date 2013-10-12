package com.onelightstudio.onebike;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * Created by thomas on 11/10/13.
 * Fork of Andrew Jackson's AppRater: https://github.com/codechimp-org/AppRater
 */
public class AppRater {

    private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCHES_UNTIL_PROMPT = 0;

    /**
     * Call this method at the end of your OnCreate method to determine whether to show the rate prompt
     */
    public static void appLaunched(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait for at least the number of launches and the number of days used until prompt
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateAlertDialog(context, editor);
            }
        }

        editor.commit();
    }

    /**
     * Call this method directly if you want to force a rate prompt, useful for testing purposes
     */
    public static void showRateDialog(final Context context) {
        showRateAlertDialog(context, null);
    }

    /**
     * The meat of the library, actually shows the rate prompt dialog
     */
    private static void showRateAlertDialog(final Context context, final SharedPreferences.Editor editor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rate_title);

        builder.setMessage(R.string.rate_message);

        builder.setPositiveButton(R.string.rate_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }

                dialog.dismiss();
            }
        });

        builder.setNeutralButton(R.string.rate_later, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editor != null) {
                    Long date_firstLaunch = System.currentTimeMillis();
                    editor.putLong("date_firstlaunch", date_firstLaunch);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.rate_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        builder.show();
    }
}

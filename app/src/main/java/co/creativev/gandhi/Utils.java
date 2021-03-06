package co.creativev.gandhi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.inmobi.commons.InMobi;
import com.inmobi.monetization.IMBanner;

import java.lang.reflect.Field;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    public static String LOG_TAG = "GANDHIQUOTES";
    public static String PREF_MUSIC = "PREF_MUSIC";
    public static final String PREF_KEEP_SCREEN_ON = "KEEP_SCREEN_ON";

    private final Activity activity;
    private Typeface font;

    public Utils(Activity activity) {
        this.activity = activity;
        font = Typeface.createFromAsset(activity.getAssets(), "fonts/segoepr.ttf");
    }

    public boolean getPreferenceValue(String key, boolean defaultValue) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        return preferences.getBoolean(key, defaultValue);
    }

    public int getPreferenceValue(String key, int defaultValue) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        return preferences.getInt(key, defaultValue);
    }

    public BackgroundMusic createMediaPlayer() {
        return new BackgroundMusic(activity, R.raw.raghupati);
    }

    public void savePreference(String key, boolean value) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void savePreference(String key, int value) {
        SharedPreferences preferences = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void initAds() {
        InMobi.initialize(activity, activity.getString(R.string.inmobi_id));
        IMBanner adView = (IMBanner) activity.findViewById(R.id.adView);
        adView.loadBanner();
    }

    public static int getResource(String resourceId, Class<?> idClass) {
        try {
            Field idField = idClass.getDeclaredField(resourceId);
            return idField.getInt(idField);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public Typeface getQuoteFont() {
        return font;
    }

    public void aboutDialog() {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.app_name)
                .setMessage(
                        Html.fromHtml(activity.getResources().getString(
                                R.string.about_msg)))
                .setIcon(R.drawable.ic_launcher).setCancelable(true)
                .setPositiveButton(R.string.ok, null).create().show();

    }

    public void showHelpDialog() {
        new AlertDialog.Builder(activity).setTitle("Help")
                .setMessage(Html.fromHtml(activity.getResources().getString(R.string.instructions)))
                .setCancelable(true).setPositiveButton(R.string.ok, null).create().show();
    }

    public void screenWakeMenuClicked(MenuItem item) {
        boolean wasScreenWakeOn = item.isChecked();
        boolean isScreenWakeOn = !wasScreenWakeOn;
        savePreference(PREF_KEEP_SCREEN_ON, isScreenWakeOn);
        syncScreenWakeMenu(item, isScreenWakeOn);
    }

    public void syncScreenWakeMenu(MenuItem item, boolean isScreenWakeOn) {
        item.setChecked(isScreenWakeOn);
        item.setIcon(isScreenWakeOn ? R.drawable.ic_screen_on : R.drawable.ic_screen_off);
        if (isScreenWakeOn) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}

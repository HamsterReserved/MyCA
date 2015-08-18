package com.hustca.app.fragments;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.hustca.app.R;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Hamster on 2015/8/18.
 * <p/>
 * Settings fragment. Used in pref_push activity (yet another container)
 */
public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PUSH_ENABLED = "push_enabled";
    public static final String KEY_PUSH_SOUND_ENABLED = "push_sound_enabled";
    public static final String KEY_PUSH_VIBRATION_ENABLED = "push_vibration_enabled";

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_push);

        mContext = this.getActivity();

        Preference mPrefPushEnabled = findPreference(KEY_PUSH_ENABLED);

        mPrefPushEnabled.setDefaultValue(!JPushInterface.isPushStopped(mContext));
        mPrefPushEnabled.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if ((boolean) newValue) {
                            JPushInterface.resumePush(mContext);
                        } else {
                            JPushInterface.stopPush(mContext);
                        }
                        return true;
                    }
                });

    }

    @Override
    public void onStop() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (pref.getBoolean(KEY_PUSH_ENABLED, false)) {
            BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);

            builder.notificationFlags = 0;

            if (pref.getBoolean(SettingsFragment.KEY_PUSH_SOUND_ENABLED, false)) {
                builder.notificationFlags |= Notification.DEFAULT_SOUND;
            }

            if (pref.getBoolean(SettingsFragment.KEY_PUSH_VIBRATION_ENABLED, false)) {
                builder.notificationFlags |= Notification.DEFAULT_VIBRATE;
            }

            JPushInterface.setDefaultPushNotificationBuilder(builder);
        }
        super.onStop();
    }
}

package com.schautup.brightness;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.Window;
import android.view.WindowManager;

public final class BrightnessRefreshActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(!PreferenceManager.getDefaultSharedPreferences(getApplication()).getBoolean("refreshed", false)) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

			refreshBrightness(getBrightnessLevel());

			Thread t = new Thread() {
				public void run() {
					try {
						sleep(10);
					} catch (InterruptedException e) {
					}
					PreferenceManager.getDefaultSharedPreferences(getApplication()).edit().putBoolean("refreshed", true)
							.commit();
					finish();
				}
			};
			t.start();
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}
	}

	//----------------------------------------------------------
	// Description: Refresh brightness
	//
	// No all devices can react the change of brightness.
	// See.
	// http://stackoverflow.com/questions/14101669/change-brightness-according-to-surrounding-light-in-android
	//----------------------------------------------------------

	private void refreshBrightness(float brightness) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		if (brightness < 0) {
			lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
		} else {
			lp.screenBrightness = brightness;
		}
		getWindow().setAttributes(lp);
	}

	int getBrightnessLevel() {
		try {
			int value = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
			// convert brightness level to range 0..1
			value = value / 255;
			return value;
		} catch (SettingNotFoundException e) {
			return 0;
		}
	}
}

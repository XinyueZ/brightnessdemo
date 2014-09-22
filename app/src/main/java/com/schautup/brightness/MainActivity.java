package com.schautup.brightness;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Demo to show how to control brightness thπough codes. In order to give a simple demonstration.
 * <p/>
 * <p/>
 * There's including a 5 seconds daley operation that make system brightness to max.
 * <p/>
 * There's including a 5 seconds daley operation that make system brightness to min.
 * <p/>
 * Use a dummy activity which can refresh brightness status.
 *
 * @author Xinyue Zhao.
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	/**
	 * Select brightness with max.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void selectMax(View view) {
		setBrightness(getApplication(), getWindow(), Brightness.MAX);
	}

	/**
	 * Select brightness with medium.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void selectMedium(View view) {
		setBrightness(getApplication(), getWindow(), Brightness.MEDIUM);
	}

	/**
	 * Select brightness with min.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void selectMin(View view) {
		setBrightness(getApplication(), getWindow(), Brightness.MIN);
	}

	/**
	 * Set brightness to <b>Max</b> after 5 seconds.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void fiveSecondsToMax(View view) {
		finish();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				selectMax(null);
				Toast.makeText(getApplicationContext(), "TO MAX", Toast.LENGTH_LONG).show();

				PreferenceManager.getDefaultSharedPreferences(getApplication()).edit().putBoolean("refreshed", false)
						.commit();
				Intent i = new Intent(MainActivity.this, BrightnessRefreshActivity.class);
				startActivity(i);
			}
		}, 5000);
	}

	/**
	 * Set brightness to <b>Min</b> after 5 seconds.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void fiveSecondsToMin(View view) {
		finish();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				selectMin(null);
				Toast.makeText(getApplicationContext(), "TO MIN", Toast.LENGTH_LONG).show();

				PreferenceManager.getDefaultSharedPreferences(getApplication()).edit().putBoolean("refreshed", false)
						.commit();
				Intent i = new Intent(MainActivity.this, BrightnessRefreshActivity.class);
				startActivity(i);
			}
		}, 5000);
	}

	/**
	 * Brightness levels, only max, medium, min.
	 *
	 * @author Xinyue Zhao
	 */
	enum Brightness {
		MAX(1f, 255), MEDIUM(0.5f, (255 + 10) / 2), MIN(0.1f, 10);

		public float valueF;
		public int valueI;

		Brightness(float valueF, int valueI) {
			this.valueF = valueF;
			this.valueI = valueI;
		}
	}

	/**
	 * Set system brightness.
	 *
	 * @param cxt
	 * 		{@link android.content.Context}.
	 * @param window
	 * 		{@link android.view.Window}.
	 */
	public static void setBrightness(Context cxt, Window window, Brightness brightness) {
		ContentResolver cr = cxt.getContentResolver();
		// To handle the auto.
		Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		//Get the current system brightness.
		//int currentBrightness = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
		int currentBrightness = brightness.valueI;

		/*
		 * Set whole system brightness.
		 */

		//Set the system brightness using the brightness variable value.
		Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS, currentBrightness);

		/*
		 *Set current window screen.
		 */

		//Get the current window attributes.
		WindowManager.LayoutParams params = window.getAttributes();
		//Set the brightness of this window.
		float newBrightness = (brightness.valueI / (float) 255);
		params.screenBrightness = newBrightness < 0.1 ? 0.1f : newBrightness;
		//Apply attribute changes to this window.
		window.setAttributes(params);
	}


}

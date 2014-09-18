package com.schautup.brightness;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Demo to show how to control brightness though codes. In order to give a simple demonstration, I avoid "difficult"
 * proportional calculation on system brightness value(10 to 255) which could be mirrored by current window brightness
 * (0 to 1). See {@link android.view.WindowManager.LayoutParams#screenBrightness}).
 * <p/>
 * <p/>
 * There's including a 5 seconds daley operation that make system brightness to max.
 * <p/>
 * There's including a 5 seconds daley operation that make system brightness to min.
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
		try {
			setBrightness(getApplication(), getWindow(), Brightness.MAX);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Select brightness with medium.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void selectMedium(View view) {
		try {
			setBrightness(getApplication(), getWindow(), Brightness.MEDIUM);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Select brightness with min.
	 *
	 * @param view
	 * 		No usage.
	 */
	public void selectMin(View view) {
		try {
			setBrightness(getApplication(), getWindow(), Brightness.MIN);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
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
				Toast.makeText(getApplicationContext(),
						"You can resume system brightness to MIN, but NO EFFECT on this application, because WindowManager.LayoutParams had been set after the button was clicked.",
						Toast.LENGTH_LONG).show();
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
				Toast.makeText(getApplicationContext(),
						"You can resume system brightness to MAX, but NO EFFECT on this application, because WindowManager.LayoutParams had been set after the button was clicked.",
						Toast.LENGTH_LONG).show();
			}
		}, 5000);
	}

	/**
	 * Brightness levels, only max, medium, min.
	 *
	 * @author Xinyue Zhao
	 */
	enum Brightness {
		MAX(1f), MEDIUM(0.5f), MIN(0.1f);

		public float value;

		Brightness(float _value) {
			value = _value;
		}
	}

	/**
	 * Set system brightness.
	 *
	 * @param cxt
	 * 		{@link android.content.Context}.
	 * @param window
	 * 		{@link android.view.Window}.
	 * @param brightness
	 * 		From 0.1(min), 0.5(medium) 1(max).
	 */
	public static void setBrightness(Context cxt, Window window, Brightness brightness) throws
			SettingNotFoundException {
		int max = 255;
		int medium = (255 + 10) / 2;
		int min = 10;
		try {
			//Get the content resolver.
			ContentResolver cr = cxt.getContentResolver();
			// To handle the auto.
			Settings.System.putInt(cr, Settings.System.SCREEN_BRIGHTNESS_MODE,
					Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
			//Get the current system brightness.
			int currentBrightness = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
			switch (brightness) {
			case MIN:
				currentBrightness = min;
				break;
			case MEDIUM:
				currentBrightness = medium;
				break;
			case MAX:
				currentBrightness = max;
				break;
			default:
				Toast.makeText(cxt, "Out of selection, only max-1, medium-0.5, min-0.1 are available.",
						Toast.LENGTH_LONG).show();
				break;
			}
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
			params.screenBrightness = brightness.value;
			//Apply attribute changes to this window.
			window.setAttributes(params);


		} catch (SettingNotFoundException e) {
			Log.e("Error", "Cannot access system brightness");
			throw e;
		}
	}

}

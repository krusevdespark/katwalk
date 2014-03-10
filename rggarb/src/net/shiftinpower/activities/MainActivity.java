package net.shiftinpower.activities;

import java.util.Timer;
import java.util.TimerTask;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import net.shiftinpower.koldrain.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.shiftinpower.core.C;
import net.shiftinpower.utilities.*;
import net.shiftinpower.fragments.WelcomeSlideFragmentA;
import net.shiftinpower.fragments.WelcomeSlideFragmentB;
import net.shiftinpower.fragments.WelcomeSlideFragmentC;
import net.shiftinpower.fragments.WelcomeSlideFragmentD;
import net.shiftinpower.fragments.WelcomeSlideFragmentE;

/**
 * NOTE:
 * Ideally, all the classes extend a global class from the net.shiftinpower.core package, so global variables and classes 
 * are initiated once and used throughout
 * 
 * Fonts, utility classes, shared preferences are initiated and accessed from there.
 * 
 * However, the case with MainActivity, Login and Signup screens is a bit special, as they do not employ the ActionBar and Sliding menu
 * I can fix this, but I havent had the time to do so.
 * 
 *  @author Kaloyan Kalinov
 */

public class MainActivity extends SherlockFragmentActivity implements OnClickListener {
	
	// Set up XML View Components
	private TextView tvWelcome;
	private TextView tvBraggr;
	private Button bLogin;
	private Button bSignup;

	// Fonts
	private Typeface font1;
	private Typeface font2;
	
	// Variables holding data
	private boolean isUserLoggedIn;
	
	// Shared Preferences
	private Editor sharedPreferencesEditor;
	private SharedPreferences sharedPreferences;
	private static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;
	
	// Custom class to display toasts
	private ToastMaker toastMaker = new ToastMaker();

	// View Pager
	private static final int NUM_PAGES = 5;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private Timer timer;
	private int viewPagerCurrentItem = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if(!StorageStatusChecker.isExternalStorageAvailable()) {
			toastMaker.toast(net.shiftinpower.activities.MainActivity.this, C.Errorz.DISCONNECT_STORAGE_FIRST, Toast.LENGTH_SHORT);
			finish();
		}
		
		super.onCreate(savedInstanceState);
		
		// This app operates in No Title, Fullscreen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Deal with Shared Preferences
		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		isUserLoggedIn = sharedPreferences.getBoolean("userLoggedInState", false);
		
		// Dont allow the user to accidentally get to the Main Screen if they are logged in
		if (isUserLoggedIn) {
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		
		// Set the XML layout
		setContentView(R.layout.activity_layout_activity_main);
		tvBraggr = (TextView) findViewById(R.id.tvBraggr);
		tvWelcome = (TextView) findViewById(R.id.tvWelcome);
		bLogin = (Button) findViewById(R.id.bLogin);
		bSignup = (Button) findViewById(R.id.bSignup);

		bLogin.setOnClickListener(this);
		bSignup.setOnClickListener(this);

		// Instantiate a ViewPager and a PagerAdapter for the slide show
		timer = new Timer();
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);

		slideshowTimer();

		// Set fonts
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
			font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_2);
			tvWelcome.setTypeface(font2);
			tvBraggr.setTypeface(font1);
			bLogin.setTypeface(font1);
			bSignup.setTypeface(font1);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}
	}

	@Override
	public void onBackPressed() {
		// If the user has just logged out, make double sure they are logged out, so they dont go back inside the app by accident.
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean("userLoggedInState", false);
		sharedPreferencesEditor.putInt("currentLoggedInUserId", 0);
		sharedPreferencesEditor.commit();
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (canUserAccessTheInternet()) { // Only let users go on to Login/Signup if they have internet connection, otherwise Toast about it
			
			if (id == R.id.bLogin) {
				Intent login = new Intent(this, Login.class);
				startActivity(login);

			} else if (id == R.id.bSignup) {
				Intent signup = new Intent(this, Signup.class);
				startActivity(signup);
			}
			
		} else {
			toastMaker.toast(net.shiftinpower.activities.MainActivity.this, C.Errorz.NO_INTERNET_CONNECTION, Toast.LENGTH_SHORT);
		}
	}

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// TODO try refactoring this, make it use only one Fragment with different parameters passed to it
		@Override
		public SherlockFragment getItem(int position) {
			if (position == 1) {
				return new WelcomeSlideFragmentD();
			} else if (position == 2) {
				return new WelcomeSlideFragmentE();
			} else if (position == 3) {
				return new WelcomeSlideFragmentC();
			} else if (position == 4) {
				return new WelcomeSlideFragmentB();
			} else {
				return new WelcomeSlideFragmentA();
			}
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public void slideshowTimer() {

		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {

						// for some odd reason, a for loop wouldnt work here :(
						if (viewPagerCurrentItem < NUM_PAGES) {
							mPager.setCurrentItem(viewPagerCurrentItem);
							viewPagerCurrentItem++;
						} else {
							viewPagerCurrentItem = 0;
							mPager.setCurrentItem(viewPagerCurrentItem);
							viewPagerCurrentItem++;
						}
					}
				});
			}

		}, 0, 2000); // 0 seconds delay before first execution, 2 seconds between transitions
	} // End of slideshowTimer

	public Boolean canUserAccessTheInternet() {
		final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
} // End of Class
package net.shiftinpower.core;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import net.shiftinpower.activities.*;
import net.shiftinpower.koldrain.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

/**
* This is the second highest class in the hierarchy. It takes care of setting up the Action Bar and tabs.
* KatwalkSlidingMenu is extending this class, and all activities extend KatwalkSlidingMenu
*
* @author Kaloyan Roussev
*/

public class KatwalkActionBar extends KatwalkCore {

	// This is needed because of the actionbar
	private int mTitleRes;
	protected ActionBar actionBar;

	// Constructor that takes in the titleRes needed for the actionbar. All classes using the action bar need to make a call to that constructor
	public KatwalkActionBar(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (C.Preferences.PORTRAIT_MODE_ENFORCED) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		setTitle(mTitleRes); // This is the title at the Actionbar, but we are not using it

		
		// Set up the Action Bar
		actionBar = getSherlock().getActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.shape_purple_background_gradient_actionbar_experimental));
		actionBar.setDisplayHomeAsUpEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_HOME_UP_ENABLED);
		actionBar.setDisplayShowTitleEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_DISPLAY_TITLE);
		actionBar.setDisplayUseLogoEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_DISPLAY_USE_LOGO);
		actionBar.setHomeButtonEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_HOME_BUTTON_ENABLED);
		actionBar.setIcon(R.drawable.icon_logo);

	} // End of onCreate Method
	
	

	@Override
	protected void onResume() {
		super.onResume();
		
		// If the user is not logged in, they are sent back to the login screen with the activity stack cleared
		// This check is only valid for activities that are extending the action bar, thus being in the logged in part of the app
		userLoggedInState = sharedPreferences.getBoolean("userLoggedInState", false);
		if (!userLoggedInState) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.action_bar_items, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			toggle();
			return true;
		} else if (itemId == R.id.abFeed) {
			Intent feed = new Intent(this, Home.class);
			startActivity(feed);
			return true;
		} else if (itemId == R.id.abNotifications) {
			
			Intent notifications = new Intent(this, NotImplementedYetScreen.class);
			//Intent notifications = new Intent(this, Notifications.class);
			startActivity(notifications);
			return true;
		} else if (itemId == R.id.abAddNewItem) {
			Intent addNewItem = new Intent(this, ItemAddStepOnePhotos.class);
			startActivity(addNewItem);
			return true;
		} else if (itemId == R.id.abSearch) {
			Intent search = new Intent(this, SearchItems.class);
			startActivity(search);
			return true;
		} else {
			return onOptionsItemSelected(item);
		}
	} // End of onOptionsItemSelected Method

	// Handling Navigation Tabs
	public class BasePagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> tabs = new ArrayList<Fragment>();
		private ViewPager pager;

		public BasePagerAdapter(FragmentManager fragmentManager, ViewPager viewPager) {
			super(fragmentManager);
			pager = viewPager;
			pager.setAdapter(this);
			for (int i = 0; i < getCount(); i++) {
				addTab(new KatwalkSlidingMenuListFragment());
			}
		}

		public void addTab(Fragment fragment) {
			tabs.add(fragment);
		}

		@Override
		public Fragment getItem(int position) {
			return tabs.get(position);
		}

		@Override
		public int getCount() {
			return tabs.size();
		}
	} // End of BasePagerAdapter Handling Navigation Tabs

}  // End of Class

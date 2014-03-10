package net.shiftinpower.core;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.WindowManager;
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
 * 
 * @author Kaloyan Roussev
 */
// RggarbSlidingMenu is extending this class, and all activities extend RggarbSlidingMenu

public class RggarbActionBar extends RggarbCore {

	// This is needed because of the actionbar
	private int mTitleRes;

	// Constructor that takes in the titleRes needed for the actionbar. All classes using the action bar need to make a call
	// to that constructor
	public RggarbActionBar(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (C.Preferences.PORTRAIT_MODE_ENFORCED) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setTitle(mTitleRes); // This is the title at the Actionbar, but we are not using it

		// The app operates in Full Screen Mode
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Set up the Action Bar
		ActionBar actionBar = getSherlock().getActionBar();
		actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.shape_purple_background_gradient_actionbar_experimental));
		actionBar.setDisplayHomeAsUpEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_HOME_UP_ENABLED);
		actionBar.setDisplayShowTitleEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_DISPLAY_TITLE);
		actionBar.setDisplayUseLogoEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_DISPLAY_USE_LOGO);
		actionBar.setHomeButtonEnabled(C.SlidingMenuAndActionBarProperties.ACTION_BAR_HOME_BUTTON_ENABLED);
		actionBar.setIcon(R.drawable.icon_logo);

	} // End of onCreate Method

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
			// Intent notifications = new Intent(this, Notifications.class);
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
				addTab(new RggarbSlidingMenuListFragment());
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

} // End of Class

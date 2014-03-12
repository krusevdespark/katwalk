package net.shiftinpower.activities;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.core.*;
import net.shiftinpower.fragments.*;

public class Home extends KatwalkSlidingMenu {

	private TextView titleTV;

	// Constructor needed because of the ActionBar
	public Home() {
		super(R.string.app_name);
	}

	private CharSequence[] tabsText = { C.FeedTabs.ITEMS, C.FeedTabs.PEOPLE, C.FeedTabs.PLACES };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// String userFBId = getIntent().getStringExtra("userFBId");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_home);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab itemsFeedTab = actionBar.newTab();
		ActionBar.Tab peopleFeedTab = actionBar.newTab();
		ActionBar.Tab placesFeedTab = actionBar.newTab();

		Fragment itemsFeedFragment = new FeedItemsContainer();
		Fragment peopleFeedFragment = new FeedPeopleContainer();
		Fragment placesFeedFragment = new FeedPlacesContainer();

		itemsFeedTab.setTabListener(new MyTabsListener(itemsFeedFragment));
		peopleFeedTab.setTabListener(new MyTabsListener(peopleFeedFragment));
		placesFeedTab.setTabListener(new MyTabsListener(placesFeedFragment));

		actionBar.addTab(itemsFeedTab, 0, true);
		actionBar.addTab(peopleFeedTab, 1, false);
		actionBar.addTab(placesFeedTab, 2, false);

		int tabCount = actionBar.getTabCount();

		for (int i = 0; i < tabCount; i++) {
			LayoutInflater inflater = LayoutInflater.from(this);
			View customView = inflater.inflate(R.drawable.actionbar_tab_title, null);
			titleTV = (TextView) customView.findViewById(R.id.action_custom_title);
			actionBar.getTabAt(i).setCustomView(customView);

			try {
				titleTV.setTypeface(katwalk.font2);
				titleTV.setText(tabsText[i]);
				titleTV.setTextColor(getResources().getColor(R.color.green));
			} catch (Exception e) {
				// Nothing can be done in this case
				e.printStackTrace();
			}

		} // End of For Loop

	} // End of onCreate

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.home, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	} // End of MyTabsListener Class

	// If the user comes from the Login or Signup screen, they should not be able to go back there
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, Home.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		super.onBackPressed();
	} // End of onBackPressed

} // End of Class

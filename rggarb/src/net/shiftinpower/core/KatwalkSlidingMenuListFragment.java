package net.shiftinpower.core;

import net.shiftinpower.activities.*;
import net.shiftinpower.activities.person.MyProfile;
import net.shiftinpower.activities.person.PersonProfileItems;
import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;

public class KatwalkSlidingMenuListFragment extends SherlockListFragment {
	
	// Define the menu items
	private TextView text1;
	
	// Fonts
	private Typeface font2;

	private String[] menuItems = { 
			C.SlidingMenuItems.FEED, 
			C.SlidingMenuItems.MY_PROFILE, 
			C.SlidingMenuItems.ITEM_ADD, 
			C.SlidingMenuItems.MY_ITEMS, 
			C.SlidingMenuItems.SEARCH_USERS, 
			C.SlidingMenuItems.SEARCH_ITEMS, 
			C.SlidingMenuItems.MESSAGES, 
			C.SlidingMenuItems.NOTIFICATIONS, 
			C.SlidingMenuItems.POINTS_AND_STATUSES, 
			C.SlidingMenuItems.PRIVACY_POLICY, 
			C.SlidingMenuItems.TERMS_OF_SERVICE, 
			C.SlidingMenuItems.ABOUT, 
			C.SlidingMenuItems.CONTACT_US, 
			C.SlidingMenuItems.SETTINGS, 
			C.SlidingMenuItems.LOG_OUT };

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.drawable.sliding_menu_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.drawable.sliding_menu_custom_list_item, menuItems) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View rowView = super.getView(position, convertView, parent);
				text1 = (TextView) rowView.findViewById(android.R.id.text1);
				
				// Setting fonts
				try {
					font2 = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), C.Fontz.FONT_2);
					text1.setTypeface(font2);
				} catch (Exception e) {
					// Nothing can be done here
				}
				
				return rowView;
			}
		};
		setListAdapter(adapter);
	} // End of onActivityCreated

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		if (itemSelected(v, C.SlidingMenuItems.FEED)) {
			Intent feed = new Intent(getActivity(), Home.class);
			startActivity(feed);
		} else if (itemSelected(v, C.SlidingMenuItems.MY_PROFILE)) {
			Intent myProfile = new Intent(getActivity(), MyProfile.class);
			myProfile.putExtra("currentUser", true);
			startActivity(myProfile);
		} else if (itemSelected(v, C.SlidingMenuItems.ITEM_ADD)) {
			Intent addItem = new Intent(getActivity(), ItemAddStepOnePhotos.class);
			startActivity(addItem);
		} else if (itemSelected(v, C.SlidingMenuItems.MY_ITEMS)) {
			Intent userItems = new Intent(getActivity(), PersonProfileItems.class);
			startActivity(userItems);
		} else if (itemSelected(v, C.SlidingMenuItems.SEARCH_ITEMS)) {
			Intent searchItems = new Intent(getActivity(), SearchItems.class);
			startActivity(searchItems);
		} else if (itemSelected(v, C.SlidingMenuItems.SEARCH_USERS)) {
			Intent searchUsers = new Intent(getActivity(), SearchUsers.class);
			startActivity(searchUsers);
		} else if (itemSelected(v, C.SlidingMenuItems.MESSAGES)) {
			Intent messages = new Intent(getActivity(), Messages.class);
			startActivity(messages);
		} else if (itemSelected(v, C.SlidingMenuItems.NOTIFICATIONS)) {
			Intent notifications = new Intent(getActivity(), NotImplementedYetScreen.class);
			//Intent notifications = new Intent(getActivity(), Notifications.class);
			startActivity(notifications);
		} else if (itemSelected(v, C.SlidingMenuItems.POINTS_AND_STATUSES)) {
			Intent pointsAndStatuses = new Intent(getActivity(), NotImplementedYetScreen.class);
			//Intent pointsAndStatuses = new Intent(getActivity(), PointsAndStatuses.class);
			startActivity(pointsAndStatuses);
		} else if (itemSelected(v, C.SlidingMenuItems.PRIVACY_POLICY)) {
			Intent privacyPolicy = new Intent(getActivity(), NotImplementedYetScreen.class);
			//Intent privacyPolicy = new Intent(getActivity(), PrivacyPolicy.class);
			startActivity(privacyPolicy);
		} else if (itemSelected(v, C.SlidingMenuItems.TERMS_OF_SERVICE)) {
			Intent termsOfService = new Intent(getActivity(), NotImplementedYetScreen.class);
			//Intent termsOfService = new Intent(getActivity(), TermsOfService.class);
			startActivity(termsOfService);
		} else if (itemSelected(v, C.SlidingMenuItems.ABOUT)) {
			Intent about = new Intent(getActivity(), NotImplementedYetScreen.class);
			//Intent about = new Intent(getActivity(), About.class);
			startActivity(about);
		} else if (itemSelected(v, C.SlidingMenuItems.CONTACT_US)) {
			Intent contactUs = new Intent(getActivity(), ContactUs.class);
			//Intent contactUs = new Intent(getActivity(), ContactUs.class);
			startActivity(contactUs);
		} else if (itemSelected(v, C.SlidingMenuItems.SETTINGS)) {
			Intent settings = new Intent(getActivity(), net.shiftinpower.activities.Settings.class);
			startActivity(settings);
		} else if (itemSelected(v, C.SlidingMenuItems.LOG_OUT)) {
			Intent logOut = new Intent(getActivity(), LogUserOut.class);
			startActivity(logOut);
		}
	} // End of onListItemClick

	private boolean itemSelected(View v, String string) {
		return ((TextView) v.findViewById(android.R.id.text1)).getText().toString().equals(string);
	}

} // End of Class

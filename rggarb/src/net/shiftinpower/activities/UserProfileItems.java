package net.shiftinpower.activities;

import net.shiftinpower.koldrain.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import net.shiftinpower.core.*;

public class UserProfileItems extends RggarbSlidingMenu {
	int currentlyLoggedInUser;
	String currentlyLoggedInUserString;
	private static final String APP_SHARED_PREFS = "rggarb_preferences";
	private SharedPreferences sharedPrefs;
	
	public UserProfileItems() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		currentlyLoggedInUser = sharedPrefs.getInt("currentLoggedInUserId", 0);
		currentlyLoggedInUserString = Integer.toString(currentlyLoggedInUser);

		setContentView(R.layout.activity_layout_user_profile_items);
	}

	

}

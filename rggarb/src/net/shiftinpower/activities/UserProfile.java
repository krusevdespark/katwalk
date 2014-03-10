package net.shiftinpower.activities;

import net.shiftinpower.koldrain.R;
import android.os.Bundle;
import net.shiftinpower.core.*;

public class UserProfile extends RggarbSlidingMenu {

	public UserProfile() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_layout_user_profile);
			

	}
	
}

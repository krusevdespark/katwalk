package net.shiftinpower.activities.person;

import net.shiftinpower.koldrain.R;
import android.os.Bundle;
import net.shiftinpower.core.*;
/**
 * 
 * This is some other user's profile class (user that is not the one that is currently using the app) and it should always be started this way:
 * 
 * Intent userProfile = new Intent(getActivity(), UserProfile.class); 
 * 
 * userProfile.putExtra("currentUser", false);
 * 
 * startActivity(userProfile);
 * 
 * @author Kaloyan Roussev
 * 
 */
public class UserProfile extends KatwalkSlidingMenu {

	public UserProfile() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_layout_user_profile);
			

	}
	
}

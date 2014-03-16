package net.shiftinpower.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import net.shiftinpower.activities.person.MyProfile;
import net.shiftinpower.koldrain.R;

/**
* This is the third highest class in the hierarchy. It takes care of the sliding menu that is accessible through pretty much
* Every activity in the App, except for the Login/Signup screens All activities for the logged in users should extend this
* activity
*
* @author Kaloyan Roussev
*
*/
public class KatwalkSlidingMenu extends KatwalkActionBar {

	// XML View elements
	private TextView tvSlidingMenuUserName;
	private ImageView ivSlidingMenuUserAvatar;

	// Sliding Menu
	private SlidingMenu slidingMenu;
	protected ListFragment listFragment;

	// Constructor needed because of the actionbar
	public KatwalkSlidingMenu(int titleRes) {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the XML view layout
		setBehindContentView(R.drawable.sliding_menu_header);

		// Create the sliding menu.
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new KatwalkSlidingMenuListFragment()).commit();

		// Set up the sliding menu
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		listFragment = new KatwalkSlidingMenuListFragment();
		fragmentTransaction.replace(R.id.menu_frame, listFragment);
		fragmentTransaction.commit();

		slidingMenu = getSlidingMenu();
		slidingMenu.setShadowWidth(C.SlidingMenuAndActionBarProperties.SHADOW);
		slidingMenu.setBehindOffset(C.SlidingMenuAndActionBarProperties.BEHIND_OFFSET);
		slidingMenu.setFadeDegree(C.SlidingMenuAndActionBarProperties.FADE_DEGREE);

		// No matter where from on the screen, the user can pull the Screen to the right and The Sliding Menu will Slide in
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setSlidingActionBarEnabled(true);

		// Set the user name and the user avatar in the header of the sliding menu
		tvSlidingMenuUserName = (TextView) findViewById(R.id.tvSlidingMenuUserName);

		// Try setting the fonts
		try {
			tvSlidingMenuUserName.setTypeface(katwalk.font2);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		tvSlidingMenuUserName.setText(userName);
		tvSlidingMenuUserName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myProfile = new Intent(KatwalkSlidingMenu.this, MyProfile.class);
				myProfile.putExtra("currentUser", true);
				startActivity(myProfile);

			}
		});

		ivSlidingMenuUserAvatar = (ImageView) getSlidingMenu().findViewById(R.id.ivSlidingMenuUserAvatar);

		katwalk.setUserImageToImageView(ivSlidingMenuUserAvatar, userAvatarPath, userSex);

		ivSlidingMenuUserAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent myProfile = new Intent(KatwalkSlidingMenu.this, MyProfile.class);
				myProfile.putExtra("currentUser", true);
				startActivity(myProfile);

			}
		});
	} // End of onCreate

	// When the user presses the hardware Settings button, the sliding menu should open
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (slidingMenu != null) {
				slidingMenu.toggle();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	} // End of onKeyUp

} // End of Class

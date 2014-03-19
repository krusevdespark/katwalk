package net.shiftinpower.activities.person;

import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import net.shiftinpower.activities.MessagesCompose;
import net.shiftinpower.activities.NotImplementedYetScreen;
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
public class UserProfile extends PersonProfile {

	private boolean thereIsAPendingFriendRequest = false ; // TODO false for testing reasons atm
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			super.identifyUser(extras);
		}
		
		

		// Set action buttons' text
		bUserProfileActionButtonOne.setText(R.string.bAnotherUserProfileMessage);
		
		// TODO check whether a friend request has already been sent 
		if(thereIsAPendingFriendRequest){
			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileAddFriend);
		} else {
			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriendshipRequestSent);
		}
		
		// Set OnClick Listeners
		bUserProfileActionButtonOne.setOnClickListener(this);
		bUserProfileActionButtonTwo.setOnClickListener(this);
			

	} // End of onCreate
	
	@Override
	public void onClick(View v) {
		
		int id = v.getId();

		switch (id) {
		case R.string.bAnotherUserProfileMessage:
			Intent notImplementedYet = new Intent(UserProfile.this, NotImplementedYetScreen.class);
			//Intent composeMessage = new Intent(UserProfile.this, MessagesCompose.class);
			//composeMessage.putExtra("receiverId", personId);
			startActivity(notImplementedYet);
			
			break;
			
		case R.string.bAnotherUserProfileAddFriend:
			if(thereIsAPendingFriendRequest){
				bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileAddFriend);
				// TODO cancel the friend request
				katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Confirmationz.FRIEND_REQUEST_CANCELLED, Toast.LENGTH_SHORT);
			} else {
				bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriendshipRequestSent);
				// TODO send a friend request
				katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Confirmationz.FRIEND_REQUEST_SENT, Toast.LENGTH_SHORT);
			}
			break;
		}
		
	} // End of onClick
	
} // End of Class

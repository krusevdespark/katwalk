package net.shiftinpower.activities.person;

import net.shiftinpower.interfaces.OnCancelFriendRequestListener;
import net.shiftinpower.interfaces.OnRemoveFriendListener;
import net.shiftinpower.interfaces.OnFriendRequestSendListener;
import net.shiftinpower.koldrain.R;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.shiftinpower.activities.NotImplementedYetScreen;
import net.shiftinpower.activities.Settings;
import net.shiftinpower.asynctasks.DeleteUserFromServerAsync;
import net.shiftinpower.asynctasks.RemoveFriendAsync;
import net.shiftinpower.asynctasks.FriendRequestSendAsync;
import net.shiftinpower.core.*;

/**
 * 
 * This is some other user's profile class (user that is not the one that is currently using the app) and it should always be
 * started this way:
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

public class UserProfile extends PersonProfile implements OnFriendRequestSendListener, OnCancelFriendRequestListener, OnRemoveFriendListener{

	// Delete friend dialog and its XML Components
	public Dialog deleteFriendDialog;
	public Button bDeleteFriendSubmit;
	public Button bDeleteFriendCancel;
	
	// Data holding variables
	private boolean thereIsAPendingFriendRequest = false; // TODO false for testing reasons atm

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			super.identifyUser(extras);
		}
		
		// Set action buttons' text
		bUserProfileActionButtonOne.setText(R.string.bAnotherUserProfileMessage);
		
		if(!personIsFriendsWithCurrentUser){
			
			if (thereIsAPendingFriendRequest) {
				bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriendshipRequestSent);
			} else {
				bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileAddFriend);
			}
			
		} else {
			
			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriends);
			
		}

		// Set OnClick Listeners
		bUserProfileActionButtonOne.setOnClickListener(this);
		bUserProfileActionButtonTwo.setOnClickListener(this);

		tvUserProfileStatsAreVisibleNote.setVisibility(View.GONE);

		if (personQuote == null || personQuote.contentEquals(C.FallbackCopy.CLICK_HERE_TO_CHANGE_YOUR_QUOTE)) {
			tvUserQuote.setVisibility(View.GONE);
		}

	} // End of onCreate

	@Override
	public void onClick(View v) {

		int id = v.getId();

		switch (id) {
		case R.id.bUserProfileActionButtonOne:
			Intent notImplementedYet = new Intent(UserProfile.this, NotImplementedYetScreen.class);
			// Intent composeMessage = new Intent(UserProfile.this, MessagesCompose.class);
			// composeMessage.putExtra("receiverId", personId);
			startActivity(notImplementedYet);

			break;

		case R.id.bUserProfileActionButtonTwo:
			if(!personIsFriendsWithCurrentUser){
				if (thereIsAPendingFriendRequest) {
					bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileAddFriend);
					// TODO cancel the friend request
					katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Confirmationz.FRIEND_REQUEST_CANCELLED, Toast.LENGTH_SHORT);
					thereIsAPendingFriendRequest = false;
				} else {
					bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriendshipRequestSent);
					new FriendRequestSendAsync(UserProfile.this, currentlyLoggedInUser, personId);
					
				}
			} else {
				
				deleteFriendDialog = new Dialog(UserProfile.this, R.style.no_title_dialog);
				deleteFriendDialog.setContentView(R.layout.dialog_delete_friend);
				
				bDeleteFriendSubmit = (Button) deleteFriendDialog.findViewById(R.id.bDeleteFriendSubmit);
				bDeleteFriendCancel = (Button) deleteFriendDialog.findViewById(R.id.bDeleteFriendCancel);
			
				// Set fonts
				try {

					bDeleteFriendSubmit.setTypeface(katwalk.font1);
					bDeleteFriendCancel.setTypeface(katwalk.font1);

				} catch (Exception e) {
					// Nothing can be done here
					e.printStackTrace();
				}
				
				bDeleteFriendCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						deleteFriendDialog.dismiss();

					}
				});

				bDeleteFriendSubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						new RemoveFriendAsync(UserProfile.this, currentlyLoggedInUser, personId).execute();
						deleteFriendDialog.dismiss();
					}

				});

				deleteFriendDialog.show();
				
			} // End of Unfriend User Case

			break;
		}

	} // End of onClick

	@Override
	public void onFriendRequestSendSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Confirmationz.FRIEND_REQUEST_SENT, Toast.LENGTH_SHORT);
		thereIsAPendingFriendRequest = true;
	}

	@Override
	public void onFriendRequestSendFailure() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Errorz.FRIENDSHIP_REQUEST_NOT_SENT, Toast.LENGTH_LONG);
		thereIsAPendingFriendRequest = false;
	}

	@Override
	public void onRemoveFriendSuccess() {
		thereIsAPendingFriendRequest = false;
		personIsFriendsWithCurrentUser = false;
		bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileAddFriend);
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Confirmationz.FRIEND_REMOVED, Toast.LENGTH_LONG);
	}

	@Override
	public void onRemoveFriendFailure() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.UserProfile.this, C.Errorz.FRIEND_NOT_REMOVED, Toast.LENGTH_LONG);
		
	}

	@Override
	public void onCancelFriendRequestSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelFriendRequestFailure() {
		// TODO Auto-generated method stub
		
	}

} // End of Class
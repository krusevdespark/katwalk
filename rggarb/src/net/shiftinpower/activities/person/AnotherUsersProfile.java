package net.shiftinpower.activities.person;

import net.shiftinpower.interfaces.OnFriendRequestAcceptListener;
import net.shiftinpower.interfaces.OnFriendRequestCancelListener;
import net.shiftinpower.interfaces.OnFriendRequestRejectListener;
import net.shiftinpower.interfaces.OnIdentifyUserListener;
import net.shiftinpower.interfaces.OnRemoveFriendListener;
import net.shiftinpower.interfaces.OnFriendRequestSendListener;
import net.shiftinpower.koldrain.R;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import net.shiftinpower.activities.NotImplementedYetScreen;
import net.shiftinpower.asynctasks.FriendRequestAcceptAsync;
import net.shiftinpower.asynctasks.FriendRequestCancelAsync;
import net.shiftinpower.asynctasks.FriendRequestRejectAsync;
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

public class AnotherUsersProfile extends PersonProfile implements OnFriendRequestSendListener, OnFriendRequestCancelListener, OnRemoveFriendListener,
		OnFriendRequestAcceptListener, OnFriendRequestRejectListener, OnIdentifyUserListener {

	private Bundle extrasObtained;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setOnIdentifyUserListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			extrasObtained = extras;
			super.identifyUser(extras);
		}

		tvUserProfileFriendsTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent personProfileFriends = new Intent(AnotherUsersProfile.this, PersonProfileFriends.class);
				personProfileFriends.putExtra("personId", personId);
				personProfileFriends.putExtra("personName", personName);
				personProfileFriends.putExtra("comingFromMyProfile", false);
				startActivity(personProfileFriends);

			}
		});

	} // End of onCreate

	@Override
	public void onFriendRequestSendSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Confirmationz.FRIEND_REQUEST_SENT, Toast.LENGTH_SHORT);
		super.identifyUser(extrasObtained);
	}

	@Override
	public void onFriendRequestSendFailure() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Errorz.FRIENDSHIP_REQUEST_NOT_SENT, Toast.LENGTH_LONG);
	}

	@Override
	public void onRemoveFriendSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Confirmationz.FRIEND_REMOVED, Toast.LENGTH_LONG);
		super.identifyUser(extrasObtained);
	}

	@Override
	public void onRemoveFriendFailure() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Errorz.FRIEND_NOT_REMOVED, Toast.LENGTH_LONG);
	}

	@Override
	public void onFriendRequestCancelSuccess() {

		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Confirmationz.FRIEND_REQUEST_CANCELLED, Toast.LENGTH_SHORT);
		super.identifyUser(extrasObtained);

	}

	@Override
	public void onFriendRequestCancelFailure() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Errorz.FRIEND_REQUEST_NOT_CANCELLED, Toast.LENGTH_SHORT);
	}

	@Override
	public void onFriendRequestAcceptSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Confirmationz.FRIEND_REQUEST_ACCEPTED, Toast.LENGTH_SHORT);
		super.identifyUser(extrasObtained);

	}

	@Override
	public void onFriendRequestAcceptFailure() {

		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Errorz.FRIEND_REQUEST_NOT_ACCEPTED, Toast.LENGTH_SHORT);

	}

	@Override
	public void onFriendRequestRejectSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Confirmationz.FRIEND_REQUEST_REJECTED, Toast.LENGTH_SHORT);
		super.identifyUser(extrasObtained);

	}

	@Override
	public void onFriendRequestRejectFailure() {

		katwalk.toastMaker.toast(net.shiftinpower.activities.person.AnotherUsersProfile.this, C.Errorz.FRIEND_REQUEST_NOT_REJECTED, Toast.LENGTH_SHORT);

	}

	@Override
	public void onUserIdentified() {

		// Set action buttons' text and actions
		if (personHasFriendRequestFromUs) {

			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriendshipRequestSent);
			bUserProfileActionButtonTwo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					displayCancelFriendRequestDialog();

				}
			});

		} else if (personHasSentUsFriendRequest) {

			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileConfirmFriendRequest);
			bUserProfileActionButtonTwo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					displayRespondToFriendRequestDialog();

				}
			});

		} else if (personIsFriendsWithCurrentUser) {

			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileFriends);
			bUserProfileActionButtonTwo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					displayRemoveFriendDialog();

				}
			});

		} else { // Users are not friends and there are no pending friend requests between them.

			bUserProfileActionButtonTwo.setText(R.string.bAnotherUserProfileAddFriend);
			bUserProfileActionButtonTwo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new FriendRequestSendAsync(AnotherUsersProfile.this, AnotherUsersProfile.this, currentlyLoggedInUser, personId).execute();

				}
			});

		}

		bUserProfileActionButtonOne.setText(R.string.bAnotherUserProfileMessage);
		bUserProfileActionButtonOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Intent composeMessage = new Intent(UserProfile.this, MessagesCompose.class);
				// composeMessage.putExtra("receiverId", personId);

				Intent notImplementedYet = new Intent(AnotherUsersProfile.this, NotImplementedYetScreen.class);
				startActivity(notImplementedYet);

			}
		});

		tvUserProfileStatsAreVisibleNote.setVisibility(View.GONE);

		if (personQuote == null || personQuote.contentEquals(C.FallbackCopy.CLICK_HERE_TO_CHANGE_YOUR_QUOTE)) {
			tvUserQuote.setVisibility(View.GONE);
		}

	} // End ofonUserIdentified

	private void displayRemoveFriendDialog() {

		// Delete friend dialog and its XML Components
		final Dialog deleteFriendDialog;
		Button bDeleteFriendSubmit;
		Button bDeleteFriendCancel;

		deleteFriendDialog = new Dialog(AnotherUsersProfile.this, R.style.no_title_dialog);
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

				new RemoveFriendAsync(AnotherUsersProfile.this, AnotherUsersProfile.this, currentlyLoggedInUser, personId).execute();
				deleteFriendDialog.dismiss();
			}

		});

		deleteFriendDialog.show();
	} // End of displayRemoveFriendDialog

	private void displayRespondToFriendRequestDialog() {

		// Respond to friend request dialog and its XML Components
		final Dialog respondToFriendRequestDialog;
		Button bDialogRespondToFriendRequestSubmit;
		Button bDialogRespondToFriendRequestReject;
		Button bDialogRespondToFriendRequestCancel;

		respondToFriendRequestDialog = new Dialog(AnotherUsersProfile.this, R.style.no_title_dialog);
		respondToFriendRequestDialog.setContentView(R.layout.dialog_respond_to_friend_request);

		bDialogRespondToFriendRequestSubmit = (Button) respondToFriendRequestDialog.findViewById(R.id.bDialogRespondToFriendRequestSubmit);
		bDialogRespondToFriendRequestReject = (Button) respondToFriendRequestDialog.findViewById(R.id.bDialogRespondToFriendRequestReject);
		bDialogRespondToFriendRequestCancel = (Button) respondToFriendRequestDialog.findViewById(R.id.bDialogRespondToFriendRequestCancel);

		// Set fonts
		try {

			bDialogRespondToFriendRequestSubmit.setTypeface(katwalk.font1);
			bDialogRespondToFriendRequestReject.setTypeface(katwalk.font1);
			bDialogRespondToFriendRequestCancel.setTypeface(katwalk.font1);

		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		bDialogRespondToFriendRequestSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new FriendRequestAcceptAsync(AnotherUsersProfile.this, AnotherUsersProfile.this, personId, currentlyLoggedInUser).execute();
				respondToFriendRequestDialog.dismiss();

			}
		});

		bDialogRespondToFriendRequestReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new FriendRequestRejectAsync(AnotherUsersProfile.this, AnotherUsersProfile.this, personId, currentlyLoggedInUser).execute();
				respondToFriendRequestDialog.dismiss();

			}
		});

		bDialogRespondToFriendRequestCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				respondToFriendRequestDialog.dismiss();

			}
		});

		respondToFriendRequestDialog.show();

	} // End of displayRespondToFriendRequestDialog

	private void displayCancelFriendRequestDialog() {

		// Cancel friend request dialog and its XML Components
		final Dialog cancelFriendRequestDialog;
		Button bDialogCancelFriendRequestSubmit;
		Button bDialogCancelFriendRequestCancel;

		cancelFriendRequestDialog = new Dialog(AnotherUsersProfile.this, R.style.no_title_dialog);
		cancelFriendRequestDialog.setContentView(R.layout.dialog_cancel_friend_request);

		bDialogCancelFriendRequestSubmit = (Button) cancelFriendRequestDialog.findViewById(R.id.bDialogCancelFriendRequestSubmit);
		bDialogCancelFriendRequestCancel = (Button) cancelFriendRequestDialog.findViewById(R.id.bDialogCancelFriendRequestCancel);

		// Set fonts
		try {

			bDialogCancelFriendRequestSubmit.setTypeface(katwalk.font1);
			bDialogCancelFriendRequestCancel.setTypeface(katwalk.font1);

		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		bDialogCancelFriendRequestSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new FriendRequestCancelAsync(AnotherUsersProfile.this, AnotherUsersProfile.this, currentlyLoggedInUser, personId).execute();
				cancelFriendRequestDialog.dismiss();
			}
		});

		bDialogCancelFriendRequestCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				cancelFriendRequestDialog.dismiss();

			}
		});

		cancelFriendRequestDialog.show();

	} // End of displayCancelFriendRequestDialog

} // End of Class
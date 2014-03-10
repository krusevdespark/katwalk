package net.shiftinpower.activities;

import java.text.DecimalFormat;
import net.shiftinpower.asynctasks.ChangeUserQuoteAsync;
import net.shiftinpower.asynctasks.SetUserAvatarAsync;
import net.shiftinpower.asynctasks.UploadUserAvatarToServerAsync;
import net.shiftinpower.core.*;
import net.shiftinpower.utilities.PhotoHandler;
import net.shiftinpower.interfaces.OnChangeUserQuoteListener;
import net.shiftinpower.koldrain.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * NOTE: In a newer, but unstable version of the App, MyProfile and UserProfile both extend a PersonProfile class that holds
 * the code they share so code duplication/redundancy is avoided.
 * 
 * NOTE: The piece of code that assigns an image to the user avatar ImageView is extracted to a method and used in several places
 * throughout the app in a newer, but unstable version of the App.
 * 
 * @author Kaloyan Kalinov
 *
 */

public class MyProfile extends RggarbSlidingMenu implements OnClickListener, OnChangeUserQuoteListener {

	// Set up XML View Components
	private TextView tvUserName;
	private TextView tvUserQuote;
	private TextView tvMoneySpent;
	private TextView tvUserStatus;
	private TextView tvChangeUserQuoteTitle;
	private TextView tvUserProfileStatsAreVisibleNote;
	private ImageButton iUserAvatar;
	public TextView tvUserProfileItemsTab;
	public TextView tvUserProfileCommentsTab;
	public TextView tvUserProfileFollowingTab;
	public TextView tvUserProfileFriendsTab;
	public TextView tvUserProfileGalleryTab;
	public TextView tvUserProfileActivityTab;
	public Button bMyProfilePoints;
	public Button bMyProfileSettings;
	@SuppressWarnings("unused")
	private TableRow myProfileActivityHolder;

	// Change user quote dialog and its XML components
	private Dialog changeUserQuoteDialog;
	private EditText etChangeUserQuoteContent;
	private Button bChangeUserQuoteSubmit;
	private Button bChangeUserQuoteCancel;

	// Image handling variables
	private Bitmap userAvatarBitmap;

	// Constructor needed because of the way the super class works
	public MyProfile() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Assign and inflate an XML file as the view component for this screen
		setContentView(R.layout.activity_layout_user_profile);

		// Set the My Points and Settings Buttons
		bMyProfilePoints = (Button) findViewById(R.id.bUserProfileActionButtonOne);
		bMyProfilePoints.setText(String.valueOf(userPoints) + " points");
		bMyProfilePoints.setOnClickListener(this);
		bMyProfileSettings = (Button) findViewById(R.id.bUserProfileActionButtonTwo);
		bMyProfileSettings.setOnClickListener(this);

		// Set the stats panel views
		tvUserProfileItemsTab = (TextView) findViewById(R.id.tvUserProfileItemsTab);
		tvUserProfileCommentsTab = (TextView) findViewById(R.id.tvUserProfileCommentsTab);
		tvUserProfileFollowingTab = (TextView) findViewById(R.id.tvUserProfileFollowingTab);
		tvUserProfileFriendsTab = (TextView) findViewById(R.id.tvUserProfileFriendsTab);
		tvUserProfileGalleryTab = (TextView) findViewById(R.id.tvUserProfileGalleryTab);
		tvUserProfileActivityTab = (TextView) findViewById(R.id.tvUserProfileActivityTab);
		tvUserProfileStatsAreVisibleNote = (TextView) findViewById(R.id.tvUserProfileStatsAreVisibleNote);

		tvUserProfileItemsTab.setText("[" + userItemsCount + "] Items");
		tvUserProfileCommentsTab.setText("[" + userCommentsCount + "] Comments");
		tvUserProfileFollowingTab.setText("[" + userFollowingItemsCount + "] Following");
		tvUserProfileFriendsTab.setText("[" + userFriendsCount + "] Friends");
		tvUserProfileGalleryTab.setText("[" + userGalleryPhotosCount + "] Gallery");
		tvUserProfileActivityTab.setText("[" + userActivityCount + "] Activities");

		tvUserProfileItemsTab.setOnClickListener(this);
		tvUserProfileCommentsTab.setOnClickListener(this);
		tvUserProfileFollowingTab.setOnClickListener(this);
		tvUserProfileFriendsTab.setOnClickListener(this);
		tvUserProfileGalleryTab.setOnClickListener(this);
		tvUserProfileActivityTab.setOnClickListener(this);
		tvUserProfileStatsAreVisibleNote.setOnClickListener(this);

		// Trying to set some fonts
		try {
			tvUserProfileItemsTab.setTypeface(font2);
			tvUserProfileCommentsTab.setTypeface(font2);
			tvUserProfileFollowingTab.setTypeface(font2);
			tvUserProfileFriendsTab.setTypeface(font2);
			tvUserProfileGalleryTab.setTypeface(font2);
			tvUserProfileActivityTab.setTypeface(font2);
		} catch (Exception e) {
			tvUserProfileItemsTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileCommentsTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileFollowingTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileFriendsTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileGalleryTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileActivityTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
		}

		// TODO maybe put this in a OnUserActivityReceived
		myProfileActivityHolder = (TableRow) findViewById(R.id.userProfileActivityHolder);
		getSupportFragmentManager().beginTransaction().replace(R.id.userProfileActivityHolder, new MyProfileActivity()).commit();

		// Set user display name
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserName.setText(userName);
		try {
			tvUserName.setTypeface(font2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set user status
		setUserStatus(userPoints);
		tvUserStatus = (TextView) findViewById(R.id.tvUserStatus);
		tvUserStatus.setText(userStatus);

		// Set the money spent text
		tvMoneySpent = (TextView) findViewById(R.id.tvMoneySpent);
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		tvMoneySpent.setText(C.Miscellaneous.CURRENCY_SYMBOL + String.valueOf(decimalFormat.format(userMoneySpentOnItems) + " spent on items"));

		// Set avatar image
		iUserAvatar = (ImageButton) findViewById(R.id.iUserAvatar);

		if (userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE) || userAvatarPath.contentEquals("") || userAvatarPath == null) {
			if (userSex.equalsIgnoreCase("male")) {
				iUserAvatar.setImageResource(R.drawable.images_default_avatar_male);
			} else {
				iUserAvatar.setImageResource(R.drawable.images_default_avatar_female);
			}

		} else {

			try {
				userAvatarBitmap = BitmapFactory.decodeFile(userAvatarPath);
				iUserAvatar.setImageBitmap(userAvatarBitmap);

			} catch (Exception ex) {
				ex.printStackTrace();

				if (userSex.equalsIgnoreCase("male")) {
					iUserAvatar.setImageResource(R.drawable.images_default_avatar_male);
				} else {
					iUserAvatar.setImageResource(R.drawable.images_default_avatar_female);
				}

			}
		}

		// When the user clicks their avatar they should see a dialog with
		// options to remove it or upload a new one.
		iUserAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent changeAvatarDialog = new Intent(MyProfile.this, ProvideImageDialog.class);
				changeAvatarDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, userAvatarPath);
				startActivityForResult(changeAvatarDialog, C.ImageHandling.REQUEST_CODE_CHANGE_IMAGE);

			}
		}); // end of image button on click handling

		// set the user quote, add a click listener to it and open a dialog so
		// user can change it if they want to
		tvUserQuote = (TextView) findViewById(R.id.tvUserQuote);
		tvUserQuote.setText(userQuote);
		tvUserQuote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeUserQuoteDialog = new Dialog(MyProfile.this, R.style.no_title_dialog);
				changeUserQuoteDialog.setContentView(R.layout.dialog_change_user_quote);

				tvChangeUserQuoteTitle = (TextView) changeUserQuoteDialog.findViewById(R.id.tvChangeUserQuoteTitle);
				tvChangeUserQuoteTitle.setText(C.Confirmationz.CHANGE_YOUR_USER_QUOTE);

				etChangeUserQuoteContent = (EditText) changeUserQuoteDialog.findViewById(R.id.etChangeUserQuoteContent);
				bChangeUserQuoteSubmit = (Button) changeUserQuoteDialog.findViewById(R.id.bChangeUserQuoteSubmit);
				bChangeUserQuoteCancel = (Button) changeUserQuoteDialog.findViewById(R.id.bChangeUserQuoteCancel);

				// Trying to set the fonts for the dialog buttons
				try {
					bChangeUserQuoteSubmit.setTypeface(font1);
					bChangeUserQuoteCancel.setTypeface(font1);
				} catch (Exception e) {
					// There is nothing I can do here
				}

				bChangeUserQuoteSubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setUserQuote(etChangeUserQuoteContent.getText().toString());
						new ChangeUserQuoteAsync(MyProfile.this, MyProfile.this, String.valueOf(currentlyLoggedInUser), userQuote).execute();

					}
				});

				bChangeUserQuoteCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						changeUserQuoteDialog.dismiss();

					}
				});

				changeUserQuoteDialog.show();

			}
		}); // End of Change Quote Button OnClick Handling

	} // End of onCreate Method

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {

		case R.id.tvUserProfileItemsTab:
			Intent myProfileItems = new Intent(this, MyProfileItems.class);
			startActivity(myProfileItems);
			break;

		case R.id.tvUserProfileFollowingTab:
			Intent myProfileFollowing = new Intent(this, MyProfileFollowing.class);
			startActivity(myProfileFollowing);
			break;

		case R.id.tvUserProfileFriendsTab:
			Intent myProfileFriends = new Intent(this, MyProfileFriends.class);
			startActivity(myProfileFriends);
			break;

		case R.id.tvUserProfileGalleryTab:
			
			Intent myProfilePhotos = new Intent(this, NotImplementedYetScreen.class);
			//Intent notifications = new Intent(getActivity(), Notifications.class);
			
			//Intent myProfilePhotos = new Intent(this, MyProfilePhotos.class);
			startActivity(myProfilePhotos);
			break;

		case R.id.tvUserProfileCommentsTab:
			Intent myProfileReviews = new Intent(this, MyProfileComments.class);
			startActivity(myProfileReviews);
			break;

		case R.id.tvUserProfileActivityTab:
			// TODO scroll down and show the activity perhaps?
			break;

		case R.id.bUserProfileActionButtonOne:
			// Intent myPoints = new Intent(this, MyPoints.class);
			// startActivity(myPoints);
			break;

		case R.id.bUserProfileActionButtonTwo:
		case R.id.tvUserProfileStatsAreVisibleNote:
			Intent settings = new Intent(this, Settings.class);
			startActivity(settings);
			break;

		} // End of Switch

	} // End of onClick Method

	@Override
	public void onChangeUserQuoteSuccess() {
		toastMaker.toast(net.shiftinpower.activities.MyProfile.this, C.Confirmationz.USER_QUOTE_SUCCESSFULLY_CHANGED, Toast.LENGTH_SHORT);
		changeUserQuoteDialog.dismiss();
		setUserQuote(etChangeUserQuoteContent.getText().toString());
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_QUOTE, userQuote);
		sharedPreferencesEditor.commit();
		tvUserQuote.setText(etChangeUserQuoteContent.getText().toString());
	}

	@Override
	public void onChangeUserQuoteFailure(String reason) {
		if (reason.contentEquals(C.Tagz.BAD_REQUEST)) {
			toastMaker.toast(net.shiftinpower.activities.MyProfile.this, C.Errorz.USER_QUOTE_NOT_CHANGED_BAD_REQUEST_EXCUSE, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM)) {
			toastMaker.toast(net.shiftinpower.activities.MyProfile.this, C.Errorz.USER_QUOTE_NOT_CHANGED_DB_PROBLEM__EXCUSE, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.UNKNOWN_PROBLEM)) {
			toastMaker.toast(net.shiftinpower.activities.MyProfile.this, C.Errorz.USER_QUOTE_NOT_CHANGED_UNKNOWN_PROBLEM_EXCUSE, Toast.LENGTH_LONG);
		}
		changeUserQuoteDialog.dismiss();
	}

	/*
	 * We get the user points count from the database and set their status according to that. Status is something like a
	 * title, seen from everybody who visits their profile
	 */
	public void setUserStatus(int userPoints) {
		if (userPoints >= C.StatusPoints.STATUS_1_MIN && userPoints <= C.StatusPoints.STATUS_1_MAX) {
			setUserStatusVariable(C.Statuses.STATUS_1);
		} else if (userPoints >= C.StatusPoints.STATUS_2_MIN && userPoints <= C.StatusPoints.STATUS_2_MAX) {
			setUserStatusVariable(C.Statuses.STATUS_2);
		} else if (userPoints >= C.StatusPoints.STATUS_3_MIN && userPoints <= C.StatusPoints.STATUS_3_MAX) {
			setUserStatusVariable(C.Statuses.STATUS_3);
		} else if (userPoints >= C.StatusPoints.STATUS_4_MIN && userPoints <= C.StatusPoints.STATUS_4_MAX) {
			setUserStatusVariable(C.Statuses.STATUS_4);
		} else if (userPoints >= C.StatusPoints.STATUS_5_MIN && userPoints <= C.StatusPoints.STATUS_5_MAX) {
			setUserStatusVariable(C.Statuses.STATUS_5);
		}
	} // End of setUserStatus

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {
			userAvatarPath = data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY);
			if (!(userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE))) {
				userHasProvidedOwnPhoto = true;
			} else {
				userHasProvidedOwnPhoto = false;
			}

			// Set sharedPreferences to the appropriate values
			sharedPreferencesEditor = sharedPreferences.edit();
			sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userAvatarPath);
			sharedPreferencesEditor.commit();

			if (!userHasProvidedOwnPhoto) {
				// Remove user's avatar from the server
				new SetUserAvatarAsync(String.valueOf(currentlyLoggedInUser), C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE).execute();

			} else {

				// Uploading the new avatar to the Server. In that method's onPostExecute we are running the
				// SetUserAvatarAsync class that sets the new path to the avatar in the Database on the Server
				String imageFilename = PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true);
				new UploadUserAvatarToServerAsync(String.valueOf(currentlyLoggedInUser), userAvatarPath, imageFilename).execute();
			}

			// Custom method that checks the API version and restarts the activity appropriately. It is held by the
			// superclass
			// Activity restart is needed because we need to rebuild the Sliding Menu in order for it to obtain the new image
			// in the Sliding Menu Header
			restartActivity();
		}

	} // End of onActivityResult

} // End of Class
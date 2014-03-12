package net.shiftinpower.activities.person;

import net.shiftinpower.activities.NotImplementedYetScreen;
import net.shiftinpower.activities.ProvideImageDialog;
import net.shiftinpower.activities.Settings;
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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfile extends PersonProfile implements OnClickListener, OnChangeUserQuoteListener {

	// Change user quote dialog and its XML components
	private Dialog changeUserQuoteDialog;
	private EditText etChangeUserQuoteContent;
	private Button bChangeUserQuoteSubmit;
	private Button bChangeUserQuoteCancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			super.identifyUser(extras);
		}
		
		// Set OnClick Listeners
		bUserProfileActionButtonOne.setOnClickListener(this);
		bUserProfileActionButtonTwo.setOnClickListener(this);
		tvUserProfileStatsAreVisibleNote.setOnClickListener(this);
		iUserAvatar.setOnClickListener(this);

		// Add a click listener to user Quote and open a dialog so user can change it if they want to
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
					bChangeUserQuoteSubmit.setTypeface(katwalk.font1);
					bChangeUserQuoteCancel.setTypeface(katwalk.font1);
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

		case R.id.bUserProfileActionButtonOne:
			Intent myPoints = new Intent(this, NotImplementedYetScreen.class);
			startActivity(myPoints);
			break;

		case R.id.bUserProfileActionButtonTwo:
		case R.id.tvUserProfileStatsAreVisibleNote:
			Intent settings = new Intent(this, Settings.class);
			startActivity(settings);
			break;

		case R.id.iUserAvatar:
			// When the user clicks their avatar they should see a dialog with options to remove it or upload a new one.
			Intent changeAvatarDialog = new Intent(MyProfile.this, ProvideImageDialog.class);
			changeAvatarDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, userAvatarPath);
			startActivityForResult(changeAvatarDialog, C.ImageHandling.REQUEST_CODE_CHANGE_IMAGE);
			break;

		} // End of Switch
		
		super.onClick(v);

	} // End of onClick Method

	@Override
	public void onChangeUserQuoteSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.person.MyProfile.this, C.Confirmationz.USER_QUOTE_SUCCESSFULLY_CHANGED, Toast.LENGTH_SHORT);
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
			katwalk.toastMaker.toast(net.shiftinpower.activities.person.MyProfile.this, C.Errorz.ITEM_NOT_ADDED_BAD_REQUEST_EXCUSE, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.person.MyProfile.this, C.Errorz.ITEM_NOT_ADDED_DB_PROBLEM_EXCUSE, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.UNKNOWN_PROBLEM)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.person.MyProfile.this, C.Errorz.ITEM_NOT_ADDED_UNKNOWN_PROBLEM_EXCUSE, Toast.LENGTH_LONG);
		}
		changeUserQuoteDialog.dismiss();
	}

	/*
	 * We get the user points count from the database and set their status according to that. Status is something like a
	 * title, seen from everybody who visits their profile
	 */

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
				String imageFilename = PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX,
						C.ImageHandling.IMAGES_FILE_EXTENSION, true);
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
package net.shiftinpower.activities;

import net.shiftinpower.interfaces.OnUserDeletionListener;
import net.shiftinpower.interfaces.OnUserSettingsChangedListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.utilities.PhotoHandler;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import net.shiftinpower.core.*;
import net.shiftinpower.activities.person.MyProfile;
import net.shiftinpower.asynctasks.*;

public class Settings extends KatwalkSlidingMenu implements OnUserSettingsChangedListener, OnUserDeletionListener {

	// Set up XML View Components
	private TextView tvUserNameSettings;
	private TextView tvSettingsTitle;
	private TextView tvUserEmailSettings;
	private TextView tvDeleteAccountDialogTitle;
	private EditText etUserNameSettings;
	private EditText etUserPasswordOldSettings;
	private EditText etUserPasswordNewSettings;
	private EditText etUserPasswordNewAgainSettings;
	private EditText etUserQuoteSettings;
	private RadioGroup rgUserSexSettingsPage;
	private ImageButton iUserAvatarSettings;
	private Spinner sWhoCanInteractWithYourActivitySettings;
	private Spinner sWhoCanSendYouMessagesSettings;
	private CheckBox cbShowStatsSettings;
	private CheckBox cbShowMoneySpentSettings;
	private Button bSubmitSettingsPage;
	private Button bDeleteMyProfileSettingsPage;

	// Delete Account Dialog and its XML Components
	public Dialog deleteAccountDialog;
	private EditText etDeleteAccountPassword;
	private CheckBox cbDeleteAccountDeleteItemImagesAsWell;
	private Button bDeleteAccountSubmit;
	private Button bDeleteAccountCancel;

	// Variables holding data
	private String userPasswordNewInField;
	private String userPasswordNewAgainInField;
	private String userPasswordInOldPasswordField;
	private String userNameInField;
	private String userQuoteInField;
	private boolean userNameIsOK = true;
	private boolean passwordHasBeenChanged = false;
	private boolean passwordForAccountDeletionIsCorrect = false;
	private String passwordForAccountDeletionHashed;
	private String passwordForAccountDeletion;
	protected boolean userHasProvidedOwnPhoto = false;
	private boolean userHasProvidedANewAvatar = false;
	private boolean deleteUserItemImagesAsWell = false;
	private boolean dataChanged = false;
	private boolean passwordIsOK = true;

	// Temporary variables used only on this screen
	private String userNameSettingsScreen;
	private String userSexSettingsScreen;
	private String userPasswordSettingsScreen;
	private String userAvatarPathSettingsScreen;
	private String userQuoteSettingsScreen;
	private boolean userShowsMoneySettingsScreen;
	private boolean userShowsStatsSettingsScreen;
	private String userAcceptsMessagesSettingsScreen;
	private String userInteractsWithActivitiesSettingsScreen;

	// Constructor needed because of the way the super class works
	public Settings() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Assign and inflate an XML file as the view component for this screen
		setContentView(R.layout.activity_layout_settings);

		tvUserNameSettings = (TextView) findViewById(R.id.tvUserNameSettings);
		tvSettingsTitle = (TextView) findViewById(R.id.tvSettingsTitle);
		etUserNameSettings = (EditText) findViewById(R.id.etUserNameSettings);
		tvUserEmailSettings = (TextView) findViewById(R.id.tvUserEmailSettings);
		etUserPasswordOldSettings = (EditText) findViewById(R.id.etUserPasswordOldSettings);
		etUserPasswordNewSettings = (EditText) findViewById(R.id.etUserPasswordNewSettings);
		etUserPasswordNewAgainSettings = (EditText) findViewById(R.id.etUserPasswordNewAgainSettings);
		rgUserSexSettingsPage = (RadioGroup) findViewById(R.id.rgUserSexSettingsPage);
		iUserAvatarSettings = (ImageButton) findViewById(R.id.iUserAvatarSettings);
		bSubmitSettingsPage = (Button) findViewById(R.id.bSubmitSettingsPage);
		bDeleteMyProfileSettingsPage = (Button) findViewById(R.id.bDeleteMyProfileSettingsPage);
		sWhoCanInteractWithYourActivitySettings = (Spinner) findViewById(R.id.sWhoCanInteractWithYourActivitySettings);
		sWhoCanSendYouMessagesSettings = (Spinner) findViewById(R.id.sWhoCanSendYouMessagesSettings);
		cbShowStatsSettings = (CheckBox) findViewById(R.id.cbShowStatsSettings);
		cbShowMoneySpentSettings = (CheckBox) findViewById(R.id.cbShowMoneySpentSettings);
		etUserQuoteSettings = (EditText) findViewById(R.id.etUserQuoteSettings);

		// Set fonts
		try {
			tvUserNameSettings.setTypeface(katwalk.font2);
			tvSettingsTitle.setTypeface(katwalk.font1);
			bDeleteMyProfileSettingsPage.setTypeface(katwalk.font1);
			bSubmitSettingsPage.setTypeface(katwalk.font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// Set user display name
		tvUserNameSettings.setText(userName);

		// Set hints for edit text views
		etUserNameSettings.setText(userName);
		userNameSettingsScreen = userName;

		tvUserEmailSettings.setText(userEmail);

		etUserQuoteSettings.setText(userQuote);
		userQuoteSettingsScreen = userQuote;

		// Set sex radio button checked
		if (userSex.contentEquals("male") || userSex.contentEquals("Male")) {
			rgUserSexSettingsPage.check(R.id.rbSexMaleSettingsPage);
		} else {
			rgUserSexSettingsPage.check(R.id.rbSexFemaleSettingsPage);
		}
		userSexSettingsScreen = userSex;

		// Set show status and money spent checked if they should be
		if (userShowsMoney) {
			cbShowMoneySpentSettings.setChecked(true);
		} else {
			cbShowMoneySpentSettings.setChecked(false);
		}
		userShowsMoneySettingsScreen = userShowsMoney;

		if (userShowsStats) {
			cbShowStatsSettings.setChecked(true);
		} else {
			cbShowStatsSettings.setChecked(false);
		}
		userShowsStatsSettingsScreen = userShowsStats;

		// Populate the spinners with options
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.groups_of_users, R.drawable.simple_spinner_item);
		adapter.setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
		sWhoCanInteractWithYourActivitySettings.setAdapter(adapter);
		if (userInteractsWithActivities.equalsIgnoreCase(C.Miscellaneous.USER_RESTRICTION_LEVEL_NO)) {
			sWhoCanInteractWithYourActivitySettings.setSelection(0);
		} else if (userInteractsWithActivities.equalsIgnoreCase(C.Miscellaneous.USER_RESTRICTION_LEVEL_LIMITED)) {
			sWhoCanInteractWithYourActivitySettings.setSelection(1);
		} else if (userInteractsWithActivities.equalsIgnoreCase(C.Miscellaneous.USER_RESTRICTION_LEVEL_FULL)) {
			sWhoCanInteractWithYourActivitySettings.setSelection(2);
		}
		userInteractsWithActivitiesSettingsScreen = userInteractsWithActivities;

		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.groups_of_users, R.drawable.simple_spinner_item);
		adapter2.setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
		sWhoCanSendYouMessagesSettings.setAdapter(adapter2);
		if (userAcceptsMessages.equalsIgnoreCase(C.Miscellaneous.USER_RESTRICTION_LEVEL_NO)) {
			sWhoCanSendYouMessagesSettings.setSelection(0);
		} else if (userAcceptsMessages.equalsIgnoreCase(C.Miscellaneous.USER_RESTRICTION_LEVEL_LIMITED)) {
			sWhoCanSendYouMessagesSettings.setSelection(1);
		} else if (userAcceptsMessages.equalsIgnoreCase(C.Miscellaneous.USER_RESTRICTION_LEVEL_FULL)) {
			sWhoCanSendYouMessagesSettings.setSelection(2);
		}
		userAcceptsMessagesSettingsScreen = userAcceptsMessages;

		// Set checkbox listeners
		cbShowStatsSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("On Click is Actually Called", "YES");
				dataChanged = true;

				if (cbShowStatsSettings.isChecked()) {
					
					Log.d("On Click is Actually Called", "TRUE");
					userShowsStatsSettingsScreen = true;
				} else {
					Log.d("On Click is Actually Called", "FALSE");
					userShowsStatsSettingsScreen = false;
				}
			}
		});

		cbShowMoneySpentSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dataChanged = true;

				if (cbShowMoneySpentSettings.isChecked()) {
					userShowsMoneySettingsScreen = true;
				} else {
					userShowsMoneySettingsScreen = false;
				}
			}
		});

		// Set spinner listeners
		sWhoCanInteractWithYourActivitySettings.setOnItemSelectedListener(new OnItemSelectedListener() {

			// This count is needed to prevent the onItemSelected method to fire off on itself when the activity is created
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (count > 0) {
					userInteractsWithActivitiesSettingsScreen = (arg0.getItemAtPosition(arg2)).toString();

					dataChanged = true;
				}
				count++;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sWhoCanSendYouMessagesSettings.setOnItemSelectedListener(new OnItemSelectedListener() {

			// This count is needed to prevent the onItemSelected method to fire off on itself when the activity is created
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (count > 0) {
					userAcceptsMessagesSettingsScreen = (arg0.getItemAtPosition(arg2)).toString();
					dataChanged = true;
				}
				count++;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Set avatar image
		iUserAvatarSettings = (ImageButton) findViewById(R.id.iUserAvatarSettings);

		katwalk.setUserImageToImageView(iUserAvatarSettings, userAvatarPath, userSex);
		userAvatarPathSettingsScreen = userAvatarPath;

		iUserAvatarSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent changeAvatarDialog = new Intent(Settings.this, ProvideImageDialog.class);
				changeAvatarDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, userAvatarPathSettingsScreen);
				startActivityForResult(changeAvatarDialog, C.ImageHandling.REQUEST_CODE_CHANGE_IMAGE);

			}
		}); // End of image button on click handling

		// Start of delete user account handling
		bDeleteMyProfileSettingsPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				deleteAccountDialog = new Dialog(Settings.this, R.style.no_title_dialog);
				deleteAccountDialog.setContentView(R.layout.dialog_delete_account);

				tvDeleteAccountDialogTitle = (TextView) deleteAccountDialog.findViewById(R.id.tvDeleteAccountDialogTitle);
				tvDeleteAccountDialogTitle.setText(C.Confirmationz.ARE_YOU_SURE_YOU_WANT_TO_DELETE_YOUR_ACCOUNT);

				etDeleteAccountPassword = (EditText) deleteAccountDialog.findViewById(R.id.etDeleteAccountPassword);
				cbDeleteAccountDeleteItemImagesAsWell = (CheckBox) deleteAccountDialog.findViewById(R.id.cbDeleteAccountDeleteItemImagesAsWell);
				bDeleteAccountSubmit = (Button) deleteAccountDialog.findViewById(R.id.bDeleteAccountSubmit);
				bDeleteAccountCancel = (Button) deleteAccountDialog.findViewById(R.id.bDeleteAccountCancel);

				// Set fonts
				try {

					bDeleteAccountSubmit.setTypeface(katwalk.font1);
					bDeleteAccountCancel.setTypeface(katwalk.font1);

				} catch (Exception e) {
					// Nothing can be done here
					e.printStackTrace();
				}

				bDeleteAccountCancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						deleteAccountDialog.dismiss();

					}
				});

				etDeleteAccountPassword.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {

							bDeleteAccountSubmit.performClick();
							return true;
						}
						return false;
					}
				});
				bDeleteAccountSubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						passwordForAccountDeletion = etDeleteAccountPassword.getText().toString().trim();
						passwordForAccountDeletionHashed = katwalk.hashPassword.computeSHAHash(passwordForAccountDeletion);

						if (!passwordForAccountDeletionHashed.equals(userPassword)) {
							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.INCORRECT_PASSWORD, Toast.LENGTH_SHORT);
						} else {
							passwordForAccountDeletionIsCorrect = true;
						}
						// Execute the deletion from the DB and log user out
						deleteAccountDialog.dismiss();
						if (passwordForAccountDeletionIsCorrect) {
							if (cbDeleteAccountDeleteItemImagesAsWell.isChecked()) {
								deleteUserItemImagesAsWell = true;
							}
							new DeleteUserFromServerAsync(Settings.this, Settings.this, String.valueOf(currentlyLoggedInUser), userAvatarPath,
									deleteUserItemImagesAsWell).execute();
						}

					}

				});

				deleteAccountDialog.show();

			}
		});// End ot delete user account handling

		if (!userAvatarPath.contentEquals("") && !userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE)) {
			userHasProvidedOwnPhoto = true;
		}

		rgUserSexSettingsPage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				dataChanged = true;

				if (checkedId == R.id.rbSexFemaleSettingsPage) {
					userSexSettingsScreen = "Female";
					if (!userHasProvidedOwnPhoto) {
						iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_female);
					}
				} else if (checkedId == R.id.rbSexMaleSettingsPage) {
					userSexSettingsScreen = "Male";
					if (!userHasProvidedOwnPhoto) {
						iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_male);
					}
				}

			}
		}); // end of user sex changed listener

		etUserNameSettings.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {

					userNameInField = etUserNameSettings.getText().toString().trim();

					if (userNameInField.length() < (C.CharacterLimitations.USERNAME_MINIMUM_LENGTH)) {
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.USER_NAME_MIN_LENGTH_PROBLEM, Toast.LENGTH_SHORT);
						userNameIsOK = false;
					} else if (userName.length() > (C.CharacterLimitations.USERNAME_MAXIMUM_LENGTH)) {
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.USER_NAME_MAX_LENGTH_EXCEEDED, Toast.LENGTH_SHORT);
						userNameIsOK = false;
					} else if (userName.equals("")) {
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.FIELD_NOT_FILLED, Toast.LENGTH_SHORT);
						userNameIsOK = false;
					} else {
						userNameIsOK = true;
						dataChanged = true;
						userNameSettingsScreen = userNameInField;
					}
				}
			}
		}); // end of user name changed listener

		// User quote change handling
		etUserQuoteSettings.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				userQuoteInField = etUserQuoteSettings.getText().toString().trim();
				if (!hasFocus) {
					if (userQuoteInField.length() > C.CharacterLimitations.USER_QUOTE_MAXIMUM_LENGTH) {
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.QUOTE_LENGTH_EXCEEDED, Toast.LENGTH_SHORT);
					} else {
						userQuoteSettingsScreen = userQuoteInField;
						dataChanged = true;
					}
				}
			}
		}); // end of user quote changed handling

		bSubmitSettingsPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				validatePassword();

				if (dataChanged == true) {

					// If the user has dismissed the soft keypad with the back button, the validateNewPassword() wouldnt have
					// been called, se we check again
					

					if (userNameIsOK && passwordIsOK) {

						sharedPreferencesEditor = sharedPreferences.edit();
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_NAME, userNameSettingsScreen);
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_SEX, userSexSettingsScreen);
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_PASSWORD, userPasswordSettingsScreen);
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userAvatarPathSettingsScreen);
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_QUOTE, userQuoteSettingsScreen);
						sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_SHOWS_MONEY, userShowsMoneySettingsScreen);
						sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_SHOWS_STATS, userShowsStatsSettingsScreen);
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_ACCEPTS_MESSAGES, userAcceptsMessagesSettingsScreen);
						sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_INTERACTS_WITH_ACTIVITIES, userInteractsWithActivitiesSettingsScreen);
						sharedPreferencesEditor.commit();

						getUserDataFromSharedPreferencesAndAssignItToJavaObjects();

						new SaveUserSettingsOnServerAsync(Settings.this, Settings.this, String.valueOf(currentlyLoggedInUser), userShowsMoneySettingsScreen,
								userShowsStatsSettingsScreen, userAcceptsMessagesSettingsScreen, userInteractsWithActivitiesSettingsScreen,
								userNameSettingsScreen, userQuoteSettingsScreen, userSexSettingsScreen, userPasswordSettingsScreen).execute();

					} else {
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.NOT_ALL_FIELDS_FILLED_SETTINGS, Toast.LENGTH_SHORT);
					}

				} else {
					Intent intent = new Intent(Settings.this, MyProfile.class);
					intent.putExtra("currentUser", true);
					startActivity(intent);
				}

			}

		}); // End of Submit Button handling

	} // End of onCreate

	/*
	 * If the user accidentally presses the hardware back button, they will lose all their data that they've entered We want
	 * to give them a chance to stay on this screen if data bas been entered, so this is what this dialog is for
	 */
	@Override
	public void onBackPressed() {
		if (dataChanged == true) {
			Intent leaveScreenConfirmation = new Intent(Settings.this, LeaveScreenConfirmation.class);
			startActivityForResult(leaveScreenConfirmation, C.Miscellaneous.LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE);
		} else {

			super.onBackPressed();
			finish();
		}

	} // End of onBackPressed

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {

			/*
			 * If the user has clicked the Back button, we have showed them a dialog asking whether they want to leave for
			 * sure. This is the callback if they've confirmed they want to leave
			 */
			if (requestCode == C.Miscellaneous.LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					finish();
				}
			} else {
				userAvatarPathSettingsScreen = data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY);
				if (!(userAvatarPathSettingsScreen.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE))) {
					userHasProvidedOwnPhoto = true;
				} else {
					userHasProvidedOwnPhoto = false;
				}

				// Set sharedPreferences to the appropriate values
				sharedPreferencesEditor = sharedPreferences.edit();
				sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userAvatarPathSettingsScreen);
				sharedPreferencesEditor.commit();

				if (!userAvatarPathSettingsScreen.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE)) {
					// Custom method that checks the API version and restarts the activity appropriately. It is held by the
					// superclass
					// Activity restart is needed because we need to rebuild the Sliding Menu in order for it to obtain the
					// new
					// image in the Sliding Menu Header
					try {
						iUserAvatarSettings.setImageBitmap(BitmapFactory.decodeFile(userAvatarPathSettingsScreen));
						userHasProvidedANewAvatar = true;

						// Remind the user to save changes
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Confirmationz.DONT_FORGET_TO_SAVE_SETTINGS, Toast.LENGTH_SHORT);

					} catch (Exception ex) {
						ex.printStackTrace();
						userHasProvidedOwnPhoto = false;
						userHasProvidedANewAvatar = false;
						userAvatarPathSettingsScreen = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;

						if (userSex.equalsIgnoreCase("male")) {
							iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_male);

						} else {
							iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_female);
						}
					}
				} else {
					if (userSex.equalsIgnoreCase("male")) {
						iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_male);

					} else {
						iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_female);
					}
				}
			}

		} // End of result != Cancelled

	} // End of onActivityResult

	@Override
	public void onUserDeletionSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Confirmationz.ACCOUNT_DELETED, Toast.LENGTH_LONG);
		Intent logUserOut = new Intent(Settings.this, LogUserOut.class);
		startActivity(logUserOut);

		// TODO remove user item images as well

	} // End of onUserDeletionSuccess

	@Override
	public void onUserDeletionFailure(String reason) {
		if (reason.contentEquals(C.Tagz.DB_PROBLEM) || reason.contentEquals(C.Tagz.BAD_REQUEST) || reason.contentEquals(C.Tagz.NOT_FOUND)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.ACCOUNT_NOT_DELETED, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.UNKNOWN_PROBLEM)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.CONNECTION_ERROR, Toast.LENGTH_SHORT);
		}

	} // End of onUserDeletionFailure

	@Override
	public void onUserSettingsChangedSuccess() {
		katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Confirmationz.SETTINGS_SAVED, Toast.LENGTH_SHORT);

		if (userHasProvidedANewAvatar) {
			// Upload the user avatar to the server. After upload is finished,
			// the record in the database will be changed accordingly
			String imageFilename = PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX,
					C.ImageHandling.IMAGES_FILE_EXTENSION, true);
			new UploadUserAvatarToServerAsync(String.valueOf(currentlyLoggedInUser), userAvatarPathSettingsScreen, imageFilename).execute();
		}

		Intent intent = new Intent(Settings.this, MyProfile.class);
		intent.putExtra("currentUser", true);
		startActivity(intent);
	} // End of onUserSettingsChangedSuccess

	@Override
	public void onUserSettingsChangedFailure(String reason) {
		if (reason.contentEquals(C.Tagz.DB_PROBLEM) || reason.contentEquals(C.Tagz.BAD_REQUEST) || reason.contentEquals(C.Tagz.NOT_FOUND)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.SETTINGS_NOT_SAVED, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.UNKNOWN_PROBLEM)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.CONNECTION_ERROR, Toast.LENGTH_SHORT);
		}
	} // End of onUserSettingsChangedFailure

	private void validatePassword() {

		passwordHasBeenChanged = false;
		userPasswordSettingsScreen = userPassword;

		userPasswordInOldPasswordField = katwalk.hashPassword.computeSHAHash(etUserPasswordOldSettings.getText().toString().trim());
		userPasswordNewInField = etUserPasswordNewSettings.getText().toString();
		userPasswordNewAgainInField = etUserPasswordNewAgainSettings.getText().toString();

		if (etUserPasswordOldSettings.getText().toString().trim().length() > 0) {
			if (!userPasswordInOldPasswordField.equals(userPassword)) {
				katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.INCORRECT_PASSWORD, Toast.LENGTH_SHORT);
				passwordIsOK = false;
				dataChanged = true;
			} else {
				passwordIsOK = true;
				dataChanged = true;
			}
		}

		if (userPasswordNewInField.length() > 0) {
			if(passwordIsOK){
				if (userPasswordNewInField.length() < (C.CharacterLimitations.PASSWORD_MINIMUM_LENGTH)) {

					katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.PASSWORD_MIN_LENGTH_PROBLEM, Toast.LENGTH_SHORT);
				} else if (userPasswordNewInField.length() > (C.CharacterLimitations.PASSWORD_MAXIMUM_LENGTH)) {

					katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.PASSWORD_MAX_LENGTH_EXCEEDED, Toast.LENGTH_SHORT);
				} else if (userPasswordNewInField.equals("")) {
					katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.FIELD_NOT_FILLED, Toast.LENGTH_SHORT);

				} else {

					passwordHasBeenChanged = true;
					dataChanged = true;
				}
			}

		}

		if (passwordHasBeenChanged) {

			if (userPasswordNewAgainInField.equals("")) {
				katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.FIELD_NOT_FILLED, Toast.LENGTH_SHORT);
			}

			if (!(userPasswordNewAgainInField.equals(userPasswordNewInField))) {
				katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.PASSWORDS_DO_NOT_MATCH, Toast.LENGTH_SHORT);
				passwordIsOK = false;
			} else {
				passwordIsOK = true;

				dataChanged = true;
				userPasswordSettingsScreen = katwalk.hashPassword.computeSHAHash(userPasswordNewAgainInField);
			}
		}
	} // End of validateNewPassword

} // End of Class
package net.shiftinpower.activities;

import net.shiftinpower.interfaces.OnUserDeletionListener;
import net.shiftinpower.interfaces.OnUserSettingsChangedListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.utilities.PhotoHandler;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	private boolean oldPasswordOk = false;
	private boolean passwordHasBeenChanged = false;
	private boolean passwordForAccountDeletionIsCorrect = false;
	private String passwordForAccountDeletionHashed;
	private String passwordForAccountDeletion;
	protected boolean userHasProvidedOwnPhoto = false;
	private boolean userHasProvidedANewAvatar = false;
	private boolean deleteUserItemImagesAsWell = false;

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
		tvUserEmailSettings.setText(userEmail);
		etUserQuoteSettings.setText(userQuote);

		// Set sex radio button checked
		if (userSex.contentEquals("male") || userSex.contentEquals("Male")) {
			rgUserSexSettingsPage.check(R.id.rbSexMaleSettingsPage);
		} else {
			rgUserSexSettingsPage.check(R.id.rbSexFemaleSettingsPage);
		}

		// Set show status and money spent checked if they should be
		if (userShowsMoney) {
			cbShowMoneySpentSettings.setChecked(true);
		} else {
			cbShowMoneySpentSettings.setChecked(false);
		}
		if (userShowsStats) {
			cbShowStatsSettings.setChecked(true);
		} else {
			cbShowStatsSettings.setChecked(false);
		}

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

		// Set spinner listeners
		sWhoCanInteractWithYourActivitySettings.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setUserInteractsWithActivities((arg0.getItemAtPosition(arg2)).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		sWhoCanSendYouMessagesSettings.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setUserAcceptsMessages((arg0.getItemAtPosition(arg2)).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Set avatar image
		iUserAvatarSettings = (ImageButton) findViewById(R.id.iUserAvatarSettings);

		if (userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE) || userAvatarPath.contentEquals("") || userAvatarPath == null) {
			setUserAvatarPath(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);
			if (userSex.equalsIgnoreCase("male")) {
				iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_male);

			} else {
				iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_female);
			}
		} else {

			try {
				iUserAvatarSettings.setImageBitmap(BitmapFactory.decodeFile(userAvatarPath));
				userHasProvidedOwnPhoto = true;

			} catch (Exception ex) {
				ex.printStackTrace();
				userHasProvidedOwnPhoto = false;
				setUserAvatarPath(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);

				if (userSex.equalsIgnoreCase("male")) {
					iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_male);

				} else {
					iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_female);
				}
			}

		}

		iUserAvatarSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent changeAvatarDialog = new Intent(Settings.this, ProvideImageDialog.class);
				changeAvatarDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, userAvatarPath);
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

						passwordForAccountDeletion = etDeleteAccountPassword.getText().toString();
						passwordForAccountDeletionHashed = katwalk.hashPassword.computeSHAHash(passwordForAccountDeletion);

						if (!passwordForAccountDeletionHashed.equals(userPassword)) {
							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.INCORRECT_PASSWORD, Toast.LENGTH_SHORT);
						} else {
							passwordForAccountDeletionIsCorrect = true;
						}
						// Execute the deletion from the DB and log user out
						deleteAccountDialog.dismiss();
						if (passwordForAccountDeletionIsCorrect) {
							if(cbDeleteAccountDeleteItemImagesAsWell.isChecked()){
								deleteUserItemImagesAsWell = true;
							}
							new DeleteUserFromServerAsync(Settings.this, Settings.this, String.valueOf(currentlyLoggedInUser), userAvatarPath, deleteUserItemImagesAsWell).execute();
						}

					}
					
				});
				
				deleteAccountDialog.show();
				
			}
		});// End ot delete user account handling

		rgUserSexSettingsPage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbSexFemaleSettingsPage) {
					setUserSex("Female");
					if (!userHasProvidedOwnPhoto) {
						iUserAvatarSettings.setImageResource(R.drawable.images_default_avatar_female);
					}
				} else if (checkedId == R.id.rbSexMaleSettingsPage) {
					setUserSex("Male");
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

					userNameInField = etUserNameSettings.getText().toString();

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
						setUserName(userNameInField);
					}
				}
			}
		}); // end of user name changed listener

		etUserPasswordOldSettings.setOnFocusChangeListener(new OnFocusChangeListener() {
			// check whether userpassword old hashed equals the password from
			// the db before changing to the new one.
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					userPasswordInOldPasswordField = katwalk.hashPassword.computeSHAHash(etUserPasswordOldSettings.getText().toString());

					if (etUserPasswordOldSettings.getText().toString().length() > 0) {
						if (!userPasswordInOldPasswordField.equals(userPassword)) {
							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.INCORRECT_PASSWORD, Toast.LENGTH_SHORT);
						} else {
							oldPasswordOk = true;
						}
					}

				}
			}
		}); // end of old password changed listener

		etUserPasswordNewSettings.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					userPasswordNewInField = etUserPasswordNewSettings.getText().toString();
					passwordHasBeenChanged = false;
					if (userPasswordNewInField.length() > 0) {
						if (userPasswordNewInField.length() < (C.CharacterLimitations.PASSWORD_MINIMUM_LENGTH)) {

							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.PASSWORD_MIN_LENGTH_PROBLEM, Toast.LENGTH_SHORT);
						} else if (userPasswordNewInField.length() > (C.CharacterLimitations.PASSWORD_MAXIMUM_LENGTH)) {

							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.PASSWORD_MAX_LENGTH_EXCEEDED, Toast.LENGTH_SHORT);
						} else if (userPasswordNewInField.equals("")) {
							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.FIELD_NOT_FILLED, Toast.LENGTH_SHORT);

						} else {
							passwordHasBeenChanged = true;
						}
					}
				}
			}
		}); // end of new password changed listener

		etUserPasswordNewAgainSettings.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (passwordHasBeenChanged) {
						userPasswordNewAgainInField = etUserPasswordNewAgainSettings.getText().toString();
						if (userPasswordNewAgainInField.equals("")) {
							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.FIELD_NOT_FILLED, Toast.LENGTH_SHORT);
						}

						if (!(userPasswordNewAgainInField.equals(userPasswordNewInField))) {
							katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.PASSWORDS_DO_NOT_MATCH, Toast.LENGTH_SHORT);
						} else {
							if (oldPasswordOk) {
								setUserPassword(katwalk.hashPassword.computeSHAHash(userPasswordNewAgainInField));
							} else {
								katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.INCORRECT_PASSWORD, Toast.LENGTH_SHORT);
							}
						}
					}
				}
			}
		}); // end of new password again changed listener

		// User quote change handling
		etUserQuoteSettings.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				userQuoteInField = etUserQuoteSettings.getText().toString();
				if (!hasFocus) {
					if (userQuoteInField.length() > C.CharacterLimitations.USER_QUOTE_MAXIMUM_LENGTH) {
						katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.QUOTE_LENGTH_EXCEEDED, Toast.LENGTH_SHORT);
					} else {
						setUserQuote(userQuoteInField);
					}
					setUserQuote(userQuoteInField);
				}
			}
		}); // end of user quote changed handling
		
		bSubmitSettingsPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// get the user quote
				userQuote = etUserQuoteSettings.getText().toString();

				// check the values of the checkboxes
				if (cbShowMoneySpentSettings.isChecked()) {
					setUserShowsMoney(true); 
				} else {
					setUserShowsMoney(false);
				}

				if (cbShowStatsSettings.isChecked()) {
					setUserShowsStats(true);
				} else {
					setUserShowsStats(false);
				}

				if (userNameIsOK) {

					sharedPreferencesEditor = sharedPreferences.edit();
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_NAME, userName);
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_SEX, userSex);
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_PASSWORD, userPassword);
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userAvatarPath);
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_QUOTE, userQuote);
					sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_SHOWS_MONEY, userShowsMoney);
					sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_SHOWS_STATS, userShowsStats);
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_ACCEPTS_MESSAGES, userAcceptsMessages);
					sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_INTERACTS_WITH_ACTIVITIES, userInteractsWithActivities);
					sharedPreferencesEditor.commit();

					new SaveUserSettingsOnServerAsync(Settings.this, Settings.this, String.valueOf(currentlyLoggedInUser), userShowsMoney, userShowsStats, userAcceptsMessages, userInteractsWithActivities, userName, userQuote, userSex, userPassword).execute();

				} else {
					katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Errorz.NOT_ALL_FIELDS_FILLED, Toast.LENGTH_SHORT);
				}

			}

		}); // End of Submit Button handling

	} // End of onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {
			userAvatarPath = data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY);
			if(!(userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE))){
				userHasProvidedOwnPhoto = true;
			} else {
				userHasProvidedOwnPhoto = false;
			}

			// Set sharedPreferences to the appropriate values
			sharedPreferencesEditor = sharedPreferences.edit();
			sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userAvatarPath);
			sharedPreferencesEditor.commit();

			if (!userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE)) {
				// Custom method that checks the API version and restarts the activity appropriately. It is held by the superclass
				// Activity restart is needed because we need to rebuild the Sliding Menu in order for it to obtain the new image in the Sliding Menu Header
				try {
					iUserAvatarSettings.setImageBitmap(BitmapFactory.decodeFile(userAvatarPath));
					userHasProvidedANewAvatar = true;

					// Remind the user to save changes
					katwalk.toastMaker.toast(net.shiftinpower.activities.Settings.this, C.Confirmationz.DONT_FORGET_TO_SAVE_SETTINGS, Toast.LENGTH_SHORT);

				} catch (Exception ex) {
					ex.printStackTrace();
					userHasProvidedOwnPhoto = false;
					userHasProvidedANewAvatar = false;
					setUserAvatarPath(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);

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
			String imageFilename = PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true);
			new UploadUserAvatarToServerAsync(String.valueOf(currentlyLoggedInUser), userAvatarPath, imageFilename).execute();
		}

		Intent intent = new Intent(Settings.this, MyProfile.class);
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

} // End of Class
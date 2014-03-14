package net.shiftinpower.activities;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

import net.shiftinpower.asynctasks.*;
import net.shiftinpower.interfaces.*;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.utilities.*;
import net.shiftinpower.core.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Signup extends SherlockActivity implements OnCheckWhetherAUserWithSuchEmailExistsListener, OnUserCreatedListener {

	// Set up XML View Components
	private TextView tvBraggrLogoSignupPage;
	private TextView tvSignupTitle;
	private TextView tvOrLoginPage;
	private EditText etUserEmailSignupPage;
	private EditText etUsernameSignupPage;
	private EditText etUserPasswordSignupPage;
	private EditText etUserPasswordAgainSignupPage;
	private RadioGroup rgUserSexSignupPage;
	private ImageButton iUserAvatar;
	private CheckBox cbUserAgreesWithTermsOfUse;
	private Button bSubmitSignupPage;

	// Fonts
	private Typeface font1;
	private Typeface font2;

	// Variables holding Data
	private String userSex = "male";
	private String userName;
	private String userEmail;
	private String emailThatWasInitiallyChecked = "";
	private String userPassword;
	private String userPasswordHashed;
	private String userPasswordAgain;
	private String userAvatarPath = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;
	protected boolean passwordsMatch = false;
	protected boolean isValidEmail = false;
	protected boolean emailIsOk = false;
	protected boolean sexIsOK = false;
	protected boolean usernameIsOk = false;
	protected boolean passwordIsOk = false;
	protected boolean passwordAgainIsOk = false;
	protected boolean userHasProvidedOwnPhoto = false;
	protected boolean oneToastAlreadyShownSoDoNotStackAnyMoreToasts = false;
	protected boolean avatarAlreadyProvided = false;
	private String toastText = "";
	private String imageFilename;

	// Shared preferences
	private Editor editor;
	private SharedPreferences sharedPreferences;
	private static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;
	private boolean isUserLoggedIn;

	// Custom class to display toasts
	private ToastMaker toastMaker = new ToastMaker();

	// Custom class holding several image handling methods
	private PhotoHandler photoHandler = new PhotoHandler(this);

	// Verifies the validity of the Email format entered by the user
	private EmailVerifier emailVerifier = new EmailVerifier();

	// This class hashes passwords
	private HashPassword hashPassword = new HashPassword();

	// Setters
	public void setUserAvatarPath(String userAvatarPath) {
		this.userAvatarPath = userAvatarPath;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// If the user has connected the storage to the PC, they will be unable to use the app.
		// In this case it makes no sense for us to start it, so we are making them disconnect the storage first.
		if (!StorageStatusChecker.isExternalStorageAvailable()) {
			toastMaker.toast(net.shiftinpower.activities.Signup.this, C.Errorz.DISCONNECT_STORAGE_FIRST, Toast.LENGTH_SHORT);
			finish();
		}

		// This is the second part of a double check for whether the user is logged in.
		// If they are logged in and accidentally came here using the back button, they are sent back to home screen.
		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		isUserLoggedIn = sharedPreferences.getBoolean("userLoggedInState", false);
		if (isUserLoggedIn) {
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

		// Assign and inflate an XML file as the view component for this screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_signup);

		// This app operates in Full Screen, so this is what we are setting here
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Inflate the XML View components and Link them to the corresponding Java Objects in this class
		tvBraggrLogoSignupPage = (TextView) findViewById(R.id.tvBraggrLogoSignupPage);
		tvSignupTitle = (TextView) findViewById(R.id.tvSignupTitle);
		tvOrLoginPage = (TextView) findViewById(R.id.tvOrLoginPage);
		etUserEmailSignupPage = (EditText) findViewById(R.id.etUserEmailSignupPage);
		etUsernameSignupPage = (EditText) findViewById(R.id.etUsernameSignupPage);
		etUserPasswordSignupPage = (EditText) findViewById(R.id.etUserPasswordSignupPage);
		etUserPasswordAgainSignupPage = (EditText) findViewById(R.id.etUserPasswordAgainSignupPage);
		rgUserSexSignupPage = (RadioGroup) findViewById(R.id.rgUserSexSignupPage);
		cbUserAgreesWithTermsOfUse = (CheckBox) findViewById(R.id.cbUserAgreesWithTermsOfUse);
		bSubmitSignupPage = (Button) findViewById(R.id.bSubmitSignupPage);
		iUserAvatar = (ImageButton) findViewById(R.id.iUserAvatar);

		etUsernameSignupPage.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

		etUserEmailSignupPage.setHint(C.Hints.SIGNUP_USER_EMAIL_HINT);
		etUsernameSignupPage.setHint(C.Hints.SIGNUP_USER_NAME_HINT);
		etUserPasswordSignupPage.setHint(C.Hints.SIGNUP_USER_PASSWORD_HINT);
		etUserPasswordAgainSignupPage.setHint(C.Hints.SIGNUP_USER_PASSWORD_AGAIN_HINT);

		// Setting the fonts
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
			font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_2);
			tvBraggrLogoSignupPage.setTypeface(font1);
			tvSignupTitle.setTypeface(font2);
			tvOrLoginPage.setTypeface(font2);
			bSubmitSignupPage.setTypeface(font1);
		} catch (Exception e) {
			// I cant do anything here
			e.printStackTrace();
		}

		// Set the avatar image
		if (avatarAlreadyProvided) {
			iUserAvatar.setImageBitmap(photoHandler.getBitmapAndResizeIt(userAvatarPath));
		}

		iUserAvatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent changeAvatarDialog = new Intent(Signup.this, ProvideImageDialog.class);
				changeAvatarDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, userAvatarPath);
				startActivityForResult(changeAvatarDialog, C.ImageHandling.REQUEST_CODE_CHANGE_IMAGE);
			}

		}); // End of Avatar Change Handling

		rgUserSexSignupPage.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbSexFemaleSignupPage) {
					userSex = "Female";
					sexIsOK = true;
					if (!userHasProvidedOwnPhoto) {
						iUserAvatar.setImageResource(R.drawable.images_default_avatar_female);
					}
				} else if (checkedId == R.id.rbSexMaleSignupPage) {
					userSex = "Male";
					sexIsOK = true;
					if (!userHasProvidedOwnPhoto) {
						iUserAvatar.setImageResource(R.drawable.images_default_avatar_male);
					}
				}

			}
		}); // End of User Sex Select Handling

		bSubmitSignupPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Obtain the contents of the fields filled by the user
				userName = etUsernameSignupPage.getText().toString().trim();
				userEmail = etUserEmailSignupPage.getText().toString().trim();
				userPassword = etUserPasswordSignupPage.getText().toString().trim();
				userPasswordAgain = etUserPasswordAgainSignupPage.getText().toString().trim();

				// The User MUST Agree to the Terms of Use in Order to Continue
				if (cbUserAgreesWithTermsOfUse.isChecked()) {

					if (userEmail.equals("")) {
						toastMaker.toast(net.shiftinpower.activities.Signup.this, "Email: " + C.Errorz.FIELD_NOT_FILLED + toastText, Toast.LENGTH_LONG);

					} else if (!emailVerifier.isEmailValid(userEmail)) {
						toastMaker.toast(net.shiftinpower.activities.Signup.this, "Email: " + C.Errorz.EMAIL_NOT_VALID + toastText, Toast.LENGTH_LONG);

					} else {

						/*
						 * If the user has provided an email address and we've checked whether it was taken or not, And now
						 * they are re-submitting the form with the same email address (which was not taken) we should not
						 * check again. There is an Interface that catches the result from this method, and we are listening
						 * to it. Further down in this class there is a "onCheckedEmail..." method that starts the User
						 * Registration process if email is free to use.
						 */
						if (!emailThatWasInitiallyChecked.contentEquals(userEmail)) {
							new CheckWhetherAUserWithSuchEmailExistsAsync(Signup.this, userEmail, Signup.this, false).execute();

						} else {

							new CheckWhetherAUserWithSuchEmailExistsAsync(Signup.this, userEmail, Signup.this, true).execute();
						}

					}
				} else {
					toastMaker.toast(net.shiftinpower.activities.Signup.this, C.Errorz.NEED_TO_AGREE_TO_TERMS_OF_USE + toastText, Toast.LENGTH_SHORT);

				}

				toastText = "";
			}
		});

		SpannableString userAgreesWithTheTermsOfUseSpanableString = new SpannableString(this.getString(R.string.cbUserAgreesWithTermsOfUse));
		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				// display the terms of use in a dialog
			}
		};
		userAgreesWithTheTermsOfUseSpanableString.setSpan(clickableSpan, 17, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 17 and 29 are hardcoded values of the start and the end of the phrase "Terms of Use" within the R.string that says
		// "I agree to the Terms Of Use"

		cbUserAgreesWithTermsOfUse.setText(userAgreesWithTheTermsOfUseSpanableString);
		cbUserAgreesWithTermsOfUse.setMovementMethod(LinkMovementMethod.getInstance());

	} // End of onCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {
			setUserAvatarPath(data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY));
			if (!(userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE))) {
				userHasProvidedOwnPhoto = true;

			} else {
				userHasProvidedOwnPhoto = false;
			}

			/*
			 * Custom method that checks the API version and restarts the activity appropriately. It is held by the
			 * superclass Activity restart is needed because we need to rebuild the Sliding Menu in order for it to obtain
			 * the new image in the Sliding Menu Header
			 */
			if (!userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE)) {
				try {
					iUserAvatar.setImageBitmap(BitmapFactory.decodeFile(userAvatarPath));
				} catch (Exception ex) {
					ex.printStackTrace();
					userHasProvidedOwnPhoto = false;
					setUserAvatarPath(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);

					if (userSex.equalsIgnoreCase("male")) {
						iUserAvatar.setImageResource(R.drawable.images_default_avatar_male);

					} else {
						iUserAvatar.setImageResource(R.drawable.images_default_avatar_female);
					}
				}
			} else {
				if (userSex.equalsIgnoreCase("male")) {
					iUserAvatar.setImageResource(R.drawable.images_default_avatar_male);

				} else {
					iUserAvatar.setImageResource(R.drawable.images_default_avatar_female);
				}
			}
		}
	} // End of onActicvityResult

	@Override
	// SUCCESS in this case means that there is NO user with such email and registration can continue
	public void onCheckWhetherAUserWithSuchEmailExistsChecked(Boolean result) {

		emailThatWasInitiallyChecked = userEmail;
		if (result == true) {
			emailIsOk = true;

			// Validate userName
			if (userName.length() < (C.CharacterLimitations.USERNAME_MINIMUM_LENGTH)) {
				toastText += C.Errorz.USER_NAME_MIN_LENGTH_PROBLEM + "\n\n";

			} else if (userName.length() > (C.CharacterLimitations.USERNAME_MAXIMUM_LENGTH)) {
				toastText += C.Errorz.USER_NAME_MAX_LENGTH_EXCEEDED + "\n\n";

			} else if (userName.equals("")) {
				toastText += "User Name - " + C.Errorz.FIELD_NOT_FILLED + "\n\n";

			} else {
				usernameIsOk = true;
			}

			// Validate userPassword
			if (userPassword.length() < (C.CharacterLimitations.PASSWORD_MINIMUM_LENGTH)) {
				toastText += C.Errorz.PASSWORD_MIN_LENGTH_PROBLEM + "\n\n";

			} else if (userPassword.length() > (C.CharacterLimitations.PASSWORD_MAXIMUM_LENGTH)) {
				toastText += C.Errorz.PASSWORD_MAX_LENGTH_EXCEEDED + "\n\n";

			} else if (userPassword.equals("")) {
				toastText += "User Password - " + C.Errorz.FIELD_NOT_FILLED + "\n\n";

			} else {
				passwordIsOk = true;
			}

			// Validate userPasswordAgain
			userPasswordAgain = etUserPasswordAgainSignupPage.getText().toString().trim();

			if (userPasswordAgain.equals("")) {
				toastText += "User Password - " + C.Errorz.FIELD_NOT_FILLED + "\n\n";

			} else {
				passwordAgainIsOk = true;
			}

			// Check whether passwords match
			if (!(userPassword.contentEquals(userPasswordAgain))) {
				toastText += C.Errorz.PASSWORDS_DO_NOT_MATCH + "\n\n";
				passwordsMatch = false;
			} else {
				passwordsMatch = true;
			}

			// Hash the password before inserting it into the DB
			userPasswordHashed = hashPassword.computeSHAHash(userPassword);

			// Make sure the user selects their gender.
			// We do not want to assume they are male.
			if (sexIsOK == false) {
				toastText += C.Errorz.SEX_NOT_SELECTED + "\n\n";

			}

			// If all the booleans return true, assign an avatar value and then
			// Start the registration process
			// Otherwise, show all the errors at once
			if (emailIsOk && sexIsOK && usernameIsOk && passwordIsOk && passwordAgainIsOk && passwordsMatch) {

				/*
				 * if (userAvatar != null || !userAvatar.contentEquals("")) { try { userAvatar =
				 * photoHandler.getUserAvatar().toString(); } catch (NullPointerException e) { userAvatar = "default_avatar";
				 * } }
				 */
				// TODO send the image to the server in the onpostexecute of the following
				new RegisterUserOnServerAsync(this, this, userName, userPasswordHashed, userEmail, userSex, userAvatarPath).execute();

			} else {
				toastMaker.toast(net.shiftinpower.activities.Signup.this, C.Errorz.NOT_ALL_FIELDS_FILLED + toastText, Toast.LENGTH_LONG);

			}

			// Clear the contents of toastText
			toastText = "";

		} else {
			toastText += C.Errorz.EMAIL_TAKEN + "\n\n";
			toastMaker.toast(net.shiftinpower.activities.Signup.this, C.Errorz.NOT_ALL_FIELDS_FILLED + toastText, Toast.LENGTH_LONG);
			toastText = "";

		}

	} // End of onCheckWhetherAUserWithSuchEmailExistsChecked

	@Override
	public void onUserCreated(Integer userId) {

		// Set shared Preferences user logged in state and Id for the App to use later.
		editor = sharedPreferences.edit();
		editor.putBoolean("userLoggedInState", true);
		editor.putInt("currentLoggedInUserId", userId);
		editor.commit();

		// Send the image to the server if the user has uploaded one
		if (userHasProvidedOwnPhoto) {
			imageFilename = PhotoHandler.generateImageFilename(String.valueOf(userId) + C.ImageHandling.IMAGE_FILENAME_PREFIX,
					C.ImageHandling.IMAGES_FILE_EXTENSION, true);
			new UploadUserAvatarToServerAsync(String.valueOf(userId), userAvatarPath, imageFilename).execute();
		}

		Intent signupSuccessHome = new Intent(getApplicationContext(), InitialDataLoader.class);
		signupSuccessHome.putExtra("userHasJustRegistered", true);
		signupSuccessHome.putExtra("temporaryUserAvatarPath", userAvatarPath);
		startActivity(signupSuccessHome);

		// We will try to notify the user for the registration success by email
		try {
			new EmailSender(userEmail, C.Emailz.BRAGGR_OFFICIAL_ADDRESS, C.Emailz.SUCCESSFUL_SIGNUP_SUBJECT, C.Emailz.SUCCESSFUL_SIGNUP_BODY(userName), null)
					.execute();
		} catch (Exception e) {
			// There is nothing I can do here.
			e.printStackTrace();
		}

		finish();

	} // End of onUserCreated

	@Override
	public void onUserNotCreated() {

		toastMaker.toast(net.shiftinpower.activities.Signup.this, C.Errorz.USER_ACCOUNT_NOT_CREATED, Toast.LENGTH_SHORT);

	}

	@Override
	public void onBackPressed() {

		// If the user has somehow managed to get back here from the Logged in Home Screen, we will make sure they do not
		// come back there again.
		editor = sharedPreferences.edit();
		editor.putBoolean("userLoggedInState", false);
		editor.putInt("currentLoggedInUserId", 0);
		editor.commit();

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();

	} // End of onBackPressed

} // End of Class
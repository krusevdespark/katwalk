<<<<<<< HEAD
package net.shiftinpower.activities;

import java.util.Arrays;
import java.util.List;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import net.shiftinpower.core.*;
import net.shiftinpower.interfaces.OnFacebookUserRegisteredListener;
import net.shiftinpower.interfaces.OnForgottenPasswordEmailSentListener;
import net.shiftinpower.interfaces.OnUserLoginAttemptListener;
import net.shiftinpower.asynctasks.*;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.utilities.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class Login extends SherlockActivity implements OnClickListener, OnUserLoginAttemptListener, OnForgottenPasswordEmailSentListener, OnFacebookUserRegisteredListener {

	// Set up XML View Components
	private TextView tvBraggrLogoLoginPage;
	private TextView tvLoginTitle;
	private TextView tvForgottenPasswordLoginPage;
	private LoginButton authButton;
	private Button bLoginLoginPage;
	private TextView tvOrLoginPage;
	private EditText etUserEmailLoginPage;
	private EditText etUserPasswordLoginPage;

	// Variables holding Data
	private String userEmailLoginPage;
	private String userPasswordLoginPage;
	private String userWithForgottenPasswordsEmailAddress = "";
	private String userPasswordLoginPageHashed;
	public boolean dialogShown = false;
	boolean emailIsInDatabase = false;
	private boolean isUserLoggedIn;

	// Forgotten Password Dialog and its XML Components
	public Dialog forgottenPasswordDialog;
	private EditText etForgottenPasswordEmail;
	private Button bForgottenPasswordDialogSubmit;
	private Button bForgottenPasswordDialogCancel;

	// Shared Preferences
	private Editor sharedPreferencesEditor;
	private SharedPreferences sharedPreferences;
	private static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;

	// Fonts
	private Typeface font1;
	private Typeface font2;

	// Custom class to display toasts
	private ToastMaker toastMaker = new ToastMaker();

	// Verifies the validity of the Email format entered by the user
	private EmailVerifier emailVerifier = new EmailVerifier();

	// This class hashes passwords
	private HashPassword hashPassword = new HashPassword();

	// facebook variables
	private String userFBId;
	private String userName;
	private String userEmail;
	private String userSex;
	private String userFacebookAvatar;
	// private UiLifecycleHelper uiHelper;
	private static final int REAUTH_ACTIVITY_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// If the user has connected the storage to the PC, they will be unable to use the app.
		// In this case it makes no sense for us to start it, so we are making them disconnect the storage first.
		if (!StorageStatusChecker.isExternalStorageAvailable()) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.DISCONNECT_STORAGE_FIRST, Toast.LENGTH_SHORT);
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

		super.onCreate(savedInstanceState);

		// Assign and inflate an XML file as the view component for this screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_layout_login);

		// This app operates in Full Screen, so this is what we are setting here
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		

		// Inflate the XML View components and Link them to the corresponding Java Objects in this class
		tvBraggrLogoLoginPage = (TextView) findViewById(R.id.tvBraggrLogoLoginPage);
		tvLoginTitle = (TextView) findViewById(R.id.tvLoginTitle);
		tvForgottenPasswordLoginPage = (TextView) findViewById(R.id.tvForgottenPasswordLoginPage);
		authButton = (LoginButton) findViewById(R.id.authButton);
		tvOrLoginPage = (TextView) findViewById(R.id.tvOrLoginPage);
		etUserEmailLoginPage = (EditText) findViewById(R.id.etUserEmailLoginPage);
		etUserPasswordLoginPage = (EditText) findViewById(R.id.etUserPasswordLoginPage);
		bLoginLoginPage = (Button) findViewById(R.id.bLoginLoginPage);

		// Set the fonts for buttons and textviews
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
			font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_2);
			tvBraggrLogoLoginPage.setTypeface(font1);
			tvForgottenPasswordLoginPage.setTypeface(font2);
			tvLoginTitle.setTypeface(font2);
			tvOrLoginPage.setTypeface(font2);
			bLoginLoginPage.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		forgottenPasswordDialog = new Dialog(Login.this, R.style.no_title_dialog);
		forgottenPasswordDialog.setContentView(R.layout.dialog_login_forgotten_password);
		etForgottenPasswordEmail = (EditText) forgottenPasswordDialog.findViewById(R.id.etForgottenPasswordEmail);

		bForgottenPasswordDialogSubmit = (Button) forgottenPasswordDialog.findViewById(R.id.bForgottenPasswordDialogSubmit);
		bForgottenPasswordDialogCancel = (Button) forgottenPasswordDialog.findViewById(R.id.bCancelForgottenPasswordDialog);

		// Set fonts
		try {
			bForgottenPasswordDialogSubmit.setTypeface(font1);
			bForgottenPasswordDialogCancel.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// The Facebook Login / Signup Option DOES NOT WORK at the moment. There was no time to properly implement it.
		authButton.setReadPermissions(Arrays.asList("basic_info", "email", "user_likes", "user_status"));
		authButton.setOnClickListener(this);
		bLoginLoginPage.setOnClickListener(this);
		tvForgottenPasswordLoginPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bForgottenPasswordDialogSubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						forgottenPasswordSubmitOnClick();
					}
				});
				bForgottenPasswordDialogCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						forgottenPasswordDialog.dismiss();
						dialogShown = false;
					}
				});
				forgottenPasswordDialog.show();
				dialogShown = true;
			}

		});

		etUserEmailLoginPage.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				// Get the user email entered in the input box
				userEmailLoginPage = etUserEmailLoginPage.getText().toString();

				// Check whether email entered is a valid email.
				// Toast about it not being valid if it isnt
				if (userEmailLoginPage.length() > 0) {
					if (!emailVerifier.isEmailValid(userEmailLoginPage)) {
						toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.ENTER_VALID_EMAIL, Toast.LENGTH_SHORT);
					}
				}

			}
		});

		etUserPasswordLoginPage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					// When user hits "next" on their on-screen keyboard, this will try to automatically log them in.
					bLoginLoginPage.performClick();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.authButton) {
			/*
			 * if (hasFacebookPermissions(Arrays.asList("basic_info", "email", "user_likes", "user_status"))) { // The user
			 * is already logged in } else { OpenRequest openrequest = new
			 * OpenRequest(this).setPermissions(Arrays.asList("basic_info", "email", "user_likes", "user_status")); Session
			 * session = new Session(Login.this); session.openForRead(openrequest);
			 * 
			 * Session.openActiveSession(this, true, callback);
			 * 
			 * }
			 */

		} else if (id == R.id.bLoginLoginPage) {

			// Get the user email entered in the input box
			userEmailLoginPage = etUserEmailLoginPage.getText().toString();
			
			// Get the user password from the input
			userPasswordLoginPage = etUserPasswordLoginPage.getText().toString();

			if (userPasswordLoginPage == null || userEmailLoginPage == null) {
				
				toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.FILL_IN_LOGIN_CREDENTIALS, Toast.LENGTH_SHORT);
				
			} else {

				// Hash the password so we can transfer it to the db and check it.
				userPasswordLoginPageHashed = hashPassword.computeSHAHash(userPasswordLoginPage);

				// We try to log the user in. check wether email is in DB and password is correct
				// If there is something wrong, AttemptLogin will Toast about it.
				new LogUserInAsync(this, this, userEmailLoginPage, userPasswordLoginPageHashed).execute();
				// What comes next is onUserLoginAttemptSuccess or onUserLoginAttemptFailure
			}
		}
	}

	/*
	 * // Facebook callback private Session.StatusCallback callback = new Session.StatusCallback() {
	 * 
	 * @Override public void call(Session session, SessionState state, Exception exception) { onSessionStateChange(session,
	 * state, exception); } };
	 * 
	 * // Facebook session management private void onSessionStateChange(Session session, SessionState state, Exception
	 * exception) {
	 * 
	 * if (state.isOpened()) { makeMeRequest(session); } else if (state.isClosed()) {
	 * 
	 * Session sessionw = new Session(this); Session.setActiveSession(sessionw); sessionw.openForRead(new
	 * Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList("basic_info", "email", "user_likes",
	 * "user_status"))); } }
	 * 
	 * // Make an API call to get user data from Facebook and define a new callback to handle the response. private void
	 * makeMeRequest(final Session session) {
	 * 
	 * Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) { // If the response is successful if (session ==
	 * Session.getActiveSession()) { if (user != null) { userFBId = user.getId(); userName = user.getName(); userEmail =
	 * (String) user.asMap().get("email"); userSex = (String) user.getProperty("gender"); userFacebookAvatar =
	 * "http://graph.facebook.com/" + user.getId() + "/picture?type=large&redirect=true&width=400&height=400";
	 * 
	 * new RegisterFacebookUserOnServerAsync(Login.this, Login.this, userName, userEmail, userSex,
	 * userFacebookAvatar).execute(); } } if (response.getError() != null) {
	 * System.out.println(response.getError().toString()); } } }); request.executeAsync(); }
	 */
	public void forgottenPasswordSubmitOnClick() {
		userWithForgottenPasswordsEmailAddress = etForgottenPasswordEmail.getText().toString();
		if (!emailVerifier.isEmailValid(userWithForgottenPasswordsEmailAddress)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.ENTER_VALID_EMAIL, Toast.LENGTH_SHORT);
		} else {
			new ForgottenPasswordGetUserNameAndSendEmail(this, this, userWithForgottenPasswordsEmailAddress).execute();
		}

		// this will check if email exists in database
		// if email exists, we will send an email with a way for them to reset their password
		// if email doesnt exist, we will toast about it.

	}

	@Override
	public void onBackPressed() {
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean("userLoggedInState", false);
		sharedPreferencesEditor.putInt("currentLoggedInUserId", 0);
		sharedPreferencesEditor.commit();

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onUserLoginAttemptSuccess(int userId) {
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_LOGGED_IN_STATE, true);
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_ID, userId);
		sharedPreferencesEditor.commit();

		Intent signupSuccessHome = new Intent(this, InitialDataLoader.class);
		startActivity(signupSuccessHome);
		finish();

	}

	@Override
	public void onUserLoginAttemptFailure(String reason) {

		if (reason.contentEquals(C.Tagz.INVALID_CREDENTIALS)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.INCORRECT_EMAIL_PASSWORD_COMBINATION, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM) || reason.contentEquals(C.Tagz.BAD_REQUEST) || reason.contentEquals(C.Tagz.NOT_FOUND)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.CONNECTION_ERROR, Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onForgottenPasswordEmailSentSuccess() {
		forgottenPasswordDialog.dismiss();
		dialogShown = false;
		toastMaker.toast(net.shiftinpower.activities.Login.this, C.Confirmationz.FORGOTTEN_PASSWORD_RESET_LINK_SENT_VIA_MAIL, Toast.LENGTH_LONG);
	}

	@Override
	public void onForgottenPasswordEmailSentFailure(String reason) {
		forgottenPasswordDialog.dismiss();
		dialogShown = false;
		if (reason.contentEquals(C.Tagz.NOT_FOUND)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.NO_SUCH_EMAIL_IN_DATABASE, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM) || reason.contentEquals(C.Tagz.BAD_REQUEST)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.CONNECTION_ERROR, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.MAILER_PROBLEM)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.FORGOTTEN_PASSWORD_LINK_NOT_SENT_VIA_EMAIL, Toast.LENGTH_LONG);
		}
	}

	@Override
	public void OnFacebookUserRegisteredSuccess(int userId) {
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_LOGGED_IN_STATE, true);
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_REGISTERED_VIA_FB, true);
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_ID, userId);
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_FB_ID, userFBId);
		sharedPreferencesEditor.commit();

		Intent signupSuccessHome = new Intent(this, InitialDataLoader.class);

		startActivity(signupSuccessHome);
		finish();

	}

	@Override
	public void OnFacebookUserRegisteredFailure() {

		toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.FACEBOOK_LOGIN_FAILURE, Toast.LENGTH_LONG);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			// uiHelper.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(Login.this, requestCode, resultCode, data);
		} else {

			Session session = Session.getActiveSession();
			int sanitizedRequestCode = requestCode % 0x10000;
			session.onActivityResult(this, sanitizedRequestCode, resultCode, data);

		}
	} // End of onActivityResult

	public static boolean hasFacebookPermissions(List<String> permissions) {
		return Session.getActiveSession() != null && Session.getActiveSession().getPermissions().containsAll(permissions);
	}

} // End of Class
=======
package net.shiftinpower.activities;

import java.util.Arrays;
import java.util.List;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import net.shiftinpower.core.*;
import net.shiftinpower.interfaces.OnFacebookUserRegisteredListener;
import net.shiftinpower.interfaces.OnForgottenPasswordEmailSentListener;
import net.shiftinpower.interfaces.OnUserLoginAttemptListener;
import net.shiftinpower.asynctasks.*;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.utilities.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;
/**
*
* NOTE: The Facebook log in feature is NOT implemented properly. Just drafted.
*
* NOTE: oauth or another way of secure usage still not implemented
*
* NOTE:
*
* Ideally, all the classes extend a global class from the net.shiftinpower.core package, so global variables and classes
* are initiated once and used throughout
*
* Fonts, utility classes, shared preferences are initiated and accessed from there.
*
* However, the case with MainActivity, Login and Signup screens is a bit special, as they do not employ the ActionBar and Sliding menu
* I can fix this, but I havent had the time to do so.
*
* @author Kaloyan Roussev
*/
public class Login extends SherlockActivity implements OnClickListener, OnUserLoginAttemptListener, OnForgottenPasswordEmailSentListener,
		OnFacebookUserRegisteredListener {

	// Set up XML View Components
	private TextView tvBraggrLogoLoginPage;
	private TextView tvLoginTitle;
	private TextView tvForgottenPasswordLoginPage;
	private LoginButton authButton;
	private Button bLoginLoginPage;
	private TextView tvOrLoginPage;
	private EditText etUserEmailLoginPage;
	private EditText etUserPasswordLoginPage;

	// Variables holding Data
	private String userEmailLoginPage;
	private String userPasswordLoginPage;
	private String userWithForgottenPasswordsEmailAddress = "";
	private String userPasswordLoginPageHashed;
	public boolean dialogShown = false;
	boolean emailIsInDatabase = false;
	private boolean isUserLoggedIn;

	// Forgotten Password Dialog and its XML Components
	public Dialog forgottenPasswordDialog;
	private EditText etForgottenPasswordEmail;
	private Button bForgottenPasswordDialogSubmit;
	private Button bForgottenPasswordDialogCancel;

	// Shared Preferences
	private Editor sharedPreferencesEditor;
	private SharedPreferences sharedPreferences;
	private static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;

	// Fonts
	private Typeface font1;
	private Typeface font2;

	// Custom class to display toasts
	private ToastMaker toastMaker = new ToastMaker();

	// Verifies the validity of the Email format entered by the user
	private EmailVerifier emailVerifier = new EmailVerifier();

	// This class hashes passwords
	private HashPassword hashPassword = new HashPassword();

	// facebook variables
	private String userFBId;
	private String userName;
	private String userEmail;
	private String userSex;
	private String userFacebookAvatar;
	// private UiLifecycleHelper uiHelper;
	private static final int REAUTH_ACTIVITY_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// If the user has connected the storage to the PC, they will be unable to use the app.
		// In this case it makes no sense for us to start it, so we are making them disconnect the storage first.
		if (!StorageStatusChecker.isExternalStorageAvailable()) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.DISCONNECT_STORAGE_FIRST, Toast.LENGTH_SHORT);
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
		setContentView(R.layout.activity_layout_login);

		// This app operates in Full Screen, so this is what we are setting here
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Inflate the XML View components and Link them to the corresponding Java Objects in this class
		tvBraggrLogoLoginPage = (TextView) findViewById(R.id.tvBraggrLogoLoginPage);
		tvLoginTitle = (TextView) findViewById(R.id.tvLoginTitle);
		tvForgottenPasswordLoginPage = (TextView) findViewById(R.id.tvForgottenPasswordLoginPage);
		authButton = (LoginButton) findViewById(R.id.authButton);
		tvOrLoginPage = (TextView) findViewById(R.id.tvOrLoginPage);
		etUserEmailLoginPage = (EditText) findViewById(R.id.etUserEmailLoginPage);
		etUserPasswordLoginPage = (EditText) findViewById(R.id.etUserPasswordLoginPage);
		bLoginLoginPage = (Button) findViewById(R.id.bLoginLoginPage);

		// Set the fonts for buttons and textviews
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
			font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_2);
			tvBraggrLogoLoginPage.setTypeface(font1);
			tvForgottenPasswordLoginPage.setTypeface(font2);
			tvLoginTitle.setTypeface(font2);
			tvOrLoginPage.setTypeface(font2);
			bLoginLoginPage.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		forgottenPasswordDialog = new Dialog(Login.this, R.style.no_title_dialog);
		forgottenPasswordDialog.setContentView(R.layout.dialog_login_forgotten_password);
		etForgottenPasswordEmail = (EditText) forgottenPasswordDialog.findViewById(R.id.etForgottenPasswordEmail);

		bForgottenPasswordDialogSubmit = (Button) forgottenPasswordDialog.findViewById(R.id.bForgottenPasswordDialogSubmit);
		bForgottenPasswordDialogCancel = (Button) forgottenPasswordDialog.findViewById(R.id.bCancelForgottenPasswordDialog);

		// Set fonts
		try {
			bForgottenPasswordDialogSubmit.setTypeface(font1);
			bForgottenPasswordDialogCancel.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// The Facebook Login / Signup Option DOES NOT WORK at the moment. There was no time to properly implement it.
		authButton.setReadPermissions(Arrays.asList("basic_info", "email", "user_likes", "user_status"));
		authButton.setOnClickListener(this);
		bLoginLoginPage.setOnClickListener(this);
		tvForgottenPasswordLoginPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bForgottenPasswordDialogSubmit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						forgottenPasswordSubmitOnClick();
					}
				});
				bForgottenPasswordDialogCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						forgottenPasswordDialog.dismiss();
						dialogShown = false;
					}
				});
				forgottenPasswordDialog.show();
				dialogShown = true;
			}

		});

		etUserEmailLoginPage.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				// Get the user email entered in the input box
				userEmailLoginPage = etUserEmailLoginPage.getText().toString().trim();

				// Check whether email entered is a valid email.
				// Toast about it not being valid if it isnt
				if (userEmailLoginPage.length() > 0) {
					if (!emailVerifier.isEmailValid(userEmailLoginPage)) {
						toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.ENTER_VALID_EMAIL, Toast.LENGTH_SHORT, Gravity.CENTER_HORIZONTAL, 0,
								-100);
					}
				}

			}
		});

		etUserPasswordLoginPage.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					// When user hits "next" on their on-screen keyboard, this will try to automatically log them in.
					bLoginLoginPage.performClick();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.authButton) {
			/*
			 * if (hasFacebookPermissions(Arrays.asList("basic_info", "email", "user_likes", "user_status"))) { // The user
			 * is already logged in } else { OpenRequest openrequest = new
			 * OpenRequest(this).setPermissions(Arrays.asList("basic_info", "email", "user_likes", "user_status")); Session
			 * session = new Session(Login.this); session.openForRead(openrequest);
			 * 
			 * Session.openActiveSession(this, true, callback);
			 * 
			 * }
			 */

		} else if (id == R.id.bLoginLoginPage) {

			// Get the user email entered in the input box
			userEmailLoginPage = etUserEmailLoginPage.getText().toString().trim();

			// Get the user password from the input
			userPasswordLoginPage = etUserPasswordLoginPage.getText().toString().trim();

			if (userPasswordLoginPage == null || userEmailLoginPage == null) {

				toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.FILL_IN_LOGIN_CREDENTIALS, Toast.LENGTH_SHORT);

			} else {

				// Hash the password so we can transfer it to the db and check it.
				userPasswordLoginPageHashed = hashPassword.computeSHAHash(userPasswordLoginPage);

				// We try to log the user in. check wether email is in DB and password is correct
				// If there is something wrong, AttemptLogin will Toast about it.
				new LogUserInAsync(this, this, userEmailLoginPage, userPasswordLoginPageHashed).execute();
				// What comes next is onUserLoginAttemptSuccess or onUserLoginAttemptFailure
			}
		}
	}

	/*
	 * // Facebook callback private Session.StatusCallback callback = new Session.StatusCallback() {
	 * 
	 * @Override public void call(Session session, SessionState state, Exception exception) { onSessionStateChange(session,
	 * state, exception); } };
	 * 
	 * // Facebook session management private void onSessionStateChange(Session session, SessionState state, Exception
	 * exception) {
	 * 
	 * if (state.isOpened()) { makeMeRequest(session); } else if (state.isClosed()) {
	 * 
	 * Session sessionw = new Session(this); Session.setActiveSession(sessionw); sessionw.openForRead(new
	 * Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList("basic_info", "email", "user_likes",
	 * "user_status"))); } }
	 * 
	 * // Make an API call to get user data from Facebook and define a new callback to handle the response. private void
	 * makeMeRequest(final Session session) {
	 * 
	 * Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) { // If the response is successful if (session ==
	 * Session.getActiveSession()) { if (user != null) { userFBId = user.getId(); userName = user.getName(); userEmail =
	 * (String) user.asMap().get("email"); userSex = (String) user.getProperty("gender"); userFacebookAvatar =
	 * "http://graph.facebook.com/" + user.getId() + "/picture?type=large&redirect=true&width=400&height=400";
	 * 
	 * new RegisterFacebookUserOnServerAsync(Login.this, Login.this, userName, userEmail, userSex,
	 * userFacebookAvatar).execute(); } } if (response.getError() != null) {
	 * System.out.println(response.getError().toString()); } } }); request.executeAsync(); }
	 */
	public void forgottenPasswordSubmitOnClick() {
		userWithForgottenPasswordsEmailAddress = etForgottenPasswordEmail.getText().toString().trim();
		if (!emailVerifier.isEmailValid(userWithForgottenPasswordsEmailAddress)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.ENTER_VALID_EMAIL, Toast.LENGTH_SHORT);
		} else {
			new ForgottenPasswordGetUserNameAndSendEmail(this, this, userWithForgottenPasswordsEmailAddress).execute();
		}

		// this will check if email exists in database
		// if email exists, we will send an email with a way for them to reset their password
		// if email doesnt exist, we will toast about it.

	}

	@Override
	public void onBackPressed() {
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean("userLoggedInState", false);
		sharedPreferencesEditor.putInt("currentLoggedInUserId", 0);
		sharedPreferencesEditor.commit();

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onUserLoginAttemptSuccess(int userId) {
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_LOGGED_IN_STATE, true);
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_ID, userId);
		sharedPreferencesEditor.commit();

		Intent signupSuccessHome = new Intent(this, InitialDataLoader.class);
		startActivity(signupSuccessHome);
		finish();

	}

	@Override
	public void onUserLoginAttemptFailure(String reason) {

		if (reason.contentEquals(C.Tagz.INVALID_CREDENTIALS)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.INCORRECT_EMAIL_PASSWORD_COMBINATION, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM) || reason.contentEquals(C.Tagz.BAD_REQUEST) || reason.contentEquals(C.Tagz.NOT_FOUND)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.CONNECTION_ERROR, Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onForgottenPasswordEmailSentSuccess() {
		forgottenPasswordDialog.dismiss();
		dialogShown = false;
		toastMaker.toast(net.shiftinpower.activities.Login.this, C.Confirmationz.FORGOTTEN_PASSWORD_RESET_LINK_SENT_VIA_MAIL, Toast.LENGTH_LONG);
	}

	@Override
	public void onForgottenPasswordEmailSentFailure(String reason) {
		forgottenPasswordDialog.dismiss();
		dialogShown = false;
		if (reason.contentEquals(C.Tagz.NOT_FOUND)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.NO_SUCH_EMAIL_IN_DATABASE, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM) || reason.contentEquals(C.Tagz.BAD_REQUEST)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.CONNECTION_ERROR, Toast.LENGTH_SHORT);
		} else if (reason.contentEquals(C.Tagz.MAILER_PROBLEM)) {
			toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.FORGOTTEN_PASSWORD_LINK_NOT_SENT_VIA_EMAIL, Toast.LENGTH_LONG);
		}
	}

	@Override
	public void OnFacebookUserRegisteredSuccess(int userId) {
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_LOGGED_IN_STATE, true);
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_REGISTERED_VIA_FB, true);
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_ID, userId);
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_FB_ID, userFBId);
		sharedPreferencesEditor.commit();

		Intent signupSuccessHome = new Intent(this, InitialDataLoader.class);

		startActivity(signupSuccessHome);
		finish();

	}

	@Override
	public void OnFacebookUserRegisteredFailure() {

		toastMaker.toast(net.shiftinpower.activities.Login.this, C.Errorz.FACEBOOK_LOGIN_FAILURE, Toast.LENGTH_LONG);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REAUTH_ACTIVITY_CODE) {
			// uiHelper.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(Login.this, requestCode, resultCode, data);
		} else {

			Session session = Session.getActiveSession();
			int sanitizedRequestCode = requestCode % 0x10000;
			session.onActivityResult(this, sanitizedRequestCode, resultCode, data);

		}
	} // End of onActivityResult

	public static boolean hasFacebookPermissions(List<String> permissions) {
		return Session.getActiveSession() != null && Session.getActiveSession().getPermissions().containsAll(permissions);
	}

} // End of Class
>>>>>>> GoWild

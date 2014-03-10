package net.shiftinpower.core;

import net.shiftinpower.activities.MainActivity;
import net.shiftinpower.localsqlitedb.DBTools;
import net.shiftinpower.utilities.HashPassword;
import net.shiftinpower.utilities.PhotoHandler;
import net.shiftinpower.utilities.StorageStatusChecker;
import net.shiftinpower.utilities.ToastMaker;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * This is the top-level class, the Central Headquarters. RggarbActionBar inherits from here, RggarbSlidingMenu inherits from
 * RggarbActionBar and all activities inherit from there on. This class is responsible for holding the user data, obtained
 * from the InitialDataLoader, stored in a SharedPreferences file and a local SQL This way variables like
 * currentlyLoggedInUserID will be accessible from all inheriting activities This class also holds widely used Utility Class
 * references, such as the custom made ToastMaker and also PhotoHandler
 * 
 * @author Kaloyan Roussev
 */
public class RggarbCore extends SlidingFragmentActivity {

	// Variables holding various data
	protected String userStatus;
	protected boolean userHasRegisteredViaFacebook = false;
	protected String userName;
	protected String userEmail;
	protected String userPassword;
	protected String userSex;
	protected String userAvatarPath;
	protected String userAvatarPathOnServer;
	protected String userQuote;
	protected int userPoints;
	protected boolean userShowsMoney;
	protected boolean userShowsStats;
	protected String userAcceptsMessages;
	protected String userInteractsWithActivities;
	protected int userItemsCount;
	protected int userCommentsCount;
	protected int userFollowingItemsCount;
	protected int userFriendsCount;
	protected int userGalleryPhotosCount;
	protected int userActivityCount;
	protected double userMoneySpentOnItems;
	protected boolean userHasProvidedOwnPhoto;

	// Shared Preferences
	protected Editor sharedPreferencesEditor;
	protected SharedPreferences sharedPreferences;
	protected static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;
	protected int currentlyLoggedInUser;
	protected boolean userLoggedInState = false; // false by default

	// Custom class to display toasts
	protected ToastMaker toastMaker = new ToastMaker();

	// This class hashes passwords
	protected HashPassword hashPassword = new HashPassword();

	// SQLite Database Handler
	protected DBTools dbTools;

	// Fonts
	protected Typeface font1;
	protected Typeface font2;

	// Photo Handler custom class containing several methods that deal with images
	protected PhotoHandler photoHandler = new PhotoHandler(this);

	protected void setUserAvatarPath(String userAvatarPath) {
		this.userAvatarPath = userAvatarPath;
	}

	protected void setUserAvatarPathOnServer(String userAvatarPathOnServer) {
		this.userAvatarPathOnServer = userAvatarPathOnServer;
	}

	protected void setUserStatusVariable(String userStatus) {
		this.userStatus = userStatus;
	}

	protected void setUserName(String userName) {
		this.userName = userName;

	}

	protected void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	protected void setUserSex(String userSex) {
		this.userSex = userSex;

	}

	protected void setUserQuote(String userQuote) {
		this.userQuote = userQuote;
	}

	protected void setUserPoints(int userPoints) {
		this.userPoints = userPoints;
	}

	protected void setUserPassword(String userPassword) {
		this.userPassword = userPassword;

	}

	protected void setUserShowsMoney(boolean userShowsMoney) {
		this.userShowsMoney = userShowsMoney;

	}

	protected void setUserShowsStats(boolean userShowsStats) {
		this.userShowsStats = userShowsStats;

	}

	protected void setUserAcceptsMessages(String userAcceptsMessages) {
		this.userAcceptsMessages = userAcceptsMessages;

	}

	protected void setUserInteractsWithActivities(String userInteractsWithActivities) {
		this.userInteractsWithActivities = userInteractsWithActivities;

	}

	protected void setUserMoneySpentOnItems(double userMoneySpentOnItems) {
		this.userMoneySpentOnItems = userMoneySpentOnItems;
	}

	protected void setUserItemsCount(int userItemsCount) {
		this.userItemsCount = userItemsCount;
	}

	protected void setUserCommentsCount(int userCommentsCount) {
		this.userCommentsCount = userCommentsCount;
	}

	protected void setUserFollowingItemsCount(int userFollowingItemsCount) {
		this.userFollowingItemsCount = userFollowingItemsCount;
	}

	protected void setUserFriendsCount(int userFriendsCount) {
		this.userFriendsCount = userFriendsCount;
	}

	protected void setUserGalleryPhotosCount(int userGalleryPhotosCount) {
		this.userGalleryPhotosCount = userGalleryPhotosCount;
	}

	protected void setUserActivityCount(int userActivityCount) {
		this.userActivityCount = userActivityCount;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dbTools = DBTools.getInstance(this);

		// InitialDataLoader has pulled all this data from the server and stored
		// it in SharedPreferences for fast access. Now we are retrieving it
		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		currentlyLoggedInUser = sharedPreferences.getInt("currentLoggedInUserId", 0);
		setUserName(sharedPreferences.getString(C.SharedPreferencesItems.USER_NAME, ""));
		setUserEmail(sharedPreferences.getString(C.SharedPreferencesItems.USER_EMAIL, ""));
		setUserPassword(sharedPreferences.getString(C.SharedPreferencesItems.USER_PASSWORD, ""));
		setUserSex(sharedPreferences.getString(C.SharedPreferencesItems.USER_SEX, ""));
		setUserAvatarPath(sharedPreferences.getString(C.SharedPreferencesItems.USER_AVATAR_PATH, C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE));
		setUserQuote(sharedPreferences.getString(C.SharedPreferencesItems.USER_QUOTE, ""));
		setUserPoints(sharedPreferences.getInt(C.SharedPreferencesItems.USER_POINTS, 0));
		setUserShowsMoney(sharedPreferences.getBoolean(C.SharedPreferencesItems.USER_SHOWS_MONEY, true));
		setUserShowsStats(sharedPreferences.getBoolean(C.SharedPreferencesItems.USER_SHOWS_STATS, true));
		setUserAcceptsMessages(sharedPreferences.getString(C.SharedPreferencesItems.USER_ACCEPTS_MESSAGES, C.Miscellaneous.USER_RESTRICTION_LEVEL_NO));
		setUserInteractsWithActivities(sharedPreferences.getString(C.SharedPreferencesItems.USER_INTERACTS_WITH_ACTIVITIES,
				C.Miscellaneous.USER_RESTRICTION_LEVEL_NO));
		setUserItemsCount(sharedPreferences.getInt(C.SharedPreferencesItems.USER_ITEMS_COUNT, 0));
		setUserCommentsCount(sharedPreferences.getInt(C.SharedPreferencesItems.USER_COMMENTS_COUNT, 0));
		setUserFollowingItemsCount(sharedPreferences.getInt(C.SharedPreferencesItems.USER_FOLLOWING_ITEMS_COUNT, 0));
		setUserFriendsCount(sharedPreferences.getInt(C.SharedPreferencesItems.USER_FRIENDS_COUNT, 0));
		setUserGalleryPhotosCount(sharedPreferences.getInt(C.SharedPreferencesItems.USER_GALLERY_PHOTOS_COUNT, 0));
		setUserActivityCount(sharedPreferences.getInt(C.SharedPreferencesItems.USER_ACTIVITY_COUNT, 0));
		long userMoneySpentOnItemsLong = sharedPreferences.getLong(C.SharedPreferencesItems.USER_MONEY_SPENT_ON_ITEMS, 0);
		setUserMoneySpentOnItems(Double.longBitsToDouble(userMoneySpentOnItemsLong));

		if ((userAvatarPath != null) && (!userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE)) && (!userAvatarPath.contentEquals(""))) {
			userHasProvidedOwnPhoto = true;
		}
		userHasRegisteredViaFacebook = sharedPreferences.getBoolean("userHasRegisteredViaFacebook", false);

		if (!StorageStatusChecker.isExternalStorageAvailable()) {
			toastMaker.toast(net.shiftinpower.core.RggarbCore.this, C.Errorz.DISCONNECT_STORAGE_FIRST, Toast.LENGTH_SHORT);
			finish();
		}

		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		userLoggedInState = sharedPreferences.getBoolean("userLoggedInState", false);

		if (!userLoggedInState) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}

		// Setting up fonts
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
			font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_2);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}

	} // End of onCreate

	@Override
	protected void onResume() {

		// If the user cant access the internet, they cant use the app so we log them out and
		// If they try to login they will get a toast saying they need to connect to the internet beforehand
		if (!canUserAccessTheInternet()) {
			sharedPreferencesEditor = sharedPreferences.edit();
			sharedPreferencesEditor.putBoolean("userLoggedInState", false);
			sharedPreferencesEditor.putInt("currentLoggedInUserId", 0);
			sharedPreferencesEditor.commit();
		}

		// This is the check itself. if the user is not logged in, they are sent back to the login screen with the activity
		// stack cleared

		userLoggedInState = sharedPreferences.getBoolean("userLoggedInState", false);
		if (!userLoggedInState) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		super.onResume();
	} // End of onResume Method

	@Override
	protected void onRestart() {
		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		userLoggedInState = sharedPreferences.getBoolean("userLoggedInState", false);
		if (!userLoggedInState) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
		super.onRestart();
	}// End of onRestart Method

	public Boolean canUserAccessTheInternet() {
		final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("NewApi")
	public void restartActivity() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= 11) {
			this.recreate();
		} else {
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}
	}

	protected boolean isEditTextEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	protected static int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
		}
		return (int) l;
	}

} // End of Class

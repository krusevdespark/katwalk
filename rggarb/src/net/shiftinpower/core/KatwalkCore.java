package net.shiftinpower.core;

import net.shiftinpower.activities.LogUserOut;
import net.shiftinpower.localsqlitedb.SQLQueries;
import net.shiftinpower.utilities.ShowLoadingMessage;
import net.shiftinpower.utilities.StorageStatusChecker;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * This is the top-level class, the Central Headquarters. KatwalkActionBar inherits from here, KatwalkSlidingMenu inherits
 * from KatwalkActionBar and all activities inherit from there on. This class is responsible for holding the user data,
 * obtained from the InitialDataLoader, stored in a SharedPreferences file and a local SQL This way variables like
 * currentlyLoggedInUserID will be accessible from all inheriting activities.
 * 
 * This class provides some common lifecycle functionality for all the activities that inherit from it
 * 
 * @author Kaloyan Roussev
 */
public class KatwalkCore extends SlidingFragmentActivity {

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

	public KatwalkApplication katwalk;

	// Shared Preferences
	protected Editor sharedPreferencesEditor;
	protected SharedPreferences sharedPreferences;
	protected static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;
	protected int currentlyLoggedInUser;
	protected boolean userLoggedInState = false; // false by default

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get an instance of the application in order for this and all other activities to get acess to the global variables
		// and utilities
		katwalk = (KatwalkApplication) getApplication();

		/*
		 * Usually when the app crashes there are DB problems when we try to write stuff to the db that is already there /*
		 * so here we will clear the database by logging the user out and starting everything anew. I could also make it just
		 * clear the database, but at a later stage in the development process
		 */
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				ex.printStackTrace();

				ShowLoadingMessage.loading(KatwalkCore.this, C.Errorz.APP_CRASHED);
				
				// Clear the database
				katwalk.dbTools.database.execSQL(SQLQueries.tableCategoriesDrop);
				katwalk.dbTools.database.execSQL(SQLQueries.tableSubcategoriesDrop);
				katwalk.dbTools.database.execSQL(SQLQueries.tableItemsDrop);

				deleteDatabase(C.Preferences.LOCAL_SQLITE_DATABASE_NAME);
				katwalk.dbTools.closeDB();

				ShowLoadingMessage.dismissDialog();
				
				// Start everything anew like nothing happened
				Intent startAnew = new Intent(KatwalkCore.this, InitialDataLoader.class);
				startActivity(startAnew);

				// Its important to kill the VM here in order not to allow the app to go into afterlife stage (black screen)
				System.exit(0);
			}
		});

		// The app operates in Full Screen Mode
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// InitialDataLoader has pulled all this data from the server and stored it in SharedPreferences for fast access. Now
		// we are retrieving it
		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		sharedPreferencesEditor = sharedPreferences.edit();

		// This method should also be called after every change to sharedPreferences
		getUserDataFromSharedPreferencesAndAssignItToJavaObjects();

		if ((userAvatarPath != null) && (!userAvatarPath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE)) && (!userAvatarPath.contentEquals(""))) {
			userHasProvidedOwnPhoto = true;
		}

	} // End of onCreate

	@Override
	protected void onResume() {

		super.onResume();

		// If the user cant access the internet, they cant use the app so we log them out and
		// If they try to login they will get a toast saying they need to connect to the internet beforehand
		if (!katwalk.canUserAccessTheInternet()) {
			sharedPreferencesEditor.putBoolean("userLoggedInState", false);
			sharedPreferencesEditor.putInt("currentLoggedInUserId", 0);
			sharedPreferencesEditor.commit();
		}

		if (!StorageStatusChecker.isExternalStorageAvailable()) {
			katwalk.toastMaker.toast(net.shiftinpower.core.KatwalkCore.this, C.Errorz.DISCONNECT_STORAGE_FIRST, Toast.LENGTH_SHORT);
			finish();
		}

	} // End of onResume Method

	// This method should be called after every change to sharedPreferences
	public void getUserDataFromSharedPreferencesAndAssignItToJavaObjects() {

		currentlyLoggedInUser = sharedPreferences.getInt("currentLoggedInUserId", 0);
		userName = sharedPreferences.getString(C.SharedPreferencesItems.USER_NAME, "");
		userEmail = sharedPreferences.getString(C.SharedPreferencesItems.USER_EMAIL, "");
		userPassword = sharedPreferences.getString(C.SharedPreferencesItems.USER_PASSWORD, "");
		userSex = sharedPreferences.getString(C.SharedPreferencesItems.USER_SEX, "");
		userAvatarPath = sharedPreferences.getString(C.SharedPreferencesItems.USER_AVATAR_PATH, C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);
		userQuote = sharedPreferences.getString(C.SharedPreferencesItems.USER_QUOTE, "");
		userPoints = sharedPreferences.getInt(C.SharedPreferencesItems.USER_POINTS, 0);
		userShowsMoney = sharedPreferences.getBoolean(C.SharedPreferencesItems.USER_SHOWS_MONEY, true);
		userShowsStats = sharedPreferences.getBoolean(C.SharedPreferencesItems.USER_SHOWS_STATS, true);
		userAcceptsMessages = sharedPreferences.getString(C.SharedPreferencesItems.USER_ACCEPTS_MESSAGES, C.Miscellaneous.USER_RESTRICTION_LEVEL_NO);
		userInteractsWithActivities = sharedPreferences.getString(C.SharedPreferencesItems.USER_INTERACTS_WITH_ACTIVITIES, C.Miscellaneous.USER_RESTRICTION_LEVEL_NO);
		userItemsCount = sharedPreferences.getInt(C.SharedPreferencesItems.USER_ITEMS_COUNT, 0);
		userCommentsCount = sharedPreferences.getInt(C.SharedPreferencesItems.USER_COMMENTS_COUNT, 0);
		userFollowingItemsCount = sharedPreferences.getInt(C.SharedPreferencesItems.USER_FOLLOWING_ITEMS_COUNT, 0);
		userFriendsCount = sharedPreferences.getInt(C.SharedPreferencesItems.USER_FRIENDS_COUNT, 0);
		userGalleryPhotosCount = sharedPreferences.getInt(C.SharedPreferencesItems.USER_GALLERY_PHOTOS_COUNT, 0);
		userActivityCount = sharedPreferences.getInt(C.SharedPreferencesItems.USER_ACTIVITY_COUNT, 0);
		long userMoneySpentOnItemsLong = sharedPreferences.getLong(C.SharedPreferencesItems.USER_MONEY_SPENT_ON_ITEMS, 0);
		userMoneySpentOnItems = Double.longBitsToDouble(userMoneySpentOnItemsLong);

		userHasRegisteredViaFacebook = sharedPreferences.getBoolean("userHasRegisteredViaFacebook", false);

		userLoggedInState = sharedPreferences.getBoolean("userLoggedInState", false);

	} // End of getUserDataFromSharedPreferencesAndAssignItToJavaObjects

} // End of Class

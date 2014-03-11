package net.shiftinpower.core;

import java.util.LinkedHashSet;

import com.actionbarsherlock.view.Window;

import net.shiftinpower.activities.Home;
import net.shiftinpower.activities.LogUserOut;
import net.shiftinpower.asynctasks.DownloadImage;
import net.shiftinpower.asynctasks.GetCategoriesFromServerAsync;
import net.shiftinpower.asynctasks.GetSubcategoriesFromServerAsync;
import net.shiftinpower.asynctasks.GetUserItemsFromServerAsync;
import net.shiftinpower.interfaces.OnDownloadImageListener;
import net.shiftinpower.interfaces.OnGetCategoriesListener;
import net.shiftinpower.interfaces.OnGetSubcategoriesListener;
import net.shiftinpower.interfaces.OnDownloadUserInfoFromServerListener;
import net.shiftinpower.interfaces.OnGetUserItemsListener;
import net.shiftinpower.interfaces.OnInsertUserItemsInDBListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.*;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.objects.UserExtended;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.utilities.PhotoHandler;
import net.shiftinpower.utilities.ToastMaker;
import net.shiftinpower.utilities.Transporter;
import net.shiftinpower.asynctasks.DownloadUserInfoFromServerAsync;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * After the user signs up or logs in, we are connecting to the server and we are pulling certain items from there in the
 * beginning So we can reference them later locally and not depend on the server. This way screen and spinner load time is
 * reduced.
 * 
 * Here is the Sequence:
 * 
 * - get Product Categories
 * 
 * - get Product Subcategories
 * 
 * - get User Data
 * 
 * - get User Avatar if the user has provided one
 * 
 * - get User Items if the user has items
 * 
 * @author Kaloyan Roussev
 * 
 */
public class InitialDataLoader extends RggarbCore implements OnGetCategoriesListener, OnGetSubcategoriesListener, OnDownloadUserInfoFromServerListener, OnDownloadImageListener, OnGetUserItemsListener, OnInsertUserItemsInDBListener {

	// This is the AsyncTask that communicates with the server
	private DownloadUserInfoFromServerAsync userDetailsDownloader;

	protected int currentlyLoggedInUser;
	private boolean userHasRegisteredViaFacebook;
	private ImageView ivSplashScreen;
	private Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		/*
		 * This is the first time that we are opening the database for the app to use Because of Android > 1.6 and < 3.0
		 * problems, we do not close the database after every DB operation, but We are only closing it when the user logs
		 * out.
		 */
		dbTools.openDB();

		getSupportActionBar().hide();

		// This app operates in No Title, Fullscreen mode
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// We are showing the user a nice loading splash screen while doing work in the background
		setContentView(R.layout.activity_layout_splash_screen);
		setBehindContentView(R.layout.activity_layout_splash_screen);

		ivSplashScreen = (ImageView) findViewById(R.id.ivSplashScreen);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_loading_screen, bitmapOptions);
		ivSplashScreen.setImageBitmap(bitmap);

		// Get the User ID so we can pull the data from the server. Also we need to know whether they facebook registered or
		// not.
		sharedPreferences = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
		currentlyLoggedInUser = sharedPreferences.getInt(C.SharedPreferencesItems.USER_ID, 0);
		userHasRegisteredViaFacebook = sharedPreferences.getBoolean(C.SharedPreferencesItems.USER_REGISTERED_VIA_FB, false);

		new GetCategoriesFromServerAsync(this, this).execute();
		// After this has been executed we go on to either onGetCategoriesFromDatabaseSuccess or
		// onGetCategoriesFromDatabaseFailure
	} // End of onCreate

	@Override
	protected void onStop() {

		// Prevent memory leak by releasing the bitmaps from the memory
		Drawable drawable = ivSplashScreen.getDrawable();
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			bitmap.recycle();
		}
		super.onStop();
	}

	@Override
	public void onGetCategoriesSuccess(LinkedHashSet<ItemCategory> itemCategories) {

		// We have just downloaded the categories from the database, and now we are inserting them into the local SQLite DB
		// for quicker access
		new InsertCategoriesIntoDB(dbTools, itemCategories).execute();

		// and now we have to fetch the subcategories. after this is executed we go to either
		// onGetSubcategoriesFromServerSuccess or onGetSubcategoriesFromServerFailure
		new GetSubcategoriesFromServerAsync(this, this).execute();

	} // End of onGetCategoriesFromServerSuccess Method

	@Override
	public void onGetCategoriesFailure(String reason) {

		if (reason != null) {
			toastMaker.toast(net.shiftinpower.core.InitialDataLoader.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_SHORT);
		}
		new GetSubcategoriesFromServerAsync(this, this).execute();

	} // End of onGetCategoriesFromServerFailure Method

	@Override
	public void onGetSubcategoriesSuccess(LinkedHashSet<ItemSubcategory> itemSubcategories) {

		new InsertSubcategoriesIntoDB(dbTools, itemSubcategories).execute();

		// Next step is downloading the user data from the server
		userDetailsDownloader = new DownloadUserInfoFromServerAsync(String.valueOf(currentlyLoggedInUser), InitialDataLoader.this);
		userDetailsDownloader.downloadUserDetailsAndStats();
	}

	@Override
	public void onGetSubcategoriesFailure(String reason) {
		if (reason != null) {
			toastMaker.toast(net.shiftinpower.core.InitialDataLoader.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_SHORT);
		}
		// Even if we havent gotten the categories from the database, we still need to load the user details so we go on down
		// the chain.
		userDetailsDownloader = new DownloadUserInfoFromServerAsync(String.valueOf(currentlyLoggedInUser), InitialDataLoader.this);
		userDetailsDownloader.downloadUserDetailsAndStats();
	}

	@Override
	public void onDownloadUserInfoFromServerSuccess(UserExtended userDetailsAndStats) {

		/*
		 * There is no putDouble method for SharedPreferences, so we can either rewrite the class, or just convert to long
		 * (not to float, because we will lose precision. Then send the long value, and in the receiving class, convert to
		 * double.
		 */

		Transporter.instance().instanceOfTheCurrentUser = userDetailsAndStats;
		double userMoneySpentOnItemsDouble = userDetailsAndStats.getUserMoneySpentOnItems();
		long userMoneySpentOnItemsLong = Double.doubleToRawLongBits(userMoneySpentOnItemsDouble);

		// Put everything in shared preferences
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_NAME, userDetailsAndStats.getUserName());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_EMAIL, userDetailsAndStats.getUserEmail());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_SEX, userDetailsAndStats.getUserSex());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_PASSWORD, userDetailsAndStats.getUserPassword());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_LAST_SEEN, userDetailsAndStats.getUserLastSeen());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userDetailsAndStats.getUserAvatarPath());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_DATE_REGISTERED, userDetailsAndStats.getUserDateRegistered());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_QUOTE, userDetailsAndStats.getUserQuote());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_POINTS, userDetailsAndStats.getUserPoints());
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_SHOWS_MONEY, userDetailsAndStats.doesUserShowMoney());
		sharedPreferencesEditor.putBoolean(C.SharedPreferencesItems.USER_SHOWS_STATS, userDetailsAndStats.doesUserShowStats());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_ACCEPTS_MESSAGES, userDetailsAndStats.getUserAcceptsMessages());
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_INTERACTS_WITH_ACTIVITIES, userDetailsAndStats.getUserInteractsWithActivities());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_ITEMS_COUNT, userDetailsAndStats.getUserItemsCount());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_COMMENTS_COUNT, userDetailsAndStats.getUserCommentsCount());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_FOLLOWING_ITEMS_COUNT, userDetailsAndStats.getUserFollowingItemsCount());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_FRIENDS_COUNT, userDetailsAndStats.getUserFriendsCount());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_GALLERY_PHOTOS_COUNT, userDetailsAndStats.getUserGalleryPhotosCount());
		sharedPreferencesEditor.putInt(C.SharedPreferencesItems.USER_ACTIVITY_COUNT, userDetailsAndStats.getUserActivityCount());
		sharedPreferencesEditor.putLong(C.SharedPreferencesItems.USER_MONEY_SPENT_ON_ITEMS, userMoneySpentOnItemsLong);
		sharedPreferencesEditor.commit();

		/*
		 * If the user avatar is not the default avatar, we should download their avatar image from the server. Otherwise
		 * send them to the Home Page. Downloading is started in an AsyncTask class. This class is listening for the result
		 * returned After avatar downloader has succeeded or failed, the methods below handle the workflow
		 * 
		 * If the user is coming from the signup screen, at this exact moment we are still uploading their avatar to the
		 * server, so we are going to use the image file, provided at signup
		 */
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Boolean userHasJustRegistered = extras.getBoolean("userHasJustRegistered", false);
			String temporaryUserAvatarPath = extras.getString("temporaryUserAvatarPath");
			if (userHasJustRegistered) {
				// Set the avatar image path to be the one provided at signup
				sharedPreferencesEditor = sharedPreferences.edit();
				sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, temporaryUserAvatarPath);
				sharedPreferencesEditor.commit();

				// Get user's Items only if the user has any
				if (userDetailsAndStats.getUserItemsCount() > 0) {
					new GetUserItemsFromServerAsync(this, currentlyLoggedInUser, this).execute();
				} else {
					// And now the user can go to the Home Screen
					Intent home = new Intent(this, Home.class);
					startActivity(home);
				}

			}
		} else {
			/*
			 * First download user avatar if there is one, then downloading user items will start from either
			 * onDownloadUserAvatarSuccess or onDownloadUserAvatarFailure
			 */
			if (!(userDetailsAndStats.getUserAvatarPath().contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE))) {
				new DownloadImage(this, C.API.WEB_ADDRESS + C.API.IMAGES_USERS_FOLDER_THUMBNAIL + userDetailsAndStats.getUserAvatarPath()).execute();
			} else {
				// Get user's Items only if the user has any
				if (userDetailsAndStats.getUserItemsCount() > 0) {
					new GetUserItemsFromServerAsync(this, currentlyLoggedInUser, this).execute();
				} else {
					// And now the user can go to the Home Screen
					Intent home = new Intent(this, Home.class);
					startActivity(home);
				}
			}

		}

	} // End of onDownloadUserInfoFromServerSuccess Method

	@Override
	public void onDownloadUserInfoFromServerFailure(String reason) {
		toastMaker.toast(this, C.Errorz.PROBLEM_LOADING_USER_DATA, Toast.LENGTH_SHORT);
		Intent logUserOut = new Intent(this, LogUserOut.class);
		startActivity(logUserOut);
		finish();
	}

	@Override
	public void onDownloadImageSuccess(Bitmap userAvatarBitmap, String imageUrl) {

		/*
		 * After retrieving the image from the Server, we are writing it into a file Then we are getting the path to the file
		 * and we are putting it into the Shared Preferences File so we can access it later
		 */
		PhotoHandler photoHandler = new PhotoHandler(this);
		photoHandler.handleIncomingPhoto(userAvatarBitmap);
		String userAvatarPath = photoHandler.getImagePath();

		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, userAvatarPath);
		sharedPreferencesEditor.commit();

		// Get user's Items only if the user has any
		if (sharedPreferences.getInt(C.SharedPreferencesItems.USER_ITEMS_COUNT, 0) > 0) {
			new GetUserItemsFromServerAsync(this, currentlyLoggedInUser, this).execute();
		} else {
			// And now the user can go to the Home Screen
			Intent home = new Intent(this, Home.class);
			startActivity(home);
		}
	}

	@Override
	public void onDownloadImageFailure() {

		// And now the user can go to the Home Screen, sadly no avatar.
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.putString(C.SharedPreferencesItems.USER_AVATAR_PATH, C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);
		sharedPreferencesEditor.commit();

		toastMaker.toast(this, C.Errorz.PROBLEM_LOADING_USER_AVATAR, Toast.LENGTH_SHORT);

		// Get user's Items only if the user has any
		if (sharedPreferences.getInt(C.SharedPreferencesItems.USER_ITEMS_COUNT, 0) > 0) {
			new GetUserItemsFromServerAsync(this, currentlyLoggedInUser, this).execute();
		} else {
			// And now the user can go to the Home Screen
			Intent home = new Intent(this, Home.class);
			startActivity(home);
		}
	}

	@Override
	public void onGetUserItemsSuccess(LinkedHashSet<ItemBasic> userItems) {

		new InsertUserItemsIntoDB(InitialDataLoader.this, dbTools, userItems).execute();

	}

	@Override
	public void onGetUserItemsFailure(String reason) {

		toastMaker.toast(this, C.Errorz.PROBLEM_LOADING_USER_ITEMS, Toast.LENGTH_SHORT);

		Intent home = new Intent(this, Home.class);
		startActivity(home);
	}

	@Override
	public void onInsertUserItemsInDBSuccess() {
		// And now the user can go to the Home Screen
		Intent home = new Intent(this, Home.class);
		startActivity(home);

	}

	@Override
	public void onInsertUserItemsInDBFailure() {
		toastMaker.toast(this, C.Errorz.PROBLEM_LOADING_USER_ITEMS, Toast.LENGTH_SHORT);
		// And now the user can go to the Home Screen
		Intent home = new Intent(this, Home.class);
		startActivity(home);

	}

} // End of Class
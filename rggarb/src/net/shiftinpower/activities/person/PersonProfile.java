package net.shiftinpower.activities.person;

import java.text.DecimalFormat;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import net.shiftinpower.activities.NotImplementedYetScreen;
import net.shiftinpower.asynctasks.GetUserDataFromServerAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.KatwalkSlidingMenu;
import net.shiftinpower.customviews.SquareImageView;
import net.shiftinpower.interfaces.OnGetUserDataFromServerListener;
import net.shiftinpower.interfaces.OnIdentifyUserListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.UserExtended;

/**
 * 
 * This is the tempale for a Person's Profile that holds the commonalities between MyProfile Screen and User Profile Screen
 * 
 * @author Kaloyan Roussev
 * 
 */
public class PersonProfile extends KatwalkSlidingMenu implements OnClickListener, OnGetUserDataFromServerListener {

	// set up XML View Components
	protected TextView tvUserName;
	protected TextView tvUserQuote;
	protected TextView tvMoneySpent;
	protected TextView tvUserStatus;
	protected TextView tvChangeUserQuoteTitle;
	protected TextView tvUserProfileStatsAreVisibleNote;
	protected SquareImageView iUserAvatar;
	protected TextView tvUserProfileItemsTab;
	protected TextView tvUserProfileCommentsTab;
	protected TextView tvUserProfileFollowingTab;
	protected TextView tvUserProfileFriendsTab;
	protected TextView tvUserProfileGalleryTab;
	protected TextView tvUserProfileActivityTab;
	protected Button bUserProfileActionButtonOne;
	protected Button bUserProfileActionButtonTwo;
	protected TableRow myProfileActivityHolder;

	// Variables holding data
	// TODO make these package visible
	protected UserExtended userExtendedData;
	protected boolean currentUser;
	protected int personId;
	protected String personName;
	protected String personSex;
	protected String personEmail;
	protected String personAvatarPath;
	protected String personQuote;
	protected String personStatus;
	protected int personPoints;
	protected boolean personShowsMoney;
	protected boolean personShowsStats;
	protected String personAcceptsMessages;
	protected String personInteractsWithActivities;
	protected int personItemsCount;
	protected int personCommentsCount;
	protected int personFollowingItemsCount;
	protected int personFriendsCount;
	protected int personGalleryPhotosCount;
	protected int personActivityCount;
	protected double personMoneySpentOnItems;
	protected boolean personHasProvidedOwnPhoto;
	protected boolean personIsFriendsWithCurrentUser;
	protected boolean personHasFriendRequestFromUs;
	protected boolean personHasSentUsFriendRequest;
	private OnIdentifyUserListener listener;

	// Image handling variables
	protected Bitmap personAvatarBitmap;

	// Constructor needed because of the way the super class works
	public PersonProfile() {
		super(R.string.app_name);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Assign and inflate an XML file as the view component for this screen
		setContentView(R.layout.activity_layout_user_profile);

		// Assign java objects to XML View elements
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		tvUserStatus = (TextView) findViewById(R.id.tvUserStatus);
		tvMoneySpent = (TextView) findViewById(R.id.tvMoneySpent);
		iUserAvatar = (SquareImageView) findViewById(R.id.iUserAvatar);
		tvUserQuote = (TextView) findViewById(R.id.tvUserQuote);
		bUserProfileActionButtonOne = (Button) findViewById(R.id.bUserProfileActionButtonOne);
		bUserProfileActionButtonTwo = (Button) findViewById(R.id.bUserProfileActionButtonTwo);
		tvUserProfileItemsTab = (TextView) findViewById(R.id.tvUserProfileItemsTab);
		tvUserProfileCommentsTab = (TextView) findViewById(R.id.tvUserProfileCommentsTab);
		tvUserProfileFollowingTab = (TextView) findViewById(R.id.tvUserProfileFollowingTab);
		tvUserProfileFriendsTab = (TextView) findViewById(R.id.tvUserProfileFriendsTab);
		tvUserProfileGalleryTab = (TextView) findViewById(R.id.tvUserProfileGalleryTab);
		tvUserProfileActivityTab = (TextView) findViewById(R.id.tvUserProfileActivityTab);
		tvUserProfileStatsAreVisibleNote = (TextView) findViewById(R.id.tvUserProfileStatsAreVisibleNote);
		myProfileActivityHolder = (TableRow) findViewById(R.id.userProfileActivityHolder);

		// TODO maybe put this in a OnUserActivityReceived
		getSupportFragmentManager().beginTransaction().replace(R.id.userProfileActivityHolder, new PersonProfileActivity()).commit();

		// Trying to set some fonts
		try {
			tvUserName.setTypeface(katwalk.font2);
			tvUserProfileItemsTab.setTypeface(katwalk.font2);
			tvUserProfileCommentsTab.setTypeface(katwalk.font2);
			tvUserProfileFollowingTab.setTypeface(katwalk.font2);
			tvUserProfileFriendsTab.setTypeface(katwalk.font2);
			tvUserProfileGalleryTab.setTypeface(katwalk.font2);
			tvUserProfileActivityTab.setTypeface(katwalk.font2);
		} catch (Exception e) {
			tvUserProfileItemsTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileCommentsTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileFollowingTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileFriendsTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileGalleryTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
			tvUserProfileActivityTab.setTextSize(C.Fontz.FONT_SIZE_WHEN_FONT_UNAVAILABLE);
		}

		// Set onClickListeners
		tvUserProfileItemsTab.setOnClickListener(this);
		tvUserProfileCommentsTab.setOnClickListener(this);
		tvUserProfileFollowingTab.setOnClickListener(this);
		tvUserProfileFriendsTab.setOnClickListener(this);
		tvUserProfileGalleryTab.setOnClickListener(this);
		tvUserProfileActivityTab.setOnClickListener(this);

	} // End of onCreate

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {

		case R.id.tvUserProfileItemsTab:
			Intent personProfileItems = new Intent(this, PersonProfileItems.class);
			startActivity(personProfileItems);
			break;

		case R.id.tvUserProfileFollowingTab:
			Intent personProfileFollowing = new Intent(this, PersonProfileFollowing.class);
			startActivity(personProfileFollowing);
			break;

		case R.id.tvUserProfileGalleryTab:

			Intent personProfilePhotos = new Intent(this, NotImplementedYetScreen.class);
			// Intent notifications = new Intent(getActivity(), Notifications.class);

			// Intent myProfilePhotos = new Intent(this, MyProfilePhotos.class);
			startActivity(personProfilePhotos);
			break;

		case R.id.tvUserProfileCommentsTab:
			Intent personProfileReviews = new Intent(this, PersonProfileComments.class);
			startActivity(personProfileReviews);
			break;

		case R.id.tvUserProfileActivityTab:
			// TODO scroll down and show the activity perhaps?
			break;

		} // End of Switch

	} // End of onClick Method

	public void setUserStatus(int userPoints) {
		if (userPoints >= C.StatusPoints.STATUS_1_MIN && userPoints <= C.StatusPoints.STATUS_1_MAX) {
			personStatus = C.Statuses.STATUS_1;
		} else if (userPoints >= C.StatusPoints.STATUS_2_MIN && userPoints <= C.StatusPoints.STATUS_2_MAX) {
			personStatus = C.Statuses.STATUS_2;
		} else if (userPoints >= C.StatusPoints.STATUS_3_MIN && userPoints <= C.StatusPoints.STATUS_3_MAX) {
			personStatus = C.Statuses.STATUS_3;
		} else if (userPoints >= C.StatusPoints.STATUS_4_MIN && userPoints <= C.StatusPoints.STATUS_4_MAX) {
			personStatus = C.Statuses.STATUS_4;
		} else if (userPoints >= C.StatusPoints.STATUS_5_MIN && userPoints <= C.StatusPoints.STATUS_5_MAX) {
			personStatus = C.Statuses.STATUS_5;
		} else {
			personStatus = C.Statuses.STATUS_5; // TODO Later on an option for a custom status will be provided
		}
	} // End of setUserStatus

	private void handleUserDetails(UserExtended userExtendedData) {
		
		personName = userExtendedData.getUserName();
		personSex = userExtendedData.getUserSex();
		personEmail = userExtendedData.getUserEmail();
		personAvatarPath = userExtendedData.getUserAvatarPath();
		personQuote = userExtendedData.getUserQuote();
		personPoints = userExtendedData.getUserPoints();
		personShowsMoney = userExtendedData.doesUserShowMoney();
		personShowsStats = userExtendedData.doesUserShowStats();
		personAcceptsMessages = userExtendedData.getUserAcceptsMessages();
		personInteractsWithActivities = userExtendedData.getUserInteractsWithActivities();
		personItemsCount = userExtendedData.getUserItemsCount();
		personCommentsCount = userExtendedData.getUserCommentsCount();
		personFollowingItemsCount = userExtendedData.getUserFollowingItemsCount();
		personFriendsCount = userExtendedData.getUserFriendsCount();
		personGalleryPhotosCount = userExtendedData.getUserGalleryPhotosCount();
		personActivityCount = userExtendedData.getUserActivityCount();
		personMoneySpentOnItems = userExtendedData.getUserMoneySpentOnItems();
		personIsFriendsWithCurrentUser = userExtendedData.isUserIsFriendsWithCurrentUser();
		personHasFriendRequestFromUs = userExtendedData.isUserHasFriendRequestFromUs();
		personHasSentUsFriendRequest = userExtendedData.isUserHasSentUsFriendRequest();
		
		if(listener!=null){
			listener.onUserIdentified();
		}
	}

	private void handleCurrentUserDetails() {
		personName = userName;
		personSex = userSex;
		personEmail = userEmail;
		personAvatarPath = userAvatarPath;
		personQuote = userQuote;
		personPoints = userPoints;
		personShowsMoney = userShowsMoney;
		personShowsStats = userShowsStats;
		personAcceptsMessages = userAcceptsMessages;
		personInteractsWithActivities = userInteractsWithActivities;
		personItemsCount = userItemsCount;
		personCommentsCount = userCommentsCount;
		personFollowingItemsCount = userFollowingItemsCount;
		personFriendsCount = userFriendsCount;
		personGalleryPhotosCount = userGalleryPhotosCount;
		personActivityCount = userActivityCount;
		personMoneySpentOnItems = userMoneySpentOnItems;
		
	}

	private void setDisplayedData(Boolean currentUser) {
		// Set displayed text
		
		tvUserProfileItemsTab.setText("[" + personItemsCount + "] Items");
		tvUserProfileCommentsTab.setText("[" + personCommentsCount + "] Comments");
		tvUserProfileFollowingTab.setText("[" + personFollowingItemsCount + "] Following");
		tvUserProfileFriendsTab.setText("[" + personFriendsCount + "] Friends");
		tvUserProfileGalleryTab.setText("[" + personGalleryPhotosCount + "] Gallery");
		tvUserProfileActivityTab.setText("[" + personActivityCount + "] Activities");

		// Set user display name
		tvUserName.setText(personName);

		// Set user quote
		tvUserQuote.setText(personQuote);

		// Set user status
		setUserStatus(personPoints);
		tvUserStatus.setText(personStatus);

		// Set the money spent text
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		if (C.Miscellaneous.CURRENCY_SYMBOL_BEFORE_AMOUNT) {
			tvMoneySpent.setText(C.Miscellaneous.CURRENCY_SYMBOL + String.valueOf(decimalFormat.format(personMoneySpentOnItems) + " spent on items"));
		} else {
			tvMoneySpent.setText(String.valueOf(decimalFormat.format(personMoneySpentOnItems) + C.Miscellaneous.CURRENCY_SYMBOL + " spent on items"));
		}

		// Set avatar image
		if(currentUser){
			katwalk.setUserImageToImageView(iUserAvatar, personAvatarPath, personSex);
		} else {
			katwalk.setUserImageToImageViewFromWeb(iUserAvatar, personAvatarPath, C.API.IMAGES_USERS_FOLDER_ORIGINAL, personSex);
		}
		
	} // End of SetDisplayData

	protected void identifyUser(Bundle extras) {

		currentUser = extras.getBoolean("currentUser");
		personId = extras.getInt("personId", currentlyLoggedInUser);

		if (currentUser) {

			handleCurrentUserDetails();
			personAvatarPath = sharedPreferences.getString(C.SharedPreferencesItems.USER_AVATAR_PATH, C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);
			setDisplayedData(true);

		} else {
			new GetUserDataFromServerAsync(String.valueOf(personId), String.valueOf(currentlyLoggedInUser), PersonProfile.this, PersonProfile.this).execute();
		}
	} // End of identifyUser

	@Override
	public void onGetUserDataFromServerSuccess(UserExtended userDetailsAndStats) {
		userExtendedData = userDetailsAndStats;
		handleUserDetails(userExtendedData);
		setDisplayedData(false);
	}

	@Override
	public void onGetUserDataFromServerFailure(String reason) {
		// TODO Auto-generated method stub

	}
	
	protected void setOnIdentifyUserListener(OnIdentifyUserListener listener){
		this.listener = listener;
	}

} // End of Class

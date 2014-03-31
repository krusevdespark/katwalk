package net.shiftinpower.core;

/**
 * This is the main Configuration class and its modelled after Android's R class that holds references for all view objects
 * There are no hardcoded strings in this app (few exceptions), everything is taken from this class XML View components'
 * visible strings are located in the res/values/strings.xml file
 * 
 * @author Kaloyan Roussev
 * 
 */

public final class C {

	public static final class Preferences {
		// Until the Landscape version of the App is developed, we enforce Portrait Mode
		public static final boolean PORTRAIT_MODE_ENFORCED = true;
		public static final String APPLICATION_NAME = "Katwalk";
		public static final String LOCAL_SQLITE_DATABASE_NAME = "rggarb.db";
		public static final String SHARED_PREFERENCES_FILENAME = "rggarb_preferences";
		public static final String RGGARB_DIRECTORY_ON_USER_STORAGE = "/rggarb";
	}

	public static final class API {

		public static final String WEB_ADDRESS = "http://www.shiftinpower.net/";

		public static final String IMAGES_USERS_FOLDER_THUMBNAIL = "user_images/medium/";
		public static final String IMAGES_USERS_FOLDER_MINI = "user_images/mini/";
		public static final String IMAGES_USERS_FOLDER_BIG = "user_images/big/";
		public static final String IMAGES_USERS_FOLDER_ORIGINAL = "user_images/original/";
		public static final String IMAGES_ITEMS_FOLDER_THUMBNAIL = "item_images/medium/";
		public static final String IMAGES_ITEMS_FOLDER_MINI = "item_images/mini/";
		public static final String IMAGES_ITEMS_FOLDER_BIG = "item_images/big/";
		public static final String IMAGES_ITEMS_FOLDER_ORIGINAL = "item_images/original/";
		public static final String IMAGES_PLACES_FOLDER_THUMBNAIL = "place_images/medium/";
		public static final String IMAGES_PLACES_FOLDER_MINI = "place_images/mini/";
		public static final String IMAGES_PLACES_FOLDER_BIG = "place_images/big/";
		public static final String IMAGES_PLACES_FOLDER_ORIGINAL = "place_images/original/";

		// User Related
		public static final String REGISTER_USER = "register_user.php";
		public static final String CHECK_IF_USER_WITH_SAME_EMAIL_EXISTS = "check_if_user_with_same_email_exists.php";
		public static final String GET_USER_DATA = "get_user_data.php";
		public static final String GET_USER_EMAIL_AND_NAME = "get_user_name_email.php";
		public static final String LOGIN_USER = "login_user.php";
		public static final String GET_USER_EMAIL = "get_user_email.php";
		public static final String SET_LAST_SEEN = "set_last_seen.php";
		public static final String SAVE_USER_SETTINGS = "save_user_settings.php";
		public static final String DELETE_USER_ACCOUNT = "delete_user_account.php";
		public static final String SAVE_USER_DETAILS = "save_user_details.php";
		public static final String SET_USER_QUOTE = "set_user_quote.php";
		public static final String REGISTER_FACEBOOK_USER = "register_facebook_user.php";
		public static final String SET_USER_AVATAR = "set_user_avatar.php";
		public static final String DELETE_USER_AVATAR = "delete_user_avatar.php";

		// Categories Related
		public static final String GET_CATEGORIES = "get_categories.php";
		public static final String GET_SUBCATEGORIES = "get_subcategories.php";

		// Image Handling
		public static final String INCOMING_USER_IMAGES_HANDLER = "incoming_user_images_handler.php";
		public static final String INCOMING_ITEM_IMAGES_HANDLER = "incoming_item_images_handler.php";
		public static final String ADD_NEW_ITEM_IMAGE_FILENAMES_AND_DESCRIPTIONS = "add_item_image_filenames_and_descriptions.php";

		// Items Related
		public static final String ADD_NEW_ITEM = "add_new_item.php";
		public static final String GET_USER_ITEMS = "get_user_items.php";
		public static final String GET_ITEM_DATA = "get_item_info.php";

		// Friend Requests Related
		public static final String FRIEND_REQUEST_SEND = "friend_request_send.php";
		public static final String FRIEND_REQUEST_CANCEL = "friend_request_cancel.php";
		public static final String FRIEND_REQUEST_REJECT = "friend_request_reject.php";
		public static final String FRIEND_REQUEST_ACCEPT = "friend_request_accept.php";

		public static final String FRIEND_REMOVE = "friend_remove.php";

	} // End of API

	public static final class DBTables {
		public static final String CATEGORIES = "categories";
		public static final String SUBCATEGORIES = "subcategories";
		public static final String ITEMS = "items";
	}

	/*
	 * There are the tags for the databse table columns on the server that we are communicating with We should use the same
	 * names in the local sqlite db as well
	 */
	public static final class DBColumns {

		public static final String ACTION_ID = "action_id";
		public static final String POINTS_REWARDED = "points_rewarded";

		public static final String ITEM_OWNER_ID = "user_id";
		public static final String ITEM_ID = "item_id";
		public static final String ITEM_IMAGE = "item_photo";
		public static final String ITEM_NAME = "item_name";
		public static final String ITEM_TIME_ADDED = "item_time_added";
		public static final String ITEM_DESCRIPTION = "item_description";
		public static final String ITEM_BRAND = "item_brand";
		public static final String ITEM_PRICE_AQUIRED = "item_price_aquired";
		public static final String ITEM_BOUGHT_FROM = "item_bought_from";
		public static final String ITEM_BOUGHT_FROM_PLACE = "item_bought_from_place";
		public static final String ITEM_BOUGHT_FROM_PLACE_NAME = "item_bought_from_place_name";
		public static final String ITEM_CONDITION_NEW = "item_condition_new";
		public static final String ITEM_BEING_SOLD = "item_being_sold";
		public static final String ITEM_PRICE_BEING_SOLD = "item_price_being_sold";
		public static final String ITEM_WAS_A_GIFT = "item_is_gift";
		public static final String ITEM_CATEGORY_ID = "category_id";
		public static final String ITEM_SUBCATEGORY_ID = "subcategory_id";
		public static final String ITEM_NUMBER_OF_PHOTOS = "item_number_of_photos";
		public static final String ITEM_RATINGS = "item_ratings";
		public static final String ITEM_RATING = "item_rating";
		public static final String ITEM_RATING_TIMESTAMP = "time_when_user_rated_the_item";
		public static final String ITEM_RATING_USER = "user_that_rated_the_item";
		public static final String ITEM_USERS = "item_users";
		public static final String ITEM_PLACES = "item_places";
		public static final String ITEM_FOLLOWINGS = "item_followings";
		public static final String ITEM_FOLLOWER_ID = "follower_id";
		public static final String ITEM_COMMENTS = "item_comments";
		public static final String ITEM_COMMENT_ID = "comment_id";
		public static final String ITEM_COMMENTER_ID = "commenter_id";
		public static final String ITEM_COMMENT_CONTENT = "comment_content";
		public static final String ITEM_COMMENT_CREATION_TIMESTAMP = "time_comment_created";
		public static final String ITEM_COMMENT_EDITION_TIMESTAMP = "time_comment_edited";
		public static final String ITEM_CATEGORIES = "item_categories";
		public static final String ITEM_SUBCATEGORIES = "item_subcategories";
		public static final String ITEM_IMAGES = "item_images";
		public static final String ITEM_INFO = "item_info";

		public static final String CATEGORY_NAME = "category_name";
		public static final String CATEGORY_ID = "category_id";
		public static final String CATEGORY_LOGO = "category_logo";

		public static final String SUBCATEGORY_ID = "subcategory_id";
		public static final String SUBCATEGORY_NAME = "subcategory_name";
		public static final String SUBCATEGORY_PARENT_ID = "subcategory_parent_id";

		public static final String BRAND_ID = "brand_id";
		public static final String BRAND_NAME = "brand_name";
		public static final String BRAND_LOGO = "brand_logo";
		public static final String BRAND_DESCRIPTION = "brand_description";
		public static final String BRAND_WEBSITE = "brand_website";
		public static final String BRAND_FIRST_ADDED_FROM = "brand_first_added_from";

		public static final String USER_AVATAR = "user_avatar";
		public static final String USER_NAME = "user_name";
		public static final String USER_EMAIL = "user_email";
		public static final String USER_SEX = "user_sex";
		public static final String USER_QUOTE = "user_quote";
		public static final String USER_POINTS = "user_points";
		public static final String USER_DATE_REGISTERED = "user_date_registered";
		public static final String USER_LAST_SEEN = "user_last_seen";
		public static final String USER_PASSWORD = "user_password";
		public static final String USER_SHOWS_MONEY = "user_show_money";
		public static final String USER_SHOWS_STATS = "user_show_stats";
		public static final String USER_ACCEPT_MESSAGES = "user_accept_messages";
		public static final String USER_INTERACT_WITH_ACTIVITIES = "user_interact_with_activities";
		public static final String USER_ITEMS_COUNT = "user_items_count";
		public static final String USER_REVIEWS_COUNT = "user_reviews_count";
		public static final String USER_GALLERY_PHOTOS_COUNT = "user_gallery_photos_count";
		public static final String USER_ACTIVITIY_COUNT = "user_activities_count";
		public static final String USER_FRIENDS_COUNT = "user_friends_count";
		public static final String USER_FOLLOWING_ITEMS_COUNT = "user_following_items_count";
		public static final String USER_MONEY_SPENT_ON_ITEMS = "user_money_spent_on_items";
		public static final String USER_ID = "user_id";
		public static final String USER_ITEMS = "user_items";
		public static final String USER_IS_FRIEND_OF_CURRENT_USER = "is_friend";
		public static final String ANOTHER_USER_ID = "another_user_id";
		public static final String WE_SENT_THEM_FRIEND_REQUEST = "we_sent_them_friend_request";
		public static final String THEY_SENT_US_FRIEND_REQUEST = "they_sent_us_friend_request";

		public static final String MESSAGE = "message";
		public static final String SUCCESS = "success";

		public static final String PLACE_ID = "place_id";
		public static final String PLACE_NAME = "place_name";
		public static final String PLACE_TYPE = "place_type";
		public static final String PLACE_DESCRIPTION = "place_description";
		public static final String PLACE_ADDED_BY_USER = "place_added_by_user";
		public static final String PLACE_AVATAR = "place_photo";

		public static final String IMAGE_ID = "item_photo_id";
		public static final String IMAGE_FILENAME = "image_filename";
		public static final String IMAGE_URL = "item_photo";
		public static final String IMAGE_TIMESTAMP = "item_photo_time_added";
		public static final String IMAGE_DESCRIPTION = "item_photo_description";
		public static final String IMAGE_ADDED_BY_USER = "item_photo_added_by_user";
		
		// Friendship request
		public static final String FRIENDSHIP_REQUEST_SENDER_ID = "sender_id";
		public static final String FRIENDSHIP_REQUEST_RECEIVER_ID = "receiver_id";
		
		// Friendship request status
		public static final String FRIENDSHIP_REQUEST_PENDING = "pending";
		public static final String FRIENDSHIP_REQUEST_ACCEPTED = "accepted";
		public static final String FRIENDSHIP_REQUEST_REJECTED = "rejected";
		
		// Remove Friend
		public static final String REMOVE_FRIEND_REMOVER = "remover_id";
		public static final String REMOVE_FRIEND_REMOVED = "removed_id";
		

	} // End of DBColumns

	// Used when there is a corrupted record in the database, we still need to display some text
	public static final class FallbackCopy {
		public static final String NO_CATEGORY = "No category";
		public static final String NO_SUBCATEGORY = "No subcategory";
		public static final String BOUGHT_FROM_PERSON = "Person";
		public static final String PLACE_HAS_NO_NAME = "Unnamed Place";
		public static final String NO_BRAND = "No Brand";
		public static final CharSequence CLICK_HERE_TO_CHANGE_YOUR_QUOTE = "Click here to change your profile quote";
	}

	public static final class HttpResponses {

		public static final int SUCCESS = 200;
		public static final int FAILURE_BAD_REQUEST = 400;
		public static final int FAILURE_DATABASE_PROBLEM = 500;
		public static final int FAILURE_NOT_FOUND = 404;
		public static final int FAILURE_FORBIDDEN = 403; // used for email already taken or wrong login creds

	}

	public static final class FeedTabs {
		public static final String ITEMS = "Items";
		public static final String PEOPLE = "People";
		public static final String PLACES = "Places";
	}

	public static final class SlidingMenuItems {

		public static final String FEED = "Feed";
		public static final String MY_PROFILE = "My Profile";
		public static final String ITEM_ADD = "Add an Item";
		public static final String MY_ITEMS = "My Items";
		public static final String SEARCH_ITEMS = "Search Items";
		public static final String SEARCH_USERS = "Search Users";
		public static final String MESSAGES = "Messages";
		public static final String NOTIFICATIONS = "Notifications";
		public static final String POINTS_AND_STATUSES = "Points and Statuses";
		public static final String PRIVACY_POLICY = "Privacy Policy";
		public static final String TERMS_OF_SERVICE = "Terms Of Service";
		public static final String ABOUT = "About";
		public static final String CONTACT_US = "Contact Us";
		public static final String SETTINGS = "Settings";
		public static final String LOG_OUT = "Log Out";

	}

	public static final class Tagz {
		// these correspond to API's php files' output tags in the json
		// TODO research whether its worth going so far object-orienting the project. some of these values are only used once
		// in a single place.
		public static final String SUCCESS = "success";
		public static final String ITEM_ID = "item_id";
		public static final String TRANSACTION_ID = "transaction_id";
		public static final String TIME_ITEM_WAS_ADDED_TO_DB = "time_item_added";
		public static final String BAD_REQUEST = "Bad request";
		public static final String DB_PROBLEM = "Database Problem";
		public static final String UNKNOWN_PROBLEM = "Unknown Problem";
		public static final String NOT_FOUND = "No results";
		public static final String INVALID_CREDENTIALS = "Invalid credentals";
		public static final String EMAIL_TAKEN = "Email Taken";
		public static final String MAILER_PROBLEM = "Mail not sent";
		public static final String ITEM_BOUGHT_FROM_PERSON = "Person";
	}

	public static final class SharedPreferencesItems {

		public static final String USER_STATUS = "userStatus";
		public static final String USER_ID = "currentLoggedInUserId";
		public static final String USER_LOGGED_IN_STATE = "userLoggedInState";
		public static final String USER_REGISTERED_VIA_FB = "UserHasRegisteredViaFacebook";
		public static final String USER_FB_ID = "userFBId";
		public static final String USER_NAME = "userName";
		public static final String USER_EMAIL = "userEmail";
		public static final String USER_LAST_SEEN = "userLastSeen";
		public static final String USER_DATE_REGISTERED = "userDateRegistered";
		public static final String USER_PASSWORD = "userPassword";
		public static final String USER_SEX = "userSex";
		public static final String USER_AVATAR_PATH = "userAvatarPath";
		public static final String USER_QUOTE = "userQuote";
		public static final String USER_POINTS = "userPoints";
		public static final String USER_SHOWS_MONEY = "userShowMoney";
		public static final String USER_SHOWS_STATS = "userShowStats";
		public static final String USER_ACCEPTS_MESSAGES = "userAcceptMessages";
		public static final String USER_INTERACTS_WITH_ACTIVITIES = "userInteractWithActivities";
		public static final String USER_ITEMS_COUNT = "userItemsCount";
		public static final String USER_COMMENTS_COUNT = "userCommentsCount";
		public static final String USER_FOLLOWING_ITEMS_COUNT = "userFollowingItemsCount";
		public static final String USER_FRIENDS_COUNT = "userFriendsCount";
		public static final String USER_GALLERY_PHOTOS_COUNT = "userGalleryPhotosCount";
		public static final String USER_ACTIVITY_COUNT = "userActivityCount";
		public static final String USER_MONEY_SPENT_ON_ITEMS = "userMoneySpentOnItems";

	} // End of SharedPreferencesItems

	public static final class Actions {
		/*
		 * actions - here actions will be given names and int values when a user does something, we send the int to the
		 * userActions table (userId, actionId, pointsRewarded) when we need to get user's points we will query that table
		 * and sum the points for the certain user when we need to get the user status we will do a switch statement with the
		 * points ranges below
		 */
		public static final int GIVING_AWAY_ITEM_ABOVE_100_BUCKS = 1;
		public static final int ADDING_A_PLACE = 2;
		public static final int BUYING_A_PRODUCT = 3;
		public static final int GIVING_AWAY_ITEM_BETWEEN_10_AND_100_BUCKS = 4;
		public static final int BECOMING_FRIENDS = 5;
		public static final int CONTRIBUTING_TO_PLACE_PROFILE = 6;
		public static final int GIVING_AWAY_ITEM_BELOW_10_BUCKS = 7;
		public static final int FOLLOWING_A_PRODUCT = 8;
		public static final int UPLOADING_A_PRODUCT_PICTURE = 9;
		public static final int COMMEINTING_SOMEONES_PRODUCT = 10;
		public static final int ENVYING_SOMEONES_PURCHASE = 11;
		public static final int RATING_AN_ITEM = 12;
	}

	public static final class PointsForActions {
		public static final int GIVING_AWAY_ITEM_ABOVE_100_BUCKS = 1000;
		public static final int ADDING_A_PLACE = 500;
		public static final int BUYING_A_PRODUCT = 100;
		public static final int SELLING_A_PRODUCT_TO_ANOTHER_USER = 100;
		public static final int DELETING_A_PRODUCT = -100;
		public static final int GETTING_CONTENT_FLAGGED_LEVEL_1 = -100;
		public static final int GETTING_CONTENT_FLAGGED_LEVEL_2 = -1000;
		public static final int GETTING_CONTENT_FLAGGED_LEVEL_3 = -1000;
		public static final int GIVING_AWAY_ITEM_BETWEEN_10_AND_100_BUCKS = 100;
		public static final int BECOMING_FRIENDS = 50;
		public static final int CONTRIBUTING_TO_PLACE_PROFILE = 50;
		public static final int GIVING_AWAY_ITEM_BELOW_10_BUCKS = 10;
		public static final int FOLLOWING_A_PRODUCT = 5;
		public static final int UPLOADING_A_PRODUCT_PICTURE = 3;
		public static final int COMMENTING_SOMEONES_PRODUCT = 2;
		public static final int ENVYING_SOMEONES_PURCHASE = 2;
		public static final int RATING_A_PRODUCT = 1;
	}

	public static final class Statuses {
		public static final String STATUS_1 = "Window Shopper";
		public static final String STATUS_2 = "Regular Shopper";
		public static final String STATUS_3 = "Shopaholic";
		public static final String STATUS_4 = "Big Time Spender";
		public static final String STATUS_5 = "Royal";
	}

	public static final class StatusPoints {
		public static final int STATUS_1_MIN = 0;
		public static final int STATUS_1_MAX = 100;
		public static final int STATUS_2_MIN = 101;
		public static final int STATUS_2_MAX = 1000;
		public static final int STATUS_3_MIN = 1001;
		public static final int STATUS_3_MAX = 5000;
		public static final int STATUS_4_MIN = 5001;
		public static final int STATUS_4_MAX = 20000;
		public static final int STATUS_5_MIN = 20001;
		public static final int STATUS_5_MAX = 100000;
	}

	public static final class Emailz {

		public static final String BRAGGR_OFFICIAL_ADDRESS = "krusev@despark.com";
		public static final String BRAGGR_OFFICIAL_PASSWORD = "Despark1234"; // Security Breach!
		public static final String SUCCESSFUL_SIGNUP_SUBJECT = "Welcome to Katwalk!";

		public static final String SUCCESSFUL_SIGNUP_BODY(String userName) {

			String body = "Welcome, "
					+ userName
					+ "\n\nYou have successfully registered to Rggarb. \nYou can proceed to our app using your username and the password you set during the registration process.\n\n\nKind Regards,\nThe Rggarb Team!";
			return body;
		}

		public static final String FORGOTTEN_PASSWORD_RESET_ADDRESS = "no-reply@rggarb.com";
		public static final String FORGOTTEN_PASSWORD_RESET_SUBJECT = "Reset Password at Rggarb.com";
		public static final String NEW_CONTACT_US_INQUIRY = "New inquiry from the Contact Form, regarding: ";

		public static final String FORGOTTEN_PASSWORD_RESET_BODY(String userName) {
			// TODO Provide a link to the website here when a website exists.
			String body = "Hello, " + userName + "\n\nYou have requested a password reset. \nFollow this link in order to reset your password at rggarb.com";
			return body;
		}

	} // End of Emailz

	public static final class Miscellaneous {
		public static final int TOAST_TEXT_SIZE = 14;
		public static final String BUTTON_TEXT_FRIEND_REQUEST_SENT = "Request Sent";
		public static final String USER_RESTRICTION_LEVEL_NO = "Anyone";
		public static final String USER_RESTRICTION_LEVEL_LIMITED = "Friends";
		public static final String USER_RESTRICTION_LEVEL_FULL = "Noone";
		public static final String CURRENCY_SYMBOL = "$";
		public static final boolean CURRENCY_SYMBOL_BEFORE_AMOUNT = true;
		public static final String DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
		public static final int LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE = 123;
	}

	public static final class ImageHandling {
		public static final String IMAGES_FILE_EXTENSION = ".jpg";
		public static final int IMAGE_SENT_TO_SERVER_QUALITY_0_T0_100 = 100;
		public static final int INITIAL_BITMAP_RESAMPLE_SIZE = 2;
		public static final int HARDER_BITMAP_RESAMPLE_SIZE = 4;
		public static final int MAX_IMAGE_FILE_SIZE = 1000 * 1024;
		public static final int MAX_IMAGE_WIDTH = 640;
		public static final int MAX_IMAGE_HEIGHT = 480;
		public static final String IMAGE_FILENAME_PREFIX = "_image_";
		public static final int REQUEST_CODE_PICK_IMAGE = 7;
		public static final int REQUEST_CODE_TAKE_IMAGE_WITH_CAMERA = 3;
		public static final int REQUEST_CODE_CHANGE_IMAGE = 4;
		public static final String INTENT_EXTRA_IMAGE_PATH_KEY = "imagePath";
		public static final String TAG_DEFAULT_AS_SET_IN_DATABASE = "default";
		public static final String TEMP_IMAGE_FILENAME_PREFIX = "temp_image_";
	}

	public static final class Hints {
		public static final String SIGNUP_USER_NAME_HINT = "First and Last Name (" + (String.valueOf(C.CharacterLimitations.USERNAME_MINIMUM_LENGTH)) + " - "
				+ (String.valueOf(C.CharacterLimitations.USERNAME_MAXIMUM_LENGTH)) + " char)";
		public static final String SIGNUP_USER_EMAIL_HINT = "Email Address (Valid)";
		public static final String SIGNUP_USER_PASSWORD_HINT = "Password (" + (String.valueOf(C.CharacterLimitations.PASSWORD_MINIMUM_LENGTH)) + " - "
				+ (String.valueOf(C.CharacterLimitations.PASSWORD_MAXIMUM_LENGTH)) + " char)";
		public static final String SIGNUP_USER_PASSWORD_AGAIN_HINT = "Password Again";
	}

	public static final class SlidingMenuAndActionBarProperties {
		public static final int SHADOW = 15;
		public static final int BEHIND_OFFSET = 120;
		public static final float FADE_DEGREE = 0.35f;
		public static final String COLOR = "#8A2BE2";
		public static final boolean ACTION_BAR_HOME_BUTTON_ENABLED = true;
		public static final boolean ACTION_BAR_HOME_UP_ENABLED = true;
		public static final boolean ACTION_BAR_DISPLAY_TITLE = false;
		public static final boolean ACTION_BAR_DISPLAY_USE_LOGO = false;
	}

	public static final class CharacterLimitations {
		// TODO add useremail50 and the other stuff like in the db
		public static final int USERNAME_MINIMUM_LENGTH = 3;
		public static final int USERNAME_MAXIMUM_LENGTH = 30;
		public static final int PASSWORD_MINIMUM_LENGTH = 6;
		public static final int PASSWORD_MAXIMUM_LENGTH = 30;
		public static final int USER_QUOTE_MAXIMUM_LENGTH = 70;
	}

	public static final class Fontz {
		public static final String FONT_1 = "retrogirl.ttf";
		public static final String FONT_2 = "pricedown.ttf";
		public static final float FONT_SIZE_WHEN_FONT_UNAVAILABLE = 14;
	}

	public static final class Colorz {
		public static final String TOAST_BACKGROUND = "#8A2BE2";
		public static final String TOAST_TEXT_COLOR = "#FFFFFF";
	}

	public static final class Errorz {

		// internet connectivity
		public static final String NO_INTERNET_CONNECTION = "You need to have internet access in order to use this app";
		public static final String DISCONNECT_STORAGE_FIRST = "Disconnect USB storage in order to be able to use the App. Thanks!";

		// signup
		public static final String FIELD_NOT_FILLED = "Field cannot be blank";
		public static final String PASSWORDS_DO_NOT_MATCH = "Passwords do not match";
		public static final String NOT_ALL_FIELDS_FILLED = "Please fill in all the fields properly:\n\n";
		public static final String EMAIL_NOT_VALID = "Please enter a valid email address";
		public static final String USER_NAME_MIN_LENGTH_PROBLEM = "Username should be at least " + C.CharacterLimitations.USERNAME_MINIMUM_LENGTH
				+ " characters long";
		public static final String USER_NAME_MAX_LENGTH_EXCEEDED = "Username should be not exceed " + C.CharacterLimitations.USERNAME_MAXIMUM_LENGTH
				+ " characters in length";
		public static final String PASSWORD_MIN_LENGTH_PROBLEM = "Password should be at least " + C.CharacterLimitations.PASSWORD_MINIMUM_LENGTH
				+ " characters long";
		public static final String PASSWORD_MAX_LENGTH_EXCEEDED = "Password length should not exceed " + C.CharacterLimitations.PASSWORD_MAXIMUM_LENGTH
				+ " characters";
		public static final String INCORRECT_PASSWORD = "Incorrect Password";
		public static final String SEX_NOT_SELECTED = "Are you male or female?";
		public static final String DEVICE_UNABLE_TO_TAKE_PHOTOS = "Something went wrong. Probably device is unable to take photos";
		public static final String TAKING_PHOTO_FAILED = "Something went wrong after taking a photo. Please try choosing a file from your Gallery";
		public static final String PICKING_PHOTO_FROM_GALLERY_FAILED = "Something went wrong. Please try taking a picture with your camera";
		public static final String USER_NOT_CREATED = "User Not Registered due to DB Problem. Please contact us at " + C.Emailz.BRAGGR_OFFICIAL_ADDRESS;
		public static final String EMAIL_TAKEN = "Sorry, this email address is already taken";
		public static final String SETTINGS_NOT_SAVED = "Settings not saved. Please try again later";
		public static final String NEED_TO_AGREE_TO_TERMS_OF_USE = "You have to agree with our Terms of Use in order to use the app";
		public static final String USER_ACCOUNT_NOT_CREATED = "We were unable to create your account for an unknown reason. Please, excuse us";

		// login
		public static final String ENTER_VALID_EMAIL = "Please enter a valid email address";
		public static final String INCORRECT_EMAIL_PASSWORD_COMBINATION = "Incorrect email password combination";
		public static final String NO_SUCH_EMAIL_IN_DATABASE = "No such email in Database. Check your spelling";
		public static final String FILL_IN_LOGIN_CREDENTIALS = "Please, fill in your login credentials";
		public static final String CONNECTION_ERROR = "Cannot connect to the Database. Please try again later";
		public static final String FACEBOOK_LOGIN_FAILURE = "Could not enter the app via Facebook.";
		public static final String FORGOTTEN_PASSWORD_LINK_NOT_SENT_VIA_EMAIL = "Link to reset password page has not been sent due to a problem. Contact us at "
				+ C.Emailz.BRAGGR_OFFICIAL_ADDRESS;
		
		// settings
		public static final String NOT_ALL_FIELDS_FILLED_SETTINGS = "Please fill in all the fields properly";

		// user profile
		public static final String QUOTE_LENGTH_EXCEEDED = "Quote Length Cannot BeMore Than" + C.CharacterLimitations.USER_QUOTE_MAXIMUM_LENGTH;
		public static final String USER_QUOTE_NOT_CHANGED_BAD_REQUEST_EXCUSE = "Unable to change your quote! This issue will be reported and we will try to fix it soon!";
		public static final String USER_QUOTE_NOT_CHANGED_DB_PROBLEM__EXCUSE = "Unable to change your quote! There is a problem with the server at the moment, please try again later!";
		public static final String USER_QUOTE_NOT_CHANGED_UNKNOWN_PROBLEM_EXCUSE = "Unable to change your quote due to an unknown reason. Sorry.!";
		public static final String ACCOUNT_NOT_DELETED = "We couldn't delete your account for some reason. Please, try again later";
		public static final String LOG_OUT_FAILURE = "Something Went Wrong. Please Try Again Later or Clear Your App's Data from the Task Manager";
		public static final String PROBLEM_LOADING_USER_DATA = "There has been a problem retrieving user details";
		public static final String PROBLEM_LOADING_USER_AVATAR = "There has been a problem retrieving your avatar from the server. Please, excuse us!";
		public static final String PROBLEM_LOADING_USER_ITEMS = "There has been a problem downloading some of your data";

		// loading categories from the server
		public static final String CATEGORIES_LOADING_INTERRUPTED_WHILE_ADDING_A_NEW_ITEM = "Loading Categories Interrupted. Product will be given a default category. You can try to edit your product later and add the right category.";
		public static final String CATEGORIES_LOADING_TIMEOUT = "Loading Categories Seems to take a while. Check your internet connection or try later";
		public static final String CATEGORIES_LOADING_INTERRUPTED_WHILE_SEARCHING_ITEMS = "Loading Categories Interrupted. You might not be able to filter by categories.";
		public static final String CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR = "Categories have not been loaded due to an unkown error. You can add the category at a later stage";

		// adding an item to user collection
		public static final String ADDING_AN_ITEM_SOME_FIELDS_REQUIRED = "Please make sure you've filled all required fields*. Also, provide at least one picture, by tapping the Photo button at the top left-hand corner.";
		public static final String ADDING_AN_ITEM_AT_LEAST_ONE_IMAGE_REQUIRED = "When adding an item, you must provide at least one image of it.";
		public static final String ITEM_NOT_ADDED_BAD_REQUEST_EXCUSE = "Item not added due to an error on our side. The issue has been automatically reported to our team. Sorry for the inconvenience.";
		public static final String ITEM_NOT_ADDED_DB_PROBLEM_EXCUSE = "Item not added due to a database problem. The issue has been automatically reported to our team. Sorry for the inconvenience.";
		public static final String ITEM_NOT_ADDED_UNKNOWN_PROBLEM_EXCUSE = "Item not added due to an unknown problem. The issue has been automatically reported to our team. Sorry for the inconvenience.";

		// Contact Us
		public static final String NO_MESSAGE_ENTERED = "You havent entered a message";
		public static final String MESSAGE_NOT_SENT = "We were unable to send your message. Please check your connection or try sending it from the website";
		
		// User  relations 
		public static final String FRIENDSHIP_REQUEST_NOT_SENT = "There has been a problem sending your friend request. Please try again later";
		public static final String FRIEND_NOT_REMOVED = "Friend not removed. Try later";
		public static final String FRIEND_REQUEST_NOT_CANCELLED = "We were unable to cancel the friend request. Try Later.";
		public static final String FRIEND_REQUEST_NOT_ACCEPTED = "A problem occurred. Please Try later";
		public static final String FRIEND_REQUEST_NOT_REJECTED = "A problem occurred. Please Try later";
		
		// Miscellaneous
		public static final String IMAGE_TOO_LARGE = "Image is too large, the system cannot handle it";
		public static final String APP_CRASHED = "Oops. Restarting the app...";
	
	} // End of Errorz

	public static final class Confirmationz {
		public static final String FORGOTTEN_PASSWORD_RESET_LINK_SENT_VIA_MAIL = "A Link to the password reset page has been sent to your email.";
		public static final String SETTINGS_SAVED = "Settings successfully saved";
		public static final String ACCOUNT_DELETED = "Your account has been successfully deleted. We're sorry to see you leave!";
		public static final String ARE_YOU_SURE_YOU_WANT_TO_DELETE_YOUR_ACCOUNT = "Are you sure you want to delete your account? This action is irreversible!  If you are sure you want to do this, please type in your password first.";
		public static final String CHANGE_YOUR_USER_QUOTE = "Type in your new user quote of maximum " + C.CharacterLimitations.USER_QUOTE_MAXIMUM_LENGTH
				+ " characters";
		public static final String USER_QUOTE_SUCCESSFULLY_CHANGED = "User Quote Successfully Changed!";
		public static final String DONT_FORGET_TO_SAVE_SETTINGS = "Don't forget to Save Settings Changed!";
		public static final String INQUIRY_SENT = "Your Inquiry has been Sent to Our Team";
		public static final String FRIEND_REQUEST_CANCELLED = "Friend Request Cancelled";
		public static final String FRIEND_REQUEST_SENT = "Friend Request Sent";
		public static final String FRIEND_REMOVED = "You are no longer friends";
		public static final String FRIEND_REQUEST_ACCEPTED = "You are now friends!";
		public static final String FRIEND_REQUEST_REJECTED = "Friend Request Rejected";

	}

	public static final class LoadingMessages {
		public static final String ADDING_NEW_ITEM_TO_USER_COLLECTION = "Adding the item to your collection";
		public static final String CHECKING_WHETHER_EMAIL_IS_TAKEN = "Checking whether this email is free to use";
		public static final String LOADING_CATEGORIES = "Loading categories";
		public static final String LOADING_SUBCATEGORIES = "Loading subcategories";
		public static final String LOADING_USER_ITEMS = "Loading your items";
		public static final String SENDING_MESSAGE = "Sending message";
		public static final String LOADING_ITEM_DATA = "Loading Item Data";
		public static final String LOADING_USER_DATA = "Loading User Data";
		public static final String REMOVING_FRIEND = "Removing friend";
		public static final String FRIEND_REQUEST_REJECTING = "Rejecting Friend Request";
		public static final String FRIEND_REQUEST_SENDING = "Sending Friend Request";
		public static final String FRIEND_REQUEST_ACCEPTING = "Accepting Friend Request";
		public static final String FRIEND_REQUEST_CANCELLING = "Cancelling Friend Request";
	}

	public static final class PointsEarnedToasts {
		public static final String POINTS_EARNED_TOAST() {
			String toastText = "Points give you the opportunity to earn public status and privilleges. " + "Learn more at the Points and Statuses menu.";
			return toastText;
		}

		public static final String TOAST_FOR_EARNING_POINTS_FOR_GIVING_AWAY_ITEM_ABOVE_100_BUCKS() {
			String toast = "Great, you earned " + C.PointsForActions.GIVING_AWAY_ITEM_ABOVE_100_BUCKS + "points for giving away an item priced above $100. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_ADDING_A_PLACE() {
			String toast = "Great, you earned " + C.PointsForActions.ADDING_A_PLACE + "points for adding a place to Braggr. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_BUYING_A_PRODUCT() {
			String toast = "Great, you earned " + C.PointsForActions.BUYING_A_PRODUCT + "points for bragging about the product you just got. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_BECOMING_FRIENDS() {
			String toast = "Great, you earned " + C.PointsForActions.BECOMING_FRIENDS + "points for adding a new friend on Braggr. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_GIVING_AWAY_ITEM_BETWEEN_10_AND_100_BUCKS() {
			String toast = "Great, you earned " + C.PointsForActions.GIVING_AWAY_ITEM_BETWEEN_10_AND_100_BUCKS
					+ "points for giving away an item priced between $10 and $100. " + C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_CONTRIBUTING_TO_PLACE_PROFILE() {
			String toast = "Great, you earned " + C.PointsForActions.CONTRIBUTING_TO_PLACE_PROFILE + "points for contributing to this place's profile info. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_GIVING_AWAY_ITEM_BELOW_10_BUCKS() {
			String toast = "Great, you earned " + C.PointsForActions.GIVING_AWAY_ITEM_BELOW_10_BUCKS + "points for giving away an item priced below $10. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_FOLLOWING_A_PRODUCT() {
			String toast = "Great, you earned " + C.PointsForActions.FOLLOWING_A_PRODUCT + "points for following this product on braggr. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_UPLOADING_A_PRODUCT_PICTURE() {
			String toast = "Great, you earned " + C.PointsForActions.UPLOADING_A_PRODUCT_PICTURE + "points for uploading a picture for this product. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_COMMENTING_SOMEONES_PRODUCT() {
			String toast = "Great, you earned " + C.PointsForActions.COMMENTING_SOMEONES_PRODUCT + "points for commenting this product. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_ENVYING_SOMEONES_PURCHASE() {
			String toast = "Great, you earned " + C.PointsForActions.ENVYING_SOMEONES_PURCHASE
					+ "points for envying this person's purchase. You just made them feel better. " + C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

		public static final String TOAST_FOR_RATING_A_PRODUCT() {
			String toast = "Great, you earned " + C.PointsForActions.RATING_A_PRODUCT + "points for rating this product. "
					+ C.PointsEarnedToasts.POINTS_EARNED_TOAST();
			return toast;
		}

	} // End of PointsEarnedToasts

} // End of C.java

package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.*;
import net.shiftinpower.objects.UserExtended;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This is a very important class - it is a part of the InitialDataloader sequence of asynctasks carried out right after
 * login/signup.
 * 
 * It downloads the user data from the server and stores it in a POJO (Plain old java object) In a future version of the app,
 * this class will be used to get other users information
 * 
 * 
 * @author Kaloyan Roussev
 * 
 * 
 * This class is also user for obtaining another user's data from the server so we also have "anotherUserId" field so we can check whether the user we are looking
 * info for is a friend of the current user that is now using the app
 * 
 */

public class GetUserDataFromServerAsync extends AsyncTask<String, Integer, UserExtended> {

	private String userId;
	private String anotherUserId;
	private JSONParser jsonParser = new JSONParser();
	private boolean loadingMessageShown;
	private Context context;
	private OnGetUserDataFromServerListener listener;
	private String reason;

	public GetUserDataFromServerAsync(String userId, String anotherUserId, Context context, OnGetUserDataFromServerListener listener) {
		this.userId = userId;
		this.anotherUserId = anotherUserId;
		this.listener = listener;
		this.context = context;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		if (context != null) {
			ShowLoadingMessage.loading(context, C.LoadingMessages.LOADING_USER_DATA);
			loadingMessageShown = true;
		}

	}

	@Override
	protected UserExtended doInBackground(String... params) {
		int serverResponseCode;

		try {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair(C.DBColumns.USER_ID, userId));
			parameters.add(new BasicNameValuePair(C.DBColumns.ANOTHER_USER_ID, anotherUserId));

			final JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_USER_DATA, "GET", parameters);

			serverResponseCode = json.getInt(C.DBColumns.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				UserExtended userExtended = new UserExtended();

				// User data found
				userExtended.setUserLastSeen(json.getString(C.DBColumns.USER_LAST_SEEN));
				userExtended.setUserDateRegistered(json.getString(C.DBColumns.USER_DATE_REGISTERED));
				userExtended.setUserAvatarPath(json.getString(C.DBColumns.USER_AVATAR));
				userExtended.setUserSex(json.getString(C.DBColumns.USER_SEX));
				userExtended.setUserPassword(json.getString(C.DBColumns.USER_PASSWORD));
				userExtended.setUserEmail(json.getString(C.DBColumns.USER_EMAIL));
				userExtended.setUserName(json.getString(C.DBColumns.USER_NAME));
				userExtended.setUserPoints(Integer.parseInt(json.getString(C.DBColumns.USER_POINTS)));
				userExtended.setUserQuote(json.getString(C.DBColumns.USER_QUOTE));
				userExtended.setUserAcceptsMessages(json.getString(C.DBColumns.USER_ACCEPT_MESSAGES));
				userExtended.setUserInteractsWithActivities(json.getString(C.DBColumns.USER_INTERACT_WITH_ACTIVITIES));
				userExtended.setUserItemsCount(json.getInt(C.DBColumns.USER_ITEMS_COUNT));
				userExtended.setUserCommentsCount(json.getInt(C.DBColumns.USER_REVIEWS_COUNT));
				userExtended.setUserFollowingItemsCount(json.getInt(C.DBColumns.USER_FOLLOWING_ITEMS_COUNT));
				userExtended.setUserFriendsCount(json.getInt(C.DBColumns.USER_FRIENDS_COUNT));
				userExtended.setUserGalleryPhotosCount(json.getInt(C.DBColumns.USER_GALLERY_PHOTOS_COUNT));
				userExtended.setUserActivityCount(json.getInt(C.DBColumns.USER_ACTIVITIY_COUNT));
				userExtended.setUserMoneySpentOnItems(json.getDouble(C.DBColumns.USER_MONEY_SPENT_ON_ITEMS));
				
				if (json.getString(C.DBColumns.USER_IS_FRIEND_OF_CURRENT_USER).contentEquals("1")) {

					userExtended.setUserIsFriendsWithCurrentUser(true);

				} else if (json.getString(C.DBColumns.USER_IS_FRIEND_OF_CURRENT_USER).contentEquals("0")) {

					userExtended.setUserIsFriendsWithCurrentUser(false);

				} else {
					
					userExtended.setUserIsFriendsWithCurrentUser(false);
					
				}

				if (json.getString(C.DBColumns.WE_SENT_THEM_FRIEND_REQUEST).contentEquals("1")) {

					userExtended.setUserHasFriendRequestFromUs(true);

				} else if (json.getString(C.DBColumns.WE_SENT_THEM_FRIEND_REQUEST).contentEquals("0")) {

					userExtended.setUserHasFriendRequestFromUs(false);

				} else {
					
					userExtended.setUserHasFriendRequestFromUs(false);
					
				}
				
				if (json.getString(C.DBColumns.THEY_SENT_US_FRIEND_REQUEST).contentEquals("1")) {

					userExtended.setUserHasSentUsFriendRequest(true);

				} else if (json.getString(C.DBColumns.THEY_SENT_US_FRIEND_REQUEST).contentEquals("0")) {

					userExtended.setUserHasSentUsFriendRequest(false);

				} else {
					
					userExtended.setUserHasSentUsFriendRequest(false);
					
				}
				
				if ((json.getString(C.DBColumns.USER_SHOWS_MONEY)).contentEquals("1")) {

					userExtended.setUserShowsMoney(true);

				} else if ((json.getString(C.DBColumns.USER_SHOWS_MONEY)).contentEquals("0")) {

					userExtended.setUserShowsMoney(false);

				}

				if ((json.getString(C.DBColumns.USER_SHOWS_STATS)).contentEquals("1")) {

					userExtended.setUserShowsStats(true);

				} else if ((json.getString(C.DBColumns.USER_SHOWS_STATS)).contentEquals("0")) {

					userExtended.setUserShowsStats(false);

				}

				return userExtended;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {
				setReason(C.Tagz.BAD_REQUEST);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {
				setReason(C.Tagz.DB_PROBLEM);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_NOT_FOUND) {
				setReason(C.Tagz.NOT_FOUND);
				return null;
			} else {
				setReason(C.Tagz.UNKNOWN_PROBLEM);
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			setReason(C.Tagz.BAD_REQUEST);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(UserExtended userExtended) {
		super.onPostExecute(userExtended);

		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (userExtended == null) {

				listener.onGetUserDataFromServerFailure(reason);
			} else {

				listener.onGetUserDataFromServerSuccess(userExtended);
			}
		}
	} // End of onPostExecute

} // End of Class
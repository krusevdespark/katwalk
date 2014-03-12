package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.*;
import net.shiftinpower.objects.UserExtended;
import net.shiftinpower.utilities.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class DownloadUserInfoFromServerAsync {

	private String userId;
	int currentlyLoggedInUser;
	private JSONParser jsonParser = new JSONParser();

	private String userName;
	private String userEmail;
	private String userSex;
	private String userPassword;
	private String userAvatarPath;
	private String userDateRegistered;
	private String userLastSeen;
	private int userItemsCount;
	private int userReviewsCount;
	private int userFollowingItemsCount;
	private int userFriendsCount;
	private int userGalleryPhotosCount;
	private int userActivityCount;
	private int userPoints;
	private String userQuote;
	private boolean userShowsMoney = true;
	private boolean userShowsStats = true;
	private String userAcceptsMessages = C.Miscellaneous.USER_RESTRICTION_LEVEL_NO;
	private String userInteractsWithActivities = C.Miscellaneous.USER_RESTRICTION_LEVEL_NO;
	private double userMoneySpentOnItems;
	private OnDownloadUserInfoFromServerListener listener;
	private String reason;

	public DownloadUserInfoFromServerAsync(String userId, OnDownloadUserInfoFromServerListener listener) {
		this.userId = userId;
		this.listener = listener;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	private void setUserName(String userName) {
		this.userName = userName;
	}

	private void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	private void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	private void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	private void setUserAvatarPath(String userAvatarPath) {
		this.userAvatarPath = userAvatarPath;
	}

	private void setUserDateRegistered(String userDateRegistered) {
		this.userDateRegistered = userDateRegistered;
	}

	private void setUserLastSeen(String userLastSeen) {
		this.userLastSeen = userLastSeen;
	}

	public void setUserShowsMoney(boolean userShowsMoney) {
		this.userShowsMoney = userShowsMoney;
	}

	public void setUserShowsStats(boolean userShowsStats) {
		this.userShowsStats = userShowsStats;
	}

	public void setUserAcceptsMessages(String userAcceptsMessages) {
		this.userAcceptsMessages = userAcceptsMessages;
	}

	public void setUserInteractsWithActivities(String userInteractsWithActivities) {
		this.userInteractsWithActivities = userInteractsWithActivities;
	}

	public void setUserPoints(int userPoints) {
		this.userPoints = userPoints;
	}

	public void setUserQuote(String userQuote) {
		this.userQuote = userQuote;
	}

	public void setUserItemsCount(int userItemsCount) {
		this.userItemsCount = userItemsCount;
	}

	public void setUserReviewsCount(int userReviewsCount) {
		this.userReviewsCount = userReviewsCount;
	}

	public void setUserFollowingItemsCount(int userFollowingItemsCount) {
		this.userFollowingItemsCount = userFollowingItemsCount;
	}

	public void setUserFriendsCount(int userFriendsCount) {
		this.userFriendsCount = userFriendsCount;
	}

	public void setUserGalleryPhotosCount(int userGalleryPhotosCount) {
		this.userGalleryPhotosCount = userGalleryPhotosCount;
	}

	public void setUserActivityCount(int userActivityCount) {
		this.userActivityCount = userActivityCount;
	}
	
	public void setUserMoneySpentOnItems(double userMoneySpentOnItems){
		this.userMoneySpentOnItems = userMoneySpentOnItems;
	}

	public void downloadUserDetailsAndStats() {
		new DownloaderTask().execute();
	}

	private class DownloaderTask extends AsyncTask<String, Integer, UserExtended> {

		@Override
		protected UserExtended doInBackground(String... params) {
			int serverResponseCode;

			try {
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add(new BasicNameValuePair("user_id", userId));

				final JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_USER_DATA, "GET", parameters);

				serverResponseCode = json.getInt(C.DBColumns.SUCCESS);

				if (serverResponseCode == C.HttpResponses.SUCCESS) {

					// User data found
					setUserLastSeen(json.getString(C.DBColumns.USER_LAST_SEEN));
					setUserDateRegistered(json.getString(C.DBColumns.USER_DATE_REGISTERED));
					setUserAvatarPath(json.getString(C.DBColumns.USER_AVATAR));
					setUserSex(json.getString(C.DBColumns.USER_SEX));
					setUserPassword(json.getString(C.DBColumns.USER_PASSWORD));
					setUserEmail(json.getString(C.DBColumns.USER_EMAIL));
					setUserName(json.getString(C.DBColumns.USER_NAME));
					setUserPoints(Integer.parseInt(json.getString(C.DBColumns.USER_POINTS)));
					setUserQuote(json.getString(C.DBColumns.USER_QUOTE));
					setUserAcceptsMessages(json.getString(C.DBColumns.USER_ACCEPT_MESSAGES));
					setUserInteractsWithActivities(json.getString(C.DBColumns.USER_INTERACT_WITH_ACTIVITIES));
					setUserItemsCount(json.getInt(C.DBColumns.USER_ITEMS_COUNT));
					setUserReviewsCount(json.getInt(C.DBColumns.USER_REVIEWS_COUNT));
					setUserFollowingItemsCount(json.getInt(C.DBColumns.USER_FOLLOWING_ITEMS_COUNT));
					setUserFriendsCount(json.getInt(C.DBColumns.USER_FRIENDS_COUNT));
					setUserGalleryPhotosCount(json.getInt(C.DBColumns.USER_GALLERY_PHOTOS_COUNT));
					setUserActivityCount(json.getInt(C.DBColumns.USER_ACTIVITIY_COUNT));
					setUserMoneySpentOnItems(json.getDouble(C.DBColumns.USER_MONEY_SPENT_ON_ITEMS));

					if ((json.getString(C.DBColumns.USER_SHOWS_MONEY)).contentEquals("1")) {

						setUserShowsMoney(true);

					} else if ((json.getString(C.DBColumns.USER_SHOWS_MONEY)).contentEquals("0")) {

						setUserShowsMoney(false);

					}

					if ((json.getString(C.DBColumns.USER_SHOWS_STATS)).contentEquals("1")) {

						setUserShowsStats(true);

					} else if ((json.getString(C.DBColumns.USER_SHOWS_STATS)).contentEquals("0")) {

						setUserShowsStats(false);

					}

					UserExtended userDetailsAndStats = new UserExtended(userName, userEmail, userSex, userPassword, userLastSeen, userAvatarPath, userDateRegistered, userQuote, userPoints, userShowsMoney, userShowsStats, userAcceptsMessages,
							userInteractsWithActivities, userItemsCount, userReviewsCount, userFollowingItemsCount, userFriendsCount, userGalleryPhotosCount, userActivityCount, userMoneySpentOnItems);
					return userDetailsAndStats;
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
		protected void onPostExecute(UserExtended userDetailsAndStats) {
			if (listener != null) {
				if (userDetailsAndStats == null) {

					listener.onDownloadUserInfoFromServerFailure(reason);
				} else {

					listener.onDownloadUserInfoFromServerSuccess(userDetailsAndStats);
				}
			}
		} // End of onPostExecute

	} // End of DownloaderTask
	
} // End of Class 

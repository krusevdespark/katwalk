package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetUserFriendsListener;
import net.shiftinpower.interfaces.OnGetUserItemsListener;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.objects.UserBasic;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.os.AsyncTask;

/**
* This asynctask is part of the InitialDataloader sequence of tasks obtaining vital information from the server It gets the
* basic sets of data for the user items. Extended item data is obtained using the GetItemDataFromServerAsync.class for each
* item
*
* @author Kaloyan Roussev
*
*/

public class GetUserFriendsFromServerAsync extends AsyncTask<String, String, LinkedHashSet<UserBasic>> {

	private int serverResponseCode;
	private String reason;
	private int userId;
	private Context context;
	private OnGetUserFriendsListener listener;
	private JSONParser jsonParser = new JSONParser();
	private boolean loadingMessageShown;

	public GetUserFriendsFromServerAsync(Context context, int userId, OnGetUserFriendsListener listener) {
		this.userId = userId;
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
			ShowLoadingMessage.loading(context, C.LoadingMessages.LOADING_USER_FRIENDS);
			loadingMessageShown = true;
		}

	}

	@Override
	protected LinkedHashSet<UserBasic> doInBackground(String... params) {

		try {

			List<NameValuePair> getUserFriends = new ArrayList<NameValuePair>();
			getUserFriends.add(new BasicNameValuePair(C.DBColumns.USER_ID, String.valueOf(userId)));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_USER_FRIENDS, "GET", getUserFriends);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				LinkedHashSet<UserBasic> userFriends = new LinkedHashSet<UserBasic>();
				JSONArray userFriendsJSONArray = json.getJSONArray(C.DBColumns.USER_FRIENDS);

				int userFriendsCount = userFriendsJSONArray.length();

				for (int i = 0; i < userFriendsCount; i++) {
					UserBasic userFriend = new UserBasic();
					JSONObject userFriendJSONFormat = userFriendsJSONArray.getJSONObject(i);

					userFriend.setUserId(userFriendJSONFormat.getInt(C.DBColumns.USER_ID));
					userFriend.setUserAvatar(userFriendJSONFormat.getString(C.DBColumns.USER_AVATAR));
					userFriend.setUserName(userFriendJSONFormat.getString(C.DBColumns.USER_NAME));
					userFriend.setUserSex(userFriendJSONFormat.getString(C.DBColumns.USER_SEX));

					userFriends.add(userFriend);

				} // End of Loop

				return userFriends;

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
	protected void onPostExecute(LinkedHashSet<UserBasic> userFriends) {

		super.onPostExecute(userFriends);

		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (userFriends != null) {
				listener.onGetUserFriendsSuccess(userFriends);
			} else if (reason != null) {
				listener.onGetUserFriendsFailure();
			}
		}

	} // End of onPostExecute

} // End of Class
package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnUserSettingsChangedListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

public class SaveUserSettingsOnServerAsync extends AsyncTask<String, String, String> {
	
	int serverResponseCode;
	private Context context;
	private OnUserSettingsChangedListener listener;
	private JSONParser jsonParser = new JSONParser();
	private boolean userShowsMoney;
	private boolean userShowsStats;
	private String userName;
	private String userSex;
	private String userPassword;
	private String userQuote;
	private String userId;
	private String userAcceptsMessages;
	private String userInteractsWithActivities;

	private String reason;

	public SaveUserSettingsOnServerAsync(Context context, OnUserSettingsChangedListener listener, String userId, boolean userShowsMoney, boolean userShowsStats, String userAcceptsMessages, String userInteractsWithActivities, String userName, String userQuote, String userSex, String userPassword) {
		this.context = context;
		this.listener = listener;
		this.userId = userId;
		this.userShowsMoney = userShowsMoney;
		this.userShowsStats = userShowsStats;
		this.userAcceptsMessages = userAcceptsMessages;
		this.userInteractsWithActivities = userInteractsWithActivities;
		this.userName = userName;
		this.userQuote = userQuote;
		this.userSex = userSex;
		this.userPassword = userPassword;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected void onPreExecute() {
		ShowLoadingMessage.loading(context);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... args) {
		int success;

		try {
			List<NameValuePair> saveUserSettings = new ArrayList<NameValuePair>();
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_ID, userId));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_SHOWS_MONEY, String.valueOf(userShowsMoney)));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_SHOWS_STATS, String.valueOf(userShowsStats)));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_ACCEPT_MESSAGES, userAcceptsMessages));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_INTERACT_WITH_ACTIVITIES, userInteractsWithActivities));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_NAME, userName));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_QUOTE, userQuote));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_SEX, userSex));
			saveUserSettings.add(new BasicNameValuePair(C.DBColumns.USER_PASSWORD, userPassword));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.SAVE_USER_SETTINGS, "POST", saveUserSettings);

			success = json.getInt(C.Tagz.SUCCESS);

			// TODO this block of code repeats throughout the asynctask classes. extract it to a class that these classes should inherit from
			if (success == C.HttpResponses.SUCCESS) {
				return C.Tagz.SUCCESS;
			} else if (success == C.HttpResponses.FAILURE_BAD_REQUEST) {
				setReason(C.Tagz.BAD_REQUEST);
				return null;
			} else if (success == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {
				setReason(C.Tagz.DB_PROBLEM);
				return null;
			} else if (success == C.HttpResponses.FAILURE_NOT_FOUND) {
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
	} // End of doInBackgrond

	@Override
	protected void onPostExecute(String result) {
		
		ShowLoadingMessage.dismissDialog();
		if (listener != null) {
			if (result.contentEquals(C.Tagz.SUCCESS)) {
				listener.onUserSettingsChangedSuccess();
			} else if (reason != null) {
				listener.onUserSettingsChangedFailure(reason);
			}
		}
		super.onPostExecute(result);
	} // End of onPostExecute

} // End of Class
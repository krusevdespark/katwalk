package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnFacebookUserRegisteredListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

public class RegisterFacebookUserOnServerAsync extends AsyncTask<String, String, Integer> {

	private Context context;
	private String userName;
	private String userEmail;
	private String userSex;
	private String userAvatarPath;

	private JSONParser jsonParser = new JSONParser();
	private static final String TAG_MESSAGE = "message";
	private String userIdString;
	private int currentLoggedInUserId;
	private OnFacebookUserRegisteredListener listener;
	private String reason;

	public RegisterFacebookUserOnServerAsync(Context context, OnFacebookUserRegisteredListener listener, String userName, String userEmail, String userSex, String userAvatarPath) {
		this.context = context;
		this.listener = listener;
		this.userName = userName;
		this.userEmail = userEmail;
		this.userSex = userSex;
		this.userAvatarPath = userAvatarPath;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected void onPreExecute() {
		ShowLoadingMessage.loading(context, "Registering User");
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(String... args) {
		
		int serverResponseCode;
		
		try {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(C.DBColumns.USER_NAME, userName));
			params.add(new BasicNameValuePair(C.DBColumns.USER_EMAIL, userEmail));
			params.add(new BasicNameValuePair(C.DBColumns.USER_SEX, userSex));
			params.add(new BasicNameValuePair(C.DBColumns.USER_AVATAR, userAvatarPath));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.REGISTER_FACEBOOK_USER, "POST", params);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				userIdString = json.getString(TAG_MESSAGE);
				currentLoggedInUserId = Integer.parseInt(userIdString);
				return currentLoggedInUserId;
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
	protected void onPostExecute(Integer result) {
		ShowLoadingMessage.dismissDialog();
		if (listener != null) {
			if (result != null) {
				listener.OnFacebookUserRegisteredSuccess(result);

			} else {
				listener.OnFacebookUserRegisteredFailure(); // TODO send reason
			}
		}
		super.onPostExecute(result);
	}

} // End of Class
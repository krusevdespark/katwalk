package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnUserCreatedListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

public class RegisterUserOnServerAsync extends AsyncTask<String, String, Integer> {

	private Context context;
	private String userName;
	private String userPasswordHashed;
	private String userEmail;
	private String userSex;
	private String userAvatarPath;
	private JSONParser jsonParser = new JSONParser();
	private String userIdString;
	private int currentLoggedInUserId;
	private OnUserCreatedListener listener;

	public RegisterUserOnServerAsync(Context context, OnUserCreatedListener listener, String userName, String userPasswordHashed, String userEmail, String userSex, String userAvatarPath) {
		this.context = context;
		this.listener = listener;
		this.userName = userName;
		this.userPasswordHashed = userPasswordHashed;
		this.userEmail = userEmail;
		this.userSex = userSex;
		this.userAvatarPath = userAvatarPath;
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
			params.add(new BasicNameValuePair(C.DBColumns.USER_PASSWORD, userPasswordHashed));
			params.add(new BasicNameValuePair(C.DBColumns.USER_EMAIL, userEmail));
			params.add(new BasicNameValuePair(C.DBColumns.USER_SEX, userSex));
			params.add(new BasicNameValuePair(C.DBColumns.USER_AVATAR, userAvatarPath));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.REGISTER_USER, "POST", params);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				userIdString = json.getString(C.DBColumns.MESSAGE);
				currentLoggedInUserId = Integer.parseInt(userIdString);
				return currentLoggedInUserId;
			} else {				
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(Integer result) { 
		ShowLoadingMessage.dismissDialog();
		if (listener != null) {
			if (result != null) {
				listener.onUserCreated(result);
			} else {
				listener.onUserNotCreated();
			}
		}
		super.onPostExecute(result);
	}
	
} // End of Class
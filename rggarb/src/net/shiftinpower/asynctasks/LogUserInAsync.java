package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnUserLoginAttemptListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

public class LogUserInAsync extends AsyncTask<String, String, String> {
	
	private int serverResponseCode;
	private Context context;
	private String userEmailLoginPage;
	private String userPasswordLoginPageHashed;
	private JSONParser jsonParser = new JSONParser();
	protected static Integer currentLoggedInUserId;
	protected String userIdString;
	private OnUserLoginAttemptListener listener;
	private String reason;
	
	public LogUserInAsync(Context context, OnUserLoginAttemptListener listener, String userEmailLoginPage, String userPasswordLoginPageHashed) {
		this.context = context;
		this.listener = listener;
		this.userEmailLoginPage = userEmailLoginPage;
		this.userPasswordLoginPageHashed = userPasswordLoginPageHashed;
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
	protected String doInBackground(String... params) {
		try {

			// Send email and pass to db and check them
			List<NameValuePair> loginUser = new ArrayList<NameValuePair>();
			loginUser.add(new BasicNameValuePair(C.DBColumns.USER_EMAIL, userEmailLoginPage));
			loginUser.add(new BasicNameValuePair(C.DBColumns.USER_PASSWORD, userPasswordLoginPageHashed));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.LOGIN_USER, "POST", loginUser);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				userIdString = json.getString(C.DBColumns.USER_ID);
				currentLoggedInUserId = Integer.parseInt(userIdString);
				return C.Tagz.SUCCESS;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_FORBIDDEN) {
				setReason(C.Tagz.INVALID_CREDENTIALS);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {
				setReason(C.Tagz.BAD_REQUEST);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {
				setReason(C.Tagz.DB_PROBLEM);
				return null;
			} else {
				setReason(C.Tagz.UNKNOWN_PROBLEM);
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;				
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(String result) {
		ShowLoadingMessage.dismissDialog();

		if (listener != null) {
			if ((result!=null) && result.contentEquals(C.Tagz.SUCCESS)) { //TODO add (result!=null) in the if
				listener.onUserLoginAttemptSuccess(currentLoggedInUserId);
			} else if (reason!=null) {
				listener.onUserLoginAttemptFailure(reason);
			}
		}
		super.onPostExecute(result);
		
	} // End of onPostExecute

} // End of Class
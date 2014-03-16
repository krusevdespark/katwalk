package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnForgottenPasswordEmailSentListener;
import net.shiftinpower.utilities.EmailSender;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

/**
* We would like to call the user by name when we are sending them a Reset Password email
*
* @author Kaloyan Roussev
*
*/

public class ForgottenPasswordGetUserNameAndSendEmail extends AsyncTask<String, String, String> {

	private int serverResponseCode;
	private Context context;
	private OnForgottenPasswordEmailSentListener listener;
	private String userWithForgottenPasswordsEmailAddress;
	private JSONParser jsonParser = new JSONParser();
	private String userWithSuchForgottenPasswordUserName;
	private String reason;

	public ForgottenPasswordGetUserNameAndSendEmail(Context context, OnForgottenPasswordEmailSentListener listener, String userWithForgottenPasswordsEmailAddress) {
		this.context = context;
		this.listener = listener;
		this.userWithForgottenPasswordsEmailAddress = userWithForgottenPasswordsEmailAddress;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		ShowLoadingMessage.loading(context);
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			List<NameValuePair> paramz1 = new ArrayList<NameValuePair>();
			paramz1.add(new BasicNameValuePair(C.DBColumns.USER_EMAIL, userWithForgottenPasswordsEmailAddress));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_USER_EMAIL_AND_NAME, "POST", paramz1);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);
			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				userWithSuchForgottenPasswordUserName = json.getString(C.DBColumns.USER_NAME);
				return C.Tagz.SUCCESS;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_NOT_FOUND) {
				setReason(C.Tagz.NOT_FOUND);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {
				setReason(C.Tagz.DB_PROBLEM);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {
				setReason(C.Tagz.BAD_REQUEST);
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
		
		super.onPostExecute(result);
		
		ShowLoadingMessage.dismissDialog();
		if (listener != null) {
			if (result != null) {
				if (result.contentEquals(C.Tagz.SUCCESS)) {
					try {
						new EmailSender(userWithForgottenPasswordsEmailAddress, C.Emailz.FORGOTTEN_PASSWORD_RESET_ADDRESS, C.Emailz.FORGOTTEN_PASSWORD_RESET_SUBJECT, C.Emailz.FORGOTTEN_PASSWORD_RESET_BODY(userWithSuchForgottenPasswordUserName), null)
								.execute();
						listener.onForgottenPasswordEmailSentSuccess();
					} catch (Exception e) {
						e.printStackTrace();
						listener.onForgottenPasswordEmailSentFailure(C.Tagz.MAILER_PROBLEM);
					}
				} else if (reason != null) {
					listener.onForgottenPasswordEmailSentFailure(reason);
				}
			} else {
				listener.onForgottenPasswordEmailSentFailure(reason);
			}
		}
	} // End of onPostExecute

} // End of Class
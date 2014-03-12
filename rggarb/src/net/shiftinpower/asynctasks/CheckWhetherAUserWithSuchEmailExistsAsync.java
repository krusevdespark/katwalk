package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnCheckWhetherAUserWithSuchEmailExistsListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class CheckWhetherAUserWithSuchEmailExistsAsync extends AsyncTask<String, Void, Boolean>{
	private JSONParser jsonParser = new JSONParser();
	private String userEmail;
	private OnCheckWhetherAUserWithSuchEmailExistsListener listener;
	private Context context;
	private boolean emailHasAlreadyBeenChecked;
	
	public CheckWhetherAUserWithSuchEmailExistsAsync(Context context, String userEmail, OnCheckWhetherAUserWithSuchEmailExistsListener listener, boolean emailHasAlreadyBeenChecked) {
		this.userEmail = userEmail;
		this.context = context;
		this.listener = listener;
		this.emailHasAlreadyBeenChecked = emailHasAlreadyBeenChecked;
	}

	@Override
	protected void onPreExecute() {
		ShowLoadingMessage.loading(context, C.LoadingMessages.CHECKING_WHETHER_EMAIL_IS_TAKEN);
		super.onPreExecute();
	}
	
	@Override
	protected Boolean doInBackground(String... args) {
		
		int serverResponseCode;
		
		if(emailHasAlreadyBeenChecked) {
			
			return true;
			
		} else {
			
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(C.DBColumns.USER_EMAIL, userEmail));

				JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.CHECK_IF_USER_WITH_SAME_EMAIL_EXISTS, "GET", params);

				serverResponseCode = json.getInt(C.Tagz.SUCCESS);
				if (serverResponseCode == C.HttpResponses.SUCCESS) { //SUCCESS in this case means that there is NO user with such email
					return true;
				} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {
					return false;
				} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {
					return false;
				} else {
					return false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;				
			}
			
		}
		
	} // End of doInBackground

	@Override
	protected void onPostExecute(Boolean result) {
		ShowLoadingMessage.dismissDialog();
		if(listener != null) {
			listener.onCheckWhetherAUserWithSuchEmailExistsChecked(result);
		}
		super.onPostExecute(result);
	}

} // End of Class
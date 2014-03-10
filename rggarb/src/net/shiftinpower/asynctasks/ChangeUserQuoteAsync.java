package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnChangeUserQuoteListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

public	class ChangeUserQuoteAsync extends AsyncTask<String, String, String> {
	int success;
	private Context context;
	private OnChangeUserQuoteListener listener;
	private String userId;
	private String userQuote;
	private JSONParser jsonParser = new JSONParser();
	private String reason;
	
	public ChangeUserQuoteAsync(Context context, OnChangeUserQuoteListener listener, String userId, String userQuote) {
		this.context = context;
		this.listener = listener;
		this.userId = userId;
		this.userQuote = userQuote;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Override
	protected void onPreExecute() {
		ShowLoadingMessage.loading(context);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... args) {
		int serverResponseCode;

		try {
			List<NameValuePair> changeQuoteAttempt = new ArrayList<NameValuePair>();
			changeQuoteAttempt.add(new BasicNameValuePair(C.DBColumns.USER_ID, userId));
			changeQuoteAttempt.add(new BasicNameValuePair(C.DBColumns.USER_QUOTE, userQuote));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.SET_USER_QUOTE, "POST", changeQuoteAttempt);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				return C.Tagz.SUCCESS;
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
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;	
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(String result) {
		ShowLoadingMessage.dismissDialog();
		if(listener!=null) {
			if(result.contentEquals(C.Tagz.SUCCESS)) {
				listener.onChangeUserQuoteSuccess();
			} else {
				listener.onChangeUserQuoteFailure(reason);
			}
		}
		super.onPostExecute(result);
		
	} // End of onPostExecute
	
} // End of Class
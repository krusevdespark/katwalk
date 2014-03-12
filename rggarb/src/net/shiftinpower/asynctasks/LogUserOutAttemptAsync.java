package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnUserLogOutListener;
import net.shiftinpower.utilities.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class LogUserOutAttemptAsync extends AsyncTask<String, Void, Boolean> {
	private String userId;
	private JSONParser jsonParser = new JSONParser();

	private OnUserLogOutListener listener;

	public LogUserOutAttemptAsync(OnUserLogOutListener listener, String userId) {
		this.userId = userId;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(String... params) {

		int serverResponseCode;

		// Doing a lastseen insert in the server db
		try {
			List<NameValuePair> paramz = new ArrayList<NameValuePair>();
			paramz.add(new BasicNameValuePair(C.DBColumns.USER_ID, userId));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.SET_LAST_SEEN, "POST", paramz);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);
			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				return true;
			} else {
				return false;
			}
		} catch (JSONException ex) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(Boolean result) {
		if (listener != null) {
			if (result == true) {
				listener.onUserLogOutSuccess();
			} else {
				listener.onUserLogOutFailure();
			}
		}
		super.onPostExecute(result);
	} // End of onPostExecute

} // End of Class
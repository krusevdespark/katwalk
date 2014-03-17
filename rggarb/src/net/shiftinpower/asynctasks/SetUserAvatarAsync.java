package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.utilities.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class SetUserAvatarAsync extends AsyncTask<String, String, String> {
	
	private int serverResponseCode;
	private String userId;
	private String userAvatarPath;
	private JSONParser jsonParser = new JSONParser();

	public SetUserAvatarAsync(String userId, String userAvatarPath) { 
		this.userId = userId;
		this.userAvatarPath = userAvatarPath;
	}

	@Override
	protected String doInBackground(String... args) {
		
		try {
			List<NameValuePair> changeAvatarAttempt = new ArrayList<NameValuePair>();
			changeAvatarAttempt.add(new BasicNameValuePair(C.DBColumns.USER_ID, userId));
			changeAvatarAttempt.add(new BasicNameValuePair(C.DBColumns.USER_AVATAR, userAvatarPath));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.SET_USER_AVATAR, "POST", changeAvatarAttempt);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				return C.Tagz.SUCCESS;
			} else{
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
	
} // End of Class
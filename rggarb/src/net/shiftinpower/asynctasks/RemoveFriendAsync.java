package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnRemoveFriendListener;
import net.shiftinpower.utilities.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class RemoveFriendAsync extends AsyncTask<String, String, Boolean> {

	private int serverResponseCode;
	private int removerId;
	private int removedId;
	private OnRemoveFriendListener listener;
	private JSONParser jsonParser = new JSONParser();

	public RemoveFriendAsync(OnRemoveFriendListener listener, int removerId, int removedId) {
		this.listener = listener;
		this.removerId = removerId;
		this.removedId = removedId;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		List<NameValuePair> removeFriend = new ArrayList<NameValuePair>();
		removeFriend.add(new BasicNameValuePair(C.DBColumns.REMOVE_FRIEND_REMOVER, String.valueOf(removerId)));
		removeFriend.add(new BasicNameValuePair(C.DBColumns.REMOVE_FRIEND_REMOVED, String.valueOf(removedId)));

		try {

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.FRIEND_REMOVE, "POST", removeFriend);
			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				return true;
			} else {
				return null;
			}

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);

		if (listener != null) {
			if (result) {
				listener.onRemoveFriendSuccess();
			} else {
				listener.onRemoveFriendFailure();
			}
		}

	} // End of onPostExecute

} // End of Class

package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnFriendRequestSendListener;
import net.shiftinpower.utilities.JSONParser;
import android.os.AsyncTask;

public class FriendRequestSendAsync extends AsyncTask<String, String, Boolean> {

	private int serverResponseCode;
	private int senderId;
	private int receiverId;
	private OnFriendRequestSendListener listener;
	private JSONParser jsonParser = new JSONParser();

	public FriendRequestSendAsync(OnFriendRequestSendListener listener, int senderId, int receiverId) {
		this.listener = listener;
		this.senderId = senderId;
		this.receiverId = receiverId;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		List<NameValuePair> sendAFriendRequest = new ArrayList<NameValuePair>();
		sendAFriendRequest.add(new BasicNameValuePair(C.DBColumns.FRIENDSHIP_REQUEST_SENDER_ID, String.valueOf(senderId)));
		sendAFriendRequest.add(new BasicNameValuePair(C.DBColumns.FRIENDSHIP_REQUEST_RECEIVER_ID, String.valueOf(receiverId)));

		try {
			
			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.FRIEND_REQUEST_SEND, "POST", sendAFriendRequest);
			serverResponseCode = json.getInt(C.Tagz.SUCCESS);
			
			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				return true;
			} else{
				return null;
			}
			
		}catch (JSONException e) {
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
				listener.onFriendRequestSendSuccess();
			} else {
				listener.onFriendRequestSendFailure();
			}
		}

	} // End of onPostExecute

} // End of Class
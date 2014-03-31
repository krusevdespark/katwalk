package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnFriendRequestRejectListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.os.AsyncTask;

public class FriendRequestRejectAsync extends AsyncTask<String, String, Boolean>{
	
	private int serverResponseCode;
	private int senderId;
	private int receiverId;
	private Context context;
	private OnFriendRequestRejectListener listener;
	private JSONParser jsonParser = new JSONParser();
	private boolean loadingMessageShown = false;

	public FriendRequestRejectAsync(OnFriendRequestRejectListener listener, Context context, int senderId, int receiverId) {
		this.listener = listener;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		if (context != null) {
			ShowLoadingMessage.loading(context, C.LoadingMessages.FRIEND_REQUEST_REJECTING);
			loadingMessageShown = true;
		}

	} // End of onPreExecute
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		List<NameValuePair> rejectAFriendRequest = new ArrayList<NameValuePair>();
		rejectAFriendRequest.add(new BasicNameValuePair(C.DBColumns.FRIENDSHIP_REQUEST_SENDER_ID, String.valueOf(senderId)));
		rejectAFriendRequest.add(new BasicNameValuePair(C.DBColumns.FRIENDSHIP_REQUEST_RECEIVER_ID, String.valueOf(receiverId)));

		try {
			
			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.FRIEND_REQUEST_REJECT,  "POST", rejectAFriendRequest);
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
		
	} // End of doIn Background
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		
		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (result) {
				listener.onFriendRequestRejectSuccess();
			} else {
				listener.onFriendRequestRejectFailure();
			}
		}

	} // End of onPostExecute
	
} // End of Class

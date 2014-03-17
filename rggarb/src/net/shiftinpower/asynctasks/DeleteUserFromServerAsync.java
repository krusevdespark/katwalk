package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnUserDeletionListener;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;

/**
* When deleting a user, we would also like to delete their images from the images folder on the server, along with their
* products and the products images. However I havent gotten to that yet.
*
* @author Kaloyan Roussev
*
*/

public class DeleteUserFromServerAsync extends AsyncTask<String, String, String> {
	int success;
	private Context context;
	private OnUserDeletionListener listener;
	private String userToBeDeleted;
	private JSONParser jsonParser = new JSONParser();
	private String reason;
	private String userAvatarPath;
	private boolean deleteUserItemImagesAsWell;

	public DeleteUserFromServerAsync(Context context, OnUserDeletionListener listener, String userToBeDeleted, String userAvatarPath, boolean deleteUserItemImagesAsWell) {
		this.context = context;
		this.listener = listener;
		this.userToBeDeleted = userToBeDeleted;
		this.userAvatarPath = userAvatarPath;
		this.deleteUserItemImagesAsWell = deleteUserItemImagesAsWell;
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
	protected String doInBackground(String... args) {
		int serverResponseCode;

		try {
			List<NameValuePair> deleteUserAccount = new ArrayList<NameValuePair>();

			deleteUserAccount.add(new BasicNameValuePair(C.DBColumns.USER_ID, userToBeDeleted));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.DELETE_USER_ACCOUNT, "POST", deleteUserAccount);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				List<NameValuePair> deleteUserAvatar = new ArrayList<NameValuePair>();
				deleteUserAvatar.add(new BasicNameValuePair(C.DBColumns.IMAGE_FILENAME, userAvatarPath));
				JSONObject json2 = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.DELETE_USER_AVATAR, "GET", deleteUserAvatar);

				return C.Tagz.SUCCESS;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {

				setReason(C.Tagz.BAD_REQUEST);
				return null;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {

				setReason(C.Tagz.DB_PROBLEM);
				return null;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_NOT_FOUND) {

				setReason(C.Tagz.NOT_FOUND);
				return null;

			} else {

				setReason(C.Tagz.UNKNOWN_PROBLEM);
				return null;

			}

		} catch (JSONException e) {

			e.printStackTrace();
			setReason(C.Tagz.BAD_REQUEST);
			return null;

		} catch (Exception e) {

			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;

		} // End of Try Catch Block

	} // End of doInBackground

	@Override
	protected void onPostExecute(String result) {

		super.onPostExecute(result);

		ShowLoadingMessage.dismissDialog();

		// List<NameValuePair> deleteUserItemImages = new ArrayList<NameValuePair>();
		// deleteUserItemImages.add(new BasicNameValuePair("user_id", userAvatarPath));
		// JSONObject json3 = jsonParser.makeHttpRequest(Configurationz.API.WEB_ADDRESS +
		// Configurationz.API.DELETE_USER_AVATAR, "GET", deleteUserAvatar);

		if (listener != null) {
			if (result.contentEquals(C.Tagz.SUCCESS)) {
				listener.onUserDeletionSuccess();
			} else if (reason != null) {
				listener.onUserDeletionFailure(reason);
			}
		}

	} // End of onPostExecute

} // End of Class
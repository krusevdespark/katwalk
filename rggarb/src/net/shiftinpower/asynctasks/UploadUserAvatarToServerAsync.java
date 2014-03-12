package net.shiftinpower.asynctasks;

import net.shiftinpower.core.C;
import net.shiftinpower.utilities.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class UploadUserAvatarToServerAsync extends AsyncTask<String, Void, Boolean> {

	private String userAvatarPath;
	private String avatarFilename;
	private JSONParser jsonParser = new JSONParser();
	private String userId;

	public UploadUserAvatarToServerAsync(String userId, String userAvatarPath, String avatarFilename) {
		this.userId = userId;
		this.userAvatarPath = userAvatarPath;
		this.avatarFilename = avatarFilename;
	}

	@Override
	protected Boolean doInBackground(String... params) {

		try {
			JSONObject json = jsonParser.uploadImageToServer(userAvatarPath, avatarFilename, true);

			int serverResponseCode;

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
				// TODO deal with this. Later on, when I learn how to set the whole app to listen for an event, I should communicate with the listener
				// in cases of success or failure

				new SetUserAvatarAsync(userId, avatarFilename).execute();
				// started executing SetUserAvatar.
				return true;

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
	} // End of doInBackground
	
} // End of Class
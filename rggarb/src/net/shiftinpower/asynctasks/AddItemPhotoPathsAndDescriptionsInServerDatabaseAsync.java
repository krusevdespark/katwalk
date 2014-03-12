package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;
import net.shiftinpower.interfaces.OnClearDatabaseListener;
import net.shiftinpower.localsqlitedb.*;
import net.shiftinpower.core.C;
import net.shiftinpower.core.InitialDataLoader;
import net.shiftinpower.utilities.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class AddItemPhotoPathsAndDescriptionsInServerDatabaseAsync extends AsyncTask<String, Void, Boolean> implements OnClearDatabaseListener {

	private ArrayList<String> imageDescriptions;
	private ArrayList<String> imageFilenames;
	private int itemId;
	private int userId;
	private DBTools dbTools;
	private Context context;
	private JSONParser jsonParser = new JSONParser();

	public AddItemPhotoPathsAndDescriptionsInServerDatabaseAsync(DBTools dbTools, Context context, int userId, int itemId, ArrayList<String> imageDescriptions,
			ArrayList<String> imageFilenames) {
		this.imageDescriptions = imageDescriptions;
		this.imageFilenames = imageFilenames;
		this.dbTools = dbTools;
		this.itemId = itemId;
		this.userId = userId;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		int serverResponseCode;
		try {

			List<NameValuePair> imagesInformation = new ArrayList<NameValuePair>();

			imagesInformation.add(new BasicNameValuePair(C.DBColumns.USER_ID, String.valueOf(userId)));
			imagesInformation.add(new BasicNameValuePair(C.DBColumns.ITEM_ID, String.valueOf(itemId)));
			imagesInformation.add(new BasicNameValuePair(C.DBColumns.ITEM_NUMBER_OF_PHOTOS, String.valueOf(imageFilenames.size())));

			int imagesCount = imageFilenames.size();
			for (int i = 0; i < imagesCount; i++) {
				imagesInformation.add(new BasicNameValuePair("item_image_filename_" + i, imageFilenames.get(i)));
				imagesInformation.add(new BasicNameValuePair("item_image_description_" + i, imageDescriptions.get(i)));
			}

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.ADD_NEW_ITEM_IMAGE_FILENAMES_AND_DESCRIPTIONS, "POST", imagesInformation);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {
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

	@Override
	protected void onPostExecute(Boolean result) {

		new ClearDatabase(context, dbTools, this).execute();

		super.onPostExecute(result);
	}

	@Override
	public void onClearDatabaseSuccess() {
		Intent reloadData = new Intent(context, InitialDataLoader.class);
		context.startActivity(reloadData);
	}

	@Override
	public void onClearDatabaseFailure() {
		// TODO Auto-generated method stub
	}

} // End of Class
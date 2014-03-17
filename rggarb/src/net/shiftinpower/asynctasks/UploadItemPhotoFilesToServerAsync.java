package net.shiftinpower.asynctasks;

import java.util.LinkedHashSet;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import net.shiftinpower.core.C;
import net.shiftinpower.objects.TemporaryImage;
import net.shiftinpower.utilities.JSONParser;

/**
 * 
 * This AsyncTask class handles uploading Item Images to the server and storing them in a folder there It receives the Plain
 * Old Java Objects of the type ItemImage through its constructor, then reads the paths they are stored in on the phone,
 * and uploads them to the server, reading the filenames we've chosen for them 
 * 
 * @author Kaloyan Roussev
 * 
 */
public class UploadItemPhotoFilesToServerAsync extends AsyncTask<String, Void, Boolean> {

	private LinkedHashSet<TemporaryImage> itemImages;
	private JSONParser jsonParser = new JSONParser();

	public UploadItemPhotoFilesToServerAsync(LinkedHashSet<TemporaryImage> itemImages) {
		this.itemImages = itemImages;
	}

	@Override
	protected Boolean doInBackground(String... args) {

		for (TemporaryImage itemImage : itemImages) {
			try {
				uploadImageToServer(itemImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;

	} // End of doInBackground

	public boolean uploadImageToServer(TemporaryImage itemImage) throws Exception {

		try {

			JSONObject json = jsonParser.uploadImageToServer(itemImage.getPath(), itemImage.getFilename(), false);
			
			int serverResponseCode;

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
		
	} // End of uploadImageToServer method
	
} // End of Class
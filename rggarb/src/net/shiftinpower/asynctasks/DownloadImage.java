package net.shiftinpower.asynctasks;

import java.io.InputStream;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnDownloadImageListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * 
 * We are either storing the User Avatar on our server in a folder or obtaining it from Facebook Graph API this class fetches
 * the avatar and writes it into a Bitmap that can be used later
 * 
 * @author Kaloyan Roussev
 * 
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

	private Bitmap imageBitmap;
	private String imageUrl;
	private OnDownloadImageListener listener;
	private BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

	public DownloadImage(OnDownloadImageListener listener, String imageUrl) {
		this.listener = listener;
		this.imageUrl = imageUrl;
	}

	protected Bitmap doInBackground(String... urls) {

		try {

			InputStream in = new java.net.URL(imageUrl).openStream();

			// ideally, we havent uploaded a huge image to the server, but anything happens
			// so if we fetch it and it turns out huge, we will try to resample it and if it is still big we will resample it
			// harder.
			try {
				imageBitmap = BitmapFactory.decodeStream(in);
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				try {
					bitmapOptions.inSampleSize = C.ImageHandling.INITIAL_BITMAP_RESAMPLE_SIZE;
					imageBitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
				} catch (OutOfMemoryError ex2) {
					ex2.printStackTrace();
					bitmapOptions.inSampleSize = C.ImageHandling.HARDER_BITMAP_RESAMPLE_SIZE;
					imageBitmap = BitmapFactory.decodeStream(in, null, bitmapOptions);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return imageBitmap;
	} // End of doInBackground

	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);

		if (listener != null) {
			if (result != null) {
				listener.onDownloadImageSuccess(result, imageUrl);
			} else {
				listener.onDownloadImageFailure();
			}
		}
	} // End of onPostExecute

} // End of Class
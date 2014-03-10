package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
import android.graphics.Bitmap;

public interface OnDownloadImageListener {
	void onDownloadImageSuccess(Bitmap image, String imageUrl);

	void onDownloadImageFailure();
}

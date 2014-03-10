package net.shiftinpower.interfaces;

import android.graphics.Bitmap;

public interface OnDownloadImageListener {
	void onDownloadImageSuccess(Bitmap image, String imageUrl);
	void onDownloadImageFailure();
}

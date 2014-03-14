package net.shiftinpower.core;

import java.util.ArrayList;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.DBTools;
import net.shiftinpower.utilities.HashPassword;
import net.shiftinpower.utilities.PhotoHandler;
import net.shiftinpower.utilities.ToastMaker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * This is the Application class. It is the first object of the application created and it is generally used to instantiate
 * utilities that should be only instatiated once in the lifetime of the app.
 * 
 * It holds the global variables and utilities references such as DataBase, ImageLoader, ToastMaker, PhotoHandler
 * 
 * @author Kaloyan Roussev
 * 
 */
public class KatwalkApplication extends Application {

	// Custom class to display toasts
	public ToastMaker toastMaker = new ToastMaker();

	// This class hashes passwords
	public HashPassword hashPassword = new HashPassword();

	// SQLite Database Handler
	public DBTools dbTools;

	// Declare the Universal Image Loader for lazy load of images
	public ImageLoader imageLoader;

	// Fonts
	public Typeface font1;
	public Typeface font2;

	// Photo Handler custom class containing several methods that deal with images
	public PhotoHandler photoHandler = new PhotoHandler(this);

	// Bitmap Options
	public BitmapFactory.Options bitmapOptions;

	@Override
	public void onCreate() {
		super.onCreate();

		// Setting up fonts
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
			font2 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_2);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}

		dbTools = DBTools.getInstance(this);

		// Create global configuration and initialize ImageLoader with this configuration
		// https://github.com/nostra13/Android-Universal-Image-Loader
		ImageLoaderConfiguration imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(this).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(imageLoaderConfiguration);

		// Set global bitmap preferences
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inDither = false;
		bitmapOptions.inPurgeable = true;
		bitmapOptions.inInputShareable = true;
		bitmapOptions.inTempStorage = new byte[16 * 1024];

	} // End of onCreate

	public Boolean canUserAccessTheInternet() {
		final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEditTextEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public int safeLongToInt(long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
		}
		return (int) l;
	}

	// This method is used throughout the application, whenever the current user's image should be displayed and might have
	// been recently changed
	public void setUserImageToImageView(ImageView imageView, String imagePath, String sex) {
		Bitmap imageBitmap;

		if (!imagePath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE) && !imagePath.contentEquals("") && imagePath != null) {

			try {
				imageBitmap = BitmapFactory.decodeFile(imagePath);
				imageView.setImageBitmap(imageBitmap);

			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				imageBitmap = photoHandler.getBitmapAndResizeIt(imagePath);
				imageView.setImageBitmap(imageBitmap);

			} catch (Exception ex) {
				ex.printStackTrace();

				if (sex.equalsIgnoreCase("male")) {
					imageView.setImageResource(R.drawable.images_default_avatar_male);
				} else {
					imageView.setImageResource(R.drawable.images_default_avatar_female);
				}

			}

		} else {
			if (sex.equalsIgnoreCase("male")) {
				imageView.setImageResource(R.drawable.images_default_avatar_male);
			} else {
				imageView.setImageResource(R.drawable.images_default_avatar_female);
			}
		}
	} // End of setUserImageToImageView

	// This method gets an ArrayList of ImageViews and recycles their bitmaps, for memory management reasons. Usually called
	// in onPause.
	public <T extends ImageView> void recycleViewsDrawables(ArrayList<T> imageViews) {

		for (T t : imageViews) {
			if (t != null) {
				Drawable drawable = t.getDrawable();
				if (drawable != null) {
					if (drawable instanceof BitmapDrawable) {
						BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
						Bitmap bitmap = bitmapDrawable.getBitmap();
						if (bitmap != null && !bitmap.isRecycled()) {
							bitmap.recycle();
						}
					}
				}
			}

		}
	} // End of recycleViewsDrawables

	// This method gets an ImageView and recycles its bitmap, for memory management reasons. Usually called in onPause.
	public <T extends ImageView> void recycleViewsDrawables(T t) {
		if (t != null) {
			Drawable drawable = t.getDrawable();
			if (drawable != null) {
				if (drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					Bitmap bitmap = bitmapDrawable.getBitmap();
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
					}
				}
			}
		}
	} // End of recycleViewsDrawables

} // End of Class
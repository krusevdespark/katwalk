package net.shiftinpower.core;

import net.shiftinpower.localsqlitedb.DBTools;
import net.shiftinpower.utilities.HashPassword;
import net.shiftinpower.utilities.PhotoHandler;
import net.shiftinpower.utilities.ToastMaker;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

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
	
	private static KatwalkApplication singleton;

	public KatwalkApplication getInstance() {
		return singleton;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		

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
		// KIRCHO
		
		// Set global bitmap preferences
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inDither = false;
		bitmapOptions.inPurgeable = true;
		bitmapOptions.inInputShareable = true;
		bitmapOptions.inTempStorage = new byte[16 * 1024];


	}

}

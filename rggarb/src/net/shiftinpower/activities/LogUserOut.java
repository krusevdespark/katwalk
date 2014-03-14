package net.shiftinpower.activities;

import java.io.File;
import net.shiftinpower.asynctasks.LogUserOutAttemptAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.KatwalkCore;
import net.shiftinpower.interfaces.OnUserLogOutListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.ClearDatabase;
import net.shiftinpower.utilities.ToastMaker;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class LogUserOut extends KatwalkCore implements OnUserLogOutListener {

	// XML Views
	private ImageView ivSplashScreen;

	// Image handling variables
	private Bitmap bitmap;

	// Custom class to display toasts
	protected ToastMaker toastMaker = new ToastMaker();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_layout_splash_screen);
		setBehindContentView(R.layout.activity_layout_splash_screen);

		getSupportActionBar().hide();

		// This app operates in No Title, Fullscreen mode
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ivSplashScreen = (ImageView) findViewById(R.id.ivSplashScreen);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_loading_screen, katwalk.bitmapOptions);
		ivSplashScreen.setImageBitmap(bitmap);

		// Starting an async task that will log the user out and write the value for Last Seen at the server
		new LogUserOutAttemptAsync(this, String.valueOf(currentlyLoggedInUser)).execute();
	} // End of onCreate

	@Override
	protected void onStop() {
		super.onStop();
		// Prevent memory leak by releasing the bitmaps from the memory
		katwalk.recycleViewsDrawables(ivSplashScreen);
	}

	@Override
	public void onUserLogOutSuccess() {

		// Clearing the SharedPreferences file so the next user can use it flawlessly
		sharedPreferencesEditor = sharedPreferences.edit();
		sharedPreferencesEditor.clear();
		sharedPreferencesEditor.commit();

		// Clear the Rggarb folder with the temporary files
		File directory = new File(Environment.getExternalStorageDirectory() + C.Preferences.RGGARB_DIRECTORY_ON_USER_STORAGE);
		if (directory.isDirectory()) {
			String[] children = directory.list();
			int numberOfFiles = children.length;
			for (int i = 0; i < numberOfFiles; i++) {
				new File(directory, children[i]).delete();
			}
		}

		// Clearing the Local SQLite Database
		new ClearDatabase(LogUserOut.this, katwalk.dbTools).execute();

		// Sending the user to the login/signup screen
		Intent logOut = new Intent(LogUserOut.this, MainActivity.class);
		startActivity(logOut);
		finish();
	}

	@Override
	public void onUserLogOutFailure() {
		toastMaker.toast(net.shiftinpower.activities.LogUserOut.this, C.Errorz.LOG_OUT_FAILURE, Toast.LENGTH_LONG);
	}

} // End of Class
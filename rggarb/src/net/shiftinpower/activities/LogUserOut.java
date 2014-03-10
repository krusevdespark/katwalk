package net.shiftinpower.activities;

import java.io.File;
import net.shiftinpower.asynctasks.LogUserOutAttemptAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.RggarbSlidingMenu;
import net.shiftinpower.interfaces.OnUserLogOutListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.ClearDatabase;
import net.shiftinpower.utilities.ToastMaker;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

/**
 * Doing some maintenance work when logging the user out
 *  - clear database, so when they log back in again, they will get another set of fresh data (not the only time they are getting fresh data, though)
 *  - clear SharedPreferences file, so next user can use it
 *  - clear the contents of the app folder on the phone storage
 * 
 * @author Kaloyan Roussev
 *
 */

public class LogUserOut extends RggarbSlidingMenu implements OnUserLogOutListener {

	public LogUserOut() {
		super(R.string.app_name);
	}

	// Custom class to display toasts
	protected ToastMaker toastMaker = new ToastMaker();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().hide();

		setContentView(R.layout.activity_layout_splash_screen);

		// Starting an async task that will log the user out and write the value for Last Seen at the server
		new LogUserOutAttemptAsync(this, String.valueOf(currentlyLoggedInUser)).execute();
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
		new ClearDatabase(LogUserOut.this, dbTools).execute();

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
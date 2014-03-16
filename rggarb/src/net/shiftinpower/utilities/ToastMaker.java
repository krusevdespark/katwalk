package net.shiftinpower.utilities;

import net.shiftinpower.core.C;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
* Provides a way to use heavily cusomized toasts with one line of code
*
* Overloaded method allows us to choose position of the Toast on the screen
*
* @author Kaloyan Roussev
*
*/

public class ToastMaker extends Activity {

	/*
	 * This method allows us to use custom design for the toast, and calling it requires just one line of typing
	 */
	public void toast(final Context context, final String message, final int duration) {
		((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast myToast = Toast.makeText(context, message, duration);
				myToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
				TextView tv = (TextView) myToast.getView().findViewById(android.R.id.message);
				tv.setTextColor(Color.parseColor(C.Colorz.TOAST_TEXT_COLOR));
				tv.setTextSize(C.Miscellaneous.TOAST_TEXT_SIZE);
				myToast.getView().setBackgroundColor(Color.parseColor(C.Colorz.TOAST_BACKGROUND));
				myToast.show();
			}
		});
	}
	
	/*
	 * In this overloaded method, screen position for the toast can be adjusted
	 */
	public void toast(final Context context, final String message, final int duration, final int gravity, final int xOffset, final int yOffset) {
		((Activity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast myToast = Toast.makeText(context, message, duration);
				myToast.setGravity(gravity, xOffset, yOffset);
				TextView tv = (TextView) myToast.getView().findViewById(android.R.id.message);
				tv.setTextColor(Color.parseColor(C.Colorz.TOAST_TEXT_COLOR));
				tv.setTextSize(C.Miscellaneous.TOAST_TEXT_SIZE);
				myToast.getView().setBackgroundColor(Color.parseColor(C.Colorz.TOAST_BACKGROUND));
				myToast.show();
			}
		});
	}
} // End of Class

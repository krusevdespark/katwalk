package net.shiftinpower.utilities;

import net.shiftinpower.koldrain.R;
import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class ShowLoadingMessage{
	static TextView tvLoading;
	
	static Dialog dialog;

	public static void loading(Context context){

		dialog = new Dialog(context, R.style.no_title_dialog);
		dialog.setContentView(R.layout.dialog_loading);

		dialog.show();
	}
	
	public static void splashScreen(Context context){

		dialog = new Dialog(context, R.style.no_title_dialog);
		int randomNumber = randomizer(1,4);
		setRandomBackground(randomNumber);
		dialog.show();
	}
	
	public static void loading(Context context, String message){

		dialog = new Dialog(context, R.style.no_title_dialog);
		dialog.setContentView(R.layout.dialog_loading);
		tvLoading = (TextView) dialog.findViewById(R.id.tvLoading);
		tvLoading.setText(message + "...");
		dialog.show();
	}
	
	public static void dismissDialog() {
		if (dialog.isShowing()) {
            dialog.dismiss();
        }
	}
	
	public static void setRandomBackground(int randomNumber) {
		if(randomNumber == 1) {
			dialog.setContentView(R.drawable.custom_toast_background_a);
		} else if(randomNumber == 2) {
			dialog.setContentView(R.drawable.custom_toast_background_b);
		} else if(randomNumber == 3) {
			dialog.setContentView(R.drawable.custom_toast_background_c);
		} else if(randomNumber == 4) {
			dialog.setContentView(R.drawable.custom_toast_background_d);
		}
	}
	
	public static int randomizer(int min, int max) {
		int d = (max - min) + 1;
		int c = min + (int) (Math.random() * ((d)));
		return c;
	}
	
} // End of Class

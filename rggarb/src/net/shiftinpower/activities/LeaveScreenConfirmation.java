package net.shiftinpower.activities;

import net.shiftinpower.core.C;
import net.shiftinpower.koldrain.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LeaveScreenConfirmation extends Activity {
	
	// XML View components
	private Button bStayOnThisScreen;
	private Button bLeaveThisScreen;
	
	// Fonts
	private Typeface font1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// We dont want the ugly grey title bar to interrupt our dialog design, so this line removes it
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Setting up fonts
		try {
			font1 = Typeface.createFromAsset(getApplicationContext().getAssets(), C.Fontz.FONT_1);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}
		
		setContentView(R.layout.dialog_leave_add_item_page_prompt);
		
		bStayOnThisScreen = (Button) findViewById(R.id.bStayOnThisScreen);
		bLeaveThisScreen = (Button) findViewById(R.id.bLeaveThisScreen);
		
		// Set the fonts for the buttons
		try {
			bStayOnThisScreen.setTypeface(font1);
			bLeaveThisScreen.setTypeface(font1);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}
		
		bStayOnThisScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bLeaveThisScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResultOkSoPreviousActivityWontBeShown();
				finish();
			}
			
		});
		
	} // End of onCreate
	
	private void setResultOkSoPreviousActivityWontBeShown() {
	    Intent intent = new Intent();
	    if (getParent() == null) {
	    setResult(Activity.RESULT_OK, intent);
	    } else {
	        getParent().setResult(Activity.RESULT_OK, intent);
	    }
	}
	
} // End of Class

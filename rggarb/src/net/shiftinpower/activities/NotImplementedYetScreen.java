package net.shiftinpower.activities;

import net.shiftinpower.koldrain.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

public class NotImplementedYetScreen extends Activity {

	private FrameLayout notImplementedYet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// We are showing the user a Work In Progress screen
		setContentView(R.layout.activity_layout_not_implemented_yet);

		
		// The app operates in Full Screen Mode
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Make the whole screen clickable and return the user to their previous activity when clicked
		notImplementedYet = (FrameLayout) findViewById(R.id.notImplementedYet);
		notImplementedYet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
	} // End of onCreate

} // End of Class

package net.shiftinpower.activities;

import com.actionbarsherlock.view.MenuItem;
import net.shiftinpower.core.*;
import net.shiftinpower.koldrain.R;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * NOTE: This is just a basic draft
 * 
 * 
 */
public class MyProfilePhotos extends RggarbSlidingMenu {
	
	FrameLayout flMyProfileItemsHolder; 
	
	
	private TextView tvMyImageGalleryTitle;

	public MyProfilePhotos() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_layout_my_profile_image_gallery);
		
		tvMyImageGalleryTitle = (TextView) findViewById(R.id.tvMyImageGalleryTitle);
		
		try {
			tvMyImageGalleryTitle.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}
		
		//flMyProfileItemsHolder = (FrameLayout) findViewById(R.id.flMyProfileItemsHolder);
		//getSupportFragmentManager().beginTransaction().replace(R.id.flMyProfileItemsHolder, new MyProfileItemsItemFragmentList()).commit();
		
		
	} // End of onCreate

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

} // End of Class
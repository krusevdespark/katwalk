package net.shiftinpower.activities;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import net.shiftinpower.core.C;
import net.shiftinpower.core.KatwalkApplication;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.utilities.PhotoHandler;

/* Unfortunately we cannot use most of the global variables and features here because we cannot extend the RggarbCore class
 * The RggarbCore class is using the Sherlock library because it enables older versions of Android to use the Action bar and the Sliding menu
 * The Sherlock Library does not allow an activity to use Theme.Dialog, but only Theme.Sherlock.xx themes, which do not include a Dialog theme for quite some time now
 * So we have to instantiate some variables and utility classes all over again, and when starting this activity, we are including some data along with the intent.
 * To work with this class you need to pass extra data to the intent - currentImageExists (so this class knows to display it) and imagePath (where to get it from)
 */
public class ProvideImageDialog extends Activity {

	// Set up XML View Components
	private ImageView imageView;
	private Button bTakeAPhoto;
	private Button bPickFromGallery;
	private Button bDeletePhoto;
	private Button bCancelPhotoUpload;

	// Data holding variables
	private boolean currentImageExists;
	private String imagePath;

	// Shared Preferences
	protected Editor sharedPreferencesEditor;
	protected SharedPreferences sharedPreferences;
	protected static final String APP_SHARED_PREFS = C.Preferences.SHARED_PREFERENCES_FILENAME;

	private KatwalkApplication katwalk;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		katwalk = (KatwalkApplication) getApplication();
	
		// We dont want the ugly grey title bar to interrupt our dialog design, so this line removes it
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Obtain the vital user Information from the starting intent
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			imagePath = extras.getString(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY);
			if (!(imagePath.contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE))) {
				currentImageExists = true;
			} else {
				currentImageExists = false;
			}
		}
		
		if (!currentImageExists) {
			setContentView(R.layout.dialog_image_upload_without_image);

		} else {
			// This version of the Dialog layout enables the user to
			// delete the avatar they just uploaded, also they can view
			// it
			setContentView(R.layout.dialog_iimage_upload_with_image);

			// setting the views that exist on this layout only
			imageView = (ImageButton) findViewById(R.id.iImage);
			bDeletePhoto = (Button) findViewById(R.id.bDeleteImage);
			// Setting font
			try {
				bDeletePhoto.setTypeface(katwalk.font1);
			} catch (Exception e) {
			}
			bDeletePhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// Remove visible avatar on the screen
					imagePath = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;
					currentImageExists = false;

					// avatarChangeDialog.dismiss();
					// Restart the activity so the Core Class that holds all the
					// Activities picks up the new avatar and places it in the Sliding
					// Menu Header
					Intent intent = new Intent();
					intent.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, imagePath);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		}
		
		
		// Display the image
		if (currentImageExists) {

			try {
				imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));

			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				imageView.setImageBitmap(katwalk.photoHandler.getBitmapAndResizeIt(imagePath));
			}
		}

		// Setting the views that exist on both dialog layouts
		bTakeAPhoto = (Button) findViewById(R.id.bTakeAPhoto);
		bPickFromGallery = (Button) findViewById(R.id.bSelectPhotoFromFile);
		bCancelPhotoUpload = (Button) findViewById(R.id.bCancelImageUpload);

		// Setting fonts
		try {
			bTakeAPhoto.setTypeface(katwalk.font1);
			bPickFromGallery.setTypeface(katwalk.font1);
			bCancelPhotoUpload.setTypeface(katwalk.font1);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}

		bTakeAPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Check wether this device is able to take pictures
				if (PhotoHandler.isIntentAvailable(ProvideImageDialog.this, MediaStore.ACTION_IMAGE_CAPTURE)) {

					// Create the camera intent
					Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					// Create the file that the camera app should use to
					// store the taken image in
					File imageFile = null;
					try {
						imageFile = katwalk.photoHandler.createImageFile();
						imagePath = imageFile.getAbsolutePath();
						takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
					} catch (IOException e) {
						e.printStackTrace();
						imageFile = null;
						imagePath = null;
					}

					startActivityForResult(takePictureIntent, C.ImageHandling.REQUEST_CODE_TAKE_IMAGE_WITH_CAMERA);

					// finish();

				} else { // Notify the user that their device is unable
							// to take photos

					katwalk.toastMaker.toast(net.shiftinpower.activities.ProvideImageDialog.this, C.Errorz.DEVICE_UNABLE_TO_TAKE_PHOTOS, Toast.LENGTH_LONG);
					currentImageExists = false;

					// finish();

				}
			}
		}); // End of Take a Photo button handling

		bPickFromGallery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent getImageFromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getImageFromGallery.setType("image/*");
				startActivityForResult(getImageFromGallery, C.ImageHandling.REQUEST_CODE_PICK_IMAGE);
				// finish();
			}
		});

		bCancelPhotoUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		}); // End of Pick a Photo from Gallery button handling

	} // End of onResume

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {
			if (requestCode == C.ImageHandling.REQUEST_CODE_TAKE_IMAGE_WITH_CAMERA && resultCode == RESULT_OK) {

				// The Camera intent has provided the photo in the file created
				// eariler on, we need to deal with it
				if (!(katwalk.photoHandler.handleBigCameraPhoto())) {

					// Something went wrong while dealing with the image
					// Remove visible avatar on the screen
					imagePath = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;
					currentImageExists = false;

					Intent intent = new Intent();
					intent.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, imagePath);
					setResult(RESULT_OK, intent);
					finish();
				}

			} else if (requestCode == C.ImageHandling.REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && null != data) {

				// obtaining the path to the new bitmap and setting the userAvatar
				if (!(katwalk.photoHandler.handleGalleryPhoto(data))) {
					// Something went wrong while dealing with the image
					// Remove visible avatar on the screen
					imagePath = C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE;
					currentImageExists = false;

					Intent intent = new Intent();
					intent.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, imagePath);
					setResult(RESULT_OK, intent);
					finish();
				}

			} // End of requestCode == PICK_IMAGE Case

			imagePath = katwalk.photoHandler.getImagePath();
			currentImageExists = true;

			Intent intent = new Intent();
			intent.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, imagePath);
			setResult(RESULT_OK, intent);
			finish();

		}

	} // End of onActivityResult
	
	@Override
	protected void onStop() {
		super.onStop();
		katwalk.recycleViewsDrawables(imageView);		
	}	

} // End of Class
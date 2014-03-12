package net.shiftinpower.activities;

import java.util.LinkedHashSet;
import net.shiftinpower.asynctasks.UploadItemPhotoFilesToServerAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.KatwalkSlidingMenu;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.TemporaryImage;
import net.shiftinpower.utilities.PhotoHandler;
import net.shiftinpower.utilities.Transporter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class manages the first of two activities that comprise the Add Item process. Here, the user has the ability to
 * upload up to 5 images along with their description We are doing the images first, because uploads may take a while, so
 * while the user is entering data at the second step The app is going to do the uploading job in the background
 * 
 * Using LinkedHashSet to store the ItemImage objects because it denies the need for exist checks
 * Also, iteration order will match insertion order.
 * 
 * @author Kaloyan Roussev
 * 
 */
public class ItemAddStepOnePhotos extends KatwalkSlidingMenu {

	// Set up XML View Components
	private TextView tvAddAnItemStepOneTitle;
	private TextView tvAddAnItemStepOneSubTitle;
	private ImageButton iImageSlotOne;
	private ImageButton iImageSlotTwo;
	private ImageButton iImageSlotThree;
	private ImageButton iImageSlotFour;
	private ImageButton iImageSlotFive;
	private EditText etItemImageOneDescription;
	private EditText etItemImageTwoDescription;
	private EditText etItemImageThreeDescription;
	private EditText etItemImageFourDescription;
	private EditText etItemImageFiveDescription;
	private Button bAddAnItemStepOneSubmit;

	// Data holding variables
	private boolean dataEntered = false;
	private LinkedHashSet<TemporaryImage> itemImages = new LinkedHashSet<TemporaryImage>();
	private TemporaryImage itemImageOne = new TemporaryImage();
	private TemporaryImage itemImageTwo = new TemporaryImage();
	private TemporaryImage itemImageThree = new TemporaryImage();
	private TemporaryImage itemImageFour = new TemporaryImage();
	private TemporaryImage itemImageFive = new TemporaryImage();

	// Intent changeImageDialog
	private Intent changeImageDialog;

	// Constructor needed because of the Action bar
	public ItemAddStepOnePhotos() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate the XML layout and connect its components to the corresponding Java Objects
		setContentView(R.layout.activity_layout_item_add_step_one_photos);

		tvAddAnItemStepOneTitle = (TextView) findViewById(R.id.tvAddAnItemStepOneTitle);
		tvAddAnItemStepOneSubTitle = (TextView) findViewById(R.id.tvAddAnItemStepOneSubTitle);
		iImageSlotOne = (ImageButton) findViewById(R.id.iImageSlotOne);
		iImageSlotTwo = (ImageButton) findViewById(R.id.iImageSlotTwo);
		iImageSlotThree = (ImageButton) findViewById(R.id.iImageSlotThree);
		iImageSlotFour = (ImageButton) findViewById(R.id.iImageSlotFour);
		iImageSlotFive = (ImageButton) findViewById(R.id.iImageSlotFive);
		etItemImageOneDescription = (EditText) findViewById(R.id.etItemImageOneDescription);
		etItemImageTwoDescription = (EditText) findViewById(R.id.etItemImageTwoDescription);
		etItemImageThreeDescription = (EditText) findViewById(R.id.etItemImageThreeDescription);
		etItemImageFourDescription = (EditText) findViewById(R.id.etItemImageFourDescription);
		etItemImageFiveDescription = (EditText) findViewById(R.id.etItemImageFiveDescription);
		bAddAnItemStepOneSubmit = (Button) findViewById(R.id.bAddAnItemStepOneSubmit);

		// Set the fonts
		try {
			tvAddAnItemStepOneTitle.setTypeface(katwalk.font1);
			tvAddAnItemStepOneSubTitle.setTypeface(katwalk.font2);
			bAddAnItemStepOneSubmit.setTypeface(katwalk.font1);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}

		// Set the intent we are going to use with any of the 5 image buttons
		changeImageDialog = new Intent(ItemAddStepOnePhotos.this, ProvideImageDialog.class);

		iImageSlotOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemImageOne == null) {
					itemImageOne = new TemporaryImage();
				}
				changeImageDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, itemImageOne.getPath());
				startActivityForResult(changeImageDialog, 20);

			}
		});

		iImageSlotTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemImageTwo == null) {
					itemImageTwo = new TemporaryImage();
				}
				changeImageDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, itemImageTwo.getPath());
				startActivityForResult(changeImageDialog, 21);

			}
		});

		iImageSlotThree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemImageThree == null) {
					itemImageThree = new TemporaryImage();
				}
				changeImageDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, itemImageThree.getPath());
				startActivityForResult(changeImageDialog, 22);

			}
		});

		iImageSlotFour.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemImageFour == null) {
					itemImageFour = new TemporaryImage();
				}
				changeImageDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, itemImageFour.getPath());
				startActivityForResult(changeImageDialog, 23);
			}
		});

		iImageSlotFive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemImageFive == null) {
					itemImageFive = new TemporaryImage();
				}
				changeImageDialog.putExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY, itemImageFive.getPath());
				startActivityForResult(changeImageDialog, 24);
			}
		});

		bAddAnItemStepOneSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (itemImageOne != null) {
					if (!isEditTextEmpty(etItemImageOneDescription)) {
						itemImageOne.setDescription(etItemImageOneDescription.getText().toString());
					}

				}

				if (itemImageTwo != null) {
					if (!isEditTextEmpty(etItemImageTwoDescription)) {
						itemImageTwo.setDescription(etItemImageTwoDescription.getText().toString());
					}
				}

				if (itemImageThree != null) {
					if (!isEditTextEmpty(etItemImageThreeDescription)) {
						itemImageThree.setDescription(etItemImageThreeDescription.getText().toString());
					}
				}

				if (itemImageFour != null) {
					if (!isEditTextEmpty(etItemImageFourDescription)) {
						itemImageFour.setDescription(etItemImageFourDescription.getText().toString());
					}
				}

				if (itemImageFive != null) {
					if (!isEditTextEmpty(etItemImageFiveDescription)) {
						itemImageFive.setDescription(etItemImageFiveDescription.getText().toString());
					}
				}

				if (itemImages.size() == 0) {
					katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepOnePhotos.this, C.Errorz.ADDING_AN_ITEM_AT_LEAST_ONE_IMAGE_REQUIRED, Toast.LENGTH_LONG);
				} else {
					// Send the images to the database

					new UploadItemPhotoFilesToServerAsync(itemImages).execute();

					/*
					 * Android provides a system to pass objects between activities that is not at all flexible and requires
					 * a lot of typing We are going to circumvent that by using a simple custom Transporter singleton class
					 */
					Transporter.instance().itemImages = itemImages;

					/*
					 * We have just started uploading the images to the server directory As we know their filenames and the
					 * folder they are going to be stored in We can also create the database record for the item after the
					 * user enters the data in the second step Now we are sending the user to the second step of the Add Item
					 * process
					 */
					Intent itemAddSecondStepData = new Intent(ItemAddStepOnePhotos.this, ItemAddStepTwoData.class);
					startActivity(itemAddSecondStepData);
				}
			}
		});

	}// End of onCreate method

	/*
	 * If the user accidentally presses the hardware back button, they will lose all their data that they've entered We want
	 * to give them a chance to stay on this screen if data bas been entered, so this is what this dialog is for
	 */
	@Override
	public void onBackPressed() {
		if (dataEntered == true) {
			Intent leaveScreenConfirmation = new Intent(ItemAddStepOnePhotos.this, LeaveScreenConfirmation.class);
			startActivityForResult(leaveScreenConfirmation, C.Miscellaneous.LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE);
		} else {
			super.onBackPressed();
		}

	} // End of onBackPressed

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {

			/*
			 * Set the dataEntered variable to true, so if the user presses back space button, the app asks them whether they
			 * really wanted to quit, now that they have entered some data in the fields
			 */
			dataEntered = true;

			/*
			 * Here is the deal. Specs require the number of image boxes shown to dynamically change When the user has just
			 * uploaded a new image, they should see a new "Add image" box If they delete an image, the empty box should hide
			 * itself If they delete an image that is in the middle of added images, other images should move to its slot
			 * Image description input fields should also change accordingly If we delete image 3 out of 5, 4 becomes 3 and 5
			 * becomes 4 The image description field that says "Enter description for image three" should not hide.
			 * Description for image five should hide instead.
			 */

			switch (requestCode) {
			case 20:

				/*
				 * Obtain the image file path from what is returned by the image change activity Add it to the itemImages
				 * collection If its a default image, remove it from there (if its a default, it means it has been deleted)
				 * If the image has been deleted, all the subsequent images have to move up a place so we dont have an empty
				 * slot Or an empty itemImage object.
				 */
				itemImageOne.setPath(data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY));
				itemImageOne.setFilename(PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true));

				itemImages.add(itemImageOne);
				if (!isSet(itemImageOne)) {
					itemImages.remove(itemImageOne);
					if (itemImageTwo != null && isSet(itemImageTwo)) {
						itemImageOne = itemImageTwo;
						itemImages.add(itemImageOne);
						if (itemImageThree != null && isSet(itemImageThree)) {
							itemImageTwo = itemImageThree;
							itemImages.add(itemImageTwo);
							if (itemImageFour != null && isSet(itemImageFour)) {
								itemImageThree = itemImageFour;
								itemImages.add(itemImageThree);
								if (itemImageFive != null && isSet(itemImageFive)) {
									itemImageFour = itemImageFive;
									itemImages.add(itemImageFour);
								}
							}
						}
					}
				}

				break;

			case 21:

				itemImageTwo.setPath(data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY));
				itemImageTwo.setFilename(PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true));

				itemImages.add(itemImageTwo);
				if (!isSet(itemImageTwo)) {
					itemImages.remove(itemImageTwo);
					if (itemImageThree != null && isSet(itemImageThree)) {
						itemImageTwo = itemImageThree;
						itemImages.add(itemImageTwo);
						if (itemImageFour != null && isSet(itemImageFour)) {
							itemImageThree = itemImageFour;
							itemImages.add(itemImageThree);
							if (itemImageFive != null && isSet(itemImageFive)) {
								itemImageFour = itemImageFive;
								itemImages.add(itemImageFour);
							}
						}
					}
				}

				break;

			case 22:

				itemImageThree.setPath(data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY));
				itemImageThree.setFilename(PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true));

				itemImages.add(itemImageThree);
				if (!isSet(itemImageThree)) {
					itemImages.remove(itemImageThree);
					if (itemImageFour != null && isSet(itemImageFour)) {
						itemImageThree = itemImageFour;
						itemImages.add(itemImageThree);
						if (itemImageFive != null && isSet(itemImageFive)) {
							itemImageFour = itemImageFive;
							itemImages.add(itemImageFour);
						}
					}
				}

				break;

			case 23:

				itemImageFour.setPath(data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY));
				itemImageFour.setFilename(PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true));

				itemImages.add(itemImageFour);
				if (!isSet(itemImageFour)) {
					itemImages.remove(itemImageFour);
					if (itemImageFive != null && isSet(itemImageFive)) {
						itemImageFour = itemImageFive;
						itemImages.add(itemImageFour);
					}
				}

				break;

			case 24:

				itemImageFive.setPath(data.getStringExtra(C.ImageHandling.INTENT_EXTRA_IMAGE_PATH_KEY));
				itemImageFive.setFilename(PhotoHandler.generateImageFilename(String.valueOf(currentlyLoggedInUser) + C.ImageHandling.IMAGE_FILENAME_PREFIX, C.ImageHandling.IMAGES_FILE_EXTENSION, true));

				itemImages.add(itemImageFive);
				if (!isSet(itemImageFive)) {
					itemImages.remove(itemImageFive);
				}

				break;

			} // End of the SWITCH statement

			dealWithNumberOfPhotoUploadBoxesOnScreen();

		} // End of the result != cancelled check

		// If the user has clicked the Back button, we have showed them a dialog asking whether they want to leave for sure.
		// This is the callback if they've confirmed they want to leave
		if (requestCode == C.Miscellaneous.LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				finish();
			}
		}

	} // End of onActivityResult

	private void dealWithNumberOfPhotoUploadBoxesOnScreen() {

		// Clear unused itemImage objects so they dont remain in imageview boxes after deletion of an object before them in
		// the row
		int itemImagesSize = itemImages.size();
		switch (itemImagesSize) {
		case 0:
			dataEntered = false;
			itemImages.clear();
			itemImageOne = null;
			break;
		case 1:
			itemImageTwo = null;
			break;
		case 2:
			itemImageThree = null;
			break;
		case 3:
			itemImageFour = null;
			break;
		case 4:
			itemImageFive = null;
			break;
		case 5:
			break;
		}

		// Reset the images set
		etItemImageOneDescription.setVisibility(View.GONE);
		etItemImageTwoDescription.setVisibility(View.GONE);
		etItemImageThreeDescription.setVisibility(View.GONE);
		etItemImageFourDescription.setVisibility(View.GONE);
		etItemImageFiveDescription.setVisibility(View.GONE);
		iImageSlotTwo.setVisibility(View.GONE);
		iImageSlotThree.setVisibility(View.GONE);
		iImageSlotFour.setVisibility(View.GONE);
		iImageSlotFive.setVisibility(View.GONE);
		iImageSlotOne.setImageResource(R.drawable.images_default_product);
		iImageSlotTwo.setImageResource(R.drawable.images_default_product);
		iImageSlotThree.setImageResource(R.drawable.images_default_product);
		iImageSlotFour.setImageResource(R.drawable.images_default_product);
		iImageSlotFive.setImageResource(R.drawable.images_default_product);

		// Build it again, according to the images count that we are getting from the size of the itemImages arraylist
		if (itemImageOne != null && isSet(itemImageOne)) {
			iImageSlotOne.setImageBitmap(BitmapFactory.decodeFile(itemImageOne.getPath()));
			etItemImageOneDescription.setVisibility(View.VISIBLE);
			iImageSlotTwo.setVisibility(View.VISIBLE);
		}

		if (itemImageTwo != null && isSet(itemImageTwo)) {
			iImageSlotTwo.setVisibility(View.VISIBLE);
			iImageSlotTwo.setImageBitmap(BitmapFactory.decodeFile(itemImageTwo.getPath()));
			etItemImageTwoDescription.setVisibility(View.VISIBLE);
			iImageSlotThree.setVisibility(View.VISIBLE);
		}

		if (itemImageThree != null && isSet(itemImageThree)) {
			iImageSlotThree.setVisibility(View.VISIBLE);
			iImageSlotThree.setImageBitmap(BitmapFactory.decodeFile(itemImageThree.getPath()));
			etItemImageThreeDescription.setVisibility(View.VISIBLE);
			iImageSlotFour.setVisibility(View.VISIBLE);
		}

		if (itemImageFour != null && isSet(itemImageFour)) {
			iImageSlotFour.setVisibility(View.VISIBLE);
			iImageSlotFour.setImageBitmap(BitmapFactory.decodeFile(itemImageFour.getPath()));
			etItemImageFourDescription.setVisibility(View.VISIBLE);
			iImageSlotFive.setVisibility(View.VISIBLE);
		}

		if (itemImageFive != null && isSet(itemImageFive)) {
			iImageSlotFive.setVisibility(View.VISIBLE);
			iImageSlotFive.setImageBitmap(BitmapFactory.decodeFile(itemImageFive.getPath()));
			etItemImageFiveDescription.setVisibility(View.VISIBLE);
		}

	} // End of dealWithNumberOfPhotoUploadBoxesOnScreen

	/*
	 * To save us a bit of long typing and long lines, this is a helper method that checks whether An image is set to
	 * "default" (deleted) Returns true if it is set, false it if is default
	 */
	private boolean isSet(TemporaryImage itemImage) {
		return !itemImage.getPath().contentEquals(C.ImageHandling.TAG_DEFAULT_AS_SET_IN_DATABASE);
	}

} // End of Class
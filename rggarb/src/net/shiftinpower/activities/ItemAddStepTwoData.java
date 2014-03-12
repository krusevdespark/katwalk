package net.shiftinpower.activities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import net.shiftinpower.asynctasks.AddNewItemToServerAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.KatwalkSlidingMenu;
import net.shiftinpower.interfaces.OnAddNewItemToServerListener;
import net.shiftinpower.interfaces.OnGetCategoriesListener;
import net.shiftinpower.interfaces.OnGetSubcategoriesListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.*;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.TemporaryImage;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.utilities.LoadSpinnerData;
import net.shiftinpower.utilities.Transporter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ItemAddStepTwoData extends KatwalkSlidingMenu implements OnAddNewItemToServerListener, OnGetCategoriesListener, OnGetSubcategoriesListener {

	// Set up XML View Components
	private TextView tvAddAnItemTitle;
	private TextView tvAddAnItemSubTitle;
	private EditText etAddAnItemName;
	private EditText etAddAnItemBrandManufacturer;
	private Spinner sAddAnItemCategory;
	private Spinner sAddAnItemSubcategory;
	private EditText etAddAnItemWhereWhoDidYouGetItFrom;
	private CheckBox cbAddAnItemIsGift;
	private EditText etAddAnItemPrice;
	private EditText etAddAnItemComment;
	private RadioGroup rgBoughtFromAUserOrAPlace;
	private RadioGroup rgItemCondition;
	private Button bAddAnItemSubmit;

	// Data holding variables
	private ArrayList<String> imageFilenames = new ArrayList<String>();
	private ArrayList<String> imageDescriptions = new ArrayList<String>();
	private boolean dataEntered = false;
	private String itemName;
	private String itemBrand;
	private String itemBoughtFrom;
	private String itemPriceString;
	// If the user doesnt touch anything category related, we need to ensure we arent sending an empty value or a 0 value for
	// itemCategoryID
	private int itemCategoryID = 1;
	private int itemSubcategoryID;
	private String itemDescription;
	private boolean itemNew = true;
	private boolean itemBoughtFromPlace = true;
	private boolean itemIsAGift = false;

	// Constructor needed because of the ActionBar
	public ItemAddStepTwoData() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate the XML layout and connect its components to the corresponding Java Objects
		setContentView(R.layout.activity_layout_item_add_step_two_data);

		tvAddAnItemTitle = (TextView) findViewById(R.id.tvAddAnItemTitle);
		tvAddAnItemSubTitle = (TextView) findViewById(R.id.tvAddAnItemSubTitle);
		etAddAnItemName = (EditText) findViewById(R.id.etAddAnItemName);
		etAddAnItemBrandManufacturer = (EditText) findViewById(R.id.etAddAnItemBrandManufacturer);
		etAddAnItemPrice = (EditText) findViewById(R.id.etAddAnItemPrice);
		etAddAnItemComment = (EditText) findViewById(R.id.etAddAnItemComment);
		sAddAnItemCategory = (Spinner) findViewById(R.id.sAddAnItemCategory);
		sAddAnItemSubcategory = (Spinner) findViewById(R.id.sAddAnItemSubcategory);
		etAddAnItemWhereWhoDidYouGetItFrom = (EditText) findViewById(R.id.etAddAnItemWhereWhoDidYouGetItFrom);
		rgBoughtFromAUserOrAPlace = (RadioGroup) findViewById(R.id.rgBoughtFromAUserOrAPlace);
		rgItemCondition = (RadioGroup) findViewById(R.id.rgItemCondition);
		cbAddAnItemIsGift = (CheckBox) findViewById(R.id.cbAddAnItemIsGift);
		bAddAnItemSubmit = (Button) findViewById(R.id.bAddAnItemSubmit);

		// Set the fonts
		try {
			tvAddAnItemTitle.setTypeface(katwalk.font1);
			tvAddAnItemSubTitle.setTypeface(katwalk.font2);
			bAddAnItemSubmit.setTypeface(katwalk.font1);
		} catch (Exception e) {
			e.printStackTrace();
			// Nothing can be done here
		}

		// Obtain the ItemImages filenames and descriptions so we can insert them into the database record for this item
		LinkedHashSet<TemporaryImage> itemImages = Transporter.instance().itemImages;
		for (TemporaryImage itemImage : itemImages) {
			imageFilenames.add(itemImage.getFilename());
			imageDescriptions.add(itemImage.getDescription());
		}

		etAddAnItemName.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO here the autosuggest feature will take place
				if (etAddAnItemName.getText().toString() != null) {
					dataEntered = true;
				}
			}
		});

		etAddAnItemBrandManufacturer.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO here the autosuggest feature will take place
				if (etAddAnItemBrandManufacturer.getText().toString() != null) {
					dataEntered = true;
				}
			}
		});

		etAddAnItemWhereWhoDidYouGetItFrom.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO here the autosuggest feature will take place
				if (etAddAnItemWhereWhoDidYouGetItFrom.getText().toString() != null) {
					dataEntered = true;
				}
			}
		});

		// Try populating spinners with Categories & Subcategories
		new GetCategoriesFromDB(ItemAddStepTwoData.this, ItemAddStepTwoData.this, katwalk.dbTools).execute();

		cbAddAnItemIsGift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (cbAddAnItemIsGift.isChecked()) {
					itemIsAGift = true;
					etAddAnItemPrice.setVisibility(View.GONE);
				} else {
					itemIsAGift = false;
					etAddAnItemPrice.setVisibility(View.VISIBLE);
				}

			}
		});

		rgBoughtFromAUserOrAPlace.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbBoughtFromUser) {
					itemBoughtFromPlace = false;
					etAddAnItemWhereWhoDidYouGetItFrom.setVisibility(View.GONE);
					itemBoughtFrom = "Person";
				} else if (checkedId == R.id.rbBoughtFromPlace) {
					itemBoughtFromPlace = true;
					etAddAnItemWhereWhoDidYouGetItFrom.setHint(R.string.etAddAnItemWhereDidYouGetItFrom);
				}
			}
		});

		rgItemCondition.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rbItemConditionNew) {
					itemNew = true;
				} else if (checkedId == R.id.rbItemConditionUsed) {
					itemNew = false;
				}
			}
		});

		sAddAnItemCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			// This count is needed to prevent the onItemSelected method to fire off on itself when the activity is created
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (count > 0) {
					ItemCategory itemCategory = (ItemCategory) arg0.getItemAtPosition(arg2);
					itemCategoryID = itemCategory.getId();
					
					new GetSubcategoriesFromDB(ItemAddStepTwoData.this, ItemAddStepTwoData.this, katwalk.dbTools, (sAddAnItemCategory.getSelectedItemPosition() + 1))
							.execute();

				}
				count++;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				ItemCategory item = (ItemCategory) arg0.getItemAtPosition(0);
				itemCategoryID = item.getId();
			}
		});// End of Category Spinner Handling

		sAddAnItemSubcategory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ItemSubcategory itemSubcategory = (ItemSubcategory) arg0.getItemAtPosition(arg2);
				itemSubcategoryID = itemSubcategory.getId();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				ItemSubcategory itemSubcategory = (ItemSubcategory) arg0.getItemAtPosition(0);
				itemSubcategoryID = itemSubcategory.getId();
			}
		}); // End of Subcategory Spinner Handling

		bAddAnItemSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				itemName = etAddAnItemName.getText().toString();
				itemBrand = etAddAnItemBrandManufacturer.getText().toString();

				itemBoughtFrom = etAddAnItemWhereWhoDidYouGetItFrom.getText().toString();

				itemPriceString = etAddAnItemPrice.getText().toString();
				itemDescription = etAddAnItemComment.getText().toString();

				if ((itemName.contentEquals("") || itemName == null)) {
					katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepTwoData.this, C.Errorz.ADDING_AN_ITEM_SOME_FIELDS_REQUIRED, Toast.LENGTH_LONG);

				} else {
					new AddNewItemToServerAsync(katwalk.dbTools, ItemAddStepTwoData.this, ItemAddStepTwoData.this, currentlyLoggedInUser, itemName, itemBrand, String
							.valueOf(itemCategoryID), String.valueOf(itemSubcategoryID), itemBoughtFrom, itemPriceString, imageFilenames, imageDescriptions,
							itemDescription, itemNew, itemBoughtFromPlace, itemIsAGift).execute();
				}
			}
		}); // End of Submit button click handling

	} // End of onCreate

	/*
	 * If the user accidentally presses the hardware back button, they will lose all their data that they've entered We want
	 * to give them a chance to stay on this screen if data bas been entered, so this is what this dialog is for
	 */
	@Override
	public void onBackPressed() {
		if (dataEntered == true) {
			Intent leaveScreenConfirmation = new Intent(ItemAddStepTwoData.this, LeaveScreenConfirmation.class);
			startActivityForResult(leaveScreenConfirmation, C.Miscellaneous.LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE);
		} else {
			super.onBackPressed();
		}

	} // End of onBackPressed

	@Override
	public void onAddNewItemSuccess(Bundle dataRegardingTheItemJustAdded) {

		// start an activity for providing place details and then providing photo descriptions
		/*
		 * if (itemBoughtFromPlace) { Intent addPlaceDetails = new Intent(this, PlaceEdit.class);
		 * addPlaceDetails.putExtras(dataRegardingTheItemJustAdded); startActivity(addPlaceDetails); } else { Intent
		 * itemAddPhotoDescriptions = new Intent(this, ItemProfile.class);
		 * itemAddPhotoDescriptions.putExtras(dataRegardingTheItemJustAdded); startActivity(itemAddPhotoDescriptions); }
		 */
		Intent home = new Intent(this, Home.class);
		home.putExtras(dataRegardingTheItemJustAdded);
		startActivity(home); // TODO this is a temporary action becase at the moment we arent ready with placeedit or
								// itemprofile classes

	} // End of onAddNewItemSuccess

	@Override
	public void onAddNewItemFailure(String reason) {
		if (reason.contentEquals(C.Tagz.BAD_REQUEST)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepTwoData.this, C.Errorz.ITEM_NOT_ADDED_BAD_REQUEST_EXCUSE, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.DB_PROBLEM)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepTwoData.this, C.Errorz.ITEM_NOT_ADDED_DB_PROBLEM_EXCUSE, Toast.LENGTH_LONG);
		} else if (reason.contentEquals(C.Tagz.UNKNOWN_PROBLEM)) {
			katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepTwoData.this, C.Errorz.ITEM_NOT_ADDED_UNKNOWN_PROBLEM_EXCUSE, Toast.LENGTH_LONG);
		}

	} // End of onAddNewItemFailure

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_CANCELED) {
			/*
			 * If the user has clicked the Back button,we have showed them a dialog asking whether they want to leave for
			 * sure. This is the callback if they've confirmed they want to leave
			 */
			if (requestCode == C.Miscellaneous.LEAVE_SCREEN_CONFIRMATION_REQUEST_CODE) {
				if (resultCode == RESULT_OK) {
					Intent home = new Intent(ItemAddStepTwoData.this, Home.class);
					startActivity(home);
					// TODO Start deleting images uploaded task
				}
			}
		}

	} // End of onActivityResult

	@Override
	public void onGetCategoriesSuccess(LinkedHashSet<ItemCategory> itemCategories) {

		// From the itemCategories object that we've received in the callback, we are obtaining category names
		ArrayList<ItemCategory> categoryNamesArray = new ArrayList<ItemCategory>();
		for (ItemCategory itemCategory : itemCategories) {
			categoryNamesArray.add(itemCategory);
		}

		// And loading those category names to the Spinner using this custom class
		LoadSpinnerData.loadCategoriesIntoSpinner(ItemAddStepTwoData.this, categoryNamesArray, sAddAnItemCategory);

		// Initially we are loading subcategories for the first category in the spinner, so we are passing 1 to the
		// constructor
		new GetSubcategoriesFromDB(ItemAddStepTwoData.this, ItemAddStepTwoData.this, katwalk.dbTools, 1).execute();

	}

	@Override
	public void onGetCategoriesFailure(String reason) {
		katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepTwoData.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_LONG);
	}

	@Override
	public void onGetSubcategoriesSuccess(LinkedHashSet<ItemSubcategory> itemSubcategories) {

		// From the itemSubcategories object that we've received in the callback, we are obtaining subcategory names
		ArrayList<ItemSubcategory> subcategoryNamesArray = new ArrayList<ItemSubcategory>();
		for (ItemSubcategory itemSubcategory : itemSubcategories) {
			subcategoryNamesArray.add(itemSubcategory);
		}

		// And loading those subcategory names to the Spinner using this custom class
		LoadSpinnerData.loadCategoriesIntoSpinner(ItemAddStepTwoData.this, subcategoryNamesArray, sAddAnItemSubcategory);

	}

	@Override
	public void onGetSubcategoriesFailure(String reason) {

		// Tell the user we have a problem
		katwalk.toastMaker.toast(net.shiftinpower.activities.ItemAddStepTwoData.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_LONG);

	}

} // End of Class
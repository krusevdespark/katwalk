package net.shiftinpower.activities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import net.shiftinpower.adapters.MyProfileItemAdapter;
import net.shiftinpower.core.*;
import net.shiftinpower.interfaces.OnGetCategoriesListener;
import net.shiftinpower.interfaces.OnGetUserItemsListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.*;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.utilities.LoadSpinnerData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Displaying a list of user's items here. Pagination still not implemented.
 * There is a filter - by category and search by name, brand and description with autocomplete and option to show/hide the filter
 * 
 * @author Kaloyan Roussev
 *
 */

public class MyProfileItems extends RggarbSlidingMenu implements OnGetUserItemsListener, OnGetCategoriesListener {

	// XML view elements
	private TextView tvMyItemsTitle;
	private EditText etMyItemsListSearch;
	private Spinner sMyItems;
	private Button tvMyItemsFilter;
	private LinearLayout llMyItemsFilterSection;

	// XML ListView
	private ListView listOfItems;
	private LinkedHashSet<ItemBasic> userItemsObtained;
	private LinkedHashSet<ItemCategory> categoriesInfo;
	private LinkedHashSet<ItemBasic> myItems;

	// If there is no data to be fed to the adapter, we need to display a custom layout with a call to action, these are its
	// XML elements
	private View myProfileItemsEmptyView;
	private Button bEmptyMyProfileItemsAddAnItem;
	private TextView tvEmptyMyProfileItems;

	// Constructor needed because of the action bar
	public MyProfileItems() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Obtain the userItems from Database
		new GetUserItemsFromDB(MyProfileItems.this, dbTools, currentlyLoggedInUser).execute();

		// Set the XML layout
		setContentView(R.layout.activity_layout_my_profile_items);
		tvMyItemsTitle = (TextView) findViewById(R.id.tvMyItemsTitle);
		etMyItemsListSearch = (EditText) findViewById(R.id.etMyItemsListSearch);
		sMyItems = (Spinner) findViewById(R.id.sMyItems);
		tvMyItemsFilter = (Button) findViewById(R.id.tvMyItemsFilter);
		llMyItemsFilterSection = (LinearLayout) findViewById(R.id.llMyItemsFilterSection);

		// Show/Hide the filters when the Filter button is clicked
		tvMyItemsFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (llMyItemsFilterSection.getVisibility() == View.GONE) {
					tvMyItemsFilter.setText("Hide");
					llMyItemsFilterSection.setVisibility(View.VISIBLE);

				} else {
					tvMyItemsFilter.setText(R.string.tvMyItemsFilter);
					llMyItemsFilterSection.setVisibility(View.GONE);

				}

			}
		});

		// Set the ListView to be used from the custom adapter
		listOfItems = (ListView) findViewById(R.id.lvMyProfileItemsHolder);

		listOfItems.setDivider(null);
		listOfItems.setDividerHeight(0);
		listOfItems.setBackgroundColor(this.getResources().getColor(R.color.grey_dark));
		listOfItems.setPadding(0, 10, 0, 0);
		listOfItems.setVerticalScrollBarEnabled(false);

		// If there is no data for the adapter, we display a custom layout with calls to action
		myProfileItemsEmptyView = (View) findViewById(R.id.flEmptyMyProfileItems);
		bEmptyMyProfileItemsAddAnItem = (Button) myProfileItemsEmptyView.findViewById(R.id.bEmptyMyProfileItemsAddAnItem);
		tvEmptyMyProfileItems = (TextView) myProfileItemsEmptyView.findViewById(R.id.tvEmptyMyProfileItems);
		listOfItems.setEmptyView(myProfileItemsEmptyView);

		// Try setting fonts for different XML views on screen
		try {
			tvMyItemsTitle.setTypeface(font1);
			tvEmptyMyProfileItems.setTypeface(font2);
			bEmptyMyProfileItemsAddAnItem.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// Set onclick listeners for the button/s in the empty layout
		bEmptyMyProfileItemsAddAnItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent addAnItem = new Intent(MyProfileItems.this, ItemAddStepOnePhotos.class);
				startActivity(addAnItem);
			}
		});

		// Get the categories from the DB and load them into the spinner
		new GetCategoriesFromDB(MyProfileItems.this, MyProfileItems.this, dbTools).execute();

	} // End of onCreate

	@Override
	public void onGetCategoriesSuccess(LinkedHashSet<ItemCategory> itemCategories) {

		// From the itemCategories object that we've received in the callback, we are obtaining category names
		ArrayList<ItemCategory> categoryNamesArray = new ArrayList<ItemCategory>();

		// Adding an "All" Option to the Spinner.
		ItemCategory allCategoriesWorkaround = new ItemCategory();
		allCategoriesWorkaround.setName("All Categories");
		allCategoriesWorkaround.setId(99);
		categoryNamesArray.add(allCategoriesWorkaround);

		for (ItemCategory itemCategory : itemCategories) {
			categoryNamesArray.add(itemCategory);
		}

		// And loading those category names to the Spinner using this custom class
		LoadSpinnerData.loadCategoriesIntoSpinner(MyProfileItems.this, categoryNamesArray, sMyItems);

		
		// Enable the user to filter the items by Category
		sMyItems.setOnItemSelectedListener(new OnItemSelectedListener() {

			// This count is needed to prevent the onItemSelected method to fire off on itself when the activity is created
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (count > 0) {
					ItemCategory itemCategory = (ItemCategory) arg0.getItemAtPosition(arg2);
					int selectedItemId = itemCategory.getId();

					if (userItemsObtained != null) {
						
						MyProfileItemAdapter myItemsItemAdapter;
						if (selectedItemId != 99) {
							myItemsItemAdapter = new MyProfileItemAdapter(MyProfileItems.this, userItemsObtained, selectedItemId);

						} else {
							myItemsItemAdapter = new MyProfileItemAdapter(MyProfileItems.this, userItemsObtained);

						}
						listOfItems.setAdapter(myItemsItemAdapter);
					}

				}
				count++;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}); // End of Spinner OnItemSelected Listener

	}

	@Override
	public void onGetCategoriesFailure(String reason) {
		toastMaker.toast(net.shiftinpower.activities.MyProfileItems.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_LONG);
	}

	@Override
	public void onGetUserItemsSuccess(LinkedHashSet<ItemBasic> userItems) {
		// Instantiate the adapter, feed the data to it via its constructor and set the listview to use it
		userItemsObtained = userItems;
		MyProfileItemAdapter myItemsItemAdapter = new MyProfileItemAdapter(MyProfileItems.this, userItems);
		listOfItems.setAdapter(myItemsItemAdapter);
		
		etMyItemsListSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				MyProfileItemAdapter myItemsItemAdapter = new MyProfileItemAdapter(MyProfileItems.this, userItemsObtained, s);
				listOfItems.setAdapter(myItemsItemAdapter);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				
			}
		}); // End of TextWatcher

	}

	@Override
	public void onGetUserItemsFailure(String reason) {
		toastMaker.toast(net.shiftinpower.activities.MyProfileItems.this, C.Errorz.PROBLEM_LOADING_USER_ITEMS, Toast.LENGTH_LONG);

	}

} // End of Class
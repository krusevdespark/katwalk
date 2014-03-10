package net.shiftinpower.activities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.shiftinpower.core.C;
import net.shiftinpower.core.RggarbSlidingMenu;
import net.shiftinpower.interfaces.OnGetCategoriesListener;
import net.shiftinpower.interfaces.OnGetSubcategoriesListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.localsqlitedb.*;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.utilities.LoadSpinnerData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchItems extends RggarbSlidingMenu implements OnGetCategoriesListener, OnGetSubcategoriesListener {

	// XML view elements
	private TextView tvSearchItemsTitle;
	private TextView tvSearchOrSearchUsers;
	private EditText etWhatSearch;
	private Spinner sCategorySearch;
	private Spinner sSubcategorySearch;
	private EditText etBrandSearch;
	private Spinner sCountrySearch;
	private EditText etCitySearch;
	private EditText etPlaceSearch;
	private EditText etPriceFromSearch;
	private EditText etPriceToSearch;
	private Spinner sRatingAboveSearch;
	private RadioGroup rgItemConditionSearch;
	private Button bSubmitSearch;

	// Data holding variables
	private LinkedHashSet<ItemCategory> categoriesInfo;
	private LinkedHashSet<ItemSubcategory> subcategoriesInfo;
	private int itemCategoryID = 1;

	// Constructor needed because of the action bar
	public SearchItems() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the XML layout
		setContentView(R.layout.activity_layout_search_items);

		tvSearchItemsTitle = (TextView) findViewById(R.id.tvSearchItemsTitle);
		tvSearchOrSearchUsers = (TextView) findViewById(R.id.tvSearchOrSearchUsers);
		etWhatSearch = (EditText) findViewById(R.id.etWhatSearch);
		sCategorySearch = (Spinner) findViewById(R.id.sCategorySearch);
		sSubcategorySearch = (Spinner) findViewById(R.id.sSubcategorySearch);
		etBrandSearch = (EditText) findViewById(R.id.etBrandSearch);
		sCountrySearch = (Spinner) findViewById(R.id.sCountrySearch);
		etCitySearch = (EditText) findViewById(R.id.etCitySearch);
		etPlaceSearch = (EditText) findViewById(R.id.etPlaceSearch);
		etPriceFromSearch = (EditText) findViewById(R.id.etPriceFromSearch);
		etPriceToSearch = (EditText) findViewById(R.id.etPriceToSearch);
		sRatingAboveSearch = (Spinner) findViewById(R.id.sRatingAboveSearch);
		rgItemConditionSearch = (RadioGroup) findViewById(R.id.rgItemConditionSearch);
		bSubmitSearch = (Button) findViewById(R.id.bSubmitSearch);

		// Try setting fonts for different XML views on screen
		try {
			tvSearchItemsTitle.setTypeface(font1);
			tvSearchOrSearchUsers.setTypeface(font2);
			bSubmitSearch.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		bSubmitSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchItemsResults = new Intent(SearchItems.this, SearchItemsResults.class);
				startActivity(searchItemsResults);

			}

		});

		new GetCategoriesFromDB(SearchItems.this, SearchItems.this, dbTools).execute();
		
		sCategorySearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			// This count is needed to prevent the onItemSelected method to fire off on itself when the activity is created
			int count = 0;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (count > 0) {
					ItemCategory itemCategory = (ItemCategory) arg0.getItemAtPosition(arg2);
					itemCategoryID = itemCategory.getId();
					count++;
					new GetSubcategoriesFromDB(SearchItems.this, SearchItems.this, dbTools, (sCategorySearch.getSelectedItemPosition() + 1))
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

		tvSearchOrSearchUsers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchUsers = new Intent(SearchItems.this, SearchUsers.class);
				startActivity(searchUsers);
			}

		});

	} // End of onCreate

	@Override
	public void onGetCategoriesSuccess(LinkedHashSet<ItemCategory> itemCategories) {

		// From the itemCategories object that we've received in the callback, we are obtaining category names
		ArrayList<ItemCategory> categoryNamesArray = new ArrayList<ItemCategory>();
		for (ItemCategory itemCategory : itemCategories) {
			categoryNamesArray.add(itemCategory);
		}

		// And loading those category names to the Spinner using this custom class
		LoadSpinnerData.loadCategoriesIntoSpinner(SearchItems.this, categoryNamesArray, sCategorySearch);

		// Initially we are loading subcategories for the first category in the spinner, so we are passing 1 to the
		// constructor
		new GetSubcategoriesFromDB(SearchItems.this, SearchItems.this, dbTools, 1).execute();

	}

	@Override
	public void onGetCategoriesFailure(String reason) {
		toastMaker.toast(net.shiftinpower.activities.SearchItems.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_LONG);
	}

	@Override
	public void onGetSubcategoriesSuccess(LinkedHashSet<ItemSubcategory> itemSubcategories) {

		// From the itemSubcategories object that we've received in the callback, we are obtaining subcategory names
		ArrayList<ItemSubcategory> subcategoryNamesArray = new ArrayList<ItemSubcategory>();
		for (ItemSubcategory itemSubcategory : itemSubcategories) {
			subcategoryNamesArray.add(itemSubcategory);
		}

		// And loading those subcategory names to the Spinner using this custom class
		LoadSpinnerData.loadCategoriesIntoSpinner(SearchItems.this, subcategoryNamesArray, sSubcategorySearch);

	}

	@Override
	public void onGetSubcategoriesFailure(String reason) {

		// Tell the user we have a problem
		toastMaker.toast(net.shiftinpower.activities.SearchItems.this, C.Errorz.CATEGORIES_NOT_LOADED_DUE_TO_UNKOWN_ERROR, Toast.LENGTH_LONG);

	}

} // End of Class
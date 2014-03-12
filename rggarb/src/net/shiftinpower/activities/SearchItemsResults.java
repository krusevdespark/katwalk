package net.shiftinpower.activities;

import net.shiftinpower.core.KatwalkSlidingMenu;
import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SearchItemsResults extends KatwalkSlidingMenu{
	
	// XML view elements
	private TextView tvSearchItemsResultsTitle;
	private TextView tvClearSearchFiltersSearchItemsResults;
	private TextView tvAppliedFiltersSearchItemsResults;
	private TextView tvAppliedFilterPropertyOneSearchItemsResults;
	private Button bModifyItemsSearch;
	private Spinner sSortBySearchItemsResults;
	private RadioGroup rgSortItemsByAscOrDesc;
	private FrameLayout flSearchItemsResultsHolder;
	private String sortBy;
	
	// Constructor needed because of the action bar
	public SearchItemsResults() {
		super(R.string.app_name);
	}
	
	private void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the XML layout
		setContentView(R.layout.activity_layout_search_items_results);
		
		tvSearchItemsResultsTitle = (TextView) findViewById(R.id.tvSearchItemsResultsTitle);
		tvClearSearchFiltersSearchItemsResults = (TextView) findViewById(R.id.tvClearSearchFiltersSearchItemsResults);
		tvAppliedFiltersSearchItemsResults = (TextView) findViewById(R.id.tvAppliedFiltersSearchItemsResults);
		tvAppliedFilterPropertyOneSearchItemsResults = (TextView) findViewById(R.id.tvAppliedFilterPropertyOneSearchItemsResults);
		bModifyItemsSearch = (Button) findViewById(R.id.bModifyItemsSearch);
		sSortBySearchItemsResults = (Spinner) findViewById(R.id.sSortBySearchItemsResults);
		rgSortItemsByAscOrDesc = (RadioGroup) findViewById(R.id.rgSortItemsByAscOrDesc);
		flSearchItemsResultsHolder = (FrameLayout) findViewById(R.id.flSearchItemsResultsHolder);
		
		// Try setting fonts for different XML views on screen
		try {
			tvSearchItemsResultsTitle.setTypeface(katwalk.font1);
			tvClearSearchFiltersSearchItemsResults.setTypeface(katwalk.font2);
			bModifyItemsSearch.setTypeface(katwalk.font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}
		
		// WWhen ModifyItemsSearch button is pressed, it redirects the user back to the SearchItems activity
		bModifyItemsSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent searchItems = new Intent(SearchItemsResults.this, SearchItems.class);
				startActivity(searchItems);
			}
		});
		
		// When the ClearSearchFilters text is pressed, it just shows all the results from the database
		tvClearSearchFiltersSearchItemsResults.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO do a search query for all items of table products
				
			}
		});
		
		// Populate the spinner with options
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortSearchItemsResultsBy, R.drawable.simple_spinner_item);
		adapter.setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
		sSortBySearchItemsResults.setAdapter(adapter);
		
		// Set spinner listener
		sSortBySearchItemsResults.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setSortBy((arg0.getItemAtPosition(arg2)).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		// Set on checked changed listener for the radiogroup so it changes the way results are displayed
		rgSortItemsByAscOrDesc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
			}
		});
		
	} // End of onCreate
	
} // End of Class
package net.shiftinpower.activities;

import net.shiftinpower.core.KatwalkSlidingMenu;
import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SearchUsersResults extends KatwalkSlidingMenu {
	
	// XML view elements
	private TextView tvSearchUsersResultsTitle;
	private TextView tvClearSearchFiltersSearchUsersResults;
	private TextView tvAppliedFiltersSearchUsersResults;
	private TextView tvAppliedFilterPropertyOneSearchUsersResults;
	private Button bModifyUsersSearch;
	private Spinner sSortBySearchUsersResults;
	private RadioGroup rgSortUsersByAscOrDesc;
	private FrameLayout flSearchUsersResultsHolder;
	private String sortBy;
	
	// Constructor needed because of the action bar
	public SearchUsersResults() {
		super(R.string.app_name);
	}

	private void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the XML layout
		setContentView(R.layout.activity_layout_search_users_results);

		tvSearchUsersResultsTitle = (TextView) findViewById(R.id.tvSearchUsersResultsTitle);
		tvClearSearchFiltersSearchUsersResults = (TextView) findViewById(R.id.tvClearSearchFiltersSearchUsersResults);
		tvAppliedFiltersSearchUsersResults = (TextView) findViewById(R.id.tvAppliedFiltersSearchUsersResults);
		tvAppliedFilterPropertyOneSearchUsersResults = (TextView) findViewById(R.id.tvAppliedFilterPropertyOneSearchUsersResults);
		bModifyUsersSearch = (Button) findViewById(R.id.bModifyUsersSearch);
		sSortBySearchUsersResults = (Spinner) findViewById(R.id.sSortBySearchUsersResults);
		rgSortUsersByAscOrDesc = (RadioGroup) findViewById(R.id.rgSortUsersByAscOrDesc);
		flSearchUsersResultsHolder = (FrameLayout) findViewById(R.id.flSearchUsersResultsHolder);
		
		// Try setting fonts for different XML views on screen
		try {
			tvSearchUsersResultsTitle.setTypeface(katwalk.font1);
			tvClearSearchFiltersSearchUsersResults.setTypeface(katwalk.font2);
			bModifyUsersSearch.setTypeface(katwalk.font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// When ModifyItemsSearch button is pressed, it redirects the user back to the SearchItems activity
		bModifyUsersSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchUsers = new Intent(SearchUsersResults.this, SearchUsers.class);
				startActivity(searchUsers);
			}
		});

		// When the ClearSearchFilters text is pressed, it just shows all the results from the database
		tvClearSearchFiltersSearchUsersResults.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO do a search query for all items from table users

			}
		});

		// Set on checked changed listener for the radiogroup so it changes the way results are displayed
		rgSortUsersByAscOrDesc.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

			}
		});

		// Populate the spinner with options
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortSearchUsersResultsBy, R.drawable.simple_spinner_item);
		adapter.setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
		sSortBySearchUsersResults.setAdapter(adapter);

		// Set spinner listener
		sSortBySearchUsersResults.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setSortBy((arg0.getItemAtPosition(arg2)).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
	} // End of onCreate
	
} // End of Class
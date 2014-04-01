package net.shiftinpower.activities.person;

import java.util.ArrayList;

import net.shiftinpower.activities.Home;
import net.shiftinpower.activities.SearchItems;
import net.shiftinpower.adapters.PersonProfileFollowingAdapter;
import net.shiftinpower.core.*;
import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PersonProfileFollowing extends KatwalkSlidingMenu {

	// XML view elements
	private TextView tvUserNameFollowingList;
	private EditText etFollowingListSearch;
	
	// XML ListView
	private ListView listOfFollowedItems;
	
	// Data fed to the adapter
	private ArrayList<String> itemNames = new ArrayList<String>();
	private ArrayList<String> itemBrands = new ArrayList<String>();
	private ArrayList<Integer> itemPrices = new ArrayList<Integer>();
	private ArrayList<Double> itemRatings = new ArrayList<Double>();
	private ArrayList<String> itemsBoughtFrom = new ArrayList<String>();
	private ArrayList<String> itemImagePaths = new ArrayList<String>();
	private ArrayList<Integer> imageRatingsCount = new ArrayList<Integer>();
	private ArrayList<Integer> itemIds = new ArrayList<Integer>();
	
	// If there is no data to be fed to the adapter, we need to display a custom layout with a call to action, these are its XML elements
	private View myProfileFollowingsEmptyView;
	private Button bEmptyMyProfileFollowingsSearchItems;
	private Button bEmptyMyProfileFollowingsVisitItemsFeed;
	private TextView tvEmptyMyProfileFollowings;

	// Constructor needed because of the action bar
	public PersonProfileFollowing() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the XML layout
		setContentView(R.layout.activity_layout_person_profile_following);
		
		// Assign java objects to XML View elements
		tvUserNameFollowingList = (TextView) findViewById(R.id.tvUserNameFollowingList);
		etFollowingListSearch = (EditText) findViewById(R.id.etFollowingListSearch);
		
		// Set the ListView to be used from the custom adapter
		listOfFollowedItems = (ListView) findViewById(R.id.llMyProfileFollowingHolder);

		// If there is no data for the adapter, we display a custom layout with calls to action
		myProfileFollowingsEmptyView = (View) findViewById(R.id.flEmptyMyProfileFollowings);
		bEmptyMyProfileFollowingsSearchItems = (Button) myProfileFollowingsEmptyView.findViewById(R.id.bEmptyMyProfileFollowingsSearchItems);
		bEmptyMyProfileFollowingsVisitItemsFeed = (Button) myProfileFollowingsEmptyView.findViewById(R.id.bEmptyMyProfileFollowingsVisitItemsFeed);
		tvEmptyMyProfileFollowings = (TextView) myProfileFollowingsEmptyView.findViewById(R.id.tvEmptyMyProfileFollowings);
		listOfFollowedItems.setEmptyView(myProfileFollowingsEmptyView);

		// Instantiate the adapter, feed the data to it via its constructor and set the listview to use it
		PersonProfileFollowingAdapter myProfileFollowingAdapter = new PersonProfileFollowingAdapter(this, itemNames, itemBrands, itemPrices, itemRatings, itemsBoughtFrom, itemImagePaths, itemIds, imageRatingsCount);
		listOfFollowedItems.setAdapter(myProfileFollowingAdapter);

		// Try setting fonts for different XML views on screen
		try {
			tvUserNameFollowingList.setTypeface(katwalk.font1);
			bEmptyMyProfileFollowingsVisitItemsFeed.setTypeface(katwalk.font1);
			bEmptyMyProfileFollowingsSearchItems.setTypeface(katwalk.font1);
			tvEmptyMyProfileFollowings.setTypeface(katwalk.font2);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}
		
		// Set onclick listeners for the button/s in the empty layout
		bEmptyMyProfileFollowingsSearchItems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchItems = new Intent(PersonProfileFollowing.this, SearchItems.class);
				startActivity(searchItems);
			}
		});

		bEmptyMyProfileFollowingsVisitItemsFeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent visitItemsFeed = new Intent(PersonProfileFollowing.this, Home.class);
				startActivity(visitItemsFeed);
			}
		});
		
		//TODO create a TextWatcher for this
		//etFollowingListSearch
		
	} // End of onCreate

} // End of Class
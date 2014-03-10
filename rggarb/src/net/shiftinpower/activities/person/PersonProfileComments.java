package net.shiftinpower.activities.person;

import java.util.ArrayList;

import net.shiftinpower.activities.Home;
import net.shiftinpower.activities.SearchItems;
import net.shiftinpower.adapters.MyProfileCommentAdapter;
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

public class PersonProfileComments extends RggarbSlidingMenu {

	// XML view elements
	private TextView tvUserNameMyReviewsList;
	private EditText etMyReviewsListSearch;

	// XML list view
	private ListView listOfComments;

	// Data fed to the adapter
	private ArrayList<String> itemNames = new ArrayList<String>();
	private ArrayList<Double> itemRatings = new ArrayList<Double>();
	private ArrayList<String> itemComments = new ArrayList<String>();
	private ArrayList<String> itemCommentDates = new ArrayList<String>();
	private ArrayList<Integer> itemIds = new ArrayList<Integer>();

	// If there is no data to be fed to the adapter, we need to display a custom layout with a call to action, these are its XML elements
	private View myProfileCommentsEmptyView;
	private Button bEmptyMyProfileCommentsSearchItems;
	private Button bEmptyMyProfileCommentsVisitItemsFeed;
	private TextView tvEmptyMyProfileComments;

	// Constructor needed because of the action bar
	public PersonProfileComments() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the XML layout
		setContentView(R.layout.activity_layout_my_profile_comments);
		tvUserNameMyReviewsList = (TextView) findViewById(R.id.tvUserNameMyCommentsList);
		etMyReviewsListSearch = (EditText) findViewById(R.id.etMyCommentsListSearch);

		// set the ListView to be used from the custom adapter
		listOfComments = (ListView) findViewById(R.id.lvMyProfileCommentsHolder);
		
		//if there is no data for the adapter, we display a custom layout with calls to action
		myProfileCommentsEmptyView = (View) findViewById(R.id.flEmptyMyProfileComments);
		bEmptyMyProfileCommentsSearchItems = (Button) myProfileCommentsEmptyView.findViewById(R.id.bEmptyMyProfileCommentsSearchItems);
		bEmptyMyProfileCommentsVisitItemsFeed = (Button) myProfileCommentsEmptyView.findViewById(R.id.bEmptyMyProfileCommentsVisitItemsFeed);
		tvEmptyMyProfileComments = (TextView) myProfileCommentsEmptyView.findViewById(R.id.tvEmptyMyProfileComments);
		listOfComments.setEmptyView(myProfileCommentsEmptyView);
		
		//instantiate the adapter, feed the data to it via its constructor and set the listview to use it
		MyProfileCommentAdapter myProfileCommentAdapter = new MyProfileCommentAdapter(this, itemNames, itemRatings, itemComments, itemCommentDates, itemIds);
		listOfComments.setAdapter(myProfileCommentAdapter);
		
		// Try setting fonts for different XML views on screen
		try {
			tvUserNameMyReviewsList.setTypeface(font1);
			bEmptyMyProfileCommentsSearchItems.setTypeface(font1);
			bEmptyMyProfileCommentsVisitItemsFeed.setTypeface(font1);
			tvEmptyMyProfileComments.setTypeface(font2);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}
		
		// Set onclick listeners for the button/s in the empty layout
		bEmptyMyProfileCommentsSearchItems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchItems = new Intent(PersonProfileComments.this, SearchItems.class);
				startActivity(searchItems);
			}
		});

		bEmptyMyProfileCommentsVisitItemsFeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent visitItemsFeed = new Intent(PersonProfileComments.this, Home.class);
				startActivity(visitItemsFeed);
			}
		});
		
		//TODO add a textwatcher here
		//etMyReviewsListSearch

	} // End of onCreate
	
} // End of Class
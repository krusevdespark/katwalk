package net.shiftinpower.activities;

import net.shiftinpower.core.RggarbSlidingMenu;
import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchUsers extends RggarbSlidingMenu{
	
	// XML view elements
	private TextView tvSearchUsersTitle;
	private TextView tvSearchOrSearchItems;
	private EditText etNameSearchUsers;
	private EditText etEmailSearchUsers;
	private Spinner sCountrySearchUsers;
	private Button bSubmitSearchUsers;
	
	// Constructor needed because of the action bar
	public SearchUsers() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the XML layout
		setContentView(R.layout.activity_layout_search_users);
		
		tvSearchUsersTitle = (TextView) findViewById(R.id.tvSearchUsersTitle);
		tvSearchOrSearchItems = (TextView) findViewById(R.id.tvSearchOrSearchItems);
		etNameSearchUsers = (EditText) findViewById(R.id.etNameSearchUsers);
		etEmailSearchUsers = (EditText) findViewById(R.id.etEmailSearchUsers);
		sCountrySearchUsers = (Spinner) findViewById(R.id.sCountrySearchUsers);
		bSubmitSearchUsers = (Button) findViewById(R.id.bSubmitSearchUsers);
		
		// Try setting fonts for different XML views on screen
		try {
			tvSearchUsersTitle.setTypeface(font1);
			tvSearchOrSearchItems.setTypeface(font2);
			bSubmitSearchUsers.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}
		
		bSubmitSearchUsers.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent searchUsersResults = new Intent(SearchUsers.this, SearchUsersResults.class);
				startActivity(searchUsersResults);
			}
			
		});
		
		tvSearchOrSearchItems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent searchItems = new Intent(SearchUsers.this, SearchItems.class);
				startActivity(searchItems);
			}
		});
		
	} // End of onCreate
	
} //End of Class
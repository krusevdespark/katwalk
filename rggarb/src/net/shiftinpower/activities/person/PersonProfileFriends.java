package net.shiftinpower.activities.person;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import net.shiftinpower.activities.SearchUsers;
import net.shiftinpower.adapters.MyProfileFriendAdapter;
import net.shiftinpower.asynctasks.GetUserFriendsFromServerAsync;
import net.shiftinpower.core.*;
import net.shiftinpower.interfaces.OnGetUserFriendsListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.UserBasic;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PersonProfileFriends extends KatwalkSlidingMenu implements OnGetUserFriendsListener {

	// XML view elements
	private TextView tvUserNameMyFriendsList;
	private TextView tvMyItemsFeedItemName;
	private EditText etMyFriendsListSearch;

	// XML ListView
	private ListView listOfFriends;

	// Data fed to the adapter
	private ArrayList<String> userNames = new ArrayList<String>();
	private ArrayList<String> userAvatarPaths = new ArrayList<String>();
	private ArrayList<Integer> userIds = new ArrayList<Integer>();

	// If there is no data to be fed to the adapter, we need to display a custom layout with a call to action, these are its
	// XML elements
	private View myProfileFriendsEmptyView;
	private Button bEmptyMyProfileFriendsFindFriends;
	private Button bEmptyMyProfileFriendsInviteFBFriends;
	private TextView tvEmptyMyProfileFriends;

	private int personId;

	// Constructor needed because of the action bar
	public PersonProfileFriends() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			personId = extras.getInt("personId");
		}

		new GetUserFriendsFromServerAsync(PersonProfileFriends.this, personId, PersonProfileFriends.this).execute();

		// Set the XML layout
		setContentView(R.layout.activity_layout_my_profile_friends);

		// Assign java objects to XML View elements
		tvUserNameMyFriendsList = (TextView) findViewById(R.id.tvUserNameMyFriendsList);
		etMyFriendsListSearch = (EditText) findViewById(R.id.etMyFriendsListSearch);

		// Set the ListView to be used from the custom adapter
		listOfFriends = (ListView) findViewById(R.id.lvMyProfileFriendsHolder);

		// If there is no data for the adapter, we display a custom layout with calls to action
		myProfileFriendsEmptyView = (View) findViewById(R.id.flEmptyMyProfileFriends);
		bEmptyMyProfileFriendsFindFriends = (Button) myProfileFriendsEmptyView.findViewById(R.id.bEmptyMyProfileFriendsFindFriends);
		bEmptyMyProfileFriendsInviteFBFriends = (Button) myProfileFriendsEmptyView.findViewById(R.id.bEmptyMyProfileFriendsInviteFBFriends);
		tvEmptyMyProfileFriends = (TextView) myProfileFriendsEmptyView.findViewById(R.id.tvEmptyMyProfileFriends);
		listOfFriends.setEmptyView(myProfileFriendsEmptyView);

		// Try setting fonts for different XML views on screen
		try {
			tvUserNameMyFriendsList.setTypeface(katwalk.font1);
			bEmptyMyProfileFriendsFindFriends.setTypeface(katwalk.font1);
			bEmptyMyProfileFriendsInviteFBFriends.setTypeface(katwalk.font1);
			tvEmptyMyProfileFriends.setTypeface(katwalk.font2);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// Set onclick listeners for the button/s in the empty layout
		bEmptyMyProfileFriendsFindFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent findFriends = new Intent(PersonProfileFriends.this, SearchUsers.class);
				startActivity(findFriends);
			}
		});

		bEmptyMyProfileFriendsInviteFBFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
			}
		});

		// TODO textwatcher
		// etMyFriendsListSearch
	} // End of onCreate

	@Override
	public void onGetUserFriendsSuccess(LinkedHashSet<UserBasic> userFriends) {
		
		// Instantiate the adapter, feed the data to it via its constructor and set the listview to use it
		MyProfileFriendAdapter myProfileFriendAdapter = new MyProfileFriendAdapter(PersonProfileFriends.this, katwalk.imageLoader, katwalk.imageLoaderOptions, userFriends);
		listOfFriends.setAdapter(myProfileFriendAdapter);

	}

	@Override
	public void onGetUserFriendsFailure() {
		// TODO Auto-generated method stub

	}

} // End of Class
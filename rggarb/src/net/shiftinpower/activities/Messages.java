package net.shiftinpower.activities;

import java.util.ArrayList;

import net.shiftinpower.adapters.MessagesAdapter;
import net.shiftinpower.core.KatwalkSlidingMenu;
import net.shiftinpower.koldrain.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
*
* NOTE: This is just a draft ot the class with the Empty List View implemented
*
* @author Kaloyan Kalinov
*
*/

public class Messages extends KatwalkSlidingMenu {

	// XML view elements
	private TextView tvMessagesTitle;
	private EditText etMessagesSearch;
	private TextView tvMessagesWhoCanSendMeMessagesNote;

	// XML ListView
	private ListView listOfMessages;

	// Data fed to the adapter
	private ArrayList<String> userNames = new ArrayList<String>();
	private ArrayList<String> userAvatarPaths = new ArrayList<String>();
	private ArrayList<String> datesOfMessages = new ArrayList<String>();
	private ArrayList<String> messageContents = new ArrayList<String>();
	private ArrayList<Integer> userIds = new ArrayList<Integer>();

	// If there is no data to be fed to the adapter, we need to display a custom layout with a call to action, these are its
	// XML elements
	private View myMessagesEmptyView;
	private TextView tvEmptyMessages;

	// Constructor needed because of the action bar
	public Messages() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the XML layout
		setContentView(R.layout.activity_layout_messages);
		tvMessagesTitle = (TextView) findViewById(R.id.tvMessagesTitle);
		etMessagesSearch = (EditText) findViewById(R.id.etMessagesSearch);
		tvMessagesWhoCanSendMeMessagesNote = (TextView) findViewById(R.id.tvMessagesWhoCanSendMeMessagesNote);

		// The "Who can send me messages" text is a button that leads to Settings Screen
		tvMessagesWhoCanSendMeMessagesNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent settings = new Intent(Messages.this, Settings.class);
				startActivity(settings);

			}
		});

		// Set the ListView to be used from the custom adapter
		listOfMessages = (ListView) findViewById(R.id.lvMessagesHolder);

		// If there is no data for the adapter, we display a custom layout with calls to action
		myMessagesEmptyView = (View) findViewById(R.id.flEmptyMessages);
		tvEmptyMessages = (TextView) myMessagesEmptyView.findViewById(R.id.tvEmptyMessages);
		listOfMessages.setEmptyView(myMessagesEmptyView);

		// Instantiate the adapter, feed the data to it via its constructor and set the listview to use it
		MessagesAdapter messagesAdapter = new MessagesAdapter(this, userNames, userAvatarPaths, datesOfMessages, messageContents, userIds);
		listOfMessages.setAdapter(messagesAdapter);

		// Try setting fonts for different XML views on screen
		try {
			tvMessagesTitle.setTypeface(katwalk.font1);
			tvEmptyMessages.setTypeface(katwalk.font2);
		} catch (Exception e) {
		}

		// TODO textwatcher
		// etMessagesSearch

	} // End of onCreate

} // End of Class
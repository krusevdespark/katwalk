package net.shiftinpower.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import net.shiftinpower.asynctasks.ContactUsSendEmail;
import net.shiftinpower.asynctasks.ForgottenPasswordGetUserNameAndSendEmail;
import net.shiftinpower.core.C;
import net.shiftinpower.core.RggarbSlidingMenu;
import net.shiftinpower.interfaces.OnContactUsSendEmailListener;
import net.shiftinpower.koldrain.R;

public class ContactUs extends RggarbSlidingMenu implements OnContactUsSendEmailListener{
	
	// Set up XML View Components
	private TextView tvContactUsTitle;
	private TextView tvContactUsSubject;
	private Spinner sContactUsLineOfInquiry;
	private EditText etContactUsEnterYourMessage;
	private Button bContactUsSubmit;

	// Variables holding data
	private String contactUsEmailLineOfInquiry;
	private String contactUsEmailContent;

	// Constructor needed because of the way the super class works
	public ContactUs() {
		super(R.string.app_name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Assign and inflate an XML file as the view component for this screen
		setContentView(R.layout.activity_layout_contact_us);
		
		tvContactUsTitle = (TextView) findViewById(R.id.tvContactUsTitle);
		tvContactUsSubject = (TextView) findViewById(R.id.tvContactUsSubject);
		sContactUsLineOfInquiry = (Spinner) findViewById(R.id.sContactUsLineOfInquiry);
		etContactUsEnterYourMessage = (EditText) findViewById(R.id.etContactUsEnterYourMessage);
		bContactUsSubmit = (Button) findViewById(R.id.bContactUsSubmit);
		
		// Set fonts
		try {
			tvContactUsTitle.setTypeface(font1);
			bContactUsSubmit.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// Populate the spinner with options
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sContactUsLineOfInquiry, R.drawable.simple_spinner_item);
		adapter.setDropDownViewResource(R.drawable.simple_spinner_dropdown_item);
		sContactUsLineOfInquiry.setAdapter(adapter);
		
		// Set spinner listeners
		sContactUsLineOfInquiry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				contactUsEmailLineOfInquiry = (arg0.getItemAtPosition(arg2)).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				contactUsEmailLineOfInquiry = (arg0.getItemAtPosition(0)).toString();
			}
		}); // End of Spinner Handling
		
		
		bContactUsSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(!isEditTextEmpty(etContactUsEnterYourMessage)){
					contactUsEmailContent  = etContactUsEnterYourMessage.getText().toString();
					new ContactUsSendEmail(ContactUs.this, ContactUs.this, userEmail, contactUsEmailLineOfInquiry, contactUsEmailContent).execute();
				} else {
					toastMaker.toast(net.shiftinpower.activities.ContactUs.this, C.Errorz.NO_MESSAGE_ENTERED, Toast.LENGTH_SHORT);
				}
				
			}
		});
		
	} // End of onCreate

	@Override
	public void onContactUsSendEmailSuccess() {
		toastMaker.toast(net.shiftinpower.activities.ContactUs.this, C.Confirmationz.INQUIRY_SENT, Toast.LENGTH_SHORT);
		Intent home = new Intent(ContactUs.this, Home.class);
		startActivity(home);
		finish();
	}

	@Override
	public void onContactUsSendEmailFailure() {
		toastMaker.toast(net.shiftinpower.activities.ContactUs.this, C.Errorz.MESSAGE_NOT_SENT, Toast.LENGTH_LONG);
		
	}
	
} // End of Class

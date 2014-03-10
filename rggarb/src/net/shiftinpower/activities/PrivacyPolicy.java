package net.shiftinpower.activities;

import net.shiftinpower.core.RggarbSlidingMenu;
import net.shiftinpower.koldrain.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

public class PrivacyPolicy extends RggarbSlidingMenu {
	
	private TextView tvPrivacyPolicyTitle;
	private TextView tvPrivacyPolicyContent;
	//SpannableString privacyPolicyRichTextContent = new SpannableString("src/assets/privacy_policy.xml"); // This doesnt work
	
	public PrivacyPolicy() {
		super(R.string.app_name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_privacy_policy);
		
		tvPrivacyPolicyTitle = (TextView) findViewById(R.id.tvPrivacyPolicyTitle);
		tvPrivacyPolicyContent = (TextView) findViewById(R.id.tvPrivacyPolicyContent);
		
		//tvPrivacyPolicyContent.setText(privacyPolicyRichTextContent);
		
	}
}

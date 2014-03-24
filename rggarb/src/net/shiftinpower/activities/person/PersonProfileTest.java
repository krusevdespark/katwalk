package net.shiftinpower.activities.person;

import net.shiftinpower.koldrain.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public abstract class PersonProfileTest extends RelativeLayout{

	public PersonProfileTest(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    initImpl();
	}

	final private void initImpl() {
	    ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.activity_layout_user_profile, this, true);
	    init();
	}

	protected abstract void init();
	
}

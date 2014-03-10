package net.shiftinpower.fragments;

import net.shiftinpower.koldrain.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * This is one of the Sliding Menu items used by the ViewPager at MainActivity
 * 
 * @author Kaloyan Roussev
 * 
 */
public class WelcomeSlideFragmentA extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.viewpager_slide_welcome_b, container, false);

		return rootView;
	}
} // End of Class

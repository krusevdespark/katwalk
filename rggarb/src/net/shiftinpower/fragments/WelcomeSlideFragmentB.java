package net.shiftinpower.fragments;

import net.shiftinpower.koldrain.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class WelcomeSlideFragmentB extends SherlockFragment{
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        ViewGroup rootView = (ViewGroup) inflater.inflate(
	                R.layout.viewpager_slide_welcome_a, container, false);

	        return rootView;
	    }

} // End of Class

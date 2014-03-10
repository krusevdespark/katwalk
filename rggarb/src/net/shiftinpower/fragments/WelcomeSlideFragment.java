package net.shiftinpower.fragments;

import net.shiftinpower.koldrain.R;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragment;

public class WelcomeSlideFragment extends SherlockFragment {

	private String TAG_ARGUMENT = "visiblePage";
	private ImageView ivViewPagerSlide;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.viewpager_slide_welcome_a, container, false);
		ivViewPagerSlide = (ImageView) rootView.findViewById(R.id.ivViewPagerSlide);

		int visiblePage = getArguments().getInt(TAG_ARGUMENT);
		switch (visiblePage) {

		case 1:

			ivViewPagerSlide.setBackgroundResource(R.drawable.images_splash_d);
			return rootView;
		case 2:

			ivViewPagerSlide.setBackgroundResource(R.drawable.images_splash_c);
			return rootView;

		case 3:

			ivViewPagerSlide.setBackgroundResource(R.drawable.images_splash_a);
			return rootView;

		case 4:

			ivViewPagerSlide.setBackgroundResource(R.drawable.images_splash_b);
			return rootView;

		case 0:

			ivViewPagerSlide.setBackgroundResource(R.drawable.images_splash_e);
			return rootView;

		}
		return rootView;
	} // End of onCreateView

	@Override
	public void onDestroyView() {
		
		Log.d("Kylie", "Im in onDestroyView");
		
		Drawable drawable = ivViewPagerSlide.getDrawable();
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			bitmap.recycle();
		}
		super.onDestroyView();
	}

	public SherlockFragment newInstance(int visiblePage) {

		WelcomeSlideFragment welcomeSlideFragment = new WelcomeSlideFragment();

		Bundle arguments = new Bundle();
		arguments.putInt(TAG_ARGUMENT, visiblePage);

		welcomeSlideFragment.setArguments(arguments);
		return welcomeSlideFragment;
	}
} // End of Class

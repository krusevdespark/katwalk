package net.shiftinpower.fragments;

import net.shiftinpower.koldrain.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	private Bitmap bitmap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.viewpager_slide_welcome, container, false);
		ivViewPagerSlide = (ImageView) rootView.findViewById(R.id.ivViewPagerSlide);

		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inDither = false;
		bitmapOptions.inPurgeable = true;
		bitmapOptions.inInputShareable = true;
		bitmapOptions.inTempStorage = new byte[16 * 1024];

		int visiblePage = getArguments().getInt(TAG_ARGUMENT);
		switch (visiblePage) {

		case 1:
			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_splash_d, bitmapOptions);
			ivViewPagerSlide.setImageBitmap(bitmap);
			return rootView;
		case 2:

			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_splash_c, bitmapOptions);
			ivViewPagerSlide.setImageBitmap(bitmap);
			return rootView;

		case 3:

			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_splash_a, bitmapOptions);
			ivViewPagerSlide.setImageBitmap(bitmap);
			return rootView;

		case 4:

			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_splash_b, bitmapOptions);
			ivViewPagerSlide.setImageBitmap(bitmap);
			return rootView;

		case 0:

			bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.images_splash_e, bitmapOptions);
			ivViewPagerSlide.setImageBitmap(bitmap);
			return rootView;

		}
		return rootView;
	} // End of onCreateView

	@Override
	public void onDestroyView() {

		// Prevent memory leak by releasing the bitmaps from the memory
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

package net.shiftinpower.customviews;

import android.content.Context;
import android.util.AttributeSet;

import com.applidium.shutterbug.FetchableImageView;

/**
 * FetchableImageView is a custom ImageView class that comes with the Shutterburg lazy load library We extend it here to
 * further customize it, this time to force certain ImageViews to be SQUARE
 * 
 * @author Kaloyan Roussev
 * 
 */
public class SquareFetchableImageView extends FetchableImageView {

	public SquareFetchableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		setMeasuredDimension(width, width);
	}

}
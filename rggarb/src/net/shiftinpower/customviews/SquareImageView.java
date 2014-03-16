package net.shiftinpower.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
* Force certain ImageViews to be SQUARE
* For this to be used in XML, replace ImageView withnet.shiftinpower.customviews.SquareImageView
*  
* @author Kaloyan Roussev
*
*/

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

}
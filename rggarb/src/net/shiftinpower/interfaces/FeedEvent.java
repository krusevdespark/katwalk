package net.shiftinpower.interfaces;

import android.view.View;

/**
* This is probably going to be used by the Activity Stream where we are going to use different types of Items to feed the
* ListView with
*
* @author Kaloyan Roussev
*
*/

public interface FeedEvent {
    public View getView(View convertView);
    public int getViewType();
}

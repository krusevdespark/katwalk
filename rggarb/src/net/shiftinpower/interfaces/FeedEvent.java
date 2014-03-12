package net.shiftinpower.interfaces;

import android.view.View;

public interface FeedEvent {
    public View getView(View convertView);
    public int getViewType();
}

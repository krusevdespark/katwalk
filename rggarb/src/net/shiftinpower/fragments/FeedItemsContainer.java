package net.shiftinpower.fragments;

import net.shiftinpower.koldrain.R;

import com.actionbarsherlock.app.SherlockListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedItemsContainer extends SherlockListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] values = new String[] { "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3", "Samsung Galaxy S3" };

		
		//TODO use the custom adapter here instead of this shit
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.item_adapterable_feed_item_available_at_place, R.id.tvItemsFeedItemName, values);
		setListAdapter(adapter);
		
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
		getListView().setBackgroundColor(getActivity().getResources().getColor(R.color.grey_dark));
		getListView().setPadding(0, 10, 0, 0);
		getListView().setVerticalScrollBarEnabled(false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// do something with the data

	}
} // End of Class
       
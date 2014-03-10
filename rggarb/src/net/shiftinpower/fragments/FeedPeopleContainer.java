package net.shiftinpower.fragments;

import java.util.ArrayList;

import net.shiftinpower.adapters.MyProfileFollowingAdapter;
import net.shiftinpower.koldrain.R;

import com.actionbarsherlock.app.SherlockListFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * This is just a draft
 * @author Kaloyan Roussev
 *
 */
public class FeedPeopleContainer extends SherlockListFragment {
				
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	
		ViewGroup listView = (ViewGroup) inflater.inflate(R.layout.activity_layout_feed_people, container, false);
		ListView feedPeople = (ListView) listView.findViewById(android.R.id.list);

		//MyProfileFollowingAdapter myProfileFollowingAdapter = new MyProfileFollowingAdapter(getActivity(), itemNames, itemBrands, itemPrices, itemRatings, itemsBoughtFrom, itemImagePaths, itemIds, imageRatingsCount);
		//feedPeople.setAdapter(myProfileFollowingAdapter);

		return listView;
	}

} // End of Class

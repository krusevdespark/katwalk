package net.shiftinpower.adapters;

import java.util.ArrayList;

import net.shiftinpower.koldrain.R;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * Just a draft
 * 
 * @author Kaloyan Roussev
 * 
 */
public class MyProfileFollowingAdapter extends BaseAdapter {

	private ArrayList<String> itemNames =  new ArrayList<String>();
	private ArrayList<String> itemBrands = new ArrayList<String>();
	private ArrayList<Integer> itemPrices = new ArrayList<Integer>();
	private ArrayList<Double> itemRatings = new ArrayList<Double>();
	private ArrayList<String> itemsBoughtFrom  = new ArrayList<String>();
	private ArrayList<String> itemImagePaths   = new ArrayList<String>();
	private ArrayList<Integer> itemRatingsCount = new ArrayList<Integer>();
	private ArrayList<Integer> itemIds = new ArrayList<Integer>();
	private Context context;
	private LayoutInflater layoutInflater;
	
	private ImageButton iFollowingFeedItemImage;
	private TextView tvFollowingListItemName;
	private TextView tvFollowingFeedItemPrice;
	private TextView tvFollowingFeedItemRatingAndComments;
	private TextView tvFollowingFeedItemBrandName;
	private TextView tvFollowingFeedItemBoughtFrom;
	private Button bFollowingFeedActionButtonRate;
	private Button bFollowingFeedActionButtonComment;
	private Button bFollowingFeedActionButtonUnfollow;
	private Button bFollowingFeedActionButtonSell;
	
	public MyProfileFollowingAdapter(Context context, ArrayList<String> itemNames, ArrayList<String> itemBrands, ArrayList<Integer> itemPrices, ArrayList<Double> itemRatings, ArrayList<String> itemsBoughtFrom, ArrayList<String> itemImagePaths, ArrayList<Integer> itemIds, ArrayList<Integer> itemRatingsCount){
		
		this.context = context;
		this.itemNames = itemNames;
		this.itemBrands = itemBrands;
		this.itemPrices = itemPrices;
		this.itemRatings = itemRatings;
		this.itemsBoughtFrom = itemsBoughtFrom;
		this.itemImagePaths = itemImagePaths;
		this.itemRatingsCount = itemRatingsCount;
		this.itemIds = itemIds;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return itemIds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return itemIds.get(arg0)	;
	}

	@Override
	public long getItemId(int position) {
		return itemIds.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			convertView = layoutInflater.inflate(R.layout.item_adapterable_my_profile_followed_item, parent, false);
		}
		
		iFollowingFeedItemImage = (ImageButton) convertView.findViewById(R.id.iFollowingFeedItemImage);
		tvFollowingListItemName = (TextView) convertView.findViewById(R.id.tvFollowingListItemName);
		tvFollowingFeedItemPrice = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemPrice);
		tvFollowingFeedItemRatingAndComments = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemRatingAndComments);
		tvFollowingFeedItemBrandName = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemBrandName);
		tvFollowingFeedItemBoughtFrom = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemBoughtFrom);
		bFollowingFeedActionButtonRate = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonRate);
		bFollowingFeedActionButtonComment = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonComment);
		bFollowingFeedActionButtonUnfollow = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonUnfollow);
		bFollowingFeedActionButtonSell = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonSell);	
		
		tvFollowingListItemName.setText(itemNames.get(position));
		tvFollowingFeedItemPrice.setText("$"+String.valueOf(itemPrices.get(position)));
		tvFollowingFeedItemRatingAndComments.setText(String.valueOf(itemRatings.get(position)) + " /10 (" + itemRatingsCount + ")");
		tvFollowingFeedItemBrandName.setText(itemBrands.get(position));
		tvFollowingFeedItemBoughtFrom.setText(itemsBoughtFrom.get(position));
		
		iFollowingFeedItemImage.setImageBitmap(BitmapFactory.decodeFile(itemImagePaths.get(position)));
		
		
		
		return convertView;
		
	} // End of getView
	
} // End of Class
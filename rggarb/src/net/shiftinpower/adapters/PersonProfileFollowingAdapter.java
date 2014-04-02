package net.shiftinpower.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.shiftinpower.activities.item.ItemProfilePrivate;
import net.shiftinpower.adapters.PersonProfileItemsAdapter.ViewHolder;
import net.shiftinpower.core.C;
import net.shiftinpower.customviews.SquareImageView;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.utilities.Transporter;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PersonProfileFollowingAdapter extends BaseAdapter {

	private ArrayList<String> itemNames = new ArrayList<String>();
	private ArrayList<String> itemBrands = new ArrayList<String>();
	private ArrayList<Double> itemPrices = new ArrayList<Double>();
	private ArrayList<String> itemImages = new ArrayList<String>();
	private ArrayList<Integer> itemRatings = new ArrayList<Integer>();
	private ArrayList<String> itemsBoughtFromPlaceName = new ArrayList<String>();
	private ArrayList<String> itemImagePaths = new ArrayList<String>();
	private ArrayList<Integer> itemRatingsCount = new ArrayList<Integer>();
	private ArrayList<Integer> itemIds = new ArrayList<Integer>();
	private Context context;
	private LayoutInflater layoutInflater;
	
	private LinkedHashSet<ItemBasic> followedItems;
	private int itemId;
	private int selectedCategoryId;
	private CharSequence autocompleteInput;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	static class ViewHolder {
		SquareImageView iFollowingFeedItemImage;
		TextView tvFollowingListItemName;
		TextView tvFollowingFeedItemPrice;
		TextView tvFollowingFeedItemRatingAndComments;
		TextView tvFollowingFeedItemBrandName;
		TextView tvFollowingFeedItemBoughtFrom;
		Button bFollowingFeedActionButtonRate;
		Button bFollowingFeedActionButtonComment;
		Button bFollowingFeedActionButtonUnfollow;
		Button bFollowingFeedActionButtonSell;
	}
	
	public PersonProfileFollowingAdapter(Context context, ImageLoader imageLoader, DisplayImageOptions options, LinkedHashSet<ItemBasic> followedItems){
		
		this.context = context;
		this.followedItems = followedItems;
		this.imageLoader = imageLoader;
		this.options = options;
		
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		prepareItemsList(followedItems);

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
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			convertView = layoutInflater.inflate(R.layout.item_adapterable_my_profile_followed_item, parent, false);
		}
		
		ViewHolder holder = new ViewHolder();
		
		holder.iFollowingFeedItemImage = (SquareImageView) convertView.findViewById(R.id.iFollowingFeedItemImage);
		holder.tvFollowingListItemName = (TextView) convertView.findViewById(R.id.tvFollowingListItemName);
		holder.tvFollowingFeedItemPrice = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemPrice);
		holder.tvFollowingFeedItemRatingAndComments = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemRatingAndComments);
		holder.tvFollowingFeedItemBrandName = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemBrandName);
		holder.tvFollowingFeedItemBoughtFrom = (TextView) convertView.findViewById(R.id.tvFollowingFeedItemBoughtFrom);
		holder.bFollowingFeedActionButtonRate = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonRate);
		holder.bFollowingFeedActionButtonComment = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonComment);
		holder.bFollowingFeedActionButtonUnfollow = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonUnfollow);
		holder.bFollowingFeedActionButtonSell = (Button) convertView.findViewById(R.id.bFollowingFeedActionButtonSell);	
		
		convertView.setTag(holder);
		
		holder.tvFollowingListItemName.setText(itemNames.get(position));
		holder.tvFollowingFeedItemPrice.setText("$"+String.valueOf(itemPrices.get(position)));
		holder.tvFollowingFeedItemRatingAndComments.setText(String.valueOf(itemRatings.get(position)) + " /10 (" + itemRatingsCount + ")");
		holder.tvFollowingFeedItemBrandName.setText(itemBrands.get(position));
		holder.tvFollowingFeedItemBoughtFrom.setText(itemsBoughtFromPlaceName.get(position));
		
		imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_THUMBNAIL + itemImages.get(position), holder.iFollowingFeedItemImage, options);
		
		if (itemRatings.get(position) == 0) {
			holder.tvFollowingFeedItemRatingAndComments.setText("No ratings yet");
		} else {
			holder.tvFollowingFeedItemRatingAndComments.setText(String.valueOf(itemRatings.get(position)) + "/10 (1)"); // 1
																														// for
																														// now
		}
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				itemId = (int) getItemId(position);

				ItemBasic selectedItem = null;
				Iterator<ItemBasic> itererator = followedItems.iterator();
				while ((selectedItem == null) && (itererator.hasNext())) {
					ItemBasic local = itererator.next();
					if (local.getItemId() == itemId) {
						selectedItem = local;
					}
				}

				Transporter.instance().userItem = selectedItem;

				Intent itemProfile = new Intent(context, ItemProfilePrivate.class);
				context.startActivity(itemProfile);
			}
		});
		
		return convertView;
		
	} // End of getView
	
	private void prepareItemsList(LinkedHashSet<ItemBasic> myItems) {
		for (ItemBasic userItem : myItems) {
			fillDataInArrays(userItem);
		}
	}

	private void prepareItemsList(LinkedHashSet<ItemBasic> myItems, int selectedCategoryId) {
		for (ItemBasic userItem : myItems) {
			if (userItem.getItemCategory() == selectedCategoryId) {
				fillDataInArrays(userItem);
			}
		}
	}

	private void prepareItemsList(LinkedHashSet<ItemBasic> myItems, CharSequence autocompleteInput) {
		for (ItemBasic userItem : myItems) {

			if (userItem.getItemBrand().toLowerCase(Locale.getDefault()).contains((autocompleteInput.toString()).toLowerCase())
					|| userItem.getItemName().toLowerCase(Locale.getDefault()).contains((autocompleteInput.toString()).toLowerCase())
					|| userItem.getItemDescription().toLowerCase(Locale.getDefault()).contains((autocompleteInput.toString()).toLowerCase())) {

				fillDataInArrays(userItem);
			}
		}
	}

	private void fillDataInArrays(ItemBasic userItem) {
		itemIds.add(userItem.getItemId());
		itemNames.add(userItem.getItemName());
		itemBrands.add(userItem.getItemBrand());
		itemPrices.add(userItem.getItemPriceAquired());
		itemRatings.add(userItem.getItemRating());
		itemImages.add(userItem.getItemImage());
		itemsBoughtFromPlaceName.add(userItem.getItemBoughtFromPlaceName());
	}
	
} // End of Class
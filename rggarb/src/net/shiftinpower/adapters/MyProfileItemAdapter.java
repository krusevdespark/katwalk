package net.shiftinpower.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.shiftinpower.activities.item.ItemProfileMine;
import net.shiftinpower.activities.item.ItemProfilePublic;
import net.shiftinpower.core.C;
import net.shiftinpower.customviews.SquareImageView;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.utilities.Transporter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MyProfileItemAdapter extends BaseAdapter {

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
	private LinkedHashSet<ItemBasic> myItems;
	private LayoutInflater layoutInflater;
	private int itemId;
	private int selectedCategoryId;
	private CharSequence autocompleteInput;
	private ImageLoader imageLoader;

	static class ViewHolder {
		SquareImageView iMyItemsFeedItemImage;
		TextView tvMyItemsFeedItemName;
		TextView tvMyItemsFeedItemPrice;
		TextView tvMyItemsFeedItemRatingAndComments;
		TextView tvMyItemsFeedItemBrandName;
		// TODO go to strings and chang from bought from to gotten from
		TextView tvMyItemsFeedItemBoughtFrom;
		// TODO add an edit item button
		Button bMyItemsFeedActionButtonRate;
		Button bMyItemsFeedActionButtonComment;
		Button bMyItemsFeedActionButtonDelete;
		Button bMyItemsFeedActionButtonSell;
	}

	// Default Constructor
	public MyProfileItemAdapter(Context context, LinkedHashSet<ItemBasic> myItems) {

		this.context = context;
		this.myItems = myItems;
		
		imageLoader = ImageLoader.getInstance();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		prepareItemsList(myItems);
		
	}

	// Constructor allowing items from a given category to be displayed only
	public MyProfileItemAdapter(Context context, LinkedHashSet<ItemBasic> myItems, int selectedCategoryId) {

		this.context = context;
		this.myItems = myItems;
		this.selectedCategoryId = selectedCategoryId;
		
		imageLoader = ImageLoader.getInstance();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		prepareItemsList(myItems, selectedCategoryId);
		
	}

	// Constructor used from the search edittext, passing a part of a string, so the adapter only shows items who correspond
	// to it
	public MyProfileItemAdapter(Context context, LinkedHashSet<ItemBasic> myItems, CharSequence autocompleteInput) {

		this.context = context;
		this.myItems = myItems;
		this.autocompleteInput = autocompleteInput;
		
		imageLoader = ImageLoader.getInstance();
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		prepareItemsList(myItems, autocompleteInput);
		
	}

	@Override
	public int getCount() {
		return itemIds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return itemIds.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return itemIds.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_adapterable_my_profile_item, parent, false);
		}
		ViewHolder holder = new ViewHolder();
		holder.iMyItemsFeedItemImage = (SquareImageView) convertView.findViewById(R.id.iMyItemsFeedItemImage);
		holder.tvMyItemsFeedItemName = (TextView) convertView.findViewById(R.id.tvMyItemsFeedItemName);
		holder.tvMyItemsFeedItemPrice = (TextView) convertView.findViewById(R.id.tvMyItemsFeedItemPrice);
		holder.tvMyItemsFeedItemRatingAndComments = (TextView) convertView.findViewById(R.id.tvMyItemsFeedItemRatingAndComments);
		holder.tvMyItemsFeedItemBrandName = (TextView) convertView.findViewById(R.id.tvMyItemsFeedItemBrandName);
		holder.tvMyItemsFeedItemBoughtFrom = (TextView) convertView.findViewById(R.id.tvMyItemsFeedItemBoughtFrom);
		holder.bMyItemsFeedActionButtonRate = (Button) convertView.findViewById(R.id.bMyItemsFeedActionButtonRate);
		holder.bMyItemsFeedActionButtonComment = (Button) convertView.findViewById(R.id.bMyItemsFeedActionButtonComment);
		holder.bMyItemsFeedActionButtonDelete = (Button) convertView.findViewById(R.id.bMyItemsFeedActionButtonDelete);
		holder.bMyItemsFeedActionButtonSell = (Button) convertView.findViewById(R.id.bMyItemsFeedActionButtonSell);

		convertView.setTag(holder);

		holder.tvMyItemsFeedItemName.setText(itemNames.get(position));
		holder.tvMyItemsFeedItemPrice.setText("$" + String.valueOf(itemPrices.get(position)));

		holder.tvMyItemsFeedItemBrandName.setText(itemBrands.get(position));
		holder.tvMyItemsFeedItemBoughtFrom.setText(itemsBoughtFromPlaceName.get(position));

		holder.iMyItemsFeedItemImage.setVisibility(View.VISIBLE);
		//holder.iMyItemsFeedItemImage.setImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_THUMBNAIL + itemImages.get(position),
				//R.drawable.images_default_product);
		
		
		imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_THUMBNAIL + itemImages.get(position), holder.iMyItemsFeedItemImage);

		if (itemRatings.get(position) == 0) {
			holder.tvMyItemsFeedItemRatingAndComments.setText("No ratings yet");
		} else {
			holder.tvMyItemsFeedItemRatingAndComments.setText(String.valueOf(itemRatings.get(position)) + "/10 (1)"); // 1
																														// for
																														// now
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				itemId = (int) getItemId(position);

				ItemBasic selectedItem = null;
				Iterator<ItemBasic> itererator = myItems.iterator();
				while ((selectedItem == null) && (itererator.hasNext())) {
					ItemBasic local = itererator.next();
					if (local.getItemId() == itemId) {
						selectedItem = local;
					}
				}

				Transporter.instance().userItem = selectedItem;

				Intent itemProfile = new Intent(context, ItemProfileMine.class);
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
package net.shiftinpower.activities.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.shiftinpower.activities.PlaceProfile;
import net.shiftinpower.asynctasks.GetItemDataFromServerAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.RggarbSlidingMenu;
import net.shiftinpower.customviews.SquareImageView;
import net.shiftinpower.interfaces.OnGetItemDataListener;
import net.shiftinpower.koldrain.R;
import net.shiftinpower.objects.Brand;
import net.shiftinpower.objects.Image;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemComment;
import net.shiftinpower.objects.ItemExtended;
import net.shiftinpower.objects.Rating;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.objects.Place;
import net.shiftinpower.objects.UserBasic;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.utilities.Transporter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ItemProfileMine extends RggarbSlidingMenu implements OnGetItemDataListener {

	// Set up XML View Components
	private SquareImageView iItemProfilePrivateImageSlotOne;
	private TextView tvItemProfilePrivateItemName;
	private TextView tvItemProfilePrivateBrandName;
	private TextView tvItemProfilePrivatePriceValue;
	private TextView tvItemProfilePrivateDescription;
	private TextView tvItemProfilePrivateAddedTimestamp;
	private TextView tvItemProfilePrivateGottenFromHeading;
	private TextView tvItemProfilePrivateGottenFromPlaceUserName;
	private Button bItemProfilePrivateMainCategory;
	private Button bItemProfilePrivateMainSubcategory;
	private Button bItemProfilePrivateEditItem;
	private Button bItemProfilePrivateDeleteItem;
	private Button bItemProfilePrivateSellItem;
	private Button bItemProfilePrivateViewPublicProfile;

	// Variables holding data
	private ItemBasic itemBasic;
	private Brand brand;
	private LinkedHashSet<Image> itemImages;
	private LinkedHashSet<ItemCategory> itemCategories;
	private LinkedHashSet<ItemSubcategory> itemSubcategories;
	private LinkedHashSet<Place> itemPlaces;
	private String[] imageUrls;
	int[] necessaryImageViews;
	private String itemCategory;
	private String itemSubcategory;
	String itemPlaceName;
	int itemPlaceId;
	
	private ImageLoader imageLoader = ImageLoader.getInstance();

	// Constructor needed because of the way the super class works
	public ItemProfileMine() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_item_profile_mine);

		// Assign and inflate an XML file as the view component for this screen
		iItemProfilePrivateImageSlotOne = (SquareImageView) findViewById(R.id.iItemProfilePrivateImageSlotOne);
		tvItemProfilePrivateItemName = (TextView) findViewById(R.id.tvItemProfilePrivateItemName);
		tvItemProfilePrivateBrandName = (TextView) findViewById(R.id.tvItemProfilePrivateBrandName);
		tvItemProfilePrivatePriceValue = (TextView) findViewById(R.id.tvItemProfilePrivatePriceValue);
		tvItemProfilePrivateDescription = (TextView) findViewById(R.id.tvItemProfilePrivateDescription);
		tvItemProfilePrivateAddedTimestamp = (TextView) findViewById(R.id.tvItemProfilePrivateAddedTimestamp);
		tvItemProfilePrivateGottenFromHeading = (TextView) findViewById(R.id.tvItemProfilePrivateGottenFromHeading);
		tvItemProfilePrivateGottenFromPlaceUserName = (TextView) findViewById(R.id.tvItemProfilePrivateGottenFromPlaceUserName);
		bItemProfilePrivateMainCategory = (Button) findViewById(R.id.bItemProfilePrivateMainCategory);
		bItemProfilePrivateMainSubcategory = (Button) findViewById(R.id.bItemProfilePrivateMainSubcategory);
		bItemProfilePrivateEditItem = (Button) findViewById(R.id.bItemProfilePrivateEditItem);
		bItemProfilePrivateDeleteItem = (Button) findViewById(R.id.bItemProfilePrivateDeleteItem);
		bItemProfilePrivateSellItem = (Button) findViewById(R.id.bItemProfilePrivateSellItem);
		bItemProfilePrivateViewPublicProfile = (Button) findViewById(R.id.bItemProfilePrivateViewPublicProfile);

		// Set fonts
		try {
			tvItemProfilePrivateItemName.setTypeface(font2);
			tvItemProfilePrivateBrandName.setTypeface(font1);
			tvItemProfilePrivatePriceValue.setTypeface(font2);
			bItemProfilePrivateViewPublicProfile.setTypeface(font1);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// Obtain the itemId from the intent that started this activity
		ItemBasic userItem = Transporter.instance().userItem;

		new GetItemDataFromServerAsync(this, this, userItem.getItemId(), currentlyLoggedInUser).execute();

	} // End of onCreate

	@Override
	public void onGetItemDataSuccess(ItemExtended itemParameters) {

		// Unpack the data
		itemBasic = itemParameters.getItemBasic();
		brand = itemParameters.getBrand();
		itemImages = itemParameters.getItemImages();
		itemCategories = itemParameters.getItemCategories();
		itemSubcategories = itemParameters.getItemSubcategories();
		itemPlaces = itemParameters.getItemPlaces();

		// Set texts
		tvItemProfilePrivateItemName.setText(itemBasic.getItemName());
		if(!brand.getBrandName().contentEquals("")){
			tvItemProfilePrivateBrandName.setText(brand.getBrandName());
		} else {
			tvItemProfilePrivateBrandName.setText(C.FallbackCopy.NO_BRAND); // TODO Extract these to a FallBackCopy subclass of C
		}
				
		if (C.Miscellaneous.CURRENCY_SYMBOL_BEFORE_AMOUNT) {
			tvItemProfilePrivatePriceValue.setText(C.Miscellaneous.CURRENCY_SYMBOL + String.valueOf(itemBasic.getItemPriceAquired()));
		} else {
			tvItemProfilePrivatePriceValue.setText(String.valueOf(itemBasic.getItemPriceAquired()) + " " + C.Miscellaneous.CURRENCY_SYMBOL);
		}

		if (itemBasic.getItemDescription() != null && !itemBasic.getItemDescription().contentEquals("")) {
			tvItemProfilePrivateDescription.setText(itemBasic.getItemDescription());
			tvItemProfilePrivateDescription.setVisibility(View.VISIBLE);
		}

		tvItemProfilePrivateAddedTimestamp.setText(itemBasic.getItemTimeAdded());

		if (itemPlaces != null) {

			Iterator<Place> itemPlacesIterator = itemPlaces.iterator();
			while (itemPlacesIterator.hasNext()) {
				Place itemPlace = itemPlacesIterator.next();
				itemPlaceName = itemPlace.getPlaceName();
				itemPlaceId = itemPlace.getPlaceId();
			}

			if (itemBasic.isItemBoughtFromPlace()) {
				if(!itemPlaceName.contentEquals("")){
					tvItemProfilePrivateGottenFromPlaceUserName.setText(itemPlaceName);
				} else {
					tvItemProfilePrivateGottenFromPlaceUserName.setText(C.FallbackCopy.PLACE_HAS_NO_NAME);
				}
				
			} else {
				tvItemProfilePrivateGottenFromPlaceUserName.setText(C.FallbackCopy.BOUGHT_FROM_PERSON);
			}

			tvItemProfilePrivateGottenFromHeading.setVisibility(View.VISIBLE);
			tvItemProfilePrivateGottenFromPlaceUserName.setVisibility(View.VISIBLE);

			tvItemProfilePrivateGottenFromPlaceUserName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent goToPlaceProfile = new Intent(ItemProfileMine.this, PlaceProfile.class);
					goToPlaceProfile.putExtra("placeId", itemPlaceId);
					// startActivity(goToPlaceProfile); // TODO uncomment this later when PlaceProfile is ready
				}
			});

		}

		// Item can have several categories. Get one of them and display it
		Iterator<ItemCategory> categoriesIterator = itemCategories.iterator();
		try {
			itemCategory = categoriesIterator.next().getName();
			bItemProfilePrivateMainCategory.setText(itemCategory);
		} catch (Exception e) {
			itemCategory = C.FallbackCopy.NO_CATEGORY;
			bItemProfilePrivateMainCategory.setVisibility(View.GONE);
		}

		// Do the same for subcategory - show the first one as the name of the button. If the user clicks the button
		// They will get a list of all subcategories and if they click one of them they will get lots of items from the same
		// subcategory
		Iterator<ItemSubcategory> subcategoriesIterator = itemSubcategories.iterator();
		try {
			itemSubcategory = subcategoriesIterator.next().getName();
			bItemProfilePrivateMainSubcategory.setText(itemSubcategory);
		} catch (Exception e) {
			itemSubcategory = C.FallbackCopy.NO_SUBCATEGORY; // Just in case
			bItemProfilePrivateMainSubcategory.setVisibility(View.GONE);
		}

		bItemProfilePrivateViewPublicProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent itemProfile = new Intent(ItemProfileMine.this, ItemProfilePublic.class);
				startActivity(itemProfile);

			}
		});

		// Set item's main set of images
		Iterator<Image> imagesIterator = itemImages.iterator();
		int itemImagesCount = itemImages.size();
		imageUrls = new String[itemImagesCount];
		int i = 0;
		while (imagesIterator.hasNext()) {
			imageUrls[i] = imagesIterator.next().getImageUrl();
			i++;
		}

		// An item cannot be added if there is no at least one image, so we always have the first image
		imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_ORIGINAL + imageUrls[0],iItemProfilePrivateImageSlotOne);
		
		int[] initialImageViews = { R.id.iItemProfilePrivateImageSlotTwo, R.id.iItemProfilePrivateImageSlotThree, R.id.iItemProfilePrivateImageSlotFour,
				R.id.iItemProfilePrivateImageSlotFive };

		for (int x = 0; x < (itemImagesCount - 1); x++) { // -1 because one is already used at iItemProfileImageSlotOne
			necessaryImageViews = new int[itemImagesCount - 1];
			necessaryImageViews[x] = initialImageViews[x];
			SquareImageView fetchableImageView = (SquareImageView) findViewById(necessaryImageViews[x]);
			fetchableImageView.setVisibility(View.VISIBLE);
			imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_ORIGINAL + imageUrls[x + 1], fetchableImageView);
		}
	}

	@Override
	public void onGetItemDataFailure() {
		// TODO USE THE ITEM BASIC AND APOLOGIZE FOR THE INCONVENIENCE

	}

} // End of Class

// private int itemId;
// private int transactionId;
// private String timeItemWasAddedToDB;
// itemId = dataRegardingTheItemJustAdded.getInt("itemId");
// transactionId = dataRegardingTheItemJustAdded.getInt("transactionId");
// timeItemWasAddedToDB = dataRegardingTheItemJustAdded.getString("timeItemWasAddedToDB");
package net.shiftinpower.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.applidium.shutterbug.FetchableImageView;

import net.shiftinpower.asynctasks.GetItemDataFromServerAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.RggarbSlidingMenu;
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
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ItemProfileOtherUsers extends RggarbSlidingMenu implements OnGetItemDataListener {

	// Set up XML View Components
	private TextView tvItemProfileItemName;
	private TextView tvItemProfileBrandName;
	private TextView tvItemProfilePhotosValue;
	private TextView tvItemProfileAveragePriceValue;
	private TextView tvItemProfileRatingValue;
	private TextView tvItemProfileFollowedByValue;
	private TextView tvItemProfileActionCallToRateAndComment;
	private TextView tvItemProfileActionCallToFollow;
	private TextView tvItemProfileOwnedByUsersValue;
	private TextView tvItemProfileOwnedByUsersHeading;
	private TextView tvItemProfileOwnedByFriendsValue;
	private TextView tvItemProfileOfferedAtPlacesValue;
	private TextView tvItemProfileOfferedAtPlacesHeading;
	private TextView tvItemProfileUsersSellingTheirItemsValue;
	private TextView tvItemProfileCommentsValue;
	private TextView tvItemProfileSimilarItemsValue;
	private Button bItemProfileMainCategory;
	private Button bItemProfileMainSubcategory;
	private Button bItemProfileAddYours;
	private FetchableImageView iItemProfileImageSlotOne;
	private FetchableImageView iItemProfileImageSlotTwo;
	private FetchableImageView iItemProfileImageSlotThree;
	private FetchableImageView iItemProfileImageSlotFour;
	private FetchableImageView iItemProfileImageSlotFive;

	// Variables holding data
	private int itemId;
	private ItemBasic itemBasic;
	private Brand brand;
	private LinkedHashSet<Image> itemImages;
	private LinkedHashSet<ItemCategory> itemCategories;
	private LinkedHashSet<ItemSubcategory> itemSubcategories;
	private LinkedHashSet<UserBasic> itemUsers;
	private LinkedHashSet<Place> itemPlaces;
	private LinkedHashSet<ItemComment> itemComments;
	private LinkedHashSet<Rating> itemRatings;
	private ArrayList<Integer> itemFollowers;
	private double itemAverageRating;
	private String itemCategory;
	private String itemSubcategory;
	private String[] imageUrls;
	private String[] itemUsersAvatarsUrls;
	int[] necessaryImageViews;
	int[] necessaryUserImageViews;

	// Constructor needed because of the way the super class works
	public ItemProfileOtherUsers() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_item_profile_public);

		// Assign and inflate an XML file as the view component for this screen
		tvItemProfileItemName = (TextView) findViewById(R.id.tvItemProfileItemName);
		tvItemProfileBrandName = (TextView) findViewById(R.id.tvItemProfileBrandName);
		iItemProfileImageSlotOne = (FetchableImageView) findViewById(R.id.iItemProfileImageSlotOne);
		tvItemProfilePhotosValue = (TextView) findViewById(R.id.tvItemProfilePhotosValue);
		tvItemProfileAveragePriceValue = (TextView) findViewById(R.id.tvItemProfileAveragePriceValue);
		tvItemProfileRatingValue = (TextView) findViewById(R.id.tvItemProfileRatingValue);
		tvItemProfileFollowedByValue = (TextView) findViewById(R.id.tvItemProfileFollowedByValue);
		tvItemProfileOwnedByUsersValue = (TextView) findViewById(R.id.tvItemProfileOwnedByUsersValue);
		tvItemProfileOwnedByUsersHeading = (TextView) findViewById(R.id.tvItemProfileOwnedByUsersHeading);
		tvItemProfileOwnedByFriendsValue = (TextView) findViewById(R.id.tvItemProfileOwnedByFriendsValue);
		tvItemProfileOfferedAtPlacesValue = (TextView) findViewById(R.id.tvItemProfileOfferedAtPlacesValue);
		tvItemProfileOfferedAtPlacesHeading = (TextView) findViewById(R.id.tvItemProfileOfferedAtPlacesHeading);
		tvItemProfileUsersSellingTheirItemsValue = (TextView) findViewById(R.id.tvItemProfileUsersSellingTheirItemsValue);
		tvItemProfileCommentsValue = (TextView) findViewById(R.id.tvItemProfileCommentsValue);
		tvItemProfileSimilarItemsValue = (TextView) findViewById(R.id.tvItemProfileSimilarItemsValue);
		bItemProfileAddYours = (Button) findViewById(R.id.bItemProfileAddYours);
		bItemProfileMainCategory = (Button) findViewById(R.id.bItemProfileMainCategory);
		bItemProfileMainSubcategory = (Button) findViewById(R.id.bItemProfileMainSubcategory);

		// Set fonts
		try {
			tvItemProfileItemName.setTypeface(font2);
			tvItemProfileBrandName.setTypeface(font1);
			tvItemProfilePhotosValue.setTypeface(font2);
			tvItemProfileAveragePriceValue.setTypeface(font2);
			tvItemProfileRatingValue.setTypeface(font2);
			tvItemProfileFollowedByValue.setTypeface(font2);
			tvItemProfileOwnedByUsersValue.setTypeface(font2);
			tvItemProfileOwnedByFriendsValue.setTypeface(font2);
			tvItemProfileOfferedAtPlacesValue.setTypeface(font2);
			tvItemProfileUsersSellingTheirItemsValue.setTypeface(font2);
			tvItemProfileCommentsValue.setTypeface(font2);
			tvItemProfileSimilarItemsValue.setTypeface(font2);
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
		itemUsers = itemParameters.getItemUsers();
		itemPlaces = itemParameters.getItemPlaces();
		itemComments = itemParameters.getItemComments();
		itemRatings = itemParameters.getItemRatings();
		itemFollowers = itemParameters.getItemFollowers();

		// Calculate average price
		// TODO int averagePrice = //

		// Calculate rating
		Iterator<Rating> ratingsIterator = itemRatings.iterator();
		int ratingsSum = 0;
		while (ratingsIterator.hasNext()) {
			ratingsSum += ratingsIterator.next().getItemRating();
		}
		if (itemRatings.size() != 0) { // Avoid ArithmeticException Divide by zero
			itemAverageRating = ratingsSum / itemRatings.size();
		} else {
			itemAverageRating = 0;
		}

		// Set texts
		tvItemProfileItemName.setText(itemBasic.getItemName());
		tvItemProfileBrandName.setText(brand.getBrandName());
		tvItemProfilePhotosValue.setText(String.valueOf(itemImages.size()));
		
		if(C.Miscellaneous.CURRENCY_SYMBOL_BEFORE_AMOUNT){
			tvItemProfileAveragePriceValue.setText(C.Miscellaneous.CURRENCY_SYMBOL + String.valueOf(itemBasic.getItemPriceAquired()));
		} else {
			tvItemProfileAveragePriceValue.setText(String.valueOf(itemBasic.getItemPriceAquired())+ " " + C.Miscellaneous.CURRENCY_SYMBOL);
		}
		
		// TODO Later change to Average Price
		
		if (itemAverageRating != 0) {
			tvItemProfileRatingValue.setText(String.valueOf(itemAverageRating) + "/10 [" + String.valueOf(itemRatings.size()) + "]");
		} else {
			tvItemProfileRatingValue.setText("N/A");
		}

		tvItemProfileFollowedByValue.setText(String.valueOf(itemFollowers.size()));
		tvItemProfileCommentsValue.setText(String.valueOf(itemComments.size()));

		if (itemUsers.size() == 1) {
			tvItemProfileOwnedByUsersHeading.setText("person has one");
		}
		tvItemProfileOwnedByUsersValue.setText(String.valueOf(itemUsers.size()));

		// TODO deal with friends of mine that own this item and cover the case when there is only one.

		if (itemPlaces.size() == 1) {
			tvItemProfileOfferedAtPlacesHeading.setText("place offers this item");
		}
		tvItemProfileOfferedAtPlacesValue.setText(String.valueOf(itemPlaces.size()));

		// Item can have several categories. Get one of them and display it
		Iterator<ItemCategory> categoriesIterator = itemCategories.iterator();
		try {
			itemCategory = categoriesIterator.next().getName();
			bItemProfileMainCategory.setText(itemCategory);
		} catch (Exception e) {
			itemCategory = "No category";
			bItemProfileMainCategory.setVisibility(View.GONE);
		}

		// Do the same for subcategory - show the first one as the name of the button. If the user clicks the button
		// They will get a list of all subcategories and if they click one of them they will get lots of items from the same subcategory
		Iterator<ItemSubcategory> subcategoriesIterator = itemSubcategories.iterator();
		try {
			itemSubcategory = subcategoriesIterator.next().getName();
			bItemProfileMainSubcategory.setText(itemSubcategory);
		} catch (Exception e) {
			itemSubcategory = "No subcategory"; // Just in case
			bItemProfileMainSubcategory.setVisibility(View.GONE);
		}

		// Set images
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
		iItemProfileImageSlotOne.setImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_ORIGINAL + imageUrls[0], R.drawable.images_default_product);

		int[] initialImageViews = { R.id.iItemProfileImageSlotTwo, R.id.iItemProfileImageSlotThree, R.id.iItemProfileImageSlotFour,
				R.id.iItemProfileImageSlotFive };

		for (int x = 0; x < (itemImagesCount - 1); x++) { // -1 because one is already used at iItemProfileImageSlotOne
			necessaryImageViews = new int[itemImagesCount - 1];
			necessaryImageViews[x] = initialImageViews[x];
			FetchableImageView fetchableImageView = (FetchableImageView) findViewById(necessaryImageViews[x]);
			fetchableImageView.setVisibility(View.VISIBLE);
			fetchableImageView.setImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_ORIGINAL + imageUrls[x + 1], R.drawable.images_default_product);
		}

		// Set the users that own this item images
		Iterator<UserBasic> usersIterator = itemUsers.iterator();
		int itemUsersCount = itemUsers.size();
		itemUsersAvatarsUrls = new String[itemUsersCount];
		int a = 0;
		while (usersIterator.hasNext()) {
			itemUsersAvatarsUrls[a] = usersIterator.next().getUserAvatar();
			i++;
		}

		// We have 4 imageViews available for displaying users avatars, but we might not need them all
		int[] initialUserAvatarViews = { R.id.iItemProfileUserImageSlotOne, R.id.iItemProfileUserImageSlotTwo, R.id.iItemProfileUserImageSlotThree,
				R.id.iItemProfileUserImageSlotFour };

		for (int x = 0; x < itemUsersCount; x++) {
			necessaryUserImageViews = new int[itemUsersCount];
			necessaryUserImageViews[x] = initialUserAvatarViews[x];
			FetchableImageView fetchableImageView = (FetchableImageView) findViewById(necessaryUserImageViews[x]);
			fetchableImageView.setVisibility(View.VISIBLE);
			fetchableImageView.setImage(C.API.WEB_ADDRESS + C.API.IMAGES_USERS_FOLDER_MINI + itemUsersAvatarsUrls[x], R.drawable.images_default_avatar_female);
		}

		// Set on click listeners
		// Take the user to a list of categories that this item falls into. When they click one of them a search results
		// screen is shown with items of that category
		bItemProfileMainCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itemCategoriesList = new Intent(ItemProfileOtherUsers.this, ItemProfileCategoriesList.class);
				Transporter.instance().itemCategories = itemCategories;
				startActivity(itemCategoriesList);

			}
		});

		// Take the user to a list of subcategories and make them clickable and leading to search results screen
		bItemProfileMainSubcategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itemSubcategoriesList = new Intent(ItemProfileOtherUsers.this, ItemProfileSubcategoriesList.class);
				// TODO think about using the same list but with different parameters passed to it.
				Transporter.instance().itemSubcategories = itemSubcategories;
				startActivity(itemSubcategoriesList);

			}
		});

		/*
		 * When the user is looking at an item's profile they might want to add their particular item of the kind to their
		 * own collection Send them to the item add activity and help maintain data integrity by pre-filling some of the
		 * fields for them Using the transporter to send this item's basic data there
		 */
		bItemProfileAddYours.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itemAdd = new Intent(ItemProfileOtherUsers.this, ItemAddStepOnePhotos.class);
				Transporter.instance().itemBasic = itemBasic;
				startActivity(itemAdd);

			}
		});

	}

	// TODO create a pool of image views, insert only the number that corresponds to the array size. have them in a
	// linkedhashset
	// iterate through the set and set the next image.
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
package net.shiftinpower.activities.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import net.shiftinpower.activities.ItemAddStepOnePhotos;
import net.shiftinpower.asynctasks.GetItemDataFromServerAsync;
import net.shiftinpower.core.C;
import net.shiftinpower.core.KatwalkSlidingMenu;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ItemProfilePublic extends KatwalkSlidingMenu implements OnGetItemDataListener {

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
	private TextView tvItemProfileOwnedByFriendsHeading;
	private TextView tvItemProfileOfferedAtPlacesValue;
	private TextView tvItemProfileOfferedAtPlacesHeading;
	private TextView tvItemProfileUsersSellingTheirItemsValue;
	private TextView tvItemProfileCommentsValue;
	private TextView tvItemProfileSimilarItemsValue;
	private Button bItemProfileMainCategory;
	private Button bItemProfileMainSubcategory;
	private Button bItemProfileAddYours;
	private SquareImageView iItemProfileImageSlotOne;

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
	private ArrayList<String> friendsAvatarsUrls;
	private ArrayList<String> itemPlacesUrls;
	private int[] necessaryImageViews;
	private int[] necessaryUserImageViews;
	private ArrayList<SquareImageView> imageViewsWhoseBitmapsShouldBeRecycled = new ArrayList<SquareImageView>();

	// Constructor needed because of the way the super class works
	public ItemProfilePublic() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout_item_profile_public);

		// Assign and inflate an XML file as the view component for this screen
		tvItemProfileItemName = (TextView) findViewById(R.id.tvItemProfileItemName);
		tvItemProfileBrandName = (TextView) findViewById(R.id.tvItemProfileBrandName);
		iItemProfileImageSlotOne = (SquareImageView) findViewById(R.id.iItemProfileImageSlotOne);
		tvItemProfilePhotosValue = (TextView) findViewById(R.id.tvItemProfilePhotosValue);
		tvItemProfileAveragePriceValue = (TextView) findViewById(R.id.tvItemProfileAveragePriceValue);
		tvItemProfileRatingValue = (TextView) findViewById(R.id.tvItemProfileRatingValue);
		tvItemProfileFollowedByValue = (TextView) findViewById(R.id.tvItemProfileFollowedByValue);
		tvItemProfileOwnedByUsersValue = (TextView) findViewById(R.id.tvItemProfileOwnedByUsersValue);
		tvItemProfileOwnedByUsersHeading = (TextView) findViewById(R.id.tvItemProfileOwnedByUsersHeading);
		tvItemProfileOwnedByFriendsValue = (TextView) findViewById(R.id.tvItemProfileOwnedByFriendsValue);
		tvItemProfileOwnedByFriendsHeading = (TextView) findViewById(R.id.tvItemProfileOwnedByFriendsHeading);
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
			tvItemProfileItemName.setTypeface(katwalk.font2);
			tvItemProfileBrandName.setTypeface(katwalk.font1);
			tvItemProfilePhotosValue.setTypeface(katwalk.font2);
			tvItemProfileAveragePriceValue.setTypeface(katwalk.font2);
			tvItemProfileRatingValue.setTypeface(katwalk.font2);
			tvItemProfileFollowedByValue.setTypeface(katwalk.font2);
			tvItemProfileOwnedByUsersValue.setTypeface(katwalk.font2);
			tvItemProfileOwnedByFriendsValue.setTypeface(katwalk.font2);
			tvItemProfileOfferedAtPlacesValue.setTypeface(katwalk.font2);
			tvItemProfileUsersSellingTheirItemsValue.setTypeface(katwalk.font2);
			tvItemProfileCommentsValue.setTypeface(katwalk.font2);
			tvItemProfileSimilarItemsValue.setTypeface(katwalk.font2);
		} catch (Exception e) {
			// Nothing can be done here
			e.printStackTrace();
		}

		// Obtain the itemId from the intent that started this activity
		ItemBasic userItem = Transporter.instance().userItem;

		new GetItemDataFromServerAsync(this, this, userItem.getItemId(), currentlyLoggedInUser).execute();

	} // End of onCreate
	
	@Override
	protected void onPause() {
		super.onPause();
		recycleViewsDrawables(imageViewsWhoseBitmapsShouldBeRecycled);
		finish();
	}

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

		if (C.Miscellaneous.CURRENCY_SYMBOL_BEFORE_AMOUNT) {
			tvItemProfileAveragePriceValue.setText(C.Miscellaneous.CURRENCY_SYMBOL + String.valueOf(itemBasic.getItemPriceAquired()));
		} else {
			tvItemProfileAveragePriceValue.setText(String.valueOf(itemBasic.getItemPriceAquired()) + " " + C.Miscellaneous.CURRENCY_SYMBOL);
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

		// Number of places that sell this item
		int itemPlacesCount = itemPlaces.size();
		if (itemPlacesCount == 1) {
			tvItemProfileOfferedAtPlacesHeading.setText("place offers this item");
		}
		tvItemProfileOfferedAtPlacesValue.setText(String.valueOf(itemPlacesCount));

		// Item can have several categories. Get one of them and display it
		Iterator<ItemCategory> categoriesIterator = itemCategories.iterator();
		try {
			itemCategory = categoriesIterator.next().getName();
			bItemProfileMainCategory.setText(itemCategory);
		} catch (Exception e) {
			itemCategory = "No category"; // Just in case
			bItemProfileMainCategory.setVisibility(View.GONE);
		}

		// Do the same for subcategory - show the first one as the name of the button. If the user clicks the button
		// They will get a list of all subcategories and if they click one of them they will get lots of items from the same
		// subcategory
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
		katwalk.imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_ORIGINAL + imageUrls[0], iItemProfileImageSlotOne);
		
		int[] initialImageViews = { R.id.iItemProfileImageSlotTwo, R.id.iItemProfileImageSlotThree, R.id.iItemProfileImageSlotFour,
				R.id.iItemProfileImageSlotFive };

		for (int x = 0; x < (itemImagesCount - 1); x++) { // -1 because one is already used at iItemProfileImageSlotOne
			necessaryImageViews = new int[itemImagesCount - 1];
			necessaryImageViews[x] = initialImageViews[x];
			SquareImageView fetchableImageView = (SquareImageView) findViewById(necessaryImageViews[x]);
			fetchableImageView.setVisibility(View.VISIBLE);
			if (!imageUrls[x + 1].contentEquals("null")) {

				katwalk.imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_ITEMS_FOLDER_ORIGINAL + imageUrls[x + 1], fetchableImageView);
			}

		}

		// Set the images of users that own this item
		// We have 4 imageViews available for displaying users avatars, but we might not need them all
		ArrayList<Integer> initialUserAvatarViews = new ArrayList<Integer>();
		initialUserAvatarViews.add(R.id.iItemProfileUserImageSlotOne);
		initialUserAvatarViews.add(R.id.iItemProfileUserImageSlotTwo);
		initialUserAvatarViews.add(R.id.iItemProfileUserImageSlotThree);
		initialUserAvatarViews.add(R.id.iItemProfileUserImageSlotFour);

		int initialUserAvatarViewsCount = initialUserAvatarViews.size();

		int a = 0;
		for (UserBasic itemUser : itemUsers) {
			if (a < initialUserAvatarViewsCount) {
				SquareImageView fetchableImageView = (SquareImageView) findViewById(initialUserAvatarViews.get(a));
				fetchableImageView.setVisibility(View.VISIBLE);
				imageViewsWhoseBitmapsShouldBeRecycled.add(fetchableImageView);
				if (!itemUser.getUserAvatar().contentEquals("null")) {

					katwalk.imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_USERS_FOLDER_THUMBNAIL + itemUser.getUserAvatar(), fetchableImageView);
				}
				a++;
			}
		}

		// Set the images of current user's friends that own this item
		// We have 4 imageViews available for displaying friends avatars, but we might not need them all
		ArrayList<Integer> initialFriendAvatarViews = new ArrayList<Integer>();
		initialFriendAvatarViews.add(R.id.iItemProfileFriendImageSlotOne);
		initialFriendAvatarViews.add(R.id.iItemProfileFriendImageSlotTwo);
		initialFriendAvatarViews.add(R.id.iItemProfileFriendImageSlotThree);
		initialFriendAvatarViews.add(R.id.iItemProfileFriendImageSlotFour);

		// Get the number of friends that have this item and add up to 4 avatar urls so we can show them
		int friendsThatOwnThisItemCount = 0;

		friendsAvatarsUrls = new ArrayList<String>();

		for (UserBasic itemUser : itemUsers)
			if (itemUser.isUserFriendOfCurrentUser()) {
				if (friendsAvatarsUrls.size() < initialFriendAvatarViews.size()) {
					friendsAvatarsUrls.add(itemUser.getUserAvatar());
				}
				friendsThatOwnThisItemCount++;
			}

		// Show up to 4 images of friends that have this item
		if (friendsAvatarsUrls != null) {

			for (int b = 0, c = 0; b < initialFriendAvatarViews.size() && c < friendsAvatarsUrls.size(); c++, b++) {

				SquareImageView fetchableImageView = (SquareImageView) findViewById(initialFriendAvatarViews.get(b));
				imageViewsWhoseBitmapsShouldBeRecycled.add(fetchableImageView);
				fetchableImageView.setVisibility(View.VISIBLE);
				if (!friendsAvatarsUrls.get(b).contentEquals("null")) {

					katwalk.imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_USERS_FOLDER_THUMBNAIL + friendsAvatarsUrls.get(b), fetchableImageView);
				}

			}
		}

		// Set text for the friends that own item value
		if (friendsThatOwnThisItemCount == 1) {
			tvItemProfileOwnedByFriendsHeading.setText("of your friends has one");
		} else {
			tvItemProfileOwnedByFriendsHeading.setText("of your friends have one");
		}
		tvItemProfileOwnedByFriendsValue.setText(String.valueOf(friendsThatOwnThisItemCount));

		// Set images for places that offer this item
		ArrayList<Integer> initialPlaceAvatarViews = new ArrayList<Integer>();
		initialPlaceAvatarViews.add(R.id.iItemProfilePlaceImageSlotOne);
		initialPlaceAvatarViews.add(R.id.iItemProfilePlaceImageSlotTwo);
		initialPlaceAvatarViews.add(R.id.iItemProfilePlaceImageSlotThree);
		initialPlaceAvatarViews.add(R.id.iItemProfilePlaceImageSlotFour);

		int initialPlaceAvatarViewsCount = initialPlaceAvatarViews.size();

		int c = 0;
		for (Place itemPlace : itemPlaces) {
			if (c < initialPlaceAvatarViewsCount) {
				SquareImageView fetchableImageView = (SquareImageView) findViewById(initialPlaceAvatarViews.get(c));
				imageViewsWhoseBitmapsShouldBeRecycled.add(fetchableImageView);
				fetchableImageView.setVisibility(View.VISIBLE);
				if (!itemPlace.getPlaceAvatar().contentEquals("null")) {

					katwalk.imageLoader.displayImage(C.API.WEB_ADDRESS + C.API.IMAGES_PLACES_FOLDER_THUMBNAIL + itemPlace.getPlaceAvatar(), fetchableImageView);
				}

				c++;
			}
		}

		// Set on click listeners
		// Take the user to a list of categories that this item falls into. When they click one of them a search results
		// screen is shown with items of that category
		bItemProfileMainCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itemCategoriesList = new Intent(ItemProfilePublic.this, ItemProfileCategoriesList.class);
				Transporter.instance().itemCategories = itemCategories;
				startActivity(itemCategoriesList);

			}
		});

		// Take the user to a list of subcategories and make them clickable and leading to search results screen
		bItemProfileMainSubcategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent itemSubcategoriesList = new Intent(ItemProfilePublic.this, ItemProfileSubcategoriesList.class);
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
				Intent itemAdd = new Intent(ItemProfilePublic.this, ItemAddStepOnePhotos.class);
				Transporter.instance().itemBasic = itemBasic;
				startActivity(itemAdd);

			}
		});

	} //End of onGetItemDataSuccess

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
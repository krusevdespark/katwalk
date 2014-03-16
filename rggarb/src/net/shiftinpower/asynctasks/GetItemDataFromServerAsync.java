package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetItemDataListener;
import net.shiftinpower.objects.Brand;
import net.shiftinpower.objects.Image;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.objects.ItemComment;
import net.shiftinpower.objects.ItemExtended;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.objects.Place;
import net.shiftinpower.objects.Rating;
import net.shiftinpower.objects.UserBasic;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.os.AsyncTask;

/**
* This class gets an item basic and extended set of data so we can display its public profile
* including users and friends that have the item along with their basic info, places that sell the item and so on
*
* @author Kaloyan Roussev
*
*/

public class GetItemDataFromServerAsync extends AsyncTask<String, String, ItemExtended> {

	private int serverResponseCode;
	private String reason;
	private int itemId;
	private int userId;
	private Context context;
	private OnGetItemDataListener listener;
	private JSONParser jsonParser = new JSONParser();
	private boolean loadingMessageShown;

	public GetItemDataFromServerAsync(Context context, OnGetItemDataListener listener, int itemId, int userId) {
		this.itemId = itemId;
		this.userId = userId;
		this.listener = listener;
		this.context = context;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		if (context != null) {
			ShowLoadingMessage.loading(context, C.LoadingMessages.LOADING_ITEM_DATA);
			loadingMessageShown = true;
		}

	}

	@Override
	protected ItemExtended doInBackground(String... params) {

		try {

			List<NameValuePair> getItemData = new ArrayList<NameValuePair>();
			getItemData.add(new BasicNameValuePair(C.DBColumns.ITEM_ID, String.valueOf(itemId)));
			getItemData.add(new BasicNameValuePair(C.DBColumns.USER_ID, String.valueOf(userId)));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_ITEM_DATA, "GET", getItemData);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				// Create an ItemExtended object and fill it with the objects that we are about to create.
				ItemExtended itemExtended = new ItemExtended();

				// Deal with Item's Basic Information
				ItemBasic userItem = new ItemBasic();

				JSONArray userItemsJSONArray = json.getJSONArray(C.DBColumns.ITEM_INFO);
				JSONObject userItemJSONFormat = userItemsJSONArray.getJSONObject(0);

				boolean itemWasAGift;
				if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_WAS_A_GIFT)) == 1) {
					itemWasAGift = true;
				} else {
					itemWasAGift = false;
				}

				boolean itemBeingSold;
				if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_BEING_SOLD)) == 1) {
					itemBeingSold = true;
				} else {
					itemBeingSold = false;
				}

				boolean itemBoughtFromPlace;
				if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_BOUGHT_FROM_PLACE)) == 1) {
					itemBoughtFromPlace = true;
				} else {
					itemBoughtFromPlace = false;
					userItem.setItemBoughtFromPlaceName(C.Tagz.ITEM_BOUGHT_FROM_PERSON);
				}

				boolean itemBoughtInConditionNew;
				if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_CONDITION_NEW)) == 1) {
					itemBoughtInConditionNew = true;
				} else {
					itemBoughtInConditionNew = false;
				}

				userItem.setItemBeingSold(itemBeingSold);
				userItem.setItemBoughtFromPlace(itemBoughtFromPlace);
				userItem.setItemBoughtInConditionNew(itemBoughtInConditionNew);
				userItem.setItemId(userItemJSONFormat.getInt(C.DBColumns.ITEM_ID));
				userItem.setItemName(userItemJSONFormat.getString(C.DBColumns.ITEM_NAME));
				userItem.setItemDescription(userItemJSONFormat.getString(C.DBColumns.ITEM_DESCRIPTION));
				userItem.setItemId(userItemJSONFormat.getInt(C.DBColumns.ITEM_ID));
				userItem.setItemPriceAquired(userItemJSONFormat.getDouble(C.DBColumns.ITEM_PRICE_AQUIRED));
				userItem.setItemPriceBeingSold(userItemJSONFormat.getDouble(C.DBColumns.ITEM_PRICE_BEING_SOLD));
				userItem.setItemTimeAdded(userItemJSONFormat.getString(C.DBColumns.ITEM_TIME_ADDED));
				userItem.setItemWasAGift(itemWasAGift);

				// Add the basic info to the ItemExtended Object
				itemExtended.setItemBasic(userItem);

				// Deal with the Item's Images
				LinkedHashSet<Image> itemImages = new LinkedHashSet<Image>();
				JSONArray itemImagesJSONArray = json.getJSONArray(C.DBColumns.ITEM_IMAGES);

				int itemImagesCount = itemImagesJSONArray.length();
				for (int i = 0; i < itemImagesCount; i++) {

					Image itemImage = new Image();

					JSONObject itemImageJSONFormat = itemImagesJSONArray.getJSONObject(i);

					itemImage.setImageDescription(itemImageJSONFormat.getString(C.DBColumns.IMAGE_DESCRIPTION));
					itemImage.setImageId(itemImageJSONFormat.getInt(C.DBColumns.IMAGE_ID));
					itemImage.setImageUrl(itemImageJSONFormat.getString(C.DBColumns.IMAGE_URL));
					itemImage.setImageTimeAdded(itemImageJSONFormat.getString(C.DBColumns.IMAGE_TIMESTAMP));
					itemImage.setImageAddedByUser(itemImageJSONFormat.getInt(C.DBColumns.IMAGE_ADDED_BY_USER));

					itemImages.add(itemImage);

				}

				// Add the images to the ItemExtended Object
				itemExtended.setItemImages(itemImages);

				// Deal with the Item Brand
				Brand itemBrand = new Brand();

				JSONArray itemBrandJSONArray = json.getJSONArray(C.DBColumns.ITEM_BRAND);
				JSONObject itemBrandJSONFormat = itemBrandJSONArray.getJSONObject(0);

				itemBrand.setBrandId(itemBrandJSONFormat.getInt(C.DBColumns.BRAND_ID));
				itemBrand.setBrandName(itemBrandJSONFormat.getString(C.DBColumns.BRAND_NAME));
				itemBrand.setBrandLogo(itemBrandJSONFormat.getString(C.DBColumns.BRAND_LOGO));
				itemBrand.setBrandDescription(itemBrandJSONFormat.getString(C.DBColumns.BRAND_DESCRIPTION));
				itemBrand.setBrandWebsite(itemBrandJSONFormat.getString(C.DBColumns.BRAND_WEBSITE));
				itemBrand.setBrandFirstAddedFrom(itemBrandJSONFormat.getInt(C.DBColumns.BRAND_FIRST_ADDED_FROM));

				// Add the brand to the ItemExtended Object
				itemExtended.setBrand(itemBrand);

				// Deal with the Item's Categories
				LinkedHashSet<ItemCategory> itemCategories = new LinkedHashSet<ItemCategory>();
				JSONArray itemCategoriesJSONArray = json.getJSONArray(C.DBColumns.ITEM_CATEGORIES);

				int itemCategoriesCount = itemCategoriesJSONArray.length();
				for (int i = 0; i < itemCategoriesCount; i++) {

					ItemCategory itemCategory = new ItemCategory();
					JSONObject itemCategoryJSONFormat = itemCategoriesJSONArray.getJSONObject(i);

					itemCategory.setId(itemCategoryJSONFormat.getInt(C.DBColumns.CATEGORY_ID));
					itemCategory.setName(itemCategoryJSONFormat.getString(C.DBColumns.CATEGORY_NAME));
					itemCategory.setLogo(itemCategoryJSONFormat.getString(C.DBColumns.CATEGORY_LOGO));

					itemCategories.add(itemCategory);

				}

				// Add the Item Categories to the ItemExtended Object
				itemExtended.setItemCategories(itemCategories);

				// Deal with the Item's Subcategories
				LinkedHashSet<ItemSubcategory> itemSubcategories = new LinkedHashSet<ItemSubcategory>();
				JSONArray itemSubcategoriesJSONArray = json.getJSONArray(C.DBColumns.ITEM_SUBCATEGORIES);

				int itemSubcategoriesCount = itemSubcategoriesJSONArray.length();
				for (int i = 0; i < itemSubcategoriesCount; i++) {

					ItemSubcategory itemSubcategory = new ItemSubcategory();
					JSONObject itemSubcategoryJSONFormat = itemSubcategoriesJSONArray.getJSONObject(i);

					itemSubcategory.setId(itemSubcategoryJSONFormat.getInt(C.DBColumns.SUBCATEGORY_ID));
					itemSubcategory.setName(itemSubcategoryJSONFormat.getString(C.DBColumns.SUBCATEGORY_NAME));
					itemSubcategory.setParentID(itemSubcategoryJSONFormat.getInt(C.DBColumns.SUBCATEGORY_PARENT_ID));

					itemSubcategories.add(itemSubcategory);

				}

				// Add the Item Categories to the ItemExtended Object
				itemExtended.setItemSubcategories(itemSubcategories);

				// Deal with the Item's Comments
				LinkedHashSet<ItemComment> itemComments = new LinkedHashSet<ItemComment>();
				JSONArray itemCommentsJSONArray = json.getJSONArray(C.DBColumns.ITEM_COMMENTS);

				int itemCommentsCount = itemCommentsJSONArray.length();
				for (int i = 0; i < itemCommentsCount; i++) {

					ItemComment itemComment = new ItemComment();
					JSONObject itemCommentJSONFormat = itemCommentsJSONArray.getJSONObject(i);

					itemComment.setCommentId(itemCommentJSONFormat.getInt(C.DBColumns.ITEM_COMMENT_ID));
					itemComment.setCommenterId(itemCommentJSONFormat.getInt(C.DBColumns.ITEM_COMMENTER_ID));
					itemComment.setItemId(itemCommentJSONFormat.getInt(C.DBColumns.ITEM_ID));
					itemComment.setCommentContent(itemCommentJSONFormat.getString(C.DBColumns.ITEM_COMMENT_CONTENT));
					itemComment.setCommentDateCreated(itemCommentJSONFormat.getString(C.DBColumns.ITEM_COMMENT_CREATION_TIMESTAMP));
					itemComment.setCommentDateEdited(itemCommentJSONFormat.getString(C.DBColumns.ITEM_COMMENT_EDITION_TIMESTAMP));

					itemComments.add(itemComment);

				}

				// Add the Item Comments to the ItemExtended Object
				itemExtended.setItemComments(itemComments);

				// Deal with the Item Followers
				ArrayList<Integer> itemFollowers = new ArrayList<Integer>();
				JSONArray itemFollowersJSONArray = json.getJSONArray(C.DBColumns.ITEM_FOLLOWINGS);

				int itemFollowersCount = itemFollowersJSONArray.length();
				for (int i = 0; i < itemFollowersCount; i++) {

					JSONObject itemFollowerJSONFormat = itemFollowersJSONArray.getJSONObject(i);
					itemFollowers.add(itemFollowerJSONFormat.getInt(C.DBColumns.ITEM_FOLLOWER_ID));
				}

				// Add the Item Followers to the ItemExtended Object
				itemExtended.setItemFollowers(itemFollowers);

				// Deal with the Item Ratings
				LinkedHashSet<Rating> itemRatings = new LinkedHashSet<Rating>();
				JSONArray itemRatingsJSONArray = json.getJSONArray(C.DBColumns.ITEM_RATINGS);

				int itemRatingsCount = itemRatingsJSONArray.length();
				for (int i = 0; i < itemRatingsCount; i++) {

					Rating itemRating = new Rating();
					JSONObject itemRatingJSONFormat = itemRatingsJSONArray.getJSONObject(i);

					itemRating.setItemRating(itemRatingJSONFormat.getInt(C.DBColumns.ITEM_RATING));
					itemRating.setTimeOfRating(itemRatingJSONFormat.getString(C.DBColumns.ITEM_RATING_TIMESTAMP));
					itemRating.setUserThatRatedTheItem(itemRatingJSONFormat.getInt(C.DBColumns.ITEM_RATING_USER));

					itemRatings.add(itemRating);
				}

				// Add the Item Ratings to the ItemExtended Object
				itemExtended.setItemRatings(itemRatings);

				// Deal with places this item is available at
				LinkedHashSet<Place> itemPlaces = new LinkedHashSet<Place>();
				JSONArray itemPlacesJSONArray = json.getJSONArray(C.DBColumns.ITEM_PLACES);

				int itemPlacesCount = itemPlacesJSONArray.length();
				for (int i = 0; i < itemPlacesCount; i++) {

					Place itemPlace = new Place();
					JSONObject itemPlaceJSONFormat = itemPlacesJSONArray.getJSONObject(i);

					itemPlace.setPlaceId(itemPlaceJSONFormat.getInt(C.DBColumns.PLACE_ID));
					itemPlace.setPlaceName(itemPlaceJSONFormat.getString(C.DBColumns.PLACE_NAME));
					itemPlace.setPlaceAvatar(itemPlaceJSONFormat.getString(C.DBColumns.PLACE_AVATAR));
					itemPlace.setPlaceDescription(itemPlaceJSONFormat.getString(C.DBColumns.PLACE_DESCRIPTION));
					itemPlace.setPlaceAddedByUser(itemPlaceJSONFormat.getInt(C.DBColumns.PLACE_ADDED_BY_USER));

					itemPlaces.add(itemPlace);
				}

				// Add the Places to the ItemExtended Object
				itemExtended.setItemPlaces(itemPlaces);

				// Deal with people that own this item
				LinkedHashSet<UserBasic> itemUsers = new LinkedHashSet<UserBasic>();
				JSONArray itemUsersJSONArray = json.getJSONArray(C.DBColumns.ITEM_USERS);

				int itemUsersCount = itemUsersJSONArray.length();
				for (int i = 0; i < itemUsersCount; i++) {

					UserBasic itemUser = new UserBasic();
					JSONObject itemUserJSONFormat = itemUsersJSONArray.getJSONObject(i);

					boolean userIsFriendOfCurrentUser;
					if ((itemUserJSONFormat.getString(C.DBColumns.USER_IS_FRIEND_OF_CURRENT_USER)).contentEquals("1")) {

						userIsFriendOfCurrentUser = true;
					} else {

						userIsFriendOfCurrentUser = false;
					}

					itemUser.setUserId(itemUserJSONFormat.getInt(C.DBColumns.ITEM_OWNER_ID));
					itemUser.setUserName(itemUserJSONFormat.getString(C.DBColumns.USER_NAME));
					itemUser.setUserAvatar(itemUserJSONFormat.getString(C.DBColumns.USER_AVATAR));
					itemUser.setUserSex(itemUserJSONFormat.getString(C.DBColumns.USER_SEX));
					itemUser.setUserIsFriendOfCurrentUser(userIsFriendOfCurrentUser);

					itemUsers.add(itemUser);

				}

				// Add the Item Users to Item Extended
				itemExtended.setItemUsers(itemUsers);

				return itemExtended;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {
				setReason(C.Tagz.BAD_REQUEST);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {
				setReason(C.Tagz.DB_PROBLEM);
				return null;
			} else if (serverResponseCode == C.HttpResponses.FAILURE_NOT_FOUND) {
				setReason(C.Tagz.NOT_FOUND);
				return null;
			} else {
				setReason(C.Tagz.UNKNOWN_PROBLEM);
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			setReason(C.Tagz.BAD_REQUEST);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;
		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(ItemExtended itemExtended) {

		super.onPostExecute(itemExtended);

		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (itemExtended != null) {
				listener.onGetItemDataSuccess(itemExtended);
			} else if (reason != null) {
				listener.onGetItemDataFailure();
			}
		}

	} // End of onPostExecute

} // End of Class
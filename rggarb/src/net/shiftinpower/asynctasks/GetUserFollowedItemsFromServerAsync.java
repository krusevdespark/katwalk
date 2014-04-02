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
import net.shiftinpower.interfaces.OnGetUserFollowedItemsListener;
import net.shiftinpower.interfaces.OnGetUserItemsListener;
import net.shiftinpower.objects.ItemBasic;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.os.AsyncTask;

/**
* This asynctask is part of the InitialDataloader sequence of tasks obtaining vital information from the server It gets the
* basic sets of data for the user items. Extended item data is obtained using the GetItemDataFromServerAsync.class for each
* item
*
* @author Kaloyan Roussev
*
*/

public class GetUserFollowedItemsFromServerAsync extends AsyncTask<String, String, LinkedHashSet<ItemBasic>> {

	private int serverResponseCode;
	private String reason;
	private int userId;
	private Context context;
	private OnGetUserFollowedItemsListener listener;
	private JSONParser jsonParser = new JSONParser();
	private boolean loadingMessageShown;

	public GetUserFollowedItemsFromServerAsync(Context context, int userId, OnGetUserFollowedItemsListener listener) {
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
			ShowLoadingMessage.loading(context, C.LoadingMessages.LOADING_USER_ITEMS);
			loadingMessageShown = true;
		}

	}

	@Override
	protected LinkedHashSet<ItemBasic> doInBackground(String... params) {

		try {

			List<NameValuePair> getUserFollowedItems = new ArrayList<NameValuePair>();
			getUserFollowedItems.add(new BasicNameValuePair(C.DBColumns.ITEM_OWNER_ID, String.valueOf(userId)));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_USER_FOLLOWED_ITEMS, "GET", getUserFollowedItems);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				LinkedHashSet<ItemBasic> userFollowedItems = new LinkedHashSet<ItemBasic>();
				JSONArray userFollowedItemsJSONArray = json.getJSONArray(C.DBColumns.USER_ITEMS);

				int userFollowedItemsCount = userFollowedItemsJSONArray.length();

				for (int i = 0; i < userFollowedItemsCount; i++) {
					ItemBasic userFollowedItem = new ItemBasic();
					JSONObject userFollowedItemJSONFormat = userFollowedItemsJSONArray.getJSONObject(i);

					boolean itemBeingSold;
					if ((userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_BEING_SOLD)) == 1) {
						itemBeingSold = true;
					} else {
						itemBeingSold = false;
					}

					boolean itemBoughtFromPlace;
					if ((userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_BOUGHT_FROM_PLACE)) == 1) {
						itemBoughtFromPlace = true;
						userFollowedItem.setItemBoughtFromPlaceName(userFollowedItemJSONFormat.getString(C.DBColumns.PLACE_NAME));
					} else {
						itemBoughtFromPlace = false;
						userFollowedItem.setItemBoughtFromPlaceName("Person");
					}

					boolean itemBoughtInConditionNew;
					if ((userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_CONDITION_NEW)) == 1) {
						itemBoughtInConditionNew = true;
					} else {
						itemBoughtInConditionNew = false;
					}

					boolean itemWasAGift;
					if ((userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_WAS_A_GIFT)) == 1) {
						itemWasAGift = true;
					} else {
						itemWasAGift = false;
					}

					int itemRating;
					if ((String.valueOf(userFollowedItemJSONFormat.getString(C.DBColumns.ITEM_RATING))).contentEquals("null")) {
						itemRating = 0;
					} else {
						itemRating = userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_RATING);
					}

					userFollowedItem.setItemBeingSold(itemBeingSold);
					userFollowedItem.setItemBoughtFromPlace(itemBoughtFromPlace);
					userFollowedItem.setItemBoughtInConditionNew(itemBoughtInConditionNew);
					userFollowedItem.setItemBrand(userFollowedItemJSONFormat.getString(C.DBColumns.BRAND_NAME));
					userFollowedItem.setItemCategory(userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_CATEGORY_ID));
					userFollowedItem.setItemImage(userFollowedItemJSONFormat.getString(C.DBColumns.ITEM_IMAGE));
					userFollowedItem.setItemName(userFollowedItemJSONFormat.getString(C.DBColumns.ITEM_NAME));
					userFollowedItem.setItemDescription(userFollowedItemJSONFormat.getString(C.DBColumns.ITEM_DESCRIPTION));
					userFollowedItem.setItemRating(itemRating);

					userFollowedItem.setItemId(userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_ID));
					userFollowedItem.setItemOwnerId(userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_OWNER_ID));
					userFollowedItem.setItemPriceAquired(userFollowedItemJSONFormat.getDouble(C.DBColumns.ITEM_PRICE_AQUIRED));
					userFollowedItem.setItemPriceBeingSold(userFollowedItemJSONFormat.getDouble(C.DBColumns.ITEM_PRICE_BEING_SOLD));
					userFollowedItem.setItemSubcategory(userFollowedItemJSONFormat.getInt(C.DBColumns.ITEM_SUBCATEGORY_ID));
					userFollowedItem.setItemTimeAdded(userFollowedItemJSONFormat.getString(C.DBColumns.ITEM_TIME_ADDED));
					userFollowedItem.setItemWasAGift(itemWasAGift);

					userFollowedItems.add(userFollowedItem);

				} // End of Loop

				return userFollowedItems;

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
	protected void onPostExecute(LinkedHashSet<ItemBasic> userItems) {

		super.onPostExecute(userItems);

		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (userItems != null) {
				listener.onGetUserFollowedItemsSuccess(userItems);
			} else if (reason != null) {
				listener.onGetUserFollowedItemsFailure(reason);
			}
		}

	} // End of onPostExecute

} // End of Class
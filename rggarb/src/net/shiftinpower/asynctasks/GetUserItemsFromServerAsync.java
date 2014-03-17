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

public class GetUserItemsFromServerAsync extends AsyncTask<String, String, LinkedHashSet<ItemBasic>> {

	private int serverResponseCode;
	private String reason;
	private int userId;
	private Context context;
	private OnGetUserItemsListener listener;
	private JSONParser jsonParser = new JSONParser();
	private boolean loadingMessageShown;

	public GetUserItemsFromServerAsync(Context context, int userId, OnGetUserItemsListener listener) {
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

			List<NameValuePair> getUserItems = new ArrayList<NameValuePair>();
			getUserItems.add(new BasicNameValuePair(C.DBColumns.ITEM_OWNER_ID, String.valueOf(userId)));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_USER_ITEMS, "GET", getUserItems);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				LinkedHashSet<ItemBasic> userItems = new LinkedHashSet<ItemBasic>();
				JSONArray userItemsJSONArray = json.getJSONArray(C.DBColumns.USER_ITEMS);

				int userItemsCount = userItemsJSONArray.length();

				for (int i = 0; i < userItemsCount; i++) {
					ItemBasic userItem = new ItemBasic();
					JSONObject userItemJSONFormat = userItemsJSONArray.getJSONObject(i);

					boolean itemBeingSold;
					if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_BEING_SOLD)) == 1) {
						itemBeingSold = true;
					} else {
						itemBeingSold = false;
					}

					boolean itemBoughtFromPlace;
					if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_BOUGHT_FROM_PLACE)) == 1) {
						itemBoughtFromPlace = true;
						userItem.setItemBoughtFromPlaceName(userItemJSONFormat.getString(C.DBColumns.PLACE_NAME));
					} else {
						itemBoughtFromPlace = false;
						userItem.setItemBoughtFromPlaceName("Person");
					}

					boolean itemBoughtInConditionNew;
					if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_CONDITION_NEW)) == 1) {
						itemBoughtInConditionNew = true;
					} else {
						itemBoughtInConditionNew = false;
					}

					boolean itemWasAGift;
					if ((userItemJSONFormat.getInt(C.DBColumns.ITEM_WAS_A_GIFT)) == 1) {
						itemWasAGift = true;
					} else {
						itemWasAGift = false;
					}

					int itemRating;
					if ((String.valueOf(userItemJSONFormat.getString(C.DBColumns.ITEM_RATING))).contentEquals("null")) {
						itemRating = 0;
					} else {
						itemRating = userItemJSONFormat.getInt(C.DBColumns.ITEM_RATING);
					}

					userItem.setItemBeingSold(itemBeingSold);
					userItem.setItemBoughtFromPlace(itemBoughtFromPlace);
					userItem.setItemBoughtInConditionNew(itemBoughtInConditionNew);
					userItem.setItemBrand(userItemJSONFormat.getString(C.DBColumns.BRAND_NAME));
					userItem.setItemCategory(userItemJSONFormat.getInt(C.DBColumns.ITEM_CATEGORY_ID));
					userItem.setItemImage(userItemJSONFormat.getString(C.DBColumns.ITEM_IMAGE));
					userItem.setItemName(userItemJSONFormat.getString(C.DBColumns.ITEM_NAME));
					userItem.setItemDescription(userItemJSONFormat.getString(C.DBColumns.ITEM_DESCRIPTION));
					userItem.setItemRating(itemRating);

					userItem.setItemId(userItemJSONFormat.getInt(C.DBColumns.ITEM_ID));
					userItem.setItemOwnerId(userItemJSONFormat.getInt(C.DBColumns.ITEM_OWNER_ID));
					userItem.setItemPriceAquired(userItemJSONFormat.getDouble(C.DBColumns.ITEM_PRICE_AQUIRED));
					userItem.setItemPriceBeingSold(userItemJSONFormat.getDouble(C.DBColumns.ITEM_PRICE_BEING_SOLD));
					userItem.setItemSubcategory(userItemJSONFormat.getInt(C.DBColumns.ITEM_SUBCATEGORY_ID));
					userItem.setItemTimeAdded(userItemJSONFormat.getString(C.DBColumns.ITEM_TIME_ADDED));
					userItem.setItemWasAGift(itemWasAGift);

					userItems.add(userItem);

				} // End of Loop

				return userItems;

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
				listener.onGetUserItemsSuccess(userItems);
			} else if (reason != null) {
				listener.onGetUserItemsFailure(reason);
			}
		}

	} // End of onPostExecute

} // End of Class
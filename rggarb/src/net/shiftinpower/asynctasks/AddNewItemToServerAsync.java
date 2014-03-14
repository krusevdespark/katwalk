package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnAddNewItemToServerListener;
import net.shiftinpower.localsqlitedb.DBTools;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class AddNewItemToServerAsync extends AsyncTask<String, String, Bundle> {

	private Context context;
	private OnAddNewItemToServerListener listener;
	private JSONParser jsonParser = new JSONParser();
	private int currentlyLoggedInUser;
	private DBTools dbTools;
	private String itemName;
	private String itemBrand;
	private String itemCategoryID;
	private String itemSubcategoryID;
	private String itemBoughtFrom;
	private String itemPrice;
	private ArrayList<String> imageFilenames = new ArrayList<String>();
	private ArrayList<String> imageDescriptions = new ArrayList<String>();
	private String itemDescription;
	private boolean itemBoughtFromPlace;
	private boolean itemIsAGift;
	private boolean itemNew;
	private int itemId;
	private int transactionId;
	private String timeItemWasAddedToDB;
	private String reason;

	public AddNewItemToServerAsync(DBTools dbTools, Context context, OnAddNewItemToServerListener listener, int currentlyLoggedInUser, String itemName, String itemBrand, String itemCategoryID, String itemSubcategoryID, String itemBoughtFrom,
			String itemPrice, ArrayList<String> imageFilenames, ArrayList<String> imageDescriptions, String itemDescription, boolean itemNew, boolean itemBoughtFromPlace, boolean itemIsAGift) {
		this.dbTools = dbTools;
		this.context = context;
		this.listener = listener;
		this.currentlyLoggedInUser = currentlyLoggedInUser;
		this.itemName = itemName;
		this.itemBrand = itemBrand;
		this.itemCategoryID = itemCategoryID;
		this.itemSubcategoryID = itemSubcategoryID;
		this.itemBoughtFrom = itemBoughtFrom;
		this.itemPrice = itemPrice;
		this.imageFilenames = imageFilenames;
		this.imageDescriptions = imageDescriptions;
		this.itemDescription = itemDescription;
		this.itemNew = itemNew;
		this.itemBoughtFromPlace = itemBoughtFromPlace;
		this.itemIsAGift = itemIsAGift;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		ShowLoadingMessage.loading(context, C.LoadingMessages.ADDING_NEW_ITEM_TO_USER_COLLECTION);
	}

	@Override
	protected Bundle doInBackground(String... args) {

		int serverResponseCode;

		try {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(C.DBColumns.USER_ID, String.valueOf(currentlyLoggedInUser)));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_NAME, itemName));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_BRAND, itemBrand));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_CATEGORY_ID, itemCategoryID));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_SUBCATEGORY_ID, itemSubcategoryID));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_BOUGHT_FROM, itemBoughtFrom));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_WAS_A_GIFT, String.valueOf(itemIsAGift)));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_PRICE_AQUIRED, itemPrice));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_DESCRIPTION, itemDescription));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_CONDITION_NEW, String.valueOf(itemNew)));
			params.add(new BasicNameValuePair(C.DBColumns.ITEM_BOUGHT_FROM_PLACE, String.valueOf(itemBoughtFromPlace)));
			params.add(new BasicNameValuePair(C.DBColumns.ACTION_ID, String.valueOf(C.Actions.BUYING_A_PRODUCT)));
			params.add(new BasicNameValuePair(C.DBColumns.POINTS_REWARDED, String.valueOf(C.PointsForActions.BUYING_A_PRODUCT)));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.ADD_NEW_ITEM, "POST", params);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				itemId = json.getInt(C.Tagz.ITEM_ID);
				transactionId = json.getInt(C.Tagz.TRANSACTION_ID);
				timeItemWasAddedToDB = json.getString(C.Tagz.TIME_ITEM_WAS_ADDED_TO_DB);

				Bundle dataRegardingTheItemJustAdded = new Bundle();
				dataRegardingTheItemJustAdded.putInt("itemId", itemId);
				dataRegardingTheItemJustAdded.putInt("transactionId", transactionId);
				dataRegardingTheItemJustAdded.putString("timeItemWasAddedToDB", timeItemWasAddedToDB);

				new AddItemPhotoPathsAndDescriptionsInServerDatabaseAsync(dbTools, context, currentlyLoggedInUser, itemId, imageDescriptions, imageFilenames).execute();

				return dataRegardingTheItemJustAdded;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_BAD_REQUEST) {

				setReason(C.Tagz.BAD_REQUEST);
				return null;

			} else if (serverResponseCode == C.HttpResponses.FAILURE_DATABASE_PROBLEM) {

				setReason(C.Tagz.DB_PROBLEM);
				String message = json.getString("message");
				Log.d("message", message);
				return null;

			} else {

				setReason(C.Tagz.UNKNOWN_PROBLEM);
				return null;

			}

		} catch (JSONException e) {

			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;

		} catch (Exception e) {

			e.printStackTrace();
			setReason(C.Tagz.UNKNOWN_PROBLEM);
			return null;

		}
	} // End of doInBackground

	@Override
	protected void onPostExecute(Bundle dataRegardingTheItemJustAdded) {
		super.onPostExecute(dataRegardingTheItemJustAdded);

		ShowLoadingMessage.dismissDialog();

		if (listener != null) {
			if (dataRegardingTheItemJustAdded != null) {

				listener.onAddNewItemSuccess(dataRegardingTheItemJustAdded);

			} else {

				if (reason != null) {
					listener.onAddNewItemFailure(reason);
				}

			}
		}

	} // End of onPostExecute

} // End of Class
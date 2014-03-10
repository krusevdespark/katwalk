package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetCategoriesListener;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This asynctask is part of the InitialDataloader sequence of tasks obtaining vital information from the server
 * @author Kaloyan Roussev
 *
 */
public class GetCategoriesFromServerAsync extends AsyncTask<String, String, LinkedHashSet<ItemCategory>> {

	private int serverResponseCode;
	private OnGetCategoriesListener listener;
	private JSONParser jsonParser = new JSONParser();
	private Context context;
	private final static String TAG_GET_CATEGORIES = "get_categories";
	private boolean loadingMessageShown;

	private String reason;

	public GetCategoriesFromServerAsync(Context context, OnGetCategoriesListener listener) {
		this.listener = listener;
		this.context = context;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	protected void onPreExecute() {

		if (context != null) {
			ShowLoadingMessage.loading(context, C.LoadingMessages.LOADING_CATEGORIES);
			loadingMessageShown = true;
		}
		super.onPreExecute();

	}

	@Override
	protected LinkedHashSet<ItemCategory> doInBackground(String... args) {

		try {
			List<NameValuePair> getCategories = new ArrayList<NameValuePair>();
			getCategories.add(new BasicNameValuePair("request", TAG_GET_CATEGORIES));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_CATEGORIES, "GET", getCategories);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				JSONArray categoriesListJSONArray = json.getJSONArray("category_names");
				JSONArray categoryLogosListJSONArray = json.getJSONArray("category_logos");
				JSONArray categoryIDsListJSONArray = json.getJSONArray("category_ids");

				LinkedHashSet<ItemCategory> itemCategories = new LinkedHashSet<ItemCategory>();

				int categoriesSize = categoriesListJSONArray.length();

				if (categoriesListJSONArray != null) {
					for (int i = 0; i < categoriesSize; i++) {
						ItemCategory itemCategory = new ItemCategory();
						itemCategory.setId(Integer.parseInt(categoryIDsListJSONArray.get(i).toString()));
						itemCategory.setName(categoriesListJSONArray.get(i).toString());
						itemCategory.setLogo(categoryLogosListJSONArray.get(i).toString());
						itemCategories.add(itemCategory);
					}
				}

				return itemCategories;

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
	protected void onPostExecute(LinkedHashSet<ItemCategory> itemCategories) {

		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (itemCategories != null) {
				listener.onGetCategoriesSuccess(itemCategories);
			} else if (reason != null) {
				listener.onGetCategoriesFailure(reason);
			}
		}
		super.onPostExecute(itemCategories);
	} // End of onPostExecute

} // End of Class
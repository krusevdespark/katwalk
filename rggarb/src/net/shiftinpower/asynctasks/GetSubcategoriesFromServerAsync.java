package net.shiftinpower.asynctasks;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetSubcategoriesListener;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.utilities.JSONParser;
import net.shiftinpower.utilities.ShowLoadingMessage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class GetSubcategoriesFromServerAsync extends AsyncTask<String, String, LinkedHashSet<ItemSubcategory>> {
	
	private int serverResponseCode;
	private OnGetSubcategoriesListener listener;
	private Context context;
	private JSONParser jsonParser = new JSONParser();
	private final static String TAG_GET_SUBCATEGORIES = "get_subcategories";
	private boolean loadingMessageShown;

	private String reason;

	public GetSubcategoriesFromServerAsync(Context context, OnGetSubcategoriesListener listener) {
		this.listener = listener;
		this.context = context;
	}

	private void setReason(String reason) {
		this.reason = reason;
	}
	
	@Override
	protected void onPreExecute() {
		if (context != null) {
			ShowLoadingMessage.loading(context, C.LoadingMessages.LOADING_SUBCATEGORIES);
			loadingMessageShown = true;
		}
		super.onPreExecute();
	}

	@Override
	protected LinkedHashSet<ItemSubcategory> doInBackground(String... args) {

		try {
			List<NameValuePair> getSubcategories = new ArrayList<NameValuePair>();
			getSubcategories.add(new BasicNameValuePair("request", TAG_GET_SUBCATEGORIES));

			JSONObject json = jsonParser.makeHttpRequest(C.API.WEB_ADDRESS + C.API.GET_SUBCATEGORIES, "GET", getSubcategories);

			serverResponseCode = json.getInt(C.Tagz.SUCCESS);

			if (serverResponseCode == C.HttpResponses.SUCCESS) {

				JSONArray subcategoryIDsListJSONArray = json.getJSONArray("subcategory_ids");
				JSONArray subcategoryNamesListJSONArray = json.getJSONArray("subcategory_names");
				JSONArray subcategoryParentIDsListJSONArray = json.getJSONArray("subcategory_parent_ids");
				LinkedHashSet<ItemSubcategory> itemSubcategories = new LinkedHashSet<ItemSubcategory>();
				
				
				int subcategoriesSize = subcategoryNamesListJSONArray.length();
				if(subcategoryNamesListJSONArray!=null){
					for(int i = 0; i < subcategoriesSize; i++){
						ItemSubcategory itemSubcategory = new ItemSubcategory();
						itemSubcategory.setId(Integer.parseInt(subcategoryIDsListJSONArray.get(i).toString()));
						itemSubcategory.setName(subcategoryNamesListJSONArray.get(i).toString());
						itemSubcategory.setParentID(Integer.parseInt(subcategoryParentIDsListJSONArray.get(i).toString()));
						itemSubcategories.add(itemSubcategory);
					}					
				}
				
				return itemSubcategories;

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
	protected void onPostExecute(LinkedHashSet<ItemSubcategory> itemSubcategories) {
		
		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (itemSubcategories != null) {
				listener.onGetSubcategoriesSuccess(itemSubcategories);
			} else if (reason != null) {
				listener.onGetSubcategoriesFailure(reason);
			}
		}
		
		super.onPostExecute(itemSubcategories);
	}
	
} // End of Class
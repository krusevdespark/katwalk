package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetSubcategoriesListener;
import net.shiftinpower.objects.ItemSubcategory;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * After having obtained a fresh set of Categories from the Server Database, now we have it at hand locally for immediate
 * access
 * 
 * @author Kaloyan Roussev
 * 
 */
public class GetSubcategoriesFromDB extends AsyncTask<Void, Void, LinkedHashSet<ItemSubcategory>> {

	private DBTools dbTools;
	private int subcategoryParentId;
	private Context context;
	private boolean loadingMessageShown;
	private OnGetSubcategoriesListener listener;

	public GetSubcategoriesFromDB(OnGetSubcategoriesListener listener, Context context, DBTools dbTools, int subcategoryParentId) {
		this.dbTools = dbTools;
		this.context = context;
		this.listener = listener;
		this.subcategoryParentId = subcategoryParentId;
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
	protected LinkedHashSet<ItemSubcategory> doInBackground(Void... arg0) {

		LinkedHashSet<ItemSubcategory> itemSubcategories = new LinkedHashSet<ItemSubcategory>();

		Cursor cursor = dbTools.database.rawQuery(SQLQueries.getSubcategories(subcategoryParentId), null);

		if (cursor.moveToFirst()) {

			do {

				ItemSubcategory itemSubcategory = new ItemSubcategory();
				itemSubcategory.setId(cursor.getInt(cursor.getColumnIndex(C.DBColumns.SUBCATEGORY_ID)));
				itemSubcategory.setName(cursor.getString(cursor.getColumnIndex(C.DBColumns.SUBCATEGORY_NAME)));

				itemSubcategories.add(itemSubcategory);

			} while (cursor.moveToNext());
		}

		// Closing all cursors, databases and database helpers properly because not closing them can spring lots of trouble.
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}

		return itemSubcategories;
	} // End of doInBackground

	@Override
	protected void onPostExecute(LinkedHashSet<ItemSubcategory> itemCategories) {

		if ((context != null) && (loadingMessageShown)) {
			ShowLoadingMessage.dismissDialog();
			loadingMessageShown = false;
		}

		if (listener != null) {
			if (itemCategories != null) {
				listener.onGetSubcategoriesSuccess(itemCategories);
			} else {
				listener.onGetSubcategoriesFailure("Failure");
			}
		}

		super.onPostExecute(itemCategories);
	}

} // End of Class
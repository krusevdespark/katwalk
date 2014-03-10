package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetCategoriesListener;
import net.shiftinpower.objects.ItemCategory;
import net.shiftinpower.utilities.ShowLoadingMessage;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class GetCategoriesFromDB extends AsyncTask<Void, Void, LinkedHashSet<ItemCategory>> {

	private DBTools dbTools;
	private Context context;
	private boolean loadingMessageShown;
	private OnGetCategoriesListener listener;

	public GetCategoriesFromDB(OnGetCategoriesListener listener, Context context, DBTools dbTools) {
		this.dbTools = dbTools;
		this.listener = listener;
		this.context = context;
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
	protected LinkedHashSet<ItemCategory> doInBackground(Void... arg0) {


		LinkedHashSet<ItemCategory> itemCategories = new LinkedHashSet<ItemCategory>();

		Cursor cursor = dbTools.database.rawQuery(SQLQueries.getCategories, null);

		if (cursor.moveToFirst()) {
			do {

				ItemCategory itemCategory = new ItemCategory();
				itemCategory.setId(cursor.getInt(cursor.getColumnIndex(C.DBColumns.CATEGORY_ID)));
				itemCategory.setName(cursor.getString(cursor.getColumnIndex(C.DBColumns.CATEGORY_NAME)));
				itemCategory.setLogo(cursor.getString(cursor.getColumnIndex(C.DBColumns.CATEGORY_LOGO)));

				itemCategories.add(itemCategory);

			} while (cursor.moveToNext());
		}

		// Closing all cursors, databases and database helpers properly because not closing them can spring lots of trouble.
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}

		return itemCategories;
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
			} else {
				listener.onGetCategoriesFailure("Failure");
			}
		}

		super.onPostExecute(itemCategories);
	}

} // End of Class
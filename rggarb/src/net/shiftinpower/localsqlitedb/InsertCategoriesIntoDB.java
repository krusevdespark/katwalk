package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;

import net.shiftinpower.core.C;
import net.shiftinpower.objects.ItemCategory;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class InsertCategoriesIntoDB extends AsyncTask<Void, Void, Void> {

	private LinkedHashSet<ItemCategory> itemCategories;
	private DBTools dbTools;

	public InsertCategoriesIntoDB(DBTools dbTools, LinkedHashSet<ItemCategory> itemCategories) {
		this.itemCategories = itemCategories;
		this.dbTools = dbTools;
	}

	@Override
	protected Void doInBackground(Void... arg0) {

		ContentValues values = new ContentValues();

		dbTools.database.beginTransaction();
		try {

			for (ItemCategory itemCategory : itemCategories) {
				values.put(C.DBColumns.CATEGORY_ID, itemCategory.getId());
				values.put(C.DBColumns.CATEGORY_NAME, itemCategory.getName());
				values.put(C.DBColumns.CATEGORY_LOGO, itemCategory.getLogo());

				dbTools.database.insert(C.DBTables.CATEGORIES, null, values);

			}
			dbTools.database.setTransactionSuccessful();
		} finally {

			dbTools.database.endTransaction();
		}

		return null;

	} // End of doInBackground

} // End of Class

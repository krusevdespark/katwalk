package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;
import net.shiftinpower.core.C;
import net.shiftinpower.objects.ItemCategory;
import android.content.ContentValues;
import android.os.AsyncTask;

/**
 * After having obtained a fresh set of Categories from the Server Database, now we have it at hand locally for immediate
 * access
 * 
 * @author Kaloyan Roussev
 * 
 */
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

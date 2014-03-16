package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;

import net.shiftinpower.core.C;
import net.shiftinpower.objects.ItemSubcategory;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
* After having obtained a fresh set of Categories from the Server Database, now we have it at hand locally for immediate
* access
*
* @author Kaloyan Roussev
*
*/

public class InsertSubcategoriesIntoDB extends AsyncTask<Void, Void, Void> {

	private LinkedHashSet<ItemSubcategory> itemSubcategories;
	private DBTools dbTools;

	public InsertSubcategoriesIntoDB(DBTools dbTools, LinkedHashSet<ItemSubcategory> itemSubcategories) {
		this.itemSubcategories = itemSubcategories;
		this.dbTools = dbTools;
	}

	@Override
	protected Void doInBackground(Void... arg0) {

		ContentValues values = new ContentValues();

		dbTools.database.beginTransaction();

		try {

			for (ItemSubcategory itemSubcategory : itemSubcategories) {
				values.put(C.DBColumns.SUBCATEGORY_ID, itemSubcategory.getId());
				values.put(C.DBColumns.SUBCATEGORY_NAME, itemSubcategory.getName());
				values.put(C.DBColumns.SUBCATEGORY_PARENT_ID, itemSubcategory.getParentID());

				dbTools.database.insert(C.DBTables.SUBCATEGORIES, null, values);

			}
			dbTools.database.setTransactionSuccessful();
		} finally {

			dbTools.database.endTransaction();
		}

		return null;

	} // End of doInBackground

} // End of Class

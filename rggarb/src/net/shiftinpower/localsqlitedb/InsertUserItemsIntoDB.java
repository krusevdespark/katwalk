package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnInsertUserItemsInDBListener;
import net.shiftinpower.objects.ItemBasic;
import android.content.ContentValues;
import android.os.AsyncTask;

/**
 * After having obtained a fresh set of UserItems from the Server Database, now we have it at hand locally for immediate
 * access
 * 
 * @author Kaloyan Roussev
 * 
 */
public class InsertUserItemsIntoDB extends AsyncTask<String, Void, Boolean> {

	private LinkedHashSet<ItemBasic> userItems;
	private DBTools dbTools;
	private OnInsertUserItemsInDBListener listener;

	public InsertUserItemsIntoDB(OnInsertUserItemsInDBListener listener, DBTools dbTools, LinkedHashSet<ItemBasic> userItems) {
		this.userItems = userItems;
		this.dbTools = dbTools;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(String... arg0) {

		ContentValues values = new ContentValues();

		dbTools.database.beginTransaction();

		try {

			// Iterating all UserItem objects from the LinkedHashSet and getting their info
			for (ItemBasic userItem : userItems) {

				// Converting booleans to integers, because SQLite does not support boolean types.
				int itemBoughtFromPlace = (userItem.isItemBoughtFromPlace()) ? 1 : 0;
				int itemBoughtInConditionNew = (userItem.isItemBoughtInConditionNew()) ? 1 : 0;
				int itemBeingSold = (userItem.isItemBeingSold()) ? 1 : 0;
				int itemWasAGift = (userItem.isItemWasAGift()) ? 1 : 0;

				// Inserting values for the database to insert in a new record
				values.put(C.DBColumns.ITEM_ID, userItem.getItemId());
				values.put(C.DBColumns.ITEM_NAME, userItem.getItemName());
				values.put(C.DBColumns.ITEM_RATING, userItem.getItemRating());
				values.put(C.DBColumns.ITEM_IMAGE, userItem.getItemImage());
				values.put(C.DBColumns.ITEM_OWNER_ID, userItem.getItemOwnerId());
				values.put(C.DBColumns.ITEM_DESCRIPTION, userItem.getItemDescription());
				values.put(C.DBColumns.ITEM_TIME_ADDED, userItem.getItemTimeAdded());
				values.put(C.DBColumns.ITEM_BRAND, userItem.getItemBrand());
				values.put(C.DBColumns.ITEM_PRICE_AQUIRED, userItem.getItemPriceAquired());
				values.put(C.DBColumns.ITEM_BOUGHT_FROM_PLACE, itemBoughtFromPlace);
				values.put(C.DBColumns.ITEM_BOUGHT_FROM_PLACE_NAME, userItem.getItemBoughtFromPlaceName());
				values.put(C.DBColumns.ITEM_CONDITION_NEW, itemBoughtInConditionNew);
				values.put(C.DBColumns.ITEM_BEING_SOLD, itemBeingSold);
				values.put(C.DBColumns.ITEM_PRICE_BEING_SOLD, userItem.getItemPriceBeingSold());
				values.put(C.DBColumns.ITEM_WAS_A_GIFT, itemWasAGift);
				values.put(C.DBColumns.ITEM_CATEGORY_ID, userItem.getItemCategory());
				values.put(C.DBColumns.ITEM_SUBCATEGORY_ID, userItem.getItemSubcategory());

				// database.insertWithOnConflict(C.DBTables.ITEMS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				dbTools.database.insert(C.DBTables.ITEMS, null, values);

			} // End of For loop

			dbTools.database.setTransactionSuccessful();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		} finally {

			dbTools.database.endTransaction();

		}

	} // End of doInBackground

	@Override
	protected void onPostExecute(Boolean result) {
		if (listener != null) {
			if (result == true) {
				listener.onInsertUserItemsInDBSuccess();
			} else {
				listener.onInsertUserItemsInDBFailure();
			}
		}
		super.onPostExecute(result);
	}

} // End of Class
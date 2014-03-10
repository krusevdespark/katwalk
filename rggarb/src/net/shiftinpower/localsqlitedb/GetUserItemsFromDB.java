package net.shiftinpower.localsqlitedb;

import java.util.LinkedHashSet;
import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnGetUserItemsListener;
import net.shiftinpower.objects.ItemBasic;
import android.database.Cursor;
import android.os.AsyncTask;

/**
 * After having obtained a fresh set of UserItems from the Server Database, now we have it at hand locally for immediate
 * access
 * 
 * @author Kaloyan Roussev
 * 
 */
public class GetUserItemsFromDB extends AsyncTask<Void, Void, LinkedHashSet<ItemBasic>> {

	private DBTools dbTools;
	private int userId;
	private OnGetUserItemsListener listener;

	public GetUserItemsFromDB(OnGetUserItemsListener listener, DBTools dbTools, int userId) {
		this.dbTools = dbTools;
		this.userId = userId;
		this.listener = listener;
	}

	@Override
	protected LinkedHashSet<ItemBasic> doInBackground(Void... params) {

		// dbTools.close();

		LinkedHashSet<ItemBasic> userItems = new LinkedHashSet<ItemBasic>();

		Cursor cursor = dbTools.database.rawQuery(SQLQueries.getUserItems(userId), null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				do {
					ItemBasic userItem = new ItemBasic();

					boolean itemBeingSold;
					if (cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_BEING_SOLD)) == 1) {
						itemBeingSold = true;
					} else {
						itemBeingSold = false;
					}

					boolean itemBoughtFromPlace;
					if (cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_BOUGHT_FROM_PLACE)) == 1) {
						itemBoughtFromPlace = true;
					} else {
						itemBoughtFromPlace = false;
					}

					boolean itemBoughtInConditionNew;
					if (cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_CONDITION_NEW)) == 1) {
						itemBoughtInConditionNew = true;
					} else {
						itemBoughtInConditionNew = false;
					}

					boolean itemWasAGift;
					if (cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_WAS_A_GIFT)) == 1) {
						itemWasAGift = true;
					} else {
						itemWasAGift = false;
					}

					userItem.setItemBeingSold(itemBeingSold);
					userItem.setItemBoughtFromPlace(itemBoughtFromPlace);
					userItem.setItemBoughtFromPlaceName(cursor.getString(cursor.getColumnIndex(C.DBColumns.ITEM_BOUGHT_FROM_PLACE_NAME)));
					userItem.setItemBoughtInConditionNew(itemBoughtInConditionNew);
					userItem.setItemRating(cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_RATING)));
					userItem.setItemBrand(cursor.getString(cursor.getColumnIndex(C.DBColumns.ITEM_BRAND)));
					userItem.setItemImage(cursor.getString(cursor.getColumnIndex(C.DBColumns.ITEM_IMAGE)));
					userItem.setItemCategory(cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_CATEGORY_ID)));
					userItem.setItemName(cursor.getString(cursor.getColumnIndex(C.DBColumns.ITEM_NAME)));
					userItem.setItemDescription(cursor.getString(cursor.getColumnIndex(C.DBColumns.ITEM_DESCRIPTION)));
					userItem.setItemId(cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_ID)));
					userItem.setItemOwnerId(cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_OWNER_ID)));
					userItem.setItemPriceAquired(cursor.getDouble(cursor.getColumnIndex(C.DBColumns.ITEM_PRICE_AQUIRED)));
					userItem.setItemPriceBeingSold(cursor.getDouble(cursor.getColumnIndex(C.DBColumns.ITEM_PRICE_BEING_SOLD)));
					userItem.setItemSubcategory(cursor.getInt(cursor.getColumnIndex(C.DBColumns.ITEM_SUBCATEGORY_ID)));
					userItem.setItemTimeAdded(cursor.getString(cursor.getColumnIndex(C.DBColumns.ITEM_TIME_ADDED)));
					userItem.setItemWasAGift(itemWasAGift);

					userItems.add(userItem);

				} while (cursor.moveToNext());

			} // End of if(cursor.moveToFirst()
		}

		// Closing all cursors, databases and database helpers properly because not closing them can spring lots of trouble.
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
			cursor = null;
		}

		return userItems;

	} // End of doInBackground

	@Override
	protected void onPostExecute(LinkedHashSet<ItemBasic> userItems) {

		if (listener != null) {
			if (userItems != null) {
				listener.onGetUserItemsSuccess(userItems);
			} else {
				listener.onGetUserItemsFailure("Failure");
			}
		}

		super.onPostExecute(userItems);
	} // End of onPostExecute

} // End of Class
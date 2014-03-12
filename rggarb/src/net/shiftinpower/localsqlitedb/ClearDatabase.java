package net.shiftinpower.localsqlitedb;

import net.shiftinpower.core.C;
import net.shiftinpower.interfaces.OnClearDatabaseListener;
import android.content.Context;
import android.os.AsyncTask;

public class ClearDatabase extends AsyncTask<Void, Void, Boolean> {

	private DBTools dbTools;
	private Context context;
	private OnClearDatabaseListener listener;

	public ClearDatabase(Context context, DBTools dbTools) {
		this.context = context;
		this.dbTools = dbTools;
	}

	public ClearDatabase(Context context, DBTools dbTools, OnClearDatabaseListener listener) {
		this.context = context;
		this.dbTools = dbTools;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {

		
		// This is the only place where we close the database because of Android > 1.6 < 3.0 problems
		try {

			dbTools.database.execSQL(SQLQueries.tableCategoriesDrop);
			dbTools.database.execSQL(SQLQueries.tableSubcategoriesDrop);
			dbTools.database.execSQL(SQLQueries.tableItemsDrop);

			context.deleteDatabase(C.Preferences.LOCAL_SQLITE_DATABASE_NAME);
			dbTools.closeDB();
			return true;

		} catch (Exception ex) {

			ex.printStackTrace();

			dbTools.closeDB();
			return false;
		}

	} // End of doInBackground

	@Override
	protected void onPostExecute(Boolean result) {

		if (listener != null) {
			if (result) {

				listener.onClearDatabaseSuccess();

			} else {

				listener.onClearDatabaseFailure();

			}
		}
		super.onPostExecute(result);
	}

} // End of Class
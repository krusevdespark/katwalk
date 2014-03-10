package net.shiftinpower.localsqlitedb;

import net.shiftinpower.core.C;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {

	// Its a good practice for DBTools to be a singleton. Do not instantiate it with "new DBTools(context)" but with
	// DBTools.getInstance(context) instead
	private static DBTools sInstance;
	public SQLiteDatabase database;

	public static DBTools getInstance(Context context) {

		if (sInstance == null) {
			sInstance = new DBTools(context);
		}
		return sInstance;
	}

	public DBTools(Context context) {
		super(context, C.Preferences.LOCAL_SQLITE_DATABASE_NAME, null, 1);
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SQLQueries.tableCategoriesCreate);
		database.execSQL(SQLQueries.tableSubcategoriesCreate);
		database.execSQL(SQLQueries.tableItemsCreate);
	}

	public void onOpen(SQLiteDatabase database) {
		database.execSQL(SQLQueries.tableCategoriesCreate);
		database.execSQL(SQLQueries.tableSubcategoriesCreate);
		database.execSQL(SQLQueries.tableItemsCreate);
	}

	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		database.execSQL(SQLQueries.tableCategoriesDrop);
		database.execSQL(SQLQueries.tableSubcategoriesDrop);
		database.execSQL(SQLQueries.tableItemsDrop);
		onCreate(database);
	}
	
	public void openDB() {
		try {

			database = getWritableDatabase();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	public void closeDB(){
		if (database != null && database.isOpen()) {
			try {
				database.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

} // End of Class

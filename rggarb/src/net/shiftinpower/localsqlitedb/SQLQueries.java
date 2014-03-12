package net.shiftinpower.localsqlitedb;

public final class SQLQueries {

	// Create queries
	public static final String tableCategoriesCreate = "CREATE TABLE IF NOT EXISTS categories (category_id INTEGER PRIMARY KEY, category_name TEXT NOT NULL UNIQUE, category_logo TEXT)";
	public static final String tableSubcategoriesCreate = "CREATE TABLE IF NOT EXISTS subcategories (subcategory_id INTEGER PRIMARY KEY, subcategory_name TEXT NOT NULL UNIQUE, subcategory_parent_id INTEGER NOT NULL, FOREIGN KEY (subcategory_parent_id) REFERENCES categories(category_id))";
	public static final String tableItemsCreate = "CREATE TABLE IF NOT EXISTS items "
			+ "(item_id INTEGER PRIMARY KEY, item_name TEXT NOT NULL, user_id INTEGER NOT NULL, item_time_added TEXT NOT NULL, "
			+ "item_brand TEXT, item_rating INTEGER,  item_description TEXT, item_photo TEXT, item_price_aquired INTEGER, item_bought_from_place INTEGER, "
			+ "item_bought_from_place_name TEXT, item_condition_new INTEGER, item_being_sold INTEGER, item_price_being_sold INTEGER, "
			+ "item_is_gift INTEGER, category_id INTEGER NOT NULL, subcategory_id INTEGER, " + "FOREIGN KEY (category_id) REFERENCES categories(category_id), "
			+ "FOREIGN KEY (subcategory_id)  REFERENCES subcategories(subcategory_id))";

	// Drop Queries
	public static final String tableCategoriesDrop = "DROP TABLE IF EXISTS categories";
	public static final String tableSubcategoriesDrop = "DROP TABLE IF EXISTS subcategories";
	public static final String tableItemsDrop = "DROP TABLE IF EXISTS items";

	// Get Queries
	public static final String getCategoryNames = "SELECT category_name FROM categories";
	public static final String getCategoryIDs = "SELECT category_id FROM categories";
	public static final String getCategoryLogos = "SELECT category_logo FROM categories";

	public static final String getCategories = "SELECT * FROM categories";

	public static final String getSubcategories(int subcategoryParentId) {
		return "SELECT * FROM subcategories WHERE subcategory_parent_id = " + subcategoryParentId;
	}

	public static final String getUserItems(int userId) {
		return "SELECT * FROM items WHERE user_id = " + userId;
	}

} // End of class
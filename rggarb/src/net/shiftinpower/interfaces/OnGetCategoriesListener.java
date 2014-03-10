package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemCategory;

public interface OnGetCategoriesListener {
	void onGetCategoriesSuccess(LinkedHashSet<ItemCategory> itemCategories);

	void onGetCategoriesFailure(String reason);
}

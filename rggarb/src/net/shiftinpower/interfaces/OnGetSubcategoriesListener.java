package net.shiftinpower.interfaces;

import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemSubcategory;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnGetSubcategoriesListener {
	void onGetSubcategoriesSuccess(LinkedHashSet<ItemSubcategory> itemSubcategories);

	void onGetSubcategoriesFailure(String reason);
}

package net.shiftinpower.interfaces;

import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemSubcategory;

public interface OnGetSubcategoriesListener {
	void onGetSubcategoriesSuccess(LinkedHashSet<ItemSubcategory> itemSubcategories);
	void onGetSubcategoriesFailure(String reason);
}


package net.shiftinpower.interfaces;

import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemCategory;

public interface OnGetCategoriesListener {
	void onGetCategoriesSuccess(LinkedHashSet<ItemCategory> itemCategories);
	void onGetCategoriesFailure(String reason);
}

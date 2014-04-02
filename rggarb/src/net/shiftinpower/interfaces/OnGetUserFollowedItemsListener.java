package net.shiftinpower.interfaces;

import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemBasic;

public interface OnGetUserFollowedItemsListener {
	void onGetUserFollowedItemsSuccess(LinkedHashSet<ItemBasic> userItems);
	void onGetUserFollowedItemsFailure(String reason);
}

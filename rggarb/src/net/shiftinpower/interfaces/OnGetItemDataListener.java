package net.shiftinpower.interfaces;

import net.shiftinpower.objects.ItemExtended;

public interface OnGetItemDataListener {
	void onGetItemDataSuccess(ItemExtended itemParameters);
	void onGetItemDataFailure();
}

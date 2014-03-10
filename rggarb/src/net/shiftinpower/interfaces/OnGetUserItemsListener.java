package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
import java.util.LinkedHashSet;

import net.shiftinpower.objects.ItemBasic;

public interface OnGetUserItemsListener {
	void onGetUserItemsSuccess(LinkedHashSet<ItemBasic> userItems);

	void onGetUserItemsFailure(String reason);
}

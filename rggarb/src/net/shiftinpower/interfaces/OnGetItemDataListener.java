package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
import net.shiftinpower.objects.ItemExtended;

public interface OnGetItemDataListener {
	void onGetItemDataSuccess(ItemExtended itemParameters);

	void onGetItemDataFailure();
}

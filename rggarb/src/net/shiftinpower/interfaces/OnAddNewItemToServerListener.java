package net.shiftinpower.interfaces;

import android.os.Bundle;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnAddNewItemToServerListener {
	void onAddNewItemSuccess(Bundle dataRegardingTheItemJustAdded);

	void onAddNewItemFailure(String reason);
}

package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnInsertUserItemsInDBListener {
	void onInsertUserItemsInDBSuccess();

	void onInsertUserItemsInDBFailure();
}

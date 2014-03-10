package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnClearDatabaseListener {
	void onClearDatabaseSuccess();

	void onClearDatabaseFailure();
}

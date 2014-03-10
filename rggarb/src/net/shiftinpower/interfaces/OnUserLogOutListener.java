package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnUserLogOutListener {
	void onUserLogOutSuccess();

	void onUserLogOutFailure();
}

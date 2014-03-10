package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnUserDeletionListener {
	void onUserDeletionSuccess();

	void onUserDeletionFailure(String reason);
}

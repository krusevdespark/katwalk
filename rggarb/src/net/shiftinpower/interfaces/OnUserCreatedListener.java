package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnUserCreatedListener {
	void onUserCreated(Integer userId);

	void onUserNotCreated();
}

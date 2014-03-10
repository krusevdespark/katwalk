package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnUserLoginAttemptListener {
	void onUserLoginAttemptSuccess(int userId);

	void onUserLoginAttemptFailure(String reason);
}

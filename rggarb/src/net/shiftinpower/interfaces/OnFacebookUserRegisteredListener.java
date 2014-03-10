package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnFacebookUserRegisteredListener {
	void OnFacebookUserRegisteredSuccess(int userId);

	void OnFacebookUserRegisteredFailure();
}

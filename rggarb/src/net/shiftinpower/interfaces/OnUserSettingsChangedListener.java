package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnUserSettingsChangedListener {
	void onUserSettingsChangedSuccess();

	void onUserSettingsChangedFailure(String reason);
}

package net.shiftinpower.interfaces;

public interface OnUserSettingsChangedListener {
	void onUserSettingsChangedSuccess();
	void onUserSettingsChangedFailure(String reason);
}

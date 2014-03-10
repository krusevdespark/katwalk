package net.shiftinpower.interfaces;

public interface OnUserDeletionListener {
	void onUserDeletionSuccess();
	void onUserDeletionFailure(String reason);
}

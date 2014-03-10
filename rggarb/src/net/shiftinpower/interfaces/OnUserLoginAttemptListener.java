package net.shiftinpower.interfaces;

public interface OnUserLoginAttemptListener {
	void onUserLoginAttemptSuccess(int userId);
	void onUserLoginAttemptFailure(String reason);
}

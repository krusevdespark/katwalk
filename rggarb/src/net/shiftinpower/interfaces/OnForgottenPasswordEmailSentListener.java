package net.shiftinpower.interfaces;

public interface OnForgottenPasswordEmailSentListener {
	void onForgottenPasswordEmailSentSuccess ();
	void onForgottenPasswordEmailSentFailure (String reason);
}

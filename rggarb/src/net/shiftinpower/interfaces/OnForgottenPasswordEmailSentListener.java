package net.shiftinpower.interfaces;

/**
 * Listener/Observer pattern.
 * 
 * @author Kaloyan Roussev
 * 
 */
public interface OnForgottenPasswordEmailSentListener {
	void onForgottenPasswordEmailSentSuccess();

	void onForgottenPasswordEmailSentFailure(String reason);
}
